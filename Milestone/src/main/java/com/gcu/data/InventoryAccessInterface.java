/**
 * InventoryAccessInterface.java
 */
package com.gcu.data;

import java.util.List;

/**
 * InventoryAccessInterface
 * This interface defines the methods for accessing the inventory data from the database.
 * It is used by the InventoryAccess class.
 * @param <T> 
 */
public interface InventoryAccessInterface <T> {

	public List<T> getAll(int userid);								
	public T getByNameAndId(String t, int x);										
	boolean create(String name, 
				   String description, 
				   String image, 
				   String color, 
				   int userId);
}