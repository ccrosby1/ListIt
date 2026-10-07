/**
 * ProductMapper.java
 * This class is responsible for mapping the result set from the database to a ProductEntity object.
 */
package com.gcu.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;

import com.gcu.data.entity.ProductEntity;
import com.gcu.model.ProductModel;
import com.gcu.utilities.Utilities;

/**
 * This class is responsible for mapping the result set from the database to a ProductEntity object.
 * It implements the RowMapper interface provided by Spring JDBC.
 */
public class ProductMapper implements RowMapper<ProductEntity> {
	
	/**
	 * Maps the result set to a ProductEntity object
	 * @param rs The result set from the database
	 * @param rowNum The row number
	 * @return A ProductEntity object
	 * @throws SQLException If there is an error accessing the result set
	 */
	@Override
	public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		
		String createDate = rs.getTimestamp("created_date")			
				.toLocalDateTime()
				.format(formatter);
		
		String updateDate = rs.getTimestamp("updated_date")			
				.toLocalDateTime()
				.format(formatter);
		
		ProductEntity product = new ProductEntity(
				rs.getInt("product_id"),			
				rs.getString("name"),
				rs.getString("brand"),
				rs.getInt("quntity"),
				rs.getString("description"),
				rs.getDouble("price"),
				rs.getString("image"),
				createDate,
				updateDate,
				rs.getInt("category_category_id"));	
		return product;
	}

	/**
	 * Converts a ProductEntity to a ProductModel.
	 *
	 * @param entity The ProductEntity to convert
	 * @return The converted ProductModel, or null if the entity is null
	 */
	public static ProductModel toModel(ProductEntity entity) {

	    if (entity == null) {
	        return null;
	    }

	    ProductModel model = new ProductModel();

	    model.setId(entity.getProductId());
	    model.setName(entity.getName());
	    model.setBrand(entity.getBrand());
	    model.setDescription(entity.getDescription());
	    model.setImage(entity.getImage());
	    model.setPrice(entity.getPrice());
	    model.setQuantity(entity.getQuantity());
	    model.setCategoryId(entity.getCategoryId());
	    model.setCreatedDate(entity.getCreatedDate());
	    model.setUpdatedDate(entity.getUpdatedDate());

	    return model;
	}
	
	/**
	 * Converts a ProductEntity to a ProductModel.
	 *
	 * @param entity The ProductEntity to convert
	 * @param catalogId The catalog ID to assign to the ProductModel
	 * @return The converted ProductModel, or null if the entity is null
	 */
	public static ProductModel toModel(ProductEntity entity, int catalogId) {

	    if (entity == null) {
	        return null;
	    }

	    ProductModel model = new ProductModel();

	    model.setId(entity.getProductId());
	    model.setName(entity.getName());
	    model.setBrand(entity.getBrand());
	    model.setDescription(entity.getDescription());
	    model.setImage(entity.getImage());
	    model.setPrice(entity.getPrice());
	    model.setQuantity(entity.getQuantity());
	    model.setCatalogId(catalogId);
	    model.setCategoryId(entity.getCategoryId());
	    model.setCreatedDate(entity.getCreatedDate());
	    model.setUpdatedDate(entity.getUpdatedDate());

	    return model;
	}
	
	/**
	 * Converts a list of ProductEntity objects to a list of ProductModel objects.
	 *
	 * @param entities The list of ProductEntity objects to convert
	 * @param catalogId The catalog ID to assign to each ProductModel
	 * @return A list of converted ProductModel objects
	 */
	public static List<ProductModel> toModelList(List<ProductEntity> entities, int catalogId) {

		return entities.stream()
	            .map(entity -> toModel(entity, catalogId))
	            .collect(Collectors.toList());
	}
	
	/**
	  * Converts a ProductModel to a ProductEntity.
	  *
	  * @param model The ProductModel to convert
	  * @return The converted ProductEntity, or null if the model is null
	  */
	public static ProductEntity toEntity(ProductModel model) {

	    if (model == null) {
	        return null;
	    }

	    ProductEntity entity = new ProductEntity();

	    entity.setProductId(model.getId());
	    entity.setName(model.getName());
	    entity.setBrand(model.getBrand());
	    entity.setQuantity(model.getQuantity());
	    entity.setCategoryId(model.getCategoryId());
	    entity.setDescription(model.getDescription());
	    entity.setPrice(model.getPrice());
	    entity.setImage(model.getImage());
	    entity.setUpdatedDate(Utilities.getCurrentTime());

	    return entity;
	}
}
