/**
 * StorageServiceInterface.java
 * this interface defines the methods for storing and deleting files in a storage catalogService.
 * It includes methods for storing and deleting files, as well as getting the file path.
 * It also includes methods for storing and deleting catalog images.
 */
package com.gcu.business;

import org.springframework.web.multipart.MultipartFile;

import com.gcu.model.ProductModel;

import jakarta.validation.Valid;



/**
 * this interface defines the methods for storing and deleting files in a storage catalogService.
 * It includes methods for storing and deleting files, as well as getting the file path.
 * It also includes methods for storing and deleting catalog images.
 */
public interface StorageServiceInterface {

	/**
	 * This method deletes all files in the specified directory.
	 * @param fullDir
	 * @return
	 */
	public boolean deleteCatalogImages(int catalogId); // Method to delete catalog images
	/**
	 * This method gets the file path of the specified directory.
	 * @return
	 */
	public String getFilePath(); // Method to get the file path
	/**
	 * This method gets the file path of the specified directory.
	 * @param catalogId
	 * @return
	 */
	public boolean deleteCatalogImage(String filename, int catalogId); // Method to delete a specific catalog image
	/**
	 * This method stores a catalog image in the specified directory.
	 * @param file
	 * @param catalogId
	 * @return
	 */
	public String storeImage(MultipartFile file, String imageType, int catalogId, int productId);
	/**
	 * This method updates the product image in the specified directory.
	 * @param product
	 * @param imageFile
	 */
	public void updateProductImage(@Valid ProductModel product, MultipartFile imageFile);
}
