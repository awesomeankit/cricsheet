package com.cricket.cricsheet.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cricket.cricsheet.model.UserProperty;
import com.cricket.cricsheet.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserProperty userProperty= userRepository.findByName(username);
		if(userProperty != null)
			return new User(userProperty.getName(), userProperty.getPassword(), new ArrayList<>());
		else 
			return null;
	}

}
