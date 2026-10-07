/**
 * ProductAccessInterface.java
 */
package com.gcu.data;

import java.util.List;

/**
 * ProductAccessInterface
 * This interface defines the methods for accessing the product data from the database.
 * It is used by the ProductAccess class.
 * @param <T> 
 */
public interface ProductAccessInterface <T> {
	
	public List<T> getAll();										
	public T getById(int prodcutId);											
	public boolean update(T t);										
	public boolean delete(int productId);										
	public T getByNameAndId(String name, int catalogId);
	boolean createProduct(String name, String brand, int quantity, String description, String image, double price,
	int categoryId, String createdDate, String updatedDate, int catalogId);
	void deleteAllProductsByCatalog(int catalogId);			
}