package com.cricket.cricsheet.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cricket.cricsheet.model.UserProperty;

public interface UserRepository extends MongoRepository<UserProperty, String> {
	
	UserProperty findByName(String name);
	
}
