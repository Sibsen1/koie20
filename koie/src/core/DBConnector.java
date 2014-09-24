package koie.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	private Core CoreClass;
	
	String DBhostAddress;
	String DBUserName;
	String DBPassword;
	
	Connection connection;
	
	public DBConnector(Core CoreClass, String DBhostAddress, String DBUserName, String DBPassword) {
		this.CoreClass = CoreClass;
		
		this.DBhostAddress = DBhostAddress;
		this.DBUserName = DBUserName;
		this.DBPassword = DBPassword;
		
		try {
			
			connect();
			
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			
			// Kanskje Core.ConnectionFailed(); som ikke krasjer programmet? -Sindre

			// * Kopipasta fra MySQL:
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private void connect() throws SQLException {
		
		this.connection = 
				DriverManager.getConnection(
						DBhostAddress, DBUserName,DBPassword);
	}
	
	
	public ResultSet getQuery() {
		String SQLString;

		// TODO
		SQLString = "";
		
		return executeSQL(SQLString);
	}
	
	
	public void insertRow() {
		String SQLString;

		// TODO
		SQLString = "";
		
		executeSQL(SQLString);
	}
	
	public void deleteRow() {
		String SQLString;
		
		// TODO
		SQLString = "";
		
		executeSQL(SQLString);
	}

	public void editRow() {
		String SQLString;
		
		// TODO
		SQLString = "";
		
		executeSQL(SQLString);
	}
	
	
	private ResultSet executeSQL(String SQLString) { 
		Statement statement = null;
		ResultSet result = null;
		
		try {
			
			statement = connection.createStatement();
			
			if (statement.execute(SQLString)) {     // Returnerer 1 om query (SELECT), 0 om noe blir endret (eks. INSERT) -Sindre 
				result = statement.getResultSet();
			}
			
		} catch (SQLException ex) {
			// TODO: Kanskje Core.DBConnectionFailure() som i konstruktøren igjen -Sindre
			
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    
		} finally {
			
			try {
				statement.close();
			} catch (SQLException sqlex) { }
		}
		
		return result;
	}

}
