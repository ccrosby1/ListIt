/**
 * UserRepository
 */
package com.gcu.data.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gcu.data.entity.LoginEntity;

/**
 * LoginRepository
 * @param username the username to set
 */
public interface LoginRepository extends CrudRepository<LoginEntity, Integer> {
	
	@Query(value = "SELECT * FROM user_credentials WHERE username = :username AND password = :password")		
	public LoginEntity findByLoginInfo(@Param("username")String username, @Param("password")String password);
		
		
	@Query(value = "SELECT * FROM user_credentials WHERE user_id = :userId")									
	public LoginEntity findByUserId(@Param("userId")int userId);
	
	@Query(value = "SELECT username FROM user_credentials WHERE user_id = :userId")		
	public String findUserNameByUserId(@Param("userId")int userId);
	
	@Query(value = "SELECT roles_id FROM user_roles WHERE user_id = :userId")
	public int findRoleIdByUserId(@Param("userId")int userId);
	
	public LoginEntity findByUsername(String username);
	
	
}
