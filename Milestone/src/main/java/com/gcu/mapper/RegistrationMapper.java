/**
 * RegistrationMapper.java
 * 
 * This class is responsible for mapping registartion objects between models and entities
 */
package com.gcu.mapper;

import com.gcu.data.entity.UserEntity;
import com.gcu.model.LoginModel;
import com.gcu.model.RegistrationModel;

public class RegistrationMapper {

	/**
    * Converts UserEntity and LoginModel to a RegistrationModel.
    *
    * @param userEnt the UserEntity
    * @param loginMod the LoginModel
    * @return the combined RegistrationModel
    */
   public static RegistrationModel toModel(
           UserEntity userEnt,
           LoginModel loginMod) {

       if (userEnt == null || loginMod == null) {
           return null;
       }

       RegistrationModel model = new RegistrationModel();

       model.setUsername(loginMod.getUsername());
       model.setPassword(loginMod.getPassword());
       model.setFirstName(userEnt.getFirstName());
       model.setLastName(userEnt.getLastName());
       model.setEmail(userEnt.getEmail());
       model.setPhone(userEnt.getPhone());
       model.setCreatedDate(userEnt.getCreatedDate());
       model.setUpdatedDate(userEnt.getUpdatedDate());

       return model;
   }
}