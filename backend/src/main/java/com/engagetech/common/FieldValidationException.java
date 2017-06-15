package com.engagetech.common;

/**
 * Generic exception to be raised on field not passing validation rules. 
 */
@SuppressWarnings("serial")
public class FieldValidationException extends APIException {
	public FieldValidationException(String msg) {
		super(422, msg);
	}
	
	public FieldValidationException(String field, int maxSize) {
		super(422, String.format("%s is too long. Max %d length expected.", field, maxSize));
	}
}
