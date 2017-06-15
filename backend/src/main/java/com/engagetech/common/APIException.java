package com.engagetech.common;

/**
 * Generic application (e.g. expected by business domain) exception. 
 * Occurs during REST method execution and returned (as HTTP status and message) to frontend. 
 * Subclass with proper codes and default messages.
 */
@SuppressWarnings("serial")
public abstract class APIException extends Exception {
	private int code;
	
	public APIException(int code, String message) {
		super(message);
		this.code = code;
	}

	public APIException(String message) {
		super(message);
		this.code = 400;
	}
	
	public int getCode() {
		return code;
	}
}

