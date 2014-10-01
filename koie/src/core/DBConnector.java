package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
	private Core CoreClass;
	
	String DBhostAddress;
	String DBUserName;
	String DBPassword;
	
	public Connection connection;
	
	List<String> tables;
	List<List<String>> tableColumns;
	
	public DBConnector(Core CoreClass, String DBhostAddress, String DBUserName, String DBPassword) {
		this.CoreClass = CoreClass;
		
		this.DBhostAddress = DBhostAddress;
		this.DBUserName = DBUserName;
		this.DBPassword = DBPassword;
	
		
		ResultSet res = null;
		try {
			connect();
					
			res = executeSQL(
					"SELECT * FROM INFORMATION_SCHEMA.TABLES LIMIT 40, 999");
			
			tables = new ArrayList<String>();
			while (res.next()) {
				tables.add(res.getString(3));
			}
			res.close();
			
			tableColumns = new ArrayList<List<String>>();
			
			int tablesSize = tables.size();	
			for (int tableI = 0; tableI < tablesSize; tableI++) {
				
				res = getQuery(tables.get(tableI));
				tableColumns.add(new ArrayList<String>());
				
				int columnsSize = res.getMetaData().getColumnCount();
				for (int i = 1; i <= columnsSize; i++) {
					
					tableColumns.get(tableI).add(
							res.getMetaData().getColumnLabel(i));
				}
			}			
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			
			// Kanskje Core.ConnectionFailed(); som ikke krasjer programmet? -Sindre
			
			// * Kopipasta fra MySQL:
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			
			try {
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}

	private void connect() throws SQLException {
		// Dette kan være nødvendig for noen: -Sindre
		
		/*try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		this.connection = 
				DriverManager.getConnection(
						DBhostAddress, DBUserName, DBPassword);
	}
		
	public ResultSet getQuery(String DBName, String... columns) {
		
		if (columns.length < 1)
			return executeSQL("SELECT * FROM "+ DBName);
		
		StringBuilder sBuild = new StringBuilder();
		int columnI = 0;
		for (; columnI < columns.length - 1; columnI++) {
			
			sBuild.append(columns[columnI]);
			sBuild.append(",");
		}
		sBuild.append(columns[columnI]);
		
		return executeSQL(String.format("SELECT %s FROM %s", sBuild, DBName));
	}	
	
	public ResultSet getQueryJoined(String DBName, String DB2Name, 
			String matchingColumn1, String matchingColumn2, String... columns) {
		// TODO
		String SQLString = "";
		
		// Gir en Resultset med tabellradene hvor matchingColumn1 == matchingColumn1
		// * columns-argumentene som skal vises må skrives på form 'table.column'  -Sindre
		
		return executeSQL(SQLString);
	}
	public void insertRow(String DBName, Object... fields) {
		String SQLString;

		// TODO
		SQLString = "";
		
		executeSQL(SQLString);
	}
	
	public void deleteRow(String table, String ID) {
		String sqlStatement = "DELETE FROM 'Table' = ? WHERE 'ID' = ?";
		PreparedStatement deleteRow;
		try {
			deleteRow = connection.prepareStatement(sqlStatement);
			deleteRow.setString(1, table);
			deleteRow.setString(2, ID);
			deleteRow.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL Exception: Could not delete row from table with ID");
			e.printStackTrace();
		}
	}

	public void editRow(String DBName, int rowNumber, Object... writableFields) {
		String SQLString;
		
		// TODO
		SQLString = "";
		
		executeSQL(SQLString);
	}	
	
	private ResultSet executeSQL(String SQLString) { 
		Statement statement = null;
		ResultSet result = null;
		
		try {
			
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			if (statement.execute(SQLString)) {     // Returnerer 1 om query (SELECT), 0 om noe blir endret (eks. INSERT) -Sindre 
				result = statement.getResultSet();
			}
			
		} catch (SQLException ex) {
			// TODO: Kanskje Core.DBConnectionFailure() som i konstruktøren igjen -Sindre
			
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    
		}
		
		return result;
	}
	public static void main(String[] args) throws SQLException {
		DBConnector dbc = new DBConnector(new Core(),
				Core.DBhostAddress, Core.DBUserName, Core.DBPassword);
	
		ResultSet res = dbc.getQuery("koie");
		ResultSetMetaData resMeta = res.getMetaData();
		
		int columnCount = resMeta.getColumnCount();
		for (int columnI = 1; columnI <= columnCount; columnI++) {
			System.out.print(resMeta.getColumnType(columnI)+"  ");
			System.out.println(resMeta.getColumnTypeName(columnI));
		}
	}
}
