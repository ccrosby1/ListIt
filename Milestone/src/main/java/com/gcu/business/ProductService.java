/**
 * ProductService.java
 */
package com.gcu.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gcu.data.ProductAccess;
import com.gcu.data.entity.ProductEntity;
import com.gcu.mapper.ProductMapper;
import com.gcu.model.ProductModel;
import com.gcu.utilities.CSVLogger;
import com.gcu.utilities.Utilities;

import jakarta.validation.Valid;

/**
 * This class is responsible for managing products in the application.
 * It provides methods to create, read, update, and delete products.
 */
@Service
public class ProductService implements ProductServiceInterface {	
	@Autowired
	private ProductAccess productAccess;
	@Autowired
	private StorageServiceInterface storageService;
	@Autowired
	private CategoryService categoryService;

	
	/**
	 * Adds a product model to the database
	 * @param product
	 */
	@Override
	public boolean createProduct(ProductModel product) { 		
		String time = Utilities.getCurrentTime(); 						
		
		try {	
			boolean created = productAccess.createProduct(
	                product.getName(),
	                product.getBrand(),
	                product.getQuantity(),
	                product.getDescription(),
	                product.getImage(),
	                product.getPrice(),
	                product.getCategoryId(),
	                time,
	                time,
	                product.getCatalogId()
	        );
				
			CSVLogger.log(
	                null,
	                "SERVICE",
	                created ? "PRODUCT_CREATE_SUCCESS" : "PRODUCT_CREATE_FAILED",
	                created
	                    ? "Product '" + product.getName() + "' created successfully"
	                    : "Failed to create product '" + product.getName() + "'",
	                ""
	        );

	        return created;
		} catch (Exception e) {
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "PRODUCT_CREATE_ERROR",
	                "Exception while creating product '" + product.getName() + "'",
	                e.getMessage()
	        );
		}
		return false;																	
	}
	
	/**
	 * Gets all products for a specific catalog
	 * @param catalogId
	 * @return
	 */
	public List<ProductModel> getAllProductsByCatalog(int catalogId) { 
		
		List<ProductEntity> productEntities = productAccess
				.getAllProductsByCatalog(catalogId); 						
		
		try {
			List<ProductModel> productModels = ProductMapper.toModelList(productEntities, catalogId);

		    for (ProductModel product : productModels) {
		        product.setCategoryName(categoryService.getCategoryNameById(product.getCategoryId()));
		    }
			
			return productModels;
		} catch (Exception e) {
			CSVLogger.log(
	                null,
	                "SERVICE",
	                "PRODUCT_GET_ALL_ERROR",
	                "Exception while retrieving products for catalog " + catalogId,
	                e.getMessage()
	        );
			
			return null;
		}
		
	}

	/**
	 * Deletes a product from the database
	 * @param productId
	 * @param catalogId
	 * @return 
	 */
	@Override
	public boolean deleteProduct(int productId) { 	
		try {
	        boolean deleted = productAccess.delete(productId);

	        CSVLogger.log(
	                null,
	                "SERVICE",
	                deleted ? "PRODUCT_DELETE_SUCCESS" : "PRODUCT_DELETE_FAILED",
	                deleted
	                    ? "Deleted product ID " + productId
	                    : "Failed to delete product ID " + productId,
	                    ""
	        );

	        return deleted;

	    } catch (Exception e) {
	        CSVLogger.log(
	                null,
	                "SERVICE",
	                "PRODUCT_DELETE_ERROR",
	                "Exception while deleting product ID " + productId,
	                e.getMessage()
	        );
	        return false;
	    } 			
	}

	/**
	 * Gets a product by id
	 * @param productId
	 * @return
	 */
	@Override
	public ProductModel getProductById(int productId) { 		
		
		ProductEntity entity = productAccess.getById(productId); 
		ProductModel product = ProductMapper.toModel(entity); 		
		
		return product; 
	}

	/**
	 * Updates a product in the database
	 * @param product
	 */
	@Override
	public boolean updateProduct(ProductModel product) {
		try {
			ProductEntity entity = ProductMapper.toEntity(product); 		
			
			boolean updated = productAccess.update(entity); 
			
			CSVLogger.log(
			        null,
			        "SERVICE",
			        updated ? "PRODUCT_UPDATE_SUCCESS" : "PRODUCT_UPDATE_FAILED",
			        updated
			            ? "Updated product '" + product.getName() + "' (ID " + product.getId() + ")"
			            : "Failed to update product '" + product.getName() + "' (ID " + product.getId() + ")",
			        ""
			);
			
			return updated;
		} catch (Exception e) {
			
			return false;
		}
	}

	/**
	 * Gets a product by name and catalog id
	 * @param name
	 * @param catalogId
	 * @return
	 */
	@Override
	public ProductEntity getProductByNameAndId(String name, int catalogId) {
		ProductEntity product = productAccess
									.getByNameAndId(name, catalogId); 				
		return product;																
	}

	/**
	 * Creates a product and handles the image upload
	 * @param product
	 * @param imageFile
	 * @return
	 */
	public ProductEntity createProductAndHandleImage(@Valid ProductModel product, MultipartFile imageFile) {
		 boolean created = createProduct(product);
		    if (!created) {
		    	CSVLogger.log(
		                null,
		                "SERVICE",
		                "PRODUCT_CREATE_IMAGE_FAILED",
		                "Product created but image workflow aborted for '" + product.getName() + "'",
		                ""
		        );
		        return null;
		    }

		    ProductEntity saved = getProductByNameAndId(product.getName(), product.getCatalogId());
		    // fetch saved entity to attach image metadata
		    if (saved == null) {
		    	CSVLogger.log(
		                null,
		                "SERVICE",
		                "PRODUCT_CREATE_IMAGE_ERROR",
		                "Product saved but could not retrieve entity for '" + product.getName() + "'",
		                ""
		        );
		        return null;
		    }
		        
		    if (imageFile != null && !imageFile.isEmpty()) {

		        product.setId(saved.getProductId());

		        // Storage service generates the unique filename and stores the image
		        String fileName = storageService.storeImage(
		                imageFile,
		                "product",
		                product.getCatalogId(),
		                saved.getProductId()
		        );

		        if (fileName == null) {
		            CSVLogger.log(
		                    null,
		                    "SERVICE",
		                    "PRODUCT_IMAGE_STORE_FAILED",
		                    "Failed to store image for product '"
		                            + product.getName() + "'",
		                    ""
		            );
		        } else {
		            product.setImage(fileName);

		            CSVLogger.log(
		                    null,
		                    "SERVICE",
		                    "PRODUCT_IMAGE_STORE_SUCCESS",
		                    "Stored image '" + fileName
		                            + "' for product '" + product.getName() + "'",
		                    ""
		            );
		        }
		}

		updateProduct(product);
		return saved;
	}
}