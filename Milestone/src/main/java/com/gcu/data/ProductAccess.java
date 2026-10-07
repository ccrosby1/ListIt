/**
 * ProductAccess.java
 */
package com.gcu.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import com.gcu.data.entity.ProductEntity;
import com.gcu.data.repository.ProductRepository;
import com.gcu.mapper.ProductMapper;
import com.gcu.utilities.CSVLogger;

/**
 * ProductAccess
 * This class provides access to the product data from the database.
 * It implements the ProductAccessInterface interface.
 * 
 * @param <T> The type of the entity (ProductEntity).
 */
@Service
public class ProductAccess implements ProductAccessInterface<ProductEntity> {
	@Autowired
	private ProductRepository productRepository;			
	@SuppressWarnings("unused")
	private DataSource dataSource;							
	private JdbcTemplate jdbcTemplate; 						
	
	
	/**
	 * Constructor
	 */
	public ProductAccess(ProductRepository productRepository, DataSource dataSource) {
		this.productRepository = productRepository;			
		this.dataSource = dataSource;						
		this.jdbcTemplate = new JdbcTemplate(dataSource);	
	}
	/**
	 * retrieves all products from the database.
	 * 
	 * @return List of ProductEntity objects representing all products in the database.
	 */
	@Override
	public List<ProductEntity> getAll() {
		String sql = "SELECT * FROM product";															
		List<ProductEntity> productEntities = jdbcTemplate.query(sql, new ProductMapper());				
		return productEntities;																			
	}
	
	/**
	 * retrieves all products from the database by catalog ID.
	 * 
	 * @param catalogId The ID of the catalog to retrieve products from.
	 * @return List of ProductEntity objects representing all products in the specified catalog.
	 */
	public List<ProductEntity> getAllProductsByCatalog(int catalogId) { 
		String sql = "SELECT p.* FROM product p JOIN catalog_product cp ON p.product_id "
				+ "= cp.product_product_id WHERE cp.catalog_catalog_id = ?";							
		
		List<ProductEntity> productEntities = jdbcTemplate.query(sql, new ProductMapper(), catalogId);	
		return productEntities;																			
	}

	/**
	 * retrieves a product by its ID.
	 * 
	 * @param prodcutId The ID of the product to retrieve.
	 * @return ProductEntity object representing the product with the given ID.
	 */
	@Override
	public ProductEntity getById(int prodcutId) {
		String sql = "SELECT * FROM product WHERE product_id = ?";								
		ProductEntity product = jdbcTemplate.query(sql, new ProductMapper(), prodcutId).get(0);	
		return product;
	}

	/**
	 * creates a new product in the database.
	 * 
	 * @param name The name of the product.
	 * @param brand The brand of the product.
	 * @param quatntity The quantity of the product.
	 * @param description The description of the product.
	 * @param image The image URL of the product.
	 * @param price The price of the product.
	 * @param createdDate The date when the product was created.
	 * @param updatedDate The date when the product was last updated.
	 * @param catId The category ID of the product.
	 * @return true if the product was created successfully, false otherwise.
	 */
	@Transactional
	@Override
	public boolean createProduct(String name, String brand,  int quantity, String description, String image, 
									double price, int categoryId, String createdDate, String updatedDate, int catalogId) {
		
		// create new product
		String sql1 = "INSERT INTO product "
							+ "(name, brand, quntity, description, image, price, created_date, updated_date, category_category_id) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
		
		// link product to catalog
		String sql2 = "INSERT INTO catalog_product "
							+ "(product_product_id, catalog_catalog_id) "
							+ "VALUES (?, ?)";											
				
		try {																			
			jdbcTemplate.update(sql1, name, brand, quantity, description, image,
								price,createdDate, updatedDate, categoryId);						
			
			// fetch newly inserted product so we can link it to catalog
			ProductEntity product2 = getNewProduct(name, brand, createdDate);						
			jdbcTemplate.update(sql2, product2.getProductId(), catalogId);				
			return true;																
		} catch (Exception e) {
			CSVLogger.log(
	                null,
	                "DATA",
	                "PRODUCT_CREATE_ERROR",
	                "Error creating product '" + name + "'",
	                e.getMessage()
	        );					
			TransactionAspectSupport
				.currentTransactionStatus()
				.setRollbackOnly();														
		}
		return false;																	
	}
	
