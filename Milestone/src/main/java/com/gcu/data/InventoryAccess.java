/**
 * InventoryAccess.java
 */
package com.gcu.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcu.data.entity.CatalogEntity;
import com.gcu.mapper.CatalogMapper;
import com.gcu.utilities.CSVLogger;
import com.gcu.utilities.Utilities;

/**
 * InventoryAccess
 * This class provides access to the inventory data in the database.
 * It implements the InventoryAccessInterface and provides methods for CRUD operations on the inventory.
 */
@Service
@Transactional
public class InventoryAccess implements InventoryAccessInterface<CatalogEntity> {	
	@SuppressWarnings("unused")							
	private DataSource dataSource;				
	private JdbcTemplate jdbcTemplate;	
	
	/**
	 * Constructor
	 */
	public InventoryAccess(DataSource dataSource) {
		this.dataSource = dataSource;							
		this.jdbcTemplate = new JdbcTemplate(dataSource);	
	}
	
	
	/**
	 * retrieves all catalogs for a given user ID.
	 * 
	 * @param userid The ID of the user whose catalogs are to be retrieved.
	 * @return List of CatalogEntity objects representing the catalogs for the given user ID.
	 */
	@Override
	public List<CatalogEntity> getAll(int userId) {
		String sql = "SELECT * FROM catalog WHERE user_id = ?";
		return jdbcTemplate.query(sql, new CatalogMapper(), userId);
	}
	
	/**
	 * retrieves a catalog by its name.
	 * 
	 * @param name The CatalogEntity object containing the name of the catalog to be retrieved.
	 * @return CatalogEntity object representing the catalog with the given name.
	 */
	@Override
	public CatalogEntity getByNameAndId(String name, int userID) {
		String sql = "SELECT * FROM catalog WHERE name = ? AND user_id = ?"; 	
		return jdbcTemplate.queryForObject(sql, new CatalogMapper(), name, userID);
	}
	
	/**
	 * checks if a catalog with the given name exists for the user.
	 * This method is used for duplicate name validation without fetching the full entity.
	 * 
	 * @param name The name of the catalog to check
	 * @param userId The ID of the user who owns the catalog
	 * @return true if a catalog with the given name exists for the user, false otherwise
	 */
	public boolean existsByNameAndUserId(String name, int userId) {
		String sql = "SELECT COUNT(*) FROM catalog WHERE name = ? AND user_id = ?";	
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, userId);
		return count != null && count > 0;	
	}
	
	/**
	 * creates a new catalog.
	 * 
	 * @param name        The name of the catalog.
	 * @param description A description of the catalog.
	 * @param image       The image of the catalog.
	 * @param color       The color of the catalog.
	 * @param createdDate The date the catalog was created.
	 * @param updatedDate The date the catalog was last updated.
	 * @param userId      The ID of the user who owns the catalog.
	 * @return true if the catalog was created successfully, false otherwise.
	 */
	@Override
	public boolean create(String name, String description, String image, String color, int userId) throws DataAccessException {
		String sql = "INSERT INTO catalog (name, description, image, color, created_date, updated_date, user_id) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		String time = Utilities.getCurrentTime();
		try {
			jdbcTemplate.update(sql, name, description, image, color, time, time, userId);
			
			CatalogEntity catalog = getByNameAndId(name, userId);
			
			// verify catalog was actually created by fetching it back
			boolean created = (catalog != null && catalog.getId() > 0);
			
			CSVLogger.log(
		            userId,
		            "DATA",
		            "CREATE_CATALOG",
		            "Catalog '" + name + "' creation attempt",
		            created ? "" : "Catalog record not found after insert"
		        );

		        return created;
			
		} catch (Exception e) {

	        CSVLogger.log(
	            userId,
	            "DATA",
	            "CREATE_CATALOG_ERROR",
	            "Exception while creating catalog '" + name + "'",
	            e.getMessage()
	        );

	        return false;	
		}
	}
	
	/**
	 * Retrieves a catalog model by its ID.
	 * 
	 * @param catalogId The ID of the catalog to be retrieved.
	 * @return CatalogModel object representing the catalog with the given ID.
	 */
	public CatalogEntity getCatalogById(int catalogId) {
		String sql = "SELECT * FROM catalog WHERE catalog_id = ?";								
		return jdbcTemplate.queryForObject(sql, new CatalogMapper(), catalogId);
	}	
}