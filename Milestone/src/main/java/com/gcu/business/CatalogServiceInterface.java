/**
 * CatalogServiceInterface.java
 */
package com.gcu.business;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gcu.model.CatalogModel;
import com.gcu.model.ProductModel;
/**
 * CatalogServiceInterface defines the methods for managing products in a catalog.
 * It provides methods to add, update, delete, and retrieve products based on various criteria.
 */
public interface CatalogServiceInterface {

	public List<ProductModel> getAllProducts(int id);
	public boolean updateCatalog(CatalogModel catalog, MultipartFile imageFile);	
	public CatalogModel getCatalogById(int catalogId);
	boolean deleteCatalog(int catalogId);
}