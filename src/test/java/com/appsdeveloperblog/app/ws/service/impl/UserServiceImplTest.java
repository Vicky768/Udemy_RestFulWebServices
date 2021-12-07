package com.appsdeveloperblog.app.ws.service.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {
	
	String testEmail = "test@Test.com";
	String testFirstName = "Sergey";
	String testUserId = "testuserid";
	String testAddressId = "testaddressid";
	String testEncodedPassword = "testencodedpassword";
	
	UserEntity userEntity ;
	
	@InjectMocks                               //this annotation will inject into the class , the mock objects that it requires
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this); 
		
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName(testFirstName);
		userEntity.setUserId(testUserId);
		userEntity.setEncryptedPassword(testEncodedPassword);
		userEntity.setAddresses(getAddressesEntity());
	}
	
	@Test
	final void testCreateUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null); 
		
		//utils.generatedAddressId(30)
		when(utils.generatedAddressId(anyInt())).thenReturn(testAddressId);
		
		//utils.generatedUserId(30)
		when(utils.generatedUserId(anyInt())).thenReturn(testUserId);
		
		//bCryptPasswordEncoder.encode(user.getPassword())
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(testEncodedPassword);
		
		//userRepository.save(userEntity);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName(testFirstName);
		userDto.setLastName("Kargopav");
		userDto.setPassword("122333");
		userDto.setEmail(testEmail);
		
		UserDto storedUserDetails = userService.createUser(userDto);
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName()); 
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName()); 
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		
		verify(utils,times(2)).generatedAddressId(30);
		verify(bCryptPasswordEncoder,times(1)).encode("122333");
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	private List<AddressDto> getAddressesDto(){
		
		AddressDto shippingAddressDto = new AddressDto();
		shippingAddressDto.setType("shipping");
		shippingAddressDto.setCity("Vancouver");
		shippingAddressDto.setCountry("Canada");
		shippingAddressDto.setPostalCode("ABC123");
		shippingAddressDto.setStreetName("123 Street name");
		
		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("Vancouver");
		billingAddressDto.setCountry("Canada");
		billingAddressDto.setPostalCode("ABC123");
		billingAddressDto.setStreetName("123 Street name");
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(billingAddressDto);
		addresses.add(shippingAddressDto);
		
		return addresses;
	}
	
	private List<AddressEntity> getAddressesEntity(){
		
		List<AddressDto> addresses = getAddressesDto();
		
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		
		return new ModelMapper().map(addresses, listType);
	}
	
	@Test
	final void testCreateUser_CreateUserServiceException()
	{
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName(testFirstName);
		userDto.setLastName("Kargopav");
		userDto.setPassword("122333");
		userDto.setEmail(testEmail);
		
		assertThrows(UserServiceException.class,
		() -> {
			userService.createUser(userDto);
		});
	}
	
	
	@Test
	final void testGetUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn( userEntity );
		//when a method findByEmail is called , it should return an object . In a sense, we are mocking 
		
		UserDto userDto = userService.getUser(testEmail);
		
		assertNotNull(userDto);
		assertEquals("Sergey", userDto.getFirstName());
	}
	
	@Test
	final void testGetUser_UsernameNotFoundException()
	{
		when( userRepository.findByEmail( anyString() ) ).thenReturn( null );
		
		//userService.getUser("test@test.com");
		
		//for JUNIT 5 
		assertThrows(UsernameNotFoundException.class, 
				()->{
					userService.getUser(testEmail);
				}
				);
		
	}
	
	
	
	
}
