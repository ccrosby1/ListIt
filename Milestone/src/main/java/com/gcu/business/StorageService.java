/**
 * StorageService.java
 *
 * This class provides methods for storing, deleting, and managing files in a specified directory.
 * It implements the StorageServiceInterface.
 * It uses Java NIO for file operations and Spring's MultipartFile for handling file uploads.
 * 
 */
package com.gcu.business;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gcu.model.ProductModel;
import com.gcu.utilities.CSVLogger;

import jakarta.validation.Valid;

/**
 * This class provides methods for storing, deleting, and managing files in a specified directory.
 * It implements the StorageServiceInterface.
 * It uses Java NIO for file operations and Spring's MultipartFile for handling file uploads.
 * 
 */
@Service
public class StorageService implements StorageServiceInterface {
	 private final Path rootPath = Paths.get("")
	            .toAbsolutePath()
	            .resolve("uploads/catalogs");
	
	/* * This method returns the path to the directory where files are stored.
	 * @return The path to the directory.
	 */
	@Override
	public String getFilePath() {
		return rootPath.toString(); 									
	}
	
	/**
	 * This method returns the path to the directory for a specific catalog ID.
	 * It creates the directory if it does not exist.
	 * @param catalogId The ID of the catalog.
	 * @return The path to the catalog's directory.
	 * @throws Exception If an error occurs while creating the directory.
	 */
	private Path getCatalogDir(int catalogId) throws Exception {
		
        Path catalogDir = rootPath.resolve(String.valueOf(catalogId));
        // ensure catalog directory exists before storing files;
        Files.createDirectories(catalogDir);
        
        CSVLogger.log(
                null,
                "SERVICE",
                "CREATE_DIR",
                "Created/verified catalog directory for ID " + catalogId,
                ""
        );
        
        return catalogDir;
    }

	/**
	 * This method deletes all images associated with a specific catalog ID.
	 * @param catalogId The ID of the catalog whose images will be deleted.
	 * @return true if the images were deleted successfully, false otherwise.
	 */
	@Override
	public boolean deleteCatalogImages(int catalogId) {
		try {
            Path catalogDir = rootPath.resolve(String.valueOf(catalogId));

            if (!Files.exists(catalogDir)) {
            	
            	CSVLogger.log(
                        null,
                        "SERVICE",
                        "DELETE_IMAGES",
                        "Catalog directory does not exist for ID " + catalogId,
                        ""
                );
            	
            	return false;
            }

            Files.walk(catalogDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            
                            CSVLogger.log(
                                    null,
                                    "SERVICE",
                                    "DELETE_IMAGE",
                                    "Deleted file: " + path.getFileName(),
                                    ""
                            );
                            
                        } catch (Exception e) {
                        	CSVLogger.log(
                                    null,
                                    "SERVICE",
                                    "DELETE_IMAGE_ERROR",
                                    "Failed to delete file: " + path.getFileName(),
                                    e.getMessage()
                            );
                        }
                    });

