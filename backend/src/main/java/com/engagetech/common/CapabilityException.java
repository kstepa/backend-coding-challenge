package com.engagetech.common;

/**
 * Generic exception to be raised when non vital part of the system is temporary unavailable.
 * E.g. user still able to use the system in some limited way
 */
@SuppressWarnings("serial")
public class CapabilityException extends APIException {

	public CapabilityException(String message) {
		super(503, message);
	}
	
}
