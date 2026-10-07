/**
 * Catalog Service
 */
package com.gcu.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.gcu.data.CatalogAccess;
import com.gcu.data.InventoryAccess;
import com.gcu.data.ProductAccess;
import com.gcu.data.entity.CatalogEntity;
import com.gcu.mapper.CatalogMapper;
import com.gcu.model.CatalogModel;
import com.gcu.model.ProductModel;
import com.gcu.utilities.CSVLogger;
/**
 * CatalogService class implements the CatalogServiceInterface and provides
 * methods to manage products in a catalog.
 */
@Service
public class CatalogService implements CatalogServiceInterface {
	CatalogModel catalog = new CatalogModel(); 					
	
	@Autowired
	InventoryAccess inventoryAccess;							
	@Autowired
	CatalogAccess catalogAccess;
	@Autowired
	ProductAccess productAccess;
	@Autowired
	StorageService storageService;
	
	/**
	 * Retrieves all products in the catalog.
	 * 
	 * @param id The ID of the product to retrieve.
	 * @return A list of all products in the catalog.
	 */
	@Override
	public List<ProductModel> getAllProducts(int id) {
		List<ProductModel> products = catalog.getProducts(); 	
		return products;										
	}
	
	/**
	 * Updates the catalog with the given catalog model.
	 *  
	 * @param userId The ID of the user whose catalogs are to be retrieved.
	 * @return A list of CatalogModel objects representing the catalogs for the given user ID.
	 */
	@Override
	public boolean updateCatalog(CatalogModel catalog, MultipartFile imageFile) {
		
		try {
			catalog.setCatalogId(catalog.getId());

	        // Handle image replacement if a new image was provided
	        if (imageFile != null && !imageFile.isEmpty()) {

	            // Save the old filename before replacing it
	            String oldImage = catalog.getImage();

	            // Store the new image first
	            String newImage = storageService.storeImage(
	                    imageFile,
	                    "catalog",
	                    catalog.getId(),
	                    catalog.getId()
	            );

	            if (newImage == null) {
	                CSVLogger.log(
	                        catalog.getUserId(),
	                        "SERVICE",
	                        "CATALOG_IMAGE_STORE_FAILED",
	                        "Failed to store new image for catalog '"
	                                + catalog.getName() + "'",
	                        ""
	                );

	                return false;
	            }

	            // Update model with the newly generated filename
	            catalog.setImage(newImage);

	            // Delete old image only after new image was successfully stored
	            if (oldImage != null && !oldImage.isEmpty()) {
	                storageService.deleteCatalogImage(
	                        oldImage,
	                        catalog.getId()
	                );
	            }
	        }
		
		// map model fields into persistence entity
		CatalogEntity entity = CatalogMapper.toEntity(catalog);
			
		boolean result = catalogAccess.update(entity);	
		
		CSVLogger.log(
                catalog.getUserId(),
                "SERVICE",
                result
                        ? "CATALOG_UPDATE_SUCCESS"
                        : "CATALOG_UPDATE_FAILED",
                result
                        ? "Updated catalog '" + catalog.getName() + "'"
                        : "Failed to update catalog '"
                                + catalog.getName() + "'",
                ""
        );
		
	    return result;
		
		} catch (Exception e) {

	        CSVLogger.log(
	                catalog.getUserId(),
	                "SERVICE",
	                "CATALOG_UPDATE_ERROR",
	                "Error updating catalog '"
	                        + catalog.getName() + "'",
	                e.getMessage()
	        );

	        return false;
	    }
	}
	
	/**
	 * Retrieves a catalog by its ID.
	 * 
	 * @param catalogId The ID of the catalog to retrieve.
	 * @return The catalog with the specified ID, or null if not found.
	 */
	@Override
	public CatalogModel getCatalogById(int catalogId) {
		CatalogModel catalog = catalogAccess.getById(catalogId);	
		return catalog;												
	}
	
	/**
	 * Deletes a catalog by its ID.
	 * @param catalogId
	 * @return
	 */
	@Transactional
	@Override
	public boolean deleteCatalog(int catalogId) {	
		try {
	        // delete all products in catalog
	        productAccess.deleteAllProductsByCatalog(catalogId);

	        // delete the catalog itself
	        catalogAccess.delete(catalogId);

	        // delete catalog image
	        storageService.deleteCatalogFolder(catalogId);
	        
	        CSVLogger.log(
	            null,
	            "SERVICE",
	            "CATALOG_DELETE_SUCCESS",
	            "Deleted catalog ID " + catalogId,
	            ""
	        );

	        return true;

	    } catch (Exception e) {
	        CSVLogger.log(
	            null,
	            "SERVICE",
	            "CATALOG_DELETE_ERROR",
	            "Error deleting catalog ID " + catalogId,
	            e.getMessage()
	        );
	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        return false;
	    }
	}
}