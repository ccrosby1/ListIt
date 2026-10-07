/**
 * CatalogEntity.java
 * Database entity for the product table
 */
package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * This class represents a catalog entity in the database.
 * It contains fields for user information and provides getters and setters for each field.
 * The class is annotated with @Table to specify the database table name and @Column to specify the column names.
 */
@Table("Catalog")
public class CatalogEntity {

	@Id
	private int catalogid;
	
	@Column("name")
	private String name;
	
	@Column("description")
	private String description;
	
	@Column("image")
	private String image;
	
	@Column("color")
	private String color;
	
	@Column("created_date")
	private String createdDate;
	
	@Column("updated_date")
	private String updatedDate;
	
	@Column("User_id")
	private final int USER_ID;
	/*
	 *Default constructor
	 */
	 public CatalogEntity() {
		this.catalogid = 0; 
		this.name = "";
		this.description = "";
		this.image = "";
		this.color = "";
		this.createdDate = "";
		this.updatedDate = "";
		this.USER_ID = 0;
	}
	/**
	 * Constructor with parameters
	 * @param name
	 * @param description
	 * @param userId
	 */
	public CatalogEntity(int catalogId, String name, String description, String image, String color, 
			String createdDate, String updatedDate, int userId) {
		this.catalogid = catalogId; 
		this.name = name;
		this.description = description;
		this.image = image;
		this.color = color;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.USER_ID = userId;
	}
	/********************************************* GETTERS AND SETTERS *********************************************/
	/**
	 * Getter for the User name
	 * 
	 * @return user ID
	 */
	public String getName() { return name; }											
	/**
	 * Setter for the User name
	 * 
	 * @param name
	 */
	public void setName(String name) { this.name = name; }								

	/**
	 * Getter for the description
	 * 
	 * @return description
	 */
	public String getDescription() { return description; }								
	/**
	 * Setter for the description
	 * 
	 * @param description
	 */
	public void setDescription(String description) { this.description = description; }	

	/**
	 * Getter for the image
	 * 
	 * @return image
	 */
	public String getImage() { return image;}											
	/**
	 * Setter for the image
	 * 
	 * @param image
	 */
	public void setImage(String image) { this.image = image; }							

	/**
	 * Getter for the color
	 * 
	 * @return color
	 */
	public String getColor() {return color; }											
	/**
	 * Setter for the color
	 * 
	 * @param color
	 */
	public void setColor(String color) { this.color = color; }							

	/**
	 * Getter for the created date
	 * 
	 * @return created date
	 */
	public String getCreatedDate() { return createdDate; }								
	/**
	 * Setter for the created date
	 * 
	 * @param createdDate
	 */
	public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }	

	/**
	 * Getter for the updated date
	 * @return
	 */
	public String getUpdatedDate() { return updatedDate; }								
	/**
	 * Setter for the updated date
	 * 
	 * @param updatedDate
	 */
	public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }	

	/* 
	 * Getter for the Catalog ID
	 * This should never be changed after creation
	 * 
	 * @return catalog ID
	 */
	public int getId() { return catalogid; } 											
	 /* Setter for the Catalog ID
	 * 
	 * @param catalogid
	 */
	public void setId(int catalogid) { this.catalogid = catalogid; }					
	/**
	 * Getter for the user ID
	 * This should never be changed after creation
	 * 
	 * @return user ID
	 */
	public int getUserId() { return USER_ID;	} 		
	

}
