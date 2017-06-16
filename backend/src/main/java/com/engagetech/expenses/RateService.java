package com.engagetech.expenses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.engagetech.common.CapabilityException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RateService {
	private static final Logger logger = LoggerFactory.getLogger(RateService.class);
	
	public float poundsFromEuros(float euros) throws CapabilityException {
		// quick and dirty
		try {
			return (float)(rate() * euros);
		} 
		catch (UnirestException e) {
			logger.error("Exception calling api.fixer.io", e);
			throw new CapabilityException("Rate exchange temporary unavailable. Please use pounds.");
		}
	}
	
	double rate() throws UnirestException {
		return Unirest
			.get("http://api.fixer.io/latest?symbols=GBP&base=EUR")
			.asJson()
			.getBody()
			.getObject()
			.getJSONObject("rates")
			.getDouble("GBP");
	}
}
