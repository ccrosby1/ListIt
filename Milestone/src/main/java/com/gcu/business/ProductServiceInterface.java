/**
 * ProductServiceInterface.java
 */
package com.gcu.business;


import com.gcu.data.entity.ProductEntity;
import com.gcu.model.ProductModel;

/**
 * This interface is used to define the contract for the ProductService class
 */
public interface ProductServiceInterface {

	public boolean createProduct(ProductModel product);									
	public boolean deleteProduct(int productId);							
	public ProductModel getProductById(int productId);									
	public boolean updateProduct(ProductModel product);									
	public ProductEntity getProductByNameAndId(String name, int catalogId);				
}
