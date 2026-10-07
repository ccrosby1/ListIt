/**
 * LoginMapper.java
 */
package com.gcu.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gcu.data.entity.LoginEntity;
import com.gcu.model.LoginModel;

/**
 * This class is responsible for mapping the result set from the database to a LoginEntity object.
 * It implements the RowMapper interface provided by Spring JDBC.
 */
public class LoginMapper implements RowMapper<LoginEntity> {

	/**
	 * Maps the result set to a LoginEntity object
	 * @param rs The result set from the database
	 * @param rowNum The row number
	 * @return A LoginEntity object
	 * @throws SQLException If there is an error accessing the result set
	 */
	@Override
	public LoginEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new LoginEntity(rs.getInt("user_id"), 
							   rs.getString("username"), 
							   rs.getString("password"));	
		
	}
	
	/**
	 * Maps login entity to login model
	 * @param entity login entity to map
	 * @return mapped LoginModel object
	 */
	public static LoginModel toModel(LoginEntity entity) {
		
		if (entity == null) {
	        return null;
	    }

	    LoginModel model = new LoginModel();

	    model.setUsername(entity.getUsername());
	    model.setPassword(entity.getPassword());

	    return model;
	}
}