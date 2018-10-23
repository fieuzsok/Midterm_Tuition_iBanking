package com.dhp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.dhp.model.OTP;
import com.dhp.model.Student;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class OTPDao {
	
	public static boolean insertOTP(OTP otp) {
		Connection con = DBConnection.getConnection();
		String sql = "insert into otp value(null,?,?,?,?)";
		System.out.println(otp.getUsername());
		PreparedStatement stmt = null;
		int rs = 0;
		try {
			stmt = con.prepareStatement(sql);
			String sqlStartDate = convertToDateTime(otp.getStartTime());
			String sqlExpiredDate = convertToDateTime(otp.getExpiredTime());
			stmt.setString(1, otp.getOPTcode());
			stmt.setString(2, otp.getUsername());
			stmt.setString(3, sqlStartDate);
			stmt.setString(4, sqlExpiredDate);
			rs = stmt.executeUpdate();
			if(rs > 0 ) return true;
			
		}catch(MySQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		finally {			
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return false;
	}
	
	public static OTP getOTP(String username) {
		Connection con = DBConnection.getConnection();
		String sql = "select * from otp where username = ? ";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1,username);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String otpCode = rs.getString("OTP_code");
				Date startDate = new Date(rs.getTimestamp("startTime").getTime());
				Date expiredDate  = new Date(rs.getTimestamp("expiredTime").getTime());
				OTP otp = new OTP(username, true, otpCode, startDate, expiredDate);
				return otp;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try { if(rs != null) rs.close();} catch(SQLException e) {e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	
	public static boolean deleteOTP(String username) {
		Connection con = DBConnection.getConnection();
		String sql = "delete from otp where username = ? ";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1,username);
			int row = stmt.executeUpdate();
			if(row > 0) return true;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try { if(rs != null) rs.close();} catch(SQLException e) {e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return false;
	}
	
	public static String convertToDateTime(java.util.Date dt) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		return currentTime;
	}
	
}
