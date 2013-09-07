package domain.businessEntity.Community;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_GroupsData")
public class GroupsData {
	public GroupsData() {}
	
	@DatabaseField(generatedId = false)
	protected String gid;
	
	@DatabaseField(canBeNull = false)
	protected String groupname;
	
	@DatabaseField(canBeNull = false)
	protected String place;
	
	@DatabaseField(canBeNull = false)
	protected String time;
	
	@DatabaseField(canBeNull = false)
	protected String info;
	
	@DatabaseField(canBeNull = false)
	protected String sponsor;
	
	@DatabaseField(canBeNull = true)
	protected String isMyJoin;
	
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
	public String getIsMyJoin() {
		return isMyJoin;
	}
	public void setIsMyJoin(String isMyJoin) {
		this.isMyJoin = isMyJoin;
	}
	
}
