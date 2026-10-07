/**
 * CategoryMapper.java
 * Mapping class for CategoryEntity
 */
package com.gcu.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;

import com.gcu.data.entity.CategoryEntity;
import com.gcu.model.CategoryModel;

/**
 * This class is responsible for mapping the result set from the database to a CategoryEntity object.
 * It implements the RowMapper interface provided by Spring JDBC.
 */
public class CategoryMapper implements RowMapper<CategoryEntity> {

	@Override
	public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new CategoryEntity(rs.getInt("category_id"),
								  rs.getString("name"),
								  rs.getInt("parent_id"),
								  rs.getInt("user_id"));	
	}

	/**
	 * Converts a CategoryEntity to a CategoryModel
	 * @param model the CategoryEntity to convert
	 * @return the converted CategoryModel
	 */
	public static CategoryModel toModel(CategoryEntity entity) {
        if (entity == null) return null;

        return new CategoryModel(
            entity.getCategoryId(),
            entity.getUserId(),
            entity.getName(),
            entity.getParentId()
        );
    }
	
	/**
	 * Converts a List of CategoryEntity to a List of CategoryModel
	 * @param model the List of CategoryEntity to convert
	 * @return the converted List of CategoryModel
	 */
	public static List<CategoryModel> toModelList(List<CategoryEntity> entities) {
        return entities.stream()
                       .map(CategoryMapper::toModel)
                       .collect(Collectors.toList());
    }

}
