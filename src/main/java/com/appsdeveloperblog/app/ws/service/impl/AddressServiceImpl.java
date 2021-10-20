package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String userId) {
		
		List<AddressDto> returnValue = new ArrayList<>();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity==null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		ModelMapper modelMapper = new ModelMapper();
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for(AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		return returnValue;
	}

	@Override
	public AddressDto getAddressById(String userId, String addressId) {
		
		AddressDto returnValue = new AddressDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity==null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		ModelMapper modelMapper = new ModelMapper();
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity==null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		returnValue = modelMapper.map(addressEntity, AddressDto.class);
		return returnValue;
	}

}
