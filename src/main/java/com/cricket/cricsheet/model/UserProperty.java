package com.cricket.cricsheet.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserProperty {
	
	private int age;
	
	private String name;
	
	private String password;
	private List<String> authorities;
	private List<String> tokens;
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
}
