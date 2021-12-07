package com.appsdeveloperblog.app.ws.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
	
	//JPA Native Query 
	@Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
			countQuery="select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
			nativeQuery=true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);
	
	
	//Query to find list of users whose firstName matches the parameters 
	@Query(value="select * from Users u where u.first_name = ?1",
			nativeQuery=true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	//Query to find list of users whose lastName matches the parameters
	@Query(value="select * from Users u where u.last_name = :lastName", 
			nativeQuery=true)
	List<UserEntity> findUserByLastName(@Param("lastName")String lastName);
	
	
}
