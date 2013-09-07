package domain.businessEntity.Helper;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import domain.businessEntity.Community.GroupsData;

public class GroupHelper extends GroupsData implements KvmSerializable{
//	private String gid;
//	private String groupname;
//	private String place;
//	private String time;
//	private String info;
//	private String sponsor;
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	@Override
	public Object getProperty(int arg0) {
		Object object = null;
		switch (arg0) {
		case 0:
			object = this.gid;
			break;
		case 1:
			object = this.groupname ;
			break;
		case 2:
			object = this.place;
			break;
		case 3:
			object = this.time;
			break;
		case 4:
			object = this.info;
			break;
		case 5:
			object = this.sponsor;
			break;
		}
		return object;
	}
	@Override
	public int getPropertyCount() {
		return 6;
	}
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch (arg0) {
		 case 0:
		 arg2.type = PropertyInfo.STRING_CLASS;
		 arg2.name = "gid";
		 break;
		case 1:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "groupname";
			break;
		case 2:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "place";
			break;
		case 3:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "time";
			break;
		case 4:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "info";
			break;
		case 5:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "sponsor";
			break;
		default:
			break;
		}
		
	}
	@Override
	public void setProperty(int arg0, Object arg1) {
		switch (arg0) {
		 case 0:
			gid =  arg1.toString();
		 break;
		case 1:
			groupname = arg1.toString();
			break;
		case 2:
			place = arg1.toString();
			break;
		case 3:
			time = arg1.toString();
			break;

		case 4:
			info = arg1.toString();
			break;
		case 5:
			sponsor = arg1.toString();
			break;
		default:
			break;
		}
		
	}
	
}
