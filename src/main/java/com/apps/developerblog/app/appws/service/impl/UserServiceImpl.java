package com.apps.developerblog.app.appws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apps.developerblog.app.appws.UserRepository;
import com.apps.developerblog.app.appws.io.entity.UserEntity;
import com.apps.developerblog.app.appws.service.UserService;
import com.apps.developerblog.app.appws.shared.Utils;
import com.apps.developerblog.app.appws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already present");
		
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		
		return returnValue;
	}

}
