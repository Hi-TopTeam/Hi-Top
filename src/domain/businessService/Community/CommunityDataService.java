package domain.businessService.Community;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import domain.businessEntity.Community.FriendsData;
import domain.businessEntity.Community.GroupsData;
import domain.businessEntity.gps.AltitudeData;
import foundation.data.DataContext;
import foundation.data.IDataContext;

public class CommunityDataService implements ICommunityDataService {
	private static String tag = "CommunityDataService";
	private IDataContext ctx = null;

	public CommunityDataService() {
		ctx = new DataContext();
	}

	@Override
	public boolean addGroupsData(GroupsData groupsData) {
		try {
			ctx.add(groupsData, GroupsData.class, String.class);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}

	}

	@Override
	public boolean deleteGroupsData(GroupsData groupsData) {
		try {
			ctx.delete(groupsData, GroupsData.class, String.class);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean UpdateGroupsData(GroupsData groupsData) {
		DeleteBuilder<GroupsData, String> db;
		try {
			db = ctx.getDeleteBuilder(GroupsData.class, String.class);
			db.where().eq("gid", groupsData.getGid());
			ctx.delete(GroupsData.class, String.class, db.prepare());
			ctx.add(groupsData, GroupsData.class, String.class);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public GroupsData getByGid(String gid) {
		GroupsData groupsData = null;
		try {
			QueryBuilder<GroupsData, String> qb = ctx.getQueryBuilder(
					GroupsData.class, String.class);
			qb.where().eq("gid", gid);
			List<GroupsData> qbDataList = ctx.query(GroupsData.class,
					String.class, qb.prepare());
			if (qbDataList.size() != 0)
				groupsData = qbDataList.get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupsData;
	}

	@Override
	public List<GroupsData> getAllData() {
		List<GroupsData> list = null;
		try {
			list = ctx.queryForAll(GroupsData.class, String.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<GroupsData> getMyJoinGroups() {
		List<GroupsData> qbDataList = null;
		try {
			QueryBuilder<GroupsData, String> qb = ctx.getQueryBuilder(
					GroupsData.class, String.class);
			qb.where().eq("isMyJoin", "ture");
			qbDataList = ctx
					.query(GroupsData.class, String.class, qb.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qbDataList;
	}

	@Override
	public boolean addFriendsData(FriendsData friendsData) {
		try {
			ctx.add(friendsData, FriendsData.class, Integer.class);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteFriend(FriendsData friendsData) {
		try {
			ctx.delete(friendsData, FriendsData.class, Integer.class);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public List<FriendsData> getAllFriends() {
		List<FriendsData> list = null;
		try {
			list = ctx.queryForAll(FriendsData.class, Integer.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public FriendsData getFriendsByUsername(String username) {
		FriendsData fData = null;

		try {
			QueryBuilder<FriendsData, Integer> qb = ctx.getQueryBuilder(
					FriendsData.class, Integer.class);
			qb.where().eq("username", username);
			List<FriendsData> qbDataList = ctx.query(FriendsData.class,
					Integer.class, qb.prepare());
			if (qbDataList.size() != 0)
				fData = qbDataList.get(0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fData;
	}

}
