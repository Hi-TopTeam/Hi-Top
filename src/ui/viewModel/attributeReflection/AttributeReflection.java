package ui.viewModel.attributeReflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author 福建师范大学软件学院   倪友聪、赵康
 *
 */
public class AttributeReflection {
	/**
	 * Android平台下可以直接打包的类(int,char等可直接打包，如发现遗漏可以再添加）
	 */
	static String[] CANPARCELABLECALSSNAMES = { "java.lang.String",
			"java.util.Date", "Integer", "Float", "Boolean", "Double",
			"Character", "Byte", "Short", "Long" };
	/**
	 * 目前只考虑集合类List，后期可再添加其它集合类
	 */
	static String[] CANPROCESSSETNAMES = { "java.util.List" };

	public enum FieldKind {

		/**
		 * 可直接打包的原子类型(其包括CANPARCELABLECALSSNAMES中的类和 Java中的原子类型如int,char等）的属性
		 */
		ParcelableAtomicField,

		/**
		 * 可直接打包的集合类型(目前仅考虑List)的属性 如List<int>或List<String>
		 */
		ParcelableCollectionField,

		/**
		 * 不可直接打包的集合类型(目前仅考虑List)的属性
		 * 
		 */
		CantParceCollectionField,

		/**
		 * 用户自定义类型的属性，不可直接打包
		 * 
		 */
		ClassUserDefinedField;
	}

	/**
	 * 根据给定的对象获取该对象所有属性名称、类型和值， 并存放在AttInfo列表中
	 * 
	 * @param obj
	 *            :输入的任意一个对象
	 * @return:输入对象的属性信息列表,空对象则返回null
	 * @throws ClassNotFoundException
	 *             :该对象对应类定义未发现
	 * @throws IllegalArgumentException
	 *             :参数异常
	 * @throws IllegalAccessException
	 *             :私有属性不允许访问
	 * @throws InstantiationException
	 *             ：实例化对象异常
	 */
	public static List<AttInfo> getAttInfosFromObject(Object obj)
			throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {

		List<AttInfo> attInfos = null;
		if (obj == null)
			return attInfos;
		Class<?> aClass = Class.forName(obj.getClass().getName());
		Field[] flds = aClass.getDeclaredFields();
		int len = flds.length;
		attInfos = new ArrayList<AttInfo>();
		// 根据对象类中每个属性
		for (int i = 0; i < len; i++) {
			Field fld = flds[i];
			fld.setAccessible(true);
			String attName = fld.getName();
			String typeName = fld.getType().getName();

			AttInfo info = new AttInfo();
			info.setAttName(attName);
			info.setTypeName(typeName);
			Object value = fld.get(obj);
			FieldKind fldKind = getFieldKind(fld);

			// 可打包原子类型或是可打包的集合List类型的属性
			if (fldKind == FieldKind.ParcelableAtomicField
					|| fldKind == FieldKind.ParcelableCollectionField) {
				info.setValue(value);
				info.setComplexAtt(null);
			} else {
				if (fldKind == FieldKind.ClassUserDefinedField) {

					// 用户自定义类
					info.setValue(null);
					// 自定义类中所有子对象的属性信息存放在AttInfosOfDefinedClass
					List<AttInfo> AttInfosOfDefinedClass = getAttInfosFromObject(value);
					info.setComplexAtt(AttInfosOfDefinedClass);

				} else {

					info.setValue(null);
					List<AttInfo> attInfosInSet = getAttInfosFromCantParceSetField(
							fld, value);
					info.setComplexAtt(attInfosInSet);
				}
			}

			attInfos.add(info);
		}
		return attInfos;
	}

	/**
	 * 类名和属性信息列表中，构建出对象
	 * 
	 * @param className
	 *            :类名
	 * @param attInfos
	 *            :类对象所对应的属性信息
	 * @return:根据属性信息构造出的对象
	 * @throws ClassNotFoundException
	 *             :类名对应的类未发现
	 * @throws InstantiationException
	 *             :实例化异常
	 * @throws IllegalAccessException
	 *             ：私有属性不允许访问
	 * @throws NoSuchFieldException
	 *             ：属性找不到
	 */
	public static Object getOjectFromAttInfos(String className,
			List<AttInfo> attInfos) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NoSuchFieldException {

		Object obj = null;
		if (className == null || attInfos == null) {
			return obj;
		}
		Class<?> cls = Class.forName(className);
		obj = cls.newInstance();

		int len = attInfos.size();
		Class<?> cls2 = obj.getClass();

		for (int i = 0; i < len; i++) {
			AttInfo att = attInfos.get(i);
			String attName = att.getAttName();
			String typeName = att.getTypeName();
			Object value = att.getValue();

			Field fld = cls2.getDeclaredField(attName);
			fld.setAccessible(true);

			if (value != null) {// 可直接打包类型
				fld.set(obj, value);
			} else {// 是集合类型或自定义类型
				boolean isCollectionType = false;
				for (String name : CANPROCESSSETNAMES) {
					if (typeName.equals(name)) {
						isCollectionType = true;
					}
				}
				if (isCollectionType) {// 是集合类型
					List<Object> subObjs = getSubObjectsFormListField(att);
					fld.set(obj, subObjs);
				} else {// 是自定类型

					Object subObj = getOjectFromAttInfos(typeName,
							att.getComplexAtt());
					fld.set(obj, subObj);
				}

			}

		}
		return obj;
	}

