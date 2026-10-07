package com.gcu.data;

import java.util.List;

import com.gcu.model.SharedCatalogModel;

public interface SharedCatalogAccessInterface {

	public List<Integer> getAllSharedWithUser(int userId);
	public boolean hasPermission(int userId, int catalogId, String permission);
	List<SharedCatalogModel> getSharedUsersForCatalog(int catalogId);
    SharedCatalogModel findShare(int catalogId, int sharedUserId);
    boolean addShare(int catalogId, int sharedUserId, String permission);
    boolean removeShare(int catalogId, int sharedUserId);

}