package domain.businessEntity.Community;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_FriendsData")
public class FriendsData {
	public FriendsData(){}
	@DatabaseField(generatedId = true)
	private int DataId;
	@DatabaseField(canBeNull = false)
	private String username;
	@DatabaseField(canBeNull = false)
	private String nickname;
	public int getDataId() {
		return DataId;
	}
	public void setDataId(int dataId) {
		DataId = dataId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
