/**
 * CatalogAccessInterface.java
 */
package com.gcu.data;

import java.util.List;

import com.gcu.model.CatalogModel;

/**
 * This interface defines the methods for accessing catalog data.
 * It provides methods to retrieve, update, and delete catalog data.
 * @param <T>
 */
public interface CatalogAccessInterface <T> {

	public List<T> getAll();								
	public CatalogModel getById(int t);											
	public T getByName(String t);										
	public boolean update(T t);										
	public void delete(int catalogId);										
	public List<Integer> getCatalogIdsByUserId(int userId);	
}
