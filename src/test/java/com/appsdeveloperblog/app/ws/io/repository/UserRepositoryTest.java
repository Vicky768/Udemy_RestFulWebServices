package com.appsdeveloperblog.app.ws.io.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		
		//Prepare UserEntity 
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("Sachin");
		userEntity.setLastName("Tendulkar");
		userEntity.setUserId("1a2b3v");
		userEntity.setEncryptedPassword("xxx");
		userEntity.setEmail("sachemail@test.com");
		userEntity.setEmailVerificationStatus(true);
		
		//Prepare AddressEntity
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("abcdef");
		addressEntity.setCity("Vancouver");
		addressEntity.setCountry("Canada");
		addressEntity.setPostalCode("ABCDDE");
		addressEntity.setStreetName("1213 Street Name");
		
		List<AddressEntity> addresses = new ArrayList<>();
		addresses.add(addressEntity);
		
		userEntity.setAddresses(addresses);
		//userRepository.save(userEntity);
		
	}

	@Test
	final void testGetVerifiedUsers() {
		
		Pageable pageableRequest = PageRequest.of(0, 2);
		Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(pages);
		
		List<UserEntity> userEntities = pages.getContent();
		assertNotNull(userEntities);
		//assertTrue(userEntities.size()==1);
	}
	
	@Test
	final void testFindUserByFirstName()
	{
		String firstName = "Ram";
		List<UserEntity> users = userRepository.findUserByFirstName(firstName);
		assertNotNull(users);
		
	    assertTrue(users.size() == 2);
		
	}
	
	@Test
	final void testFindUserByLastName()
	{
		String lastName = "Tendulkar";
		List<UserEntity> users = userRepository.findUserByLastName(lastName);
		assertNotNull(users);
		
		assertTrue(users.size() == 2);
	}

}
