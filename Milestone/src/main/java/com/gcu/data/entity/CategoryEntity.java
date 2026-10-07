/**
 * CategoryEntity.java
 * Database entity for the category table
 */
package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.annotation.Nullable;

/**
 * This class represents a category entity in the database.
 * It contains fields for user information and provides getters and setters for each field.
 * The class is annotated with @Table to specify the database table name and @Column to specify the column names.
 */
@Table("category")
public class CategoryEntity {

	@Id
	private final int CATEGORY_ID;
	
	@Column("name")
	private String name;
	
	@Column("parent_id")
	@Nullable
	private int parentId;
	
	@Column("user_id")
	private int userId;
	/**
	 * Default constructor
	 */
	public CategoryEntity() {
		this.CATEGORY_ID = 0;
		this.name = "";
		this.parentId = 0;
	}
	/**
	 * Constructor with parameters
	 * @param id
	 * @param name
	 * @param parentId
	 */
	public CategoryEntity(int id, String name, int parentId, int userId) {
		this.CATEGORY_ID = id;
		this.name = name;
		this.parentId = parentId;
		this.userId = userId;
	}
	/**************************************** GETTERS AND SETTERS ****************************************/
	/* 
	 * Getter for the Category ID
	 * This should never be changed after creation
	 * 
	 * @return the CATEGORY_ID
	 */
	public int getCategoryId() { return CATEGORY_ID;	}					
	
	/**
	 * Getter for the name
	 * @return the name
	 */
	public String getName() { return name; }								
	/**
	 * Set the name
	 * @param name the name to set
	 */
	public void setName(String name) { this.name = name; }					
	
	/**
	 * Getter for the parentId
	 * @return the parentId
	 */
	public int getParentId() { return parentId; }							
	/**
	 * Set the parentId
	 * @param parentId the parentId to set
	 */
	 public void setParentId(int parentId) { this.parentId = parentId; 	}	
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getId() {
		return this.CATEGORY_ID;
	}
	
}
