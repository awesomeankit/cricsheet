package com.cricket.cricsheet.exception;

import org.springframework.http.HttpStatus;

public class MatchException extends RuntimeException {
	String errorCode;
	
	String errorMessage;
	
	HttpStatus statusCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errormsg) {
		this.errorMessage = errormsg;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public MatchException() {
		super();
	}

	public MatchException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MatchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MatchException(String arg0) {
		super(arg0);
	}

	public MatchException(Throwable arg0) {
		super(arg0);
	}
	
}
