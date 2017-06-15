package com.engagetech.expenses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.engagetech.common.FieldValidationException;
import com.engagetech.expenses.db.Tables;
import com.engagetech.expenses.db.tables.records.ExpenseRecord;

public class ExpenseServiceTest {
	static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	static TestUtil tu = TestUtil.get();
	static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	
	private ExpenseService expenseSvc;
	
	@Before
	public void setUp() {
		tu.cleanTables();
		expenseSvc = new ExpenseService(tu.db);
	}
	
	@Test
	public void vat() {
		assertEquals(2.0f, new Expense(new java.util.Date(), "test1", 10.0f).getVat(), 0.0001f);
		assertEquals(40.0f, new Expense(new java.util.Date(), "test2", 200.0f).getVat(), 0.0001f);
	}

	@Test
	public void all() throws Exception {
		tu.db.dsl
			.batchInsert(
					new ExpenseRecord(1l, 
							new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000l), 
							new BigDecimal(100.0055), 
							"Yesterday's party"),
					new ExpenseRecord(2l, 
							new Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000l),
							new BigDecimal(1000.12), 
							"Older party"))
			.execute();
		
		
		List<Expense> lst = expenseSvc.all();
		assertEquals(2, lst.size());
		Expense e1 = lst.get(0);
		// earlier expenses returned first
		assertEquals(2l, e1.getId().longValue()); 
		assertEquals("Older party", e1.getReason());
		assertEquals(1000.12, e1.getAmount(), 0.0001);
		
		Expense e2 = lst.get(1);
		// money gets rounded to 2nd sign
		assertEquals(100.01, e2.getAmount(), 0.0001);
	}
	
	@Test
	public void insert() throws Exception {
		expenseSvc.insert(new Expense(sdf.parse("01/01/2016"), "New Year", 100.0f));
		int cnt = tu.db.dsl.fetchCount(Tables.EXPENSE);
		assertEquals(1, cnt);
		
		Expense ex = expenseSvc.all().get(0);
		assertEquals("New Year", ex.getReason());
		assertNotNull(ex.getId());
	}
	
	@Test()
	public void insertInFuture() throws Exception {
		try {
			expenseSvc.insert(new Expense(sdf.parse("01/01/2020"), "New Year", 100.0f));
			fail("FieldValidationException expected");
		} 
		catch (FieldValidationException ex) {
			assertTrue(ex.getMessage().startsWith("Expense date could only be in the past"));
		}
	}

	@Test()
	public void insertNegative() throws Exception {
		try {
			expenseSvc.insert(new Expense(sdf.parse("01/01/2017"), "New Year", -100.0f));
			fail("FieldValidationException expected");
		} 
		catch (FieldValidationException ex) {
			assertTrue(ex.getMessage().startsWith("Expense amount should be positive"));
		}
	}
}
