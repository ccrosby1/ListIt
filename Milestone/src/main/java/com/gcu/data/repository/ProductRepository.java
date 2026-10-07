/**
 * ProductRepository.java
 */
package com.gcu.data.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.ProductEntity;

/**
 * ProductRepository
 * This interface extends CrudRepository to provide CRUD operations for ProductEntity.
 * It also includes custom query methods to find products by name, brand, and created date.
 */
public interface ProductRepository extends CrudRepository<ProductEntity, Integer> {
	
	@Query(value = "SELECT * FROM product WHERE name = :name AND brand = :brand AND created_date = :created_date")	
	public ProductEntity findNewProduct(@Param("name") String name, 
										@Param("brand") String brand, 
										@Param("created_date") String createdDate);	
	
	@Query(value = "SELECT product_id FROM product WHERE name = :name AND brand = :brand AND created_date = :created_date")	
	public int findProductId(@Param("name") String name, @Param("brand") String brand, 
					@Param("created_date") String createdDate);	
	
	@Modifying
	@Query(value = "DELETE FROM product WHERE product_id = :productId")
	public void deleteByProductId(int productId);	
	
}
