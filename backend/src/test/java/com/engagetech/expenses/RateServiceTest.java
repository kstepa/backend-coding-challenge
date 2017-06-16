package com.engagetech.expenses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.engagetech.common.CapabilityException;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RateServiceTest {
	@Test
	public void poundsFromEuros() throws Exception {
		// Mock external service call to verify exchange
		RateService svc = new RateService() {
			@Override
			double rate() throws UnirestException {
				return 0.88;
			}
		};
		assertEquals(0.88, svc.poundsFromEuros(1.0f), 0.00001);
		assertEquals(88.0, svc.poundsFromEuros(100.0f), 0.00001);
	}
	
	@Test
	public void externalService() throws Exception {
		// let's rely that exchange rate will be between 0.66 and 1.0 (e.g. 0.88 +/- 0.12), should be good enough
		// often failures of this method due to unavailability will show that we cannot rely on currently used external service
		RateService svc = new RateService();
		assertEquals(0.88, svc.rate(), 0.12);
	}
	
	@Test(expected=CapabilityException.class)
	public void externalServiceUnavailable() throws Exception {
		// Mock external service call to fail on getting rate
		RateService svc = new RateService() {
			@Override
			double rate() throws UnirestException {
				throw new UnirestException("test"); 
			}
		};
		svc.poundsFromEuros(1.0f);
		fail("Exception expected");
	}
}
