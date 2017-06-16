package com.engagetech.expenses;

import java.util.Date;

public class Expense {
	public static final float VAT_RATE = 0.2f;
	
	// we don't need id for this task but will need for update/delete anyway
	private Long id; 
	private Date date;
	private String reason;
	private boolean euro;
	private float amount;
	private float vat;
	
	public Expense() {
		this.euro = false;
	}

	public Expense(Date date, String reason, float amount) {
		this();
		this.date = date;
		this.reason = reason;
		this.amount = amount;
		this.vat = vat();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}

	public String getReason() {
		return reason;
	}

	public float getAmount() {
		return amount;
	}

	public float getVat() {
		return vat;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setAmount(float amount) {
		this.amount = amount;
		this.vat = vat();
	}

	public boolean isEuro() {
		return euro;
	}

	public void setEuro(boolean euro) {
		this.euro = euro;
	}

	private float vat() {
		return amount * VAT_RATE;
	}
}
