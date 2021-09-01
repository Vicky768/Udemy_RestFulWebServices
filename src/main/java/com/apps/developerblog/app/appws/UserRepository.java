package com.apps.developerblog.app.appws;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.apps.developerblog.app.appws.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
}