            return true;

        } catch (Exception e) {
            CSVLogger.log(
                    null,
                    "SERVICE",
                    "DELETE_CATALOG_ERROR",
                    "Failed to delete catalog folder for ID " + catalogId,
                    e.getMessage()
                    );
                    
            return false;
        }
    }												

	/**
	 * This method stores a catalog image in the specified directory.
	 * @param file The file to be stored.
	 * @param imageType text prefix to help identify the type of image being stored
	 * @param catalogId The ID of the catalog where the image will be stored.
	 * @param entityId The ID of the entity (product or catalog) being stored
	 * @return true if the image was stored successfully, false otherwise.
	 */
	@Override
	public String storeImage(MultipartFile file, String imageType, int catalogId, int entityId) {
		try {
			// check that image file was provided
			if (file == null || file.isEmpty()) {
	            CSVLogger.log(
	                    null,
	                    "SERVICE",
	                    "STORE_IMAGE_SKIPPED",
	                    "No file provided for "
	                            + imageType + " ID " + entityId,
	                    ""
	            );
	        	
	        	return null;
	        }
	        
	        String originalFileName = file.getOriginalFilename();
	        
	        // check that file name was provided
	        if (originalFileName == null || originalFileName.isEmpty()) {
	            CSVLogger.log(
	                    null,
	                    "SERVICE",
	                    "STORE_IMAGE_SKIPPED",
	                    "No file name provided for "
	                            + imageType + " ID " + entityId,
	                    ""
	            ); 
	        	
	        	return null; 
	        	
	        }
	        
	        // Preserve the original file extension
	        String extension = "";
	        int extensionIndex = originalFileName.lastIndexOf('.');
	        if (extensionIndex > 0) {
	            extension = originalFileName.substring(extensionIndex);
	        }
	        
	        // Generate a unique filename
	        String fileName =
	                imageType
	                        + "_" + entityId
	                        + "_" + UUID.randomUUID()
	                        + extension;
	        
	        // Store image in the catalog directory
	        Path catalogDir = getCatalogDir(catalogId);
	        Path filePath = catalogDir.resolve(fileName);

	        Files.copy(
	        		file.getInputStream(), 
	        		filePath, 
	        		StandardCopyOption.REPLACE_EXISTING
	           	);

	        CSVLogger.log(
	                null,
	                "SERVICE",
	                "STORE_IMAGE",
	                "Stored " + imageType + " image '"
	                        + fileName + "' for catalog ID "
	                        + catalogId,
	                ""
	        );
	        
	        return fileName;

	    } catch (Exception e) {
	    	CSVLogger.log(
	                null,
	                "SERVICE",
	                "STORE_IMAGE_ERROR",
	                "Failed to store " + imageType
	                        + " image for entity ID " + entityId
	                        + " in catalog ID " + catalogId,
	                e.getMessage()
	        );
	       
	        return null;
	    }											
	}
	
	/**
	 * This method deletes a specific catalog image.
	 * @param file The name of the file to be deleted.
	 * @param catalogId The ID of the catalog where the image is located.
	 * @return true if the image was deleted successfully, false otherwise.
	 */
	@Override
	public boolean deleteCatalogImage(String file, int catalogId) {
		try {
            Path catalogDir = rootPath.resolve(String.valueOf(catalogId));
            Path filePath = catalogDir.resolve(file);

            boolean deleted = Files.deleteIfExists(filePath);
            
            CSVLogger.log(
                    null,
                    "SERVICE",
                    "DELETE_IMAGE",
                    "Deleted image '" + file + "' for catalog ID " + catalogId,
                    deleted ? "" : "File did not exist"
            );
            
            return deleted;

        } catch (Exception e) {
            CSVLogger.log(
                    null,
                    "SERVICE",
                    "DELETE_IMAGE_ERROR",
                    "Failed to delete image '" + file + "' for catalog ID " + catalogId,
                    e.getMessage()
            );
            
            return false;
        }												
	}

	/**
	 * This method updates the product image for a given product model.
	 * It deletes the old image if it exists and stores the new image.
	 * @param product The product model containing the catalog ID and old image name.
	 * @param imageFile The new image file to be stored.
	 */
	@Override
	public void updateProductImage(@Valid ProductModel product, MultipartFile imageFile) {
		try {
	        // No new image uploaded
	        if (imageFile == null || imageFile.isEmpty()) {
	        	CSVLogger.log(
                        null,
                        "SERVICE",
                        "UPDATE_IMAGE_SKIPPED",
                        "No new image uploaded for product ID " + product.getId(),
                        ""
                );
	        	
	            return;
	        }

	        int catalogId = product.getCatalogId();
	        Path catalogDir = getCatalogDir(catalogId);

	        String oldFileName = product.getImage();
	        
	        // Store the new image first
	        String newFileName = storeImage(
	                imageFile,
	                "product",
	                catalogId,
	                product.getId()
	        );

	        if (newFileName == null) {
	            CSVLogger.log(
	                    null,
	                    "SERVICE",
	                    "UPDATE_IMAGE_STORE_FAILED",
	                    "Failed to store new image for product ID "
	                            + product.getId(),
	                    ""
	            );
	            return;
	        }

	        // Delete old image if it exists
	        if (oldFileName != null && !oldFileName.isEmpty()) {
	            Path oldFilePath = catalogDir.resolve(oldFileName);
	            Files.deleteIfExists(oldFilePath);
	            
	            CSVLogger.log(
                        null,
                        "SERVICE",
                        "DELETE_OLD_IMAGE",
                        "Deleted old image '" + oldFileName + "' for product ID " + product.getId(),
                        ""
                );
	        }

	        // Update product model
	        product.setImage(newFileName);
	        
	        CSVLogger.log(
                    null,
                    "SERVICE",
                    "UPDATE_IMAGE",
                    "Updated product image to '" + newFileName + "' for product ID " + product.getId(),
                    ""
            );

	    } catch (Exception e) {  
	        CSVLogger.log(
                    null,
                    "SERVICE",
                    "UPDATE_IMAGE_ERROR",
                    "Failed to update product image for product ID " + product.getId(),
                    e.getMessage()
            );
	    }
	}
	
	/**
	 * Deletes the entire catalog folder for a given catalog ID,
	 * including all images and the directory itself.
	 *
	 * @param catalogId The ID of the catalog whose folder should be deleted.
	 * @return true if the folder was deleted successfully, false otherwise.
	 */
	public boolean deleteCatalogFolder(int catalogId) {
	    try {
	        Path catalogDir = rootPath.resolve(String.valueOf(catalogId));

	        // Folder does not exist
	        if (!Files.exists(catalogDir)) {
	            CSVLogger.log(
	                    null,
	                    "SERVICE",
	                    "DELETE_FOLDER",
	                    "Catalog folder does not exist for ID " + catalogId,
	                    ""
	            );
	            return false;
	        }

	        // Walk and delete files + folder
	        Files.walk(catalogDir)
	                .sorted(Comparator.reverseOrder())
	                .forEach(path -> {
	                    try {
	                        Files.delete(path);

	                        CSVLogger.log(
	                                null,
	                                "SERVICE",
	                                "DELETE_FOLDER_ITEM",
	                                "Deleted: " + path.toString(),
	                                ""
	                        );

	                    } catch (Exception e) {
	                        CSVLogger.log(
	                                null,
	                                "SERVICE",
	                                "DELETE_FOLDER_ITEM_ERROR",
	                                "Failed to delete: " + path.toString(),
	                                e.getMessage()
	                        );
	                    }
	                });

	        CSVLogger.log(
	                null,
	                "SERVICE",
	                "DELETE_FOLDER",
	                "Successfully deleted catalog folder for ID " + catalogId,
	                ""
	        );

	        return true;

	    } catch (Exception e) {
	        CSVLogger.log(
	                null,
	                "SERVICE",
	                "DELETE_FOLDER_ERROR",
	                "Failed to delete catalog folder for ID " + catalogId,
	                e.getMessage()
	        );
	        return false;
	    }
	}
}