	/**
	 * 判断一个类的属性Field,在Android下是否可直接打包, 目前只考虑基本类型、自定义类型和List集合三种属性Field
	 * 
	 * @param Field
	 *            ：属性
	 * @return true：是可直接打包的Field,false:不可直接打包的Field
	 */
	private static boolean isParcelableBasicTypeField(final Field fld) {
		boolean result = false;
		Class<?> fldType = fld.getType();
		String fldTypeName = fldType.getName();

		if (fldType.isPrimitive()) {// 是否是基本类型
			result = true;
		} else {// 是否是可直接打包类型
			for (String typeName : CANPARCELABLECALSSNAMES) {
				if (fldTypeName.equals(typeName)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param fld
	 *            :属性
	 * @return：属性的种类（可直接打包的原子类型属性ParcelableAtomicField、 
	 *                                                  不可直接打包的集合类型属性CantParceCollectionField
	 *                                                  、
	 *                                                  可直接打包的集合类型属性ParcelableCollectionField
	 *                                                  、 用户自定义类型的属性
	 * @throws ClassNotFoundException
	 *             :fld对应类未发现
	 */

	private static FieldKind getFieldKind(Field fld)
			throws ClassNotFoundException {
		FieldKind kind = FieldKind.CantParceCollectionField;
		boolean isCollectionType = false;
		String fldTypeName = fld.getType().getName();

		// 判断是否是集合类型
		for (String name : CANPROCESSSETNAMES) {
			if (fldTypeName.equals(name)) {
				isCollectionType = true;
			}
		}
		// 是否集合类型，则判定其kind
		if (isCollectionType) {

			// 得到参数类名(List<T>中T的名字）
			Type fldGenericType = fld.getGenericType();
			String parameterizedTypeName = "";
			Type[] types = ((ParameterizedType) fldGenericType)
					.getActualTypeArguments();
			String str = types[0].toString();
			int len = str.length();
			parameterizedTypeName = str.substring(6, len);

			// 获取参数类的属性
			Class<?> typeClss;
			typeClss = Class.forName(parameterizedTypeName);
			Field[] flds = typeClss.getDeclaredFields();

			// 判断List<T>中T是不是基本类型,并进一步判断T是否可直接打包，
			// 如可则List<T>也可直接打包
			if (flds.length == 1) {
				if (isParcelableBasicTypeField(flds[0])) {
					kind = FieldKind.ParcelableCollectionField;
				}
			}
		} else {// 不考虑其它情况，只剩下基本类型和自定义类型
			if (isParcelableBasicTypeField(fld)) {
				kind = FieldKind.ParcelableAtomicField;
			} else {
				kind = FieldKind.ClassUserDefinedField;
			}
		}

		return kind;

	}

	/**
	 * 
	 * @param listfld
	 *            :List类型属性
	 * @param value
	 *            ：该属性的值
	 * @return：该属性下的所有子对象的属性信息列表
	 * @throws ClassNotFoundException
	 *             :找不到子对象对应的类
	 * @throws InstantiationException
	 *             ：实例化子对象
	 * @throws IllegalAccessException
	 *             ：访问属性异常
	 */
	public static List<AttInfo> getAttInfosFromCantParceSetField(Field listfld,
			Object value) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		// 不可打包的集合类型
		List<AttInfo> AttInfosInSetField = null;
		if (value == null) {
			return AttInfosInSetField;
		}

		// 获取参数类的名称
		Type fldGenericType = listfld.getGenericType();
		String parameterizedTypeName = "";
		Type[] types = ((ParameterizedType) fldGenericType)
				.getActualTypeArguments();
		String str = types[0].toString();
		int len2 = str.length();
		parameterizedTypeName = str.substring(6, len2);
		Class<?> ParameterizedClass = Class.forName(parameterizedTypeName);
		List<Object> subObjs = (List<Object>) value;
		int numOfSubOj = subObjs.size();
		AttInfosInSetField = new ArrayList<AttInfo>();
		// 对每个子对象进行递归
		for (int k = 0; k < numOfSubOj; k++) {
			Object subObj = ParameterizedClass.newInstance();
			subObj = subObjs.get(k);

			// 一个子象中的属性信息
			AttInfo subAttInfo = new AttInfo();
			subAttInfo.setAttName(Integer.toString(k));
			subAttInfo.setTypeName(parameterizedTypeName);
			subAttInfo.setValue(null);

			List<AttInfo> AttInfosInSubOject = getAttInfosFromObject(subObj);
			subAttInfo.setComplexAtt(AttInfosInSubOject);

			AttInfosInSetField.add(subAttInfo);

		}
		return AttInfosInSetField;
	}

	public static List<Object> getSubObjectsFormListField(AttInfo att)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchFieldException {

		List<Object> subObjs = null;
		List<AttInfo> subAttInfos = att.getComplexAtt();
		if (subAttInfos == null) {
			return subObjs;
		}
		int numOfSubObjs = subAttInfos.size();
		if (numOfSubObjs >= 1) {
			subObjs = new ArrayList<Object>();
		}

		for (int j = 0; j < numOfSubObjs; j++) {
			String subClassName = subAttInfos.get(j).getTypeName();
			List<AttInfo> AttInfosInThisSubObj = subAttInfos.get(j)
					.getComplexAtt();
			Object subObj = getOjectFromAttInfos(subClassName,
					AttInfosInThisSubObj);
			subObjs.add(subObj);
		}
		return subObjs;
	}

}
