/**
 * CategoryModel.java
 */
package com.gcu.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This class represents a product category model.
 * It contains fields for the category ID, name, parent ID, and user ID.
 */
public class CategoryModel {
	
	private int id; 								
	@NotNull(message = "Product Category Name is required")
	@Size(min = 1, max=20, 
		  message = "Product Category Name " +
				  	"must be between 1 and " +
				  	"20 characters")				
	private String name; 							
	
	@Nullable
	private int parentId; 							
	
	@NotNull(message = "User ID was not found")
	private int userId; 							
	
	/**
	 * Default Constructor for the ProductCategoryModel class
	 * @param id
	 * @param name
	 * @param parentId
	 */
	public CategoryModel() {
		this.id = 0;
		this.userId = 0;
		this.name = "";
		this.parentId = 0;
	}
	
	/**
	 * Default Constructor for the ProductCategoryModel class with params
	 * @param id
	 * @param name
	 * @param parentId
	 */
	public CategoryModel(int id, int userID, String name, int parentId) {
		this.id = id;
		this.userId = userID; 
		this.name = name;
		this.parentId = parentId;
	}
	
	/************************************** GETTERS AND SETTERS **************************************/
	public int getId() { return id; } 										
	public void setId(int id) { this.id = id; } 							
	public String getName() { return name; } 								
	public void setName(String name) { this.name = name; } 					
	public int getParentId() { return parentId; } 							
	public void setParentId(int parentId) { this.parentId = parentId; } 	
	public int getUserId() { return userId; } 								
	public void setUserId(int userId) { this.userId = userId; } 			

}
