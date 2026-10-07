/**
 * ProductModel.java
 */
package com.gcu.model;

import com.gcu.utilities.Utilities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * ProductModel class represents a product with various attributes such as ID, name, brand, quantity, description, price, image, created date, updated date, and categories.
 * It includes validation annotations for input validation and provides constructors, getters, and setters for the attributes.
 */
public class ProductModel {
	
	private int productId; 																	
	private int catalogId; 																
	@NotBlank(message="Product Name is required") 										
	@Size(min=1, max=32, message="Product Name must be between 1 and 32 characters") 		
	private String name; 																
	
	@NotBlank(message="Product Brand is required") 										
	@Size(min=1, max=32, message="Product Brand must be between 1 and 32 characters")	
	private String brand; 																
	
	@NotNull(message="Product Quantity is required") 									
	private int quantity; 																
	
	private String description; 														
	private double price; 																
	private String image; 																
	private int categoryId; 
	private String categoryName;
	private String createdDate; 														
	private String updatedDate;	 	
	
	/**
	 * Default Constructor for the ProductModel class
	 * @param id
	 * @param name
	 * @param description
	 * @param price
	 * @param image
	 */
	public ProductModel() {
		this.productId = 0;
		this.catalogId = 0;
		this.name = "";
		this.categoryId = 0;
		this.description = "";
		this.price = 0.0;
		this.image = "";
	}
	/**
	 * Constructor for the ProductModel class with params
	 * @param id
	 * @param name
	 * @param description
	 * @param price
	 * @param image
	 */
	public ProductModel(int catId, String name, String brand, String description, 
										double price, int quantity, String image, int categoryId) {
		this.productId = 0; 					
		this.catalogId = catId; 
		this.name = name;
		this.brand = brand;
		this.categoryId = categoryId;
		this.quantity = quantity;
		this.description = description;
		this.price = price;
		this.image = image;
		setCreatedDate(Utilities.getCurrentTime()); 
		setUpdatedDate(Utilities.getCurrentTime()); 
	}
	/************************************** GETTERS AND SETTERS **************************************/
	public int getId() {
		return productId;
	}
	public void setId(int id) {
		this.productId = id;
	}
	
	public int getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

}
