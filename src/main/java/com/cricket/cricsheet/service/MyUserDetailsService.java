package com.cricket.cricsheet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
		if(userProperty != null) {
			List<GrantedAuthority> authorities = userProperty.getAuthorities().stream()
					.map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList());
			return new User(userProperty.getName(), userProperty.getPassword(), authorities);
		}else 
			return null;
	}

}
