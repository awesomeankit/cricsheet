package com.cricket.cricsheet.model;

public class ErrorModel {
	
	private String errorCode;
	private String errorMessage;
	private String errorType;
	
	
	public ErrorModel(String errorCode, String errorMessage, String errorType) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorType = errorType;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
	
	

}
