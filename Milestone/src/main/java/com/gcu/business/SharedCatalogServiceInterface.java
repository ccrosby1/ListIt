package com.gcu.business;

import java.util.List;

import com.gcu.model.CatalogModel;
import com.gcu.model.SharedCatalogModel;

public interface SharedCatalogServiceInterface {

	public List<CatalogModel> getSharedCatalogs(int userId);
	public boolean userCanViewCatalog(int userId, int catalogId);
	public boolean userCanEditCatalog(int userId, int catalogId);
	List<SharedCatalogModel> getSharedUsersForCatalog(int catalogId);
    boolean shareCatalogWithUsername(int catalogId, String username, String permission);
    boolean removeShare(int catalogId, int sharedUserId);
}
