package com.dhp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TuitionDao {

	public static boolean updateMustPay(String studentID) {
		Connection con = DBConnection.getConnection();
		int mustPay= (int) getMust_pay(studentID);
		int feePaid = mustPay;
		String sql = "update tuition set fee_paid = ?, must_pay = 0  where student_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, feePaid);
			stmt.setString(2, studentID);
			int row = stmt.executeUpdate();
			if(row > 0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	public static Object getMust_pay(String studentID) {
		int must_pay = -1 ;
		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM tuition where student_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, studentID);
			
			rs = stmt.executeQuery();
			if(rs.last() == false) return "null";
			else rs.beforeFirst();
			while (rs.next())
			{
				must_pay = rs.getInt("must_pay");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return must_pay;
	}
	
	public static Object getFeePaid(String studentID) {
		int fee_paid = -1 ;
		Connection con = DBConnection.getConnection();
		String sql = "SELECT fee_paid FROM tuition where student_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, studentID);
			
			rs = stmt.executeQuery();
			if(rs.last() == false) return "null";
			else rs.beforeFirst();
			while (rs.next())
			{
				fee_paid = rs.getInt("fee_paid");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return fee_paid;
	}
}
