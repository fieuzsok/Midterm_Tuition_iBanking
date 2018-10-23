package com.dhp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dhp.model.Account;
import com.dhp.model.Student;

public class StudentDAO {
	public static Student getAccount(String studetID) {
		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM student where student_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, studetID);
			rs = stmt.executeQuery();
			
			while (rs.next())
			{
				String ID = rs.getString("student_id");
				String name = rs.getString("full_name");	
				
					
					return new Student(ID, name);
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
}
