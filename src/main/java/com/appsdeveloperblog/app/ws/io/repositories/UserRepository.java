package com.appsdeveloperblog.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;


@Repository 
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	//UserEntity findUserByEmail(String email);
	
	//function made with find then By then the field by which u are looking
	UserEntity findByEmail(String email);
}
