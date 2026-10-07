/**
 * CatalogMapper.java
 */
package com.gcu.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;

import com.gcu.data.entity.CatalogEntity;
import com.gcu.model.CatalogModel;
import com.gcu.utilities.Utilities;

/**
 * This class is responsible for mapping the result set from the database to a CatalogEntity object.
 * It implements the RowMapper interface provided by Spring JDBC.
 */
public class CatalogMapper implements RowMapper<CatalogEntity> {
	
	/**
	 * Maps the result set to a CatalogEntity object
	 * @param rs The result set from the database
	 * @param rowNum The row number
	 * @return A CatalogEntity object
	 * @throws SQLException If there is an error accessing the result set
	 */
	@Override
	public CatalogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		
		String createDate = rs.getTimestamp("created_date")			
				.toLocalDateTime()
				.format(formatter);
		
		String updateDate = rs.getTimestamp("updated_date")			
				.toLocalDateTime()
				.format(formatter);
		
		CatalogEntity catalog = new CatalogEntity(			
				rs.getInt("catalog_id"),				
				rs.getString("name"),
				rs.getString("description"),
				rs.getString("image"),
				rs.getString("color"),
				createDate,
				updateDate,
				rs.getInt("user_id"));	
		
		catalog.setId(rs.getInt("catalog_id"));			
		
		return catalog;
	}
	
	/**
     * Converts a CatalogEntity to a CatalogModel
     *
     * @param entity the CatalogEntity to convert
     * @return the converted CatalogModel
     */
    public static CatalogModel toModel(CatalogEntity entity) {

        if (entity == null) return null;

        CatalogModel model = new CatalogModel();

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setColor(entity.getColor());
        model.setUserId(entity.getUserId());
        model.setDescription(entity.getDescription());
        model.setImage(entity.getImage());
        model.setCreatedDate(entity.getCreatedDate());
        model.setUpdatedDate(entity.getUpdatedDate());

        return model;
    }

    /**
     * Converts a List of CatalogEntity to a List of CatalogModel
     *
     * @param entities the List of CatalogEntity to convert
     * @return the converted List of CatalogModel
     */
    public static List<CatalogModel> toModelList(List<CatalogEntity> entities) {

        return entities.stream()
                .map(CatalogMapper::toModel)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a CatalogModel to a CatalogEntity.
     *
     * @param model the CatalogModel to convert
     * @return the converted CatalogEntity, or null if the model is null
     */
    public static CatalogEntity toEntity(CatalogModel model) {

        if (model == null) {
            return null;
        }

        CatalogEntity entity = new CatalogEntity();

        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setColor(model.getColor());
        entity.setImage(model.getImage());
        entity.setUpdatedDate(Utilities.getCurrentTime());

        return entity;
    }
}