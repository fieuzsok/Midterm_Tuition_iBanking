package com.dhp.services;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.shiro.crypto.hash.Sha512Hash;

import com.dhp.bl.EmailHandle;
import com.dhp.dao.AccountDao;
import com.dhp.dao.OTPDao;
import com.dhp.dao.StudentDAO;
import com.dhp.dao.TuitionDao;
import com.dhp.model.Account;
import com.dhp.model.OTP;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import javassist.expr.Instanceof;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Path("/services")
public class MyServices
{
	
	@POST
	@Path("/sign-in-secure-v2/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean signIn_ver2(@FormParam("username") String username, @FormParam("password") String password)
	{
		Account recentAccount = AccountDao.getAccount(username, password);
		if(recentAccount != null) {
			return true;
		}
		return  false;
	}
	
	
	
	@GET
	@Path("info/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Account accountInfo(@PathParam("username") String username)
	{
		return AccountDao.getAccount(username);				
	}
	
	@GET
	@Path("/pay/{student_id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getPay(@PathParam("student_id") String mssv)
	{
		Object fee = TuitionDao.getMust_pay(mssv);		
		ResponseBuilder builder = Response.ok(fee);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "*");
        builder.header(
                "Access-Control-Allow-Headers",
                "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
        return builder.build();		
	}

	@POST
	@Path("/send-email/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendEmail(@FormParam("username") String username,@FormParam("studentID") String studentID)
	{		
		Account recentAccount = AccountDao.getAccount(username);	
		Object tuitionFee =  TuitionDao.getMust_pay(studentID);
		int balance = recentAccount.getBalance();
		//kt hop le so du va hoc phi
		boolean isValid = checkValidPayMent(balance, tuitionFee);
		boolean isSent = false;
		if(isValid == true) {
			String email = recentAccount.getEmail();
			isSent = EmailHandle.emailHandler(email, username, studentID,(int) tuitionFee);			
		}
			
		ResponseBuilder builder = Response.ok(isSent);
        builder.header("Access-Control-Allow-Origin", "*");
        builder.header("Access-Control-Max-Age", "3600");
        builder.header("Access-Control-Allow-Methods", "*");
        builder.header(
                "Access-Control-Allow-Headers",
                "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
        return builder.build();
	}
	
	@POST
	@Path("/send-email-succeed/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendEmailAnnouncement(@FormParam("username") String username,@FormParam("studentID") String studentID)
	{		
		Account recentAccount = AccountDao.getAccount(username);	
		int newBalance = recentAccount.getBalance();
		int fee_paid = (int) TuitionDao.getFeePaid(studentID);
		boolean isSent = false;
		if(recentAccount != null && fee_paid >= 0) {
			String email = recentAccount.getEmail();
			isSent = EmailHandle.sendEmailAnnouncementPayment(email, studentID, fee_paid, newBalance);			
		}	
		ResponseBuilder builder = Response.ok(isSent);
		return setHeader(builder);
	}
	
	
	@POST
	@Path("/validOTP/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isValidOTP(@FormParam("username") String username,@FormParam("otpCode") String otpCode ){
		ResponseBuilder builder = null;
		OTP otp = OTPDao.getOTP(username);
		boolean isExpired = false;
		boolean isValidOTP = false;
		if (otp != null) {			
			Date now = new Date();
			if(now.compareTo(otp.getExpiredTime()) < 0){
				if(otpCode.equals(otp.getOPTcode())) {
					isExpired = true;
					isValidOTP = true;								
					deleteOTP(username);
				}
				else {
					isExpired = true;
					isValidOTP = false;
				}
			}
			else {
				isExpired = false;
				isValidOTP = false;
				deleteOTP(username);
			}								
		}	
			builder = Response.ok("{\r\n" + 
				"    \"user\": \""+ username + "\",\r\n" + 
				"    \"isExpired\": "+ isExpired+ ",\r\n" + 
				"    \"isValidOTP\": "+ isValidOTP+ "\r\n" + 
				"}");
			return setHeader(builder);
			
	}
	
	@POST
	@Path("/paymentHandler/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response paymentHandler(@FormParam("username") String username,@FormParam("student_id") String student_id ){
		ResponseBuilder builder = null;
		int tuitionFee = (int) TuitionDao.getMust_pay(student_id);		
		boolean isUpdateBalance = updateBalance(username,tuitionFee);
		boolean isUpdateTuitionFee = updateTuitionFee(student_id);
		
		builder = Response.ok("{\r\n" + 
				"    \"user\": \""+ username + "\",\r\n" + 
				"    \"Student ID\": \""+ student_id + "\",\r\n" + 
				"    \"isUpdateBalance\": "+ isUpdateBalance +",\r\n" + 
				"    \"isUpdateTuitionFee\": "+ isUpdateTuitionFee +"\r\n" + 
				"}");
		
		
		return setHeader(builder);
		
	}

	private Response setHeader(ResponseBuilder builder) {
		
	    builder.header("Access-Control-Allow-Origin", "*");
	    builder.header("Access-Control-Max-Age", "3600");
	    builder.header("Access-Control-Allow-Methods", "*");
	    builder.header(
	            "Access-Control-Allow-Headers",
	            "X-Requested-With,Host,User-Agent,Accept,Accept-Language,Accept-Encoding,Accept-Charset,Keep-Alive,Connection,Referer,Origin");
	    return builder.build();
	}
	
	public boolean deleteOTP(String username) {
		return OTPDao.deleteOTP(username);
	}
	
	public String convertToDateTime(java.util.Date dt) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		return currentTime;
	}
	
	public boolean updateBalance(String username,int debitAmount) {
		return AccountDao.updateBalance(username, debitAmount);
	}
	
	public boolean updateTuitionFee(String studentID) {
		return TuitionDao.updateMustPay(studentID);
	}
	public boolean checkValidPayMent(int balance , Object tuitionFee ) {
		
		String Error = "noError";
		boolean isSent = false;
		if( tuitionFee != null || tuitionFee != "null") {
			int tuition = (int) tuitionFee;
			if(tuition > balance || balance <= 0) {
				Error = "balance_not_enough";
			}
			else if(tuition <=0 ) {
				Error = "invalid_amount";
			}
			else {
				isSent = true;
			}
			
		}
		else {
			Error = "invalid_studentID";
		}
		return isSent;
	
	}
}
