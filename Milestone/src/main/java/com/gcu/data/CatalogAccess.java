/**
 * CatalogAccess.java
 * 
 * This class provides access to the catalog data in the database.
 * It implements the CatalogAccessInterface and provides methods to retrieve, update, and delete catalog data.
 */
package com.gcu.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gcu.data.entity.CatalogEntity;
import com.gcu.mapper.CatalogMapper;
import com.gcu.model.CatalogModel;
import com.gcu.utilities.CSVLogger;

/**
 * This class provides access to the catalog data in the database.
 * It implements the CatalogAccessInterface and provides methods to retrieve, update, and delete catalog data.
 */
@Service
public class CatalogAccess implements CatalogAccessInterface<CatalogEntity> {
	@SuppressWarnings("unused")							
	private DataSource dataSource;					
	private JdbcTemplate jdbcTemplate;					
	
	/**
	 * Constructor
	 * 
	 * @param dataSource The DataSource object for database connection.
	 */
	public CatalogAccess(DataSource dataSource) {		
		this.dataSource = dataSource;						
		this.jdbcTemplate = new JdbcTemplate(dataSource);	
	}
	
	/**
	 * getAll() retrieves all catalogs for a given user ID.
	 * 
	 * @param userid The ID of the user whose catalogs are to be retrieved.
	 * @return List of CatalogEntity objects representing the catalogs for the given user ID.
	 */
	@Override
	public List<CatalogEntity> getAll() {
		String sql = "SELECT * FROM catalog";							
		return jdbcTemplate.query(sql, new CatalogMapper());												
	}	
	/**
	 * gets all catalogs for a given user ID.
	 * @Param userid The ID of the user whose catalogs are to be retrieved.
	 */
	@Override
	public CatalogModel getById(int id) {
		String sql="SELECT * FROM catalog WHERE catalog_id = ?";						
		try {
			CatalogEntity catalog = jdbcTemplate.queryForObject(sql,new CatalogMapper(), id);		
			
			return CatalogMapper.toModel(catalog);
			
		} catch (Exception e) {														
			CSVLogger.log(
		            null,
		            "DATA",
		            "GET_CATALOG_BY_ID_ERROR",
		            "Exception retrieving catalog ID " + id,
		            e.getMessage()
		        );

		        return null;
		    }														
	}
	
	/**
	 * Get a catalog by its name
	 * @param name The name of the catalog to retrieve.
	 */
	@Override
	public CatalogEntity getByName(String name) {
		
		return null;
	}
	/**
	 * update catalog with given catalog entity
	 * @param t
	 */
	@Override
	public boolean update(CatalogEntity t) {
		String sql = "UPDATE catalog "
				   + "SET name = ?, description = ?, "
				   + "image = ?, color = ?, updated_date = ? "
				   + "WHERE catalog_id = ?"; 									
		try {
			int rows = jdbcTemplate
					.update(sql, 
						   t.getName(), 
						   t.getDescription(),
						   t.getImage(), 
						   t.getColor(),
						   t.getUpdatedDate(),
						   t.getId()); 
			
			return rows > 0;
			
		} catch (Exception e) {
			CSVLogger.log(
		            t.getUserId(),
		            "DATA",
		            "UPDATE_CATALOG_ERROR",
		            "Exception updating catalog '" + t.getName() + "'",
		            e.getMessage()
		        );

		        return false;
		    }														
	}

	/**
	 * Deletes a catalog with the given catalog ID
	 * @param catalogId the ID of the catalog to delete
	 * @return true if the catalog was deleted successfully, false otherwise
	 */
	@Override
	public void delete(int catalogId) {
		jdbcTemplate.update(
		        "DELETE FROM catalog WHERE catalog_id = ?",
		        catalogId
		    );												
	}
	
	/**
	 * Find a catalog by its ID
	 * @param id the ID of the catalog to find
	 * @return the CatalogEntity object, null otherwise
	 */
	public CatalogEntity findByCatalogId(int id) {
		String sql = "SELECT * FROM catalog WHERE catalog_id = ?";								
		return jdbcTemplate.query(sql, new CatalogMapper(), id).get(0);																			
	}
	
	/**
	 * Get all catalog IDs for a given user ID
	 * @param userId The ID of the user whose catalog IDs are to be retrieved.
	 * @return List of catalog IDs for the given user ID.
	 */
	@Override
	public List<Integer> getCatalogIdsByUserId(int userId) {
		String sql = "SELECT catalog_id FROM catalog WHERE user_id = ?";						
		return jdbcTemplate.queryForList(sql, Integer.class, userId);
	}

	/**
	 * Count the number of shared catalogs for a given user ID
	 * @param userId The ID of the user whose shared catalogs are to be counted.
	 * @return The count of shared catalogs for the given user ID.
	 */
	public int countSharedCatalogs(int userId) {
		String sql = "SELECT COUNT(*) FROM catalog_share WHERE shared_user_id = ?";
	    try {
	        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
	    } catch (Exception e) {
	        CSVLogger.log(
	            userId,
	            "DATA",
	            "COUNT_SHARED_CATALOGS_ERROR",
	            "Error counting shared catalogs for user " + userId,
	            e.getMessage()
	        );
	        return 0;
	    }
	}
}
