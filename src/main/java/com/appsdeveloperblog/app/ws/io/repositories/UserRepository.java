package com.appsdeveloperblog.app.ws.io.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;


@Repository 
//public interface UserRepository extends CrudRepository<UserEntity, Long> {
  public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>{
	
	//PagingandSorting is for pagination
	
	//UserEntity findUserByEmail(String email);
	
	//function made with find then By then the field by which u are looking
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
}
