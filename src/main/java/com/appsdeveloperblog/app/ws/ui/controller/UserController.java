package com.appsdeveloperblog.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping(path= "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, 
			MediaType.APPLICATION_JSON_VALUE})
	public UserRest getUser(@PathVariable String id) {
		
		UserRest returnValue = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		
		
		return returnValue;
	}
	
	@PostMapping(
			consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty())throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping(path= {"/{id}"}, 
				consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
				produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		
		//return "update user was called";
		
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUserDetails(id,userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}
	
	@DeleteMapping(path= {"/{id}"},
					produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		 
		return returnValue;
	}
	
	
	//http://localhost:8080/users?page=1&limit=50
	@GetMapping(produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="1")int page, 
									@RequestParam(value="limit", defaultValue="25")int limit)
	{
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page,limit); 
		
		for(UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}
	
	//https://localhost:8080/mobile-app-ws/users/Id/addresses
	@GetMapping(path="{id}/addresses",
				produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<AddressesRest> getUserAddresses(@PathVariable String id){
		
		List<AddressesRest> returnValue = new ArrayList<>();
		
		List<AddressDto> addressDto = addressService.getAddresses(id); 
		
		  ModelMapper modelMapper = new ModelMapper();
		if(addressDto!=null && !addressDto.isEmpty())
		{
			java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			returnValue = modelMapper.map(addressDto, listType);
		}
		return returnValue;
	}
	
	//https://localhost:8080/mobile-app-ws/users/Id/addresses/addressId
	@GetMapping(path="{userId}/addresses/{addressId}",
				produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public AddressesRest getUserAddressesById(@PathVariable String userId, @PathVariable String addressId) {
		
		AddressesRest returnValue = new AddressesRest();
		
		AddressDto addressDto = addressService.getAddressById(userId, addressId);
		
		ModelMapper modelMapper = new ModelMapper();
		if(addressDto!=null) {
			returnValue = modelMapper.map(addressDto, AddressesRest.class);
		}
		else 
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		return returnValue;
	}
}
