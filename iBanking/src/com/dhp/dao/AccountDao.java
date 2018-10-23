package com.dhp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dhp.model.Account;


public class AccountDao {

	public static Account getAccount(String username, String pwd) {
		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM account";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				String user = rs.getString("username");
				String pass = rs.getString("password");
				String name = rs.getString("full_name");
				String email = rs.getString("email");
				int phone= rs.getInt("phone");
				int balance = rs.getInt("balance");
				String address = rs.getString("address");
				
				if(username == null) return null;
				if(username.equals(user) && pwd.equals(pass))				
					return new Account(user, email, phone, name, balance, address);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	public static Account getAccount(String username) {
		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM account WHERE username= ?";
		PreparedStatement stmt  = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				String user = rs.getString("username");
				String name = rs.getString("full_name");
				String email = rs.getString("email");
				int phone= rs.getInt("phone");
				int balance = rs.getInt("balance");
				String address = rs.getString("address");
				
				if(username.equals(user))				
					return new Account(user, email, phone, name, balance, address);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			
			try { 
				if (rs != null) rs.close(); 
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) { 
				e.printStackTrace(); 
			}
		}
		return null;
	}
	
	public static boolean updateBalance(String username, int debitAmount) {
		Connection con = DBConnection.getConnection();
		String sql = "update account set balance = balance - ? where username = ?";
		PreparedStatement st;
		try {
			st = con.prepareStatement(sql);
			st.setInt(1, debitAmount);
			st.setString(2, username);
			int row = st.executeUpdate();
			if(row > 0) return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
		
	}
}
