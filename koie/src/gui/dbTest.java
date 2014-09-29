package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbTest {
	 
	public static void main(String[] args){
		Connection conn = null;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			
			conn = DriverManager.getConnection("jdbc:mysql://r.saggau.no:3306/koie20","user","koie20pw");
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		try{
			Statement stmt = conn.createStatement();
	        String query = "select * from user ;";
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            String userID = rs.getObject(1).toString();
	            String userMail = rs.getObject(2).toString();
	            System.out.println("ID:" + userID + " med mail:" + userMail);

	        }
		}
	   catch (SQLException e) {
	        e.printStackTrace();
	        for(Throwable ex : e) {
	            System.err.println("Error occurred " + ex);
	        }
	        System.out.println("Error in fetching data");
	    }
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
