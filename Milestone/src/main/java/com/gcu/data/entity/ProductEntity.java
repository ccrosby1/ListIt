/**
 * ProductEntity.java
 * Database entity for the product table
 */
package com.gcu.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * This class represents a product entity in the database.
 * It contains fields for user information and provides getters and setters for each field.
 * The class is annotated with @Table to specify the database table name and @Column to specify the column names.
 */
@Table("product")
public class ProductEntity {

	@Id
	private Integer productId;	
	
	@Column("name")
	private String name;
	
	@Column("brand")
	private String brand;
	
	@Column("quantity")
	private int quantity;
	
	@Column("description")
	private String description;
	
	@Column("image")
	private String image;
	
	@Column("price")
	private double price;
	
	@Column("created_date")
	private String createdDate;
	
	@Column("updated_date")
	private String updatedDate;
	
	@Column("Category_category_id")
	private int categoryId;
	/**
	 * Default constructor
	 */ 
	public ProductEntity() {
		this.productId = 0;
		this.name = "";
		this.brand = "";
		this.quantity = 0;
		this.description = "";
		this.image = "";
		this.price = 0.0;
		this.categoryId = 0;
		this.createdDate = "";
		this.updatedDate = "";
	}
	/**
	 * Constructor with parameters excluding the product ID
	 * @param id
	 * @param name
	 * @param brand
	 * @param quantity
	 * @param description
	 * @param image
	 * @param price
	 * @param categoryId
	 * @param createdDate
	 * @param updatedDate
	 */
	public ProductEntity(int productId, String name, String brand, int quantity, String description, 
						 String image, double price, int categoryId, String createdDate, String updatedDate) {
		this.productId = productId;
		this.name = name;
		this.brand = brand;
		this.quantity = quantity;
		this.description = description;
		this.image = image;
		this.price = price;
		this.categoryId = categoryId;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		
	}
	
	/**
	 * Constructor with parameters including the product ID
	 * @param productId
	 * @param name
	 * @param brand
	 * @param quantity
	 * @param description
	 * @param image
	 * @param price
	 * @param createdDate
	 * @param updatedDate
	 */
	public ProductEntity(int productId, String name, String brand, int quantity, String description, 
						 double price, String image, String createdDate, String updatedDate, int categoryId) {
		this.productId = productId;
		this.name = name;
		this.brand = brand;
		this.quantity = quantity;
		this.description = description;
		this.image = image;
		this.price = price;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.categoryId = categoryId;
	}	
	/**************************************** GETTERS AND SETTERS ****************************************/
	/**
	 * Getter for the product name
	 * @return the name
	 */
	public String getName() { return name; 	}												
	/**
	 * Setter for the product name
	 * @param name
	 */
	public void setName(String name) { this.name = name; }									
	
	/**
	 * Getter for the product brand
	 * @return the brand
	 */
	public String getBrand() { return brand; }												
	/**
	 * Setter for the product brand
	 * @param brand
	 */
	public void setBrand(String brand) { this.brand = brand; }								
	
	/**
	 * Getter for the product quantity
	 * @return the quantity
	 */
	public int getQuantity() { return quantity; }											
	/**
	 * Setter for the product quantity
	 * @param quantity
	 */
	public void setQuantity(int quantity) { this.quantity = quantity; }						
	
	/**
	 * Getter for the product description
	 * @return the description
	 */
	public String getDescription() { return description; }									
	/**
	 * Setter for the product description
	 * @param description
	 */
	public void setDescription(String description) { this.description = description; }		
	
	/**
	 * Getter for the product image
	 * @return the image
	 */
	public String getImage() { return image; }												
	/**
	 * Setter for the product image
	 * @param image
	 */
	public void setImage(String image) { this.image = image; }								
	
	/**
	 * Getter for the product price
	 * @return the price
	 */
	public double getPrice() { return price; }												
	/**
	 * Setter for the product price
	 * @param price
	 */
	public void setPrice(double price) { this.price = price; }								
	
	/**
	 * Getter for the created date
	 * @return the created date
	 */
	public String getCreatedDate() { return createdDate; }									
	/**
	 * Setter for the created date
	 * @param createdDate
	 */
	public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }		
	
	/**
	 * Getter for the updated date
	 * @return the updated date
	 */
	public String getUpdatedDate() { return updatedDate; }									
	/**
	 * Setter for the updated date
	 * @param updatedDate
	 */
	public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }		
	
	/**
	 * Getter for the product ID
	 * @return the product ID
	 */
	public int getProductId() { return productId; }											
	public void setProductId(int productId) { this.productId = productId; }					
	/**
	 * Getter for the category ID
	 * @return the category ID
	 */
	public int getCategoryId() { return categoryId; }										
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId; }														
	
}
