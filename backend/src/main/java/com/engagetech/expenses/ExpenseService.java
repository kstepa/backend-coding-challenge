package com.engagetech.expenses;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.engagetech.common.APIException;
import com.engagetech.common.DB;
import com.engagetech.common.FieldValidationException;
import com.engagetech.expenses.db.Tables;
import com.engagetech.expenses.db.tables.records.ExpenseRecord;

public class ExpenseService {
	private static final int MAX_REASON_LENGTH = 500;
	private static final double MAX_AMT = 10000.0;
	
	private DB db;

	public ExpenseService(DB db) {
		super();
		this.db = db;
	}
	
	public List<Expense> all() {
		// actually we'll need expenses for particular user, 
		// but for this basic task we just return all records
		return db.dsl
				.selectFrom(Tables.EXPENSE)
				.orderBy(Tables.EXPENSE.DATE)
				.fetch().into(Expense.class);
	}
	
	public void insert(Expense ex) throws APIException {
		if (ex.getDate() == null) {
			throw new FieldValidationException("Expense date should not be empty");
		}
		
		if (ex.getReason() == null) {
			throw new FieldValidationException("Expense reason should not be empty");
		}
		
		if (ex.getReason().length() > 500) {
			throw new FieldValidationException("Reason", MAX_REASON_LENGTH);
		}
		
		if (ex.getDate().after(new Date())) {
			throw new FieldValidationException("Expense date could only be in the past: " + ex.getDate());
		}
		if (ex.getAmount() <= 0 || ex.getAmount() > MAX_AMT) {
			throw new FieldValidationException("Expense amount should be positive, not greater than " + MAX_AMT);
		}
		
		ExpenseRecord er = new ExpenseRecord();
		er.setAmount(new BigDecimal(ex.getAmount()));
		er.setDate(new java.sql.Date(ex.getDate().getTime()));
		er.setReason(ex.getReason());
		db.dsl.insertInto(Tables.EXPENSE).set(er).execute();
	}
}