	/**
	 * retrieves a newly created product from the database.
	 * 
	 * @param name The name of the product.
	 * @param brand The brand of the product.
	 * @param createdDate The date when the product was created.
	 * @return ProductEntity object representing the newly created product.
	 */
	public ProductEntity getNewProduct(String name, String brand, String createdDate) {
		String sql = "SELECT * "
				   + "FROM product "
				   + "WHERE name = ? "
				   + "AND brand = ? "
				   + "AND created_date = ?";
		
		// retrieve newly created product
		ProductEntity newProduct = jdbcTemplate
				.query(sql, new ProductMapper(), name, brand, createdDate).get(0);	
	    return newProduct;
	}
	
	/**
	 * updates an existing product in the database.
	 * 
	 * @param t The ProductEntity object representing the product to be updated.
	 * @return true if the product was updated successfully, false otherwise.
	 */
	@Transactional
	@Override
	public boolean update(ProductEntity entity) {
		String sql = "UPDATE product SET name = ?, brand = ?, quntity = ?, " +
		        "description = ?, price = ?, image = ?, " +
		        "updated_date = ?, category_category_id = ? WHERE product_id  = ?";							
		try {
			int rows = jdbcTemplate.update(
					sql, entity.getName(), 
					entity.getBrand(), entity.getQuantity(), 
					entity.getDescription(), entity.getPrice(), 
					entity.getImage(), entity.getUpdatedDate(),
					entity.getCategoryId(), entity.getProductId());											
			
			return rows > 0;
		} catch (Exception e) {
			CSVLogger.log(
	                null,
	                "DATA",
	                "PRODUCT_UPDATE_ERROR",
	                "Error updating product ID " + entity.getProductId(),
	                e.getMessage()
	        );
			TransactionAspectSupport
				.currentTransactionStatus()
				.setRollbackOnly(); 												
		}
		
		return false;
	}

	/**
	 * deletes a product from the database.
	 * 
	 * @param productId The ID of the product to be deleted.
	 * @param catalogId The ID of the catalog from which the product is to be deleted.
	 * @return true if the product was deleted successfully, false otherwise.
	 */
	@Transactional
	@Override
	public boolean delete(int productId) { 		
		try {
	        productRepository.deleteByProductId(productId);
	        return true;

	    } catch (Exception e) {
	        CSVLogger.log(
	            null,
	            "DATA",
	            "PRODUCT_DELETE_ERROR",
	            "Error deleting product ID " + productId,
	            e.getMessage()
	        );
	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        return false;
	    }
	}

	/**
	 * retrieves a product by its name and catalog ID.
	 * 
	 * @param name The name of the product to be retrieved.
	 * @param catalogId The ID of the catalog to which the product belongs.
	 * @return ProductEntity object representing the product with the given name and catalog ID.
	 */
	@Override
	public ProductEntity getByNameAndId(String name, int catalogId) {
		String sql = "SELECT p.* "
				   + "FROM product p "
				   + "JOIN catalog_product cp "
				   + "ON p.product_id = cp.product_product_id "
				   + "WHERE cp.catalog_catalog_id = ? "
				   + "AND p.name = ?";														
		return jdbcTemplate.query(sql, new ProductMapper(), catalogId, name).get(0);	

	}
	
	/**
	 * deletes all products associated with a given catalog ID.
	 * 
	 * @param catalogId The ID of the catalog whose products are to be deleted.
	 * @return true if all products were deleted successfully, false otherwise.
	 */
	@Override
	public void deleteAllProductsByCatalog(int catalogId) {
		// get product IDs
	    List<Integer> productIds = jdbcTemplate.queryForList(
	        "SELECT product_product_id FROM catalog_product WHERE catalog_catalog_id = ?",
	        Integer.class,
	        catalogId
	    );

	    // delete each product
	    for (Integer productId : productIds) {
	        delete(productId);
	    }
	}
}