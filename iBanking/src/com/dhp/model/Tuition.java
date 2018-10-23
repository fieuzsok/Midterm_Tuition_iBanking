package com.dhp.model;

public class Tuition {

	private int tuition_id;
	private int student_id;
	private int tuition_fee;
	private int must_pay;
	private int fee_paid;
	private int exemptions;
	private String semester;
	private String scholastic;

	
	public Tuition(int tuition_id, int student_id, int tuition_fee, int exemptions,
			String semester, String scholastic) {
		this.tuition_id = tuition_id;
		this.student_id = student_id;		
		this.tuition_fee = tuition_fee;		
		this.exemptions = exemptions;
		this.semester = semester;
		this.scholastic = scholastic;
		
	}

	public int getFee_paid() {
		return fee_paid;
	}

	public void setFee_paid(int fee_paid) {
		this.fee_paid = fee_paid;
	}

	public int getTuition_id() {
		return tuition_id;
	}

	public void setTuition_id(int tuition_id) {
		this.tuition_id = tuition_id;
	}

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}


	public int getTuition_fee() {
		return tuition_fee;
	}

	public void setTuition_fee(int tuition_fee) {
		this.tuition_fee = tuition_fee;
	}

	public int getMust_pay() {
		return must_pay;
	}

	public void setMust_pay(int must_pay) {
		this.must_pay = must_pay;
	}

	public int getExemptions() {
		return exemptions;
	}

	public void setExemptions(int exemptions) {
		this.exemptions = exemptions;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getScholastic() {
		return scholastic;
	}

	public void setScholastic(String scholastic) {
		this.scholastic = scholastic;
	}

	
	
	
	
	
}
