package com.engagetech.common;

import org.aeonbits.owner.Config;

/**
 * Standard configuration for backend app (micro-service).  
 */
public interface SvcConfig extends Config {
	@DefaultValue("jdbc:mysql://localhost:3306/expenses?useSSL=false")
	String dbUrl();
	@DefaultValue("dev")
	String dbUser();
	@DefaultValue("123")
	String dbPassword();
	@DefaultValue("true")
	boolean debug();
	@DefaultValue("4567")
	int httpPort();
}
