/**
 * InventoryServiceInterface.java
 */
package com.gcu.business;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gcu.data.entity.CatalogEntity;
import com.gcu.model.CatalogModel;
/**
 * InventoryServiceInterface defines the methods for managing inventories.
 * It provides methods to add, delete, and retrieve inventories.
 */
public interface InventoryServiceInterface {
	
	List<CatalogModel> getInventory(int userID);
	public boolean addInventory(CatalogModel catalog);
	public CatalogEntity addCatalogWithImage(CatalogModel catalog, MultipartFile imageFile, int userId) throws Exception;
	public CatalogEntity getByNameAndId(String name, int userId);
	public default void init() { }
	public default void destroy() {		
	}	
}