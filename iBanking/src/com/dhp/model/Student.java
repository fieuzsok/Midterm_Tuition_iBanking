package com.dhp.model;

import java.util.Date;

public class Student {
	private String student_id;
	private String full_name;
	private Date DOB;
	
	public Student(String student_id, String full_name) {
		super();
		this.student_id = student_id;
		this.full_name = full_name;
	}
	public Student(String student_id, String full_name, Date dOB) {
		
		this.student_id = student_id;
		this.full_name = full_name;
		DOB = dOB;
	}
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public Date getDOB() {
		return DOB;
	}
	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	

}
