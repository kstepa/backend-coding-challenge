package com.engagetech.expenses;

import java.util.Properties;

import org.aeonbits.owner.ConfigFactory;

import com.engagetech.common.DB;
import com.engagetech.common.SvcConfig;
import com.engagetech.expenses.db.Tables;

class TestUtil {
	private static TestUtil instance = null;
	
	static TestUtil get() {
		if (instance == null) instance = new TestUtil();
		return instance;
	}

	final SvcConfig cfg;
	final DB db;
	
	private TestUtil() {
		Properties testDefaults = new Properties();
		testDefaults.setProperty("dbUrl", "jdbc:mysql://localhost:3306/expenses_test?useSSL=false");
		cfg = ConfigFactory.create(
				SvcConfig.class,
				System.getProperties(),
				System.getenv(),
				testDefaults);
		db = new DB(cfg);
		db.reset();
	}
	
	void cleanTables() {
		db.dsl.delete(Tables.EXPENSE).execute();
	}
}
