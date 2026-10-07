/**
 * CatalogModel.java
 */
package com.gcu.model;

import java.util.ArrayList;
import java.util.List;

import com.gcu.utilities.Utilities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * CatalogModel.java
 * This class represents a Catalog Model for the Inventory Management System.
 * It contains properties for the catalog ID, name, description, image URL, color,
 * created date, updated date, and a list of products.
 */
public class CatalogModel {
	
	private int catalogId;				
	
	@NotNull(message = "Inventory Name is required")
	@Size(min = 1, max=20, message = "Inventory Name must be between 1 and 20 characters") 
	private String name;				

	private int userid;			
	private String username;
	private String description;		
	private String image;			
	private String color; 			
	private String createdDate;		
	private String updatedDate;		
	
	List<ProductModel> products; 
	
	/************************************* Constructors *************************************/
	/**
	 * Default constructor
	 */
	public CatalogModel() { 							
		this.catalogId = 0; 							
		this.name = ""; 								
		this.description = ""; 							
		this.image = ""; 								
		this.color = ""; 								
		setCreatedDate(Utilities.getCurrentTime()); 			
		setUpdatedDate(Utilities.getCurrentTime()); 			
		this.products = new ArrayList<ProductModel>(); 	
	}
	/**
	 * Constructor with parameters
	 * @param name
	 * @param description
	 */
	public CatalogModel(int catalogId, String name, String description, String image, String color, int userid) { 
		this.catalogId = catalogId;
		this.name = name;
		this.description = description;	
		this.image = image;								
		this.color = color; 							
		this.products = new ArrayList<ProductModel>(); 	
		setCreatedDate(Utilities.getCurrentTime()); 			
		setUpdatedDate(Utilities.getCurrentTime()); 			
		
	}
	
	/************************************* Getters and Setters *************************************/
	public int getId() { return this.catalogId; }												
	public void setCatalogId(int id) { catalogId = id; }								 
	
	public String getName() { return name; }											
	public void setName(String name) { this.name = name; }								
	
	public int getUserId() { return userid; }											
	public void setUserId(int userid) { this.userid = userid; }		
	
	public String getUsername() { return username; }											
	public void setUsername(String username) { this.username = username; }
	
	public String getDescription() { return description; }								
	public void setDescription(String description) { this.description = description; }	

	public String getImage() { return image; }											
	public void setImage(String image) { this.image = image; }							

	public String getColor() { return color; }											
	public void setColor(String color) { this.color = color; }							
	
	public List<ProductModel> getProducts() { return products; }						
	public void addProduct(ProductModel product) {  products.add(product); }			
	
	public String getCreatedDate() { return createdDate; }								
	public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }	
	
	public String getUpdatedDate() { return updatedDate; }								
	public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }	
	
	public int getCount() { return products.size(); }									
	public void setId(int id) { this.catalogId = id; }									

}
