package com.dhp.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

public class OTP {
	private static final long timeStep = 300; //300s = 5p
	private int ID;
	private String username;
	private boolean isExpired;
	private String OPT_code;
	private Date startTime;
	private Date expiredTime;	
	
	public OTP(String username) {
		super();
		this.username = username;
		this.isExpired = true;		
		//OPT_code = String.valueOf(generateOPT());;
		TimeBasedOneTimePasswordGenerator();
	}

	public OTP(String username, boolean isExpired, String oPT_code,Date startTime,	Date expiredTime) {
		super();
		this.username = username;
		this.isExpired = isExpired;
		this.OPT_code = oPT_code;
		this.startTime = startTime;
		this.expiredTime = expiredTime;
	}

	public int getID() {
		return ID;
	}

	public String getUsername() {
		return username;
	}

	public void setExpired() {
		this.isExpired = false;
	}
	
	public boolean isExpired() {
		if (startTime.compareTo(expiredTime)<0) 
			return false;
		else
			return true;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public String getOPTcode() {
		return OPT_code;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	private char[] generateOPT() 
	{ 		
		// A strong password has Cap_chars, Lower_chars, 
		// numeric value and symbols. So we are using all of 
		// them to generate our password 
		String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
		String numbers = "0123456789"; 
		String values = Capital_chars + numbers ; 

		// Using random method 
		Random rndm_method = new Random(); 

		char[] password = new char[6]; 

		for (int i = 0; i < 6; i++) 
		{ 
			// Use of charAt() method : to get character value 
			// Use of nextInt() as it is scanning the value as int 
			password[i] = 
			values.charAt(rndm_method.nextInt(values.length())); 

		} 
		return password; 
	}
	
	private void TimeBasedOneTimePasswordGenerator() {
		TimeBasedOneTimePasswordGenerator totp;
		try {
			totp = new TimeBasedOneTimePasswordGenerator(timeStep, TimeUnit.SECONDS);
			 final SecretKey secretKey;
		        {
		            final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
		            // SHA-1 and SHA-256 prefer 64-byte (512-bit) keys; SHA512 prefers 128-byte (1024-bit) keys
		            keyGenerator.init(512);
		            secretKey = keyGenerator.generateKey();
		        }
		        this.startTime = new Date();
		        this.expiredTime = new Date(startTime.getTime() + totp.getTimeStep(TimeUnit.MILLISECONDS));
		        this.OPT_code = totp.generateOneTimePassword(secretKey, startTime) + "";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
