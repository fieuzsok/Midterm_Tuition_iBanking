package com.dhp.bl;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.dhp.dao.OTPDao;
import com.dhp.model.OTP;

public class EmailHandle {
	
	private static final String username = "nhatchon56@gmail.com";
	private static final String password = "446662664";	
	
	private static String getSubject() {
		return "Announcement from IT_TDT iBanking " + getNow();
	}
	
	private static String getContent(String email,String studentID, int tuitionFee, String opt_code) {
		return "Dear " + email + ", "
						+ "\n\n You have just submitted a payment request for a student with StudentID: " + studentID + ", with the tuition fee of " + NumberFormat.getInstance().format(tuitionFee) + " VND at " + getNow () + "."
						+ "\nPlease enter the code: "  + opt_code + " to confirm transaction information."
						+ "\n\nRegards,"
						+ "\nIT_TDT iBanking.";
	}
	private static String getContentPaymetSuccess(String email,String studentID, int tuitionFee, int newBalance) {
		return "Dear " + email + ", "
						+ "\n\n You have just paid successfully the tuition fee  for student with student ID: " + studentID + ", with the tuition fee of " + NumberFormat.getInstance().format(tuitionFee) + " VND at " + getNow () + "."
						+ "\n\nThe current balance in your account is:" + newBalance + "."
						+ "\n\nRegards,"
						+ "\nIT_TDT iBanking.";
	}
	//Tao OTP và luu vào CSDL, gui mail otp den nguoi dung
	public static boolean emailHandler(String email, String username , String studentID, int tuitionFee)
	{	
		//xoa OTP cu neu con
		OTPDao.deleteOTP(username);
		OTP otp = new OTP(username);		
		String otp_code = otp.getOPTcode();
		boolean isInsert = OTPDao.insertOTP(otp);		
		if(isInsert == true) {
			boolean isSent = sendEmail(email, studentID, tuitionFee, otp_code);
			if(isSent)  return true;
		}
		return false;

	}
	
	public static boolean sendEmail(String email, String studentID, int tuitionFee, String otp_code) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

		try {			
			String subject = getSubject();
			String content = getContent(email, studentID, tuitionFee,otp_code);
			Message message = new MimeMessage(session);			
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));			
			message.setSubject(subject); 
			message.setText(content);	
			Transport.send(message);

			return true;

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getNow()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date date = new Date();
	    return formatter.format(date).toString();
	}
	
	public static boolean sendEmailAnnouncementPayment(String email, String studentID, int tuitionFee, int newBalance) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

		try {			
			String subject = getSubject();
			String content = getContentPaymetSuccess(email, studentID, tuitionFee,newBalance);
			Message message = new MimeMessage(session);			
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));			
			message.setSubject(subject); 
			message.setText(content);	
			Transport.send(message);

			return true;

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
