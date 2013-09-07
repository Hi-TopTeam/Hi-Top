package domain.businessService.Community;

import java.util.List;

import domain.businessEntity.Community.FriendsData;
import domain.businessEntity.Community.GroupsData;

public interface ICommunityDataService {
	public boolean addGroupsData(GroupsData groupsData);
	public boolean deleteGroupsData(GroupsData groupsData);
	public boolean UpdateGroupsData(GroupsData groupsData);
	public GroupsData getByGid(String gid);
	public List<GroupsData> getAllData();
	public List<GroupsData> getMyJoinGroups();
	public boolean addFriendsData(FriendsData friendsData);
	public boolean deleteFriend(FriendsData friendsData);
	public List<FriendsData> getAllFriends();
	public FriendsData getFriendsByUsername(String username);
	
}
