package com.apps.developerblog.app.appws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.developerblog.app.appws.service.UserService;
import com.apps.developerblog.app.appws.shared.dto.UserDto;
import com.apps.developerblog.app.appws.ui.model.request.UserDetailsRequestModel;
import com.apps.developerblog.app.appws.ui.model.response.UserRest;



@RestController
@RequestMapping("users")   //  http://localhost:8080/users/1
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public String getUser(){
		return "get user was called";
	}
	
	
	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel requestUserDetails) {
		
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(requestUserDetails, userDto);
		
		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@PutMapping
	public String updateUser() {
		return "put was called";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "delete was called";
	}
}
