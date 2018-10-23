package com.dhp.model;

public class Account {
	
	public String username;
	public String password;
	public String email;
	public int phone;
	public String name;
	public int balance;
	public String address;
	
	public Account() {
		
	}
	
	public Account(String username, String email, int phone, String name, int balance,
			String address) {
		this.username = username;
		this.password = null;
		this.email = email;
		this.phone = phone;
		this.name = name;
		this.balance = balance;
		this.address = address;
	}

	public Account(String username, String password, String email, int phone, String name, int balance,
			String address) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.name = name;
		this.balance = balance;
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return username + "-" + email + "-" + phone + "-" + name + "-" + balance + "-" + address;
	}
	
}
