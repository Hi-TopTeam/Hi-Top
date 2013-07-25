package ui.viewModel.attributeReflection;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 
 * @author 福建师范大学软件学院   倪友聪、赵康
 *
 */
public class AttInfo implements Parcelable {

	// 属性名
	private String attName;

	// 属性的类型名
	private String typeName;

	// 属性值,如属性是Android下不可直接打包的类，则此值为空
	private Object value;

	// 属性信息列表，仅在属性的类型为不可直接打包类时使用
	private List<AttInfo> complexAtt;

	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<AttInfo> getComplexAtt() {
		return complexAtt;
	}

	public void setComplexAtt(List<AttInfo> complexAtt) {
		this.complexAtt = complexAtt;
	}

	/**
	 * 将属性信息对象写到Parcel中,实现Parcelable接口中方法
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 实现Parcelable接口中Parcel的构造类
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(attName);
		dest.writeString(typeName);
		if (complexAtt == null) {
			dest.writeValue(value);
		} else {
			dest.writeValue(null);
			int len = complexAtt.size();
			for (int i = 0; i < len; i++) {
				AttInfo subAtt = complexAtt.get(i);
				subAtt.writeToParcel(dest, flags);
			}
		}

	}

	/**
	 * 实现Parcelable接口中Parcel的构造类
	 */
	public static final Parcelable.Creator<AttInfo> CREATOR

	= new Parcelable.Creator<AttInfo>() {

		// 从Parcel中构造出属性信息对象
		public AttInfo createFromParcel(Parcel in) {
			AttInfo attInfo = new AttInfo();
			attInfo.attName = in.readString();
			attInfo.typeName = in.readString();
			attInfo.value = in.readValue(null);

			if (attInfo.value != null) {// 简单属性
				attInfo.complexAtt = null;
			} else {// 含子属性列表
				Parcel subParcel = (Parcel) in.readValue(null);
				createFromParcel(subParcel);
			}
			return attInfo;
		}

		/**
		 * 生成属性信息对象数组,实现Parcelable接口中方法
		 */
		public AttInfo[] newArray(int size) {
			return new AttInfo[size];
		}
	};
}
