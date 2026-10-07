/**
 * UserMapper.java
 */
package com.gcu.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.RowMapper;

import com.gcu.data.entity.UserEntity;
import com.gcu.model.UserModel;

/**
 * This class is responsible for mapping the result set from the database to a UserEntity object.
 * It implements the RowMapper interface provided by Spring JDBC.
 */
public class UserMapper implements RowMapper<UserEntity> {
		
	/**
	 * Maps the result set to a UserEntity object
	 * @param rs The result set from the database
	 * @param rowNum The row number
	 * @return A UserEntity object
	 * @throws SQLException If there is an error accessing the result set
	 */
	@Override
	public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		
		String createDate = rs.getTimestamp("created_date")			
				.toLocalDateTime()
				.format(formatter);
		
		String updateDate = rs.getTimestamp("update_date")			
				.toLocalDateTime()
				.format(formatter);
		
		UserEntity user = new UserEntity(rs.getInt("user_id"),		
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("email"),
				rs.getString("phone"),
				createDate,
				updateDate);
		return user;
	}

	/**
     * Converts a UserEntity to a UserModel.
     *
     * @param entity the UserEntity to convert
     * @return the converted UserModel
     */
    public static UserModel toModel(UserEntity entity) {

        if (entity == null) {
            return null;
        }

        UserModel model = new UserModel();

        model.setUserId(entity.getId());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        model.setCreatedDate(entity.getCreatedDate());
        model.setUpdatedDate(entity.getUpdatedDate());

        return model;
    }

    /**
     * Converts a UserModel to a UserEntity.
     *
     * @param model the UserModel to convert
     * @return the converted UserEntity
     */
    public static UserEntity toEntity(UserModel model) {

        if (model == null) {
            return null;
        }

        UserEntity entity = new UserEntity();

        entity.setId(model.getUserId());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        entity.setEmail(model.getEmail());
        entity.setPhone(model.getPhone());
        entity.setCreatedDate(model.getCreatedDate());
        entity.setUpdatedDate(model.getUpdatedDate());

        return entity;
    }	
}
