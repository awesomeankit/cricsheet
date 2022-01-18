package com.cricket.cricsheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cricket.cricsheet.exception.MatchException;
import com.cricket.cricsheet.model.AuthenticationRequest;
import com.cricket.cricsheet.model.AuthenticationResponse;
import com.cricket.cricsheet.model.ErrorModel;
import com.cricket.cricsheet.service.MyUserDetailsService;
import com.cricket.cricsheet.util.JwtUtil;

@RestController
@RequestMapping(value = "/cricsheet/authenticate")
public class AuthenticationController {

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
			@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		if (userDetails != null) {
			if (BCrypt.checkpw(authenticationRequest.getPassword(), userDetails.getPassword())) {
				String jwtString = jwtUtil.generateToken(userDetails);
				return ResponseEntity.ok(new AuthenticationResponse(jwtString));
			} else {
				MatchException exp = new MatchException();
				exp.setErrorCode("100");
				exp.setErrorMessage("Invalid password.");
				exp.setStatusCode(HttpStatus.BAD_REQUEST);
				throw exp;
			} 
		}
		MatchException exp = new MatchException();
		exp.setErrorCode("100");
		exp.setErrorMessage("User information not found for username=" + authenticationRequest.getUsername());
		exp.setStatusCode(HttpStatus.NOT_FOUND);
		throw exp;

	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorModel> handleMatchException(MatchException exception) {
		ErrorModel errorModel = new ErrorModel(exception.getErrorCode(), exception.getErrorMessage(),
				exception.getErrorMessage());

		return new ResponseEntity<>(errorModel, exception.getStatusCode());
	}

}
