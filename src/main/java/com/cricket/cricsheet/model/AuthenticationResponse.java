package com.cricket.cricsheet.model;

public class AuthenticationResponse {
	
	private String jwt;

	public AuthenticationResponse(String jwtString) {
		this.jwt= jwtString;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
	

}
