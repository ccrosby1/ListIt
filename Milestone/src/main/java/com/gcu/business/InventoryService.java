/**
 * InventoryService.java
 * 
 * This class implements the InventoryServiceInterface and provides methods to manage an inventory of catalogs.
 * It includes methods to initialize, destroy, retrieve, add, and delete catalogs in the inventory.
 */
package com.gcu.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gcu.data.InventoryAccess;
import com.gcu.data.entity.CatalogEntity;
import com.gcu.mapper.CatalogMapper;
import com.gcu.model.CatalogModel;
import com.gcu.utilities.CSVLogger;
/**
 * InventoryService is a catalogService class that manages an inventory of catalogs.
 * It provides methods to initialize, destroy, retrieve, add, and delete catalogs in the inventory.
 */
@Transactional
public class InventoryService implements InventoryServiceInterface {
	@Autowired
	private InventoryAccess inventoryAccess;		
	@Autowired
	private CatalogService catalogService;			
		
	/**
	 * init() initializes the inventory catalogService.
	 */
	@Override
	public void init() {
	}
	
	/**
	 * destroy() destroys the inventory catalogService.
	 */
	@Override
	public void destroy() {
		
	}
	
	/**
	 * getInventory() retrieves the inventory for a specific user.
	 * 
	 * @param userID The ID of the user whose inventory is to be retrieved.
	 * @return A list of CatalogModel objects representing the user's inventory.
	 */
	@Override
	public List<CatalogModel> getInventory(int userId) {		
		
		List<CatalogEntity> entities = inventoryAccess.getAll(userId);
		
		// Convert CatalogEntity objects to CatalogModel objects
		List<CatalogModel> models = CatalogMapper.toModelList(entities);
		
		return models;
	}
	
	/**
	 * addInventory() adds a new catalog to the inventory.
	 * 
	 * @param catalog The CatalogModel object representing the catalog to be added.
	 * @return true if the catalog was added successfully, false otherwise.
	 */
	@Override
	public boolean addInventory(CatalogModel catalog) {
		try {
			// Check if catalog name already exists for user
			boolean created = inventoryAccess.create(
                    catalog.getName(),
                    catalog.getDescription(),
                    catalog.getImage(),
                    catalog.getColor(),
                    catalog.getUserId()
            );
			
			// Log results of catalog creation
			if (!created) {

                CSVLogger.log(
                        catalog.getUserId(),
                        "SERVICE",
                        "CATALOG_CREATE_FAILED",
                        "Failed to create catalog '" + catalog.getName() + "'",
                        ""
                );
                return false;
            }
            CSVLogger.log(
                    catalog.getUserId(),
                    "SERVICE",
                    "CATALOG_CREATE_SUCCESS",
                    "Catalog '" + catalog.getName() + "' created",
                    ""
            );
            return true;
            
		} catch (Exception e) {           
            CSVLogger.log(
                    catalog.getUserId(),
                    "SERVICE",
                    "CATALOG_CREATE_ERROR",
                    "Exception creating catalog '" + catalog.getName() + "'",
                    e.getMessage()
            );
            return false;
        }
    }
	
	/**
	 * getByNameAndId() retrieves a catalog by its name and user ID.
	 * 
	 * @param name   The name of the catalog.
	 * @param userId The ID of the user who owns the catalog.
	 * @return The CatalogEntity object representing the catalog.
	 */
	@Transactional
	@Override
	public CatalogEntity getByNameAndId(String name, int userId) {
		return inventoryAccess.getByNameAndId(name, userId);	
	}	

	/**
	 * addCatalogWithImage() orchestrates the creation of a new catalog with an optional image.
	 * This method handles validation, database insertion, and file storage in a single transaction.
	 * 
	 * @param catalog The CatalogModel containing catalog data
	 * @param imageFile The image file to store (can be null for no image)
	 * @param userId The ID of the user who owns the catalog
	 * @return The created CatalogEntity
	 * @throws Exception if validation fails or database operation fails
	 */
	@Override
	@Transactional
	public CatalogEntity addCatalogWithImage(CatalogModel catalog, MultipartFile imageFile, int userId) throws Exception {
		try {	
			if (inventoryAccess.existsByNameAndUserId(catalog.getName(), userId)) {				
				CSVLogger.log(
                        userId,
                        "SERVICE",
                        "CATALOG_CREATE_DUPLICATE",
                        "Catalog name '" + catalog.getName() + "' already exists",
                        ""
                );
				throw new Exception("Catalog name already exists for this user");
			}
			
			// Set the user ID on the catalog model
			catalog.setUserId(userId);
			
			boolean created = inventoryAccess.create(
					catalog.getName(),
					catalog.getDescription(),
					catalog.getImage(),
					catalog.getColor(),
					userId);
			
			if (!created) {
				CSVLogger.log(
                        userId,
                        "SERVICE",
                        "CATALOG_CREATE_FAILED",
                        "Failed to create catalog '" + catalog.getName() + "'",
                        ""
                );
				throw new Exception("Failed to create catalog in database");
			}
			
			// Retrieve the created catalog entity
			CatalogEntity catalogEntity = inventoryAccess.getByNameAndId(catalog.getName(), userId);
			
			if (catalogEntity == null || catalogEntity.getId() == 0) {
				CSVLogger.log(
                        userId,
                        "SERVICE",
                        "CATALOG_CREATE_RETRIEVE_FAILED",
                        "Catalog '" + catalog.getName() + "' created but could not be retrieved",
                        ""
                );
				throw new Exception("Failed to retrieve created catalog");
			}

			catalog.setCatalogId(catalogEntity.getId());
			
			// Handle image file storage if provided
			boolean updated = catalogService.updateCatalog(
	                catalog,
	                imageFile
	        );

	        if (!updated) {

	            CSVLogger.log(
	                    userId,
	                    "SERVICE",
	                    "CATALOG_UPDATE_FAILED",
	                    "Catalog '" + catalog.getName()
	                            + "' was created but could not be updated",
	                    ""
	            );

	            throw new Exception(
	                    "Catalog created but failed to update catalog"
	            );
	        }

	        if (imageFile != null && !imageFile.isEmpty()) {

	            CSVLogger.log(
	                    userId,
	                    "SERVICE",
	                    "CATALOG_IMAGE_STORE_SUCCESS",
	                    "Stored image for catalog '"
	                            + catalog.getName() + "'",
	                    ""
	            );

	        } else {

	            CSVLogger.log(
	                    userId,
	                    "SERVICE",
	                    "CATALOG_CREATE_NO_IMAGE",
	                    "Catalog '" + catalog.getName()
	                            + "' created without an image",
	                    ""
	            );
	        }

	        CSVLogger.log(
	                userId,
	                "SERVICE",
	                "CATALOG_CREATE_SUCCESS",
	                "Catalog '" + catalog.getName()
	                        + "' created with ID "
	                        + catalogEntity.getId(),
	                ""
	        );

	        return catalogEntity;

	    } catch (Exception e) {

	        CSVLogger.log(
	                userId,
	                "SERVICE",
	                "CATALOG_CREATE_ERROR",
	                "Error creating catalog '"
	                        + catalog.getName() + "'",
	                e.getMessage()
	        );

	        throw e;
	    }
	}
}