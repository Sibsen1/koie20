package core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnector {
	private Core CoreClass;
	
	String DBhostAddress;
	String DBUserName;
	String DBPassword;
	
	public Connection connection;
	
	List<String> tables;
	Map<String, List<String>> tableColumns;
	Map<String, String> primaryKeys;
	
	public DBConnector(Core CoreClass, String DBhostAddress, String DBUserName, String DBPassword) throws SQLException {
		this.CoreClass = CoreClass;
		
		this.DBhostAddress = DBhostAddress;
		this.DBUserName = DBUserName;
		this.DBPassword = DBPassword;
	
		
		ResultSet res = null;
		try {
			connect();
					
			res = executeSQL(
					"SELECT * FROM INFORMATION_SCHEMA.TABLES LIMIT 40, 999");
			
			
			tables = new ArrayList<String>(); // Liste over alle tabell-navn
			primaryKeys = new HashMap<String, String>(); // Liste over hver tabell sin primary key
			
			DatabaseMetaData dataMeta = connection.getMetaData();
			
			String tableName;
			while (res.next()) {
				tableName = res.getString(3);
				tables.add(tableName);
				
				ResultSet primaryKeyResSet = dataMeta.getPrimaryKeys(null, null, tableName);
				primaryKeyResSet.next();
				
				primaryKeys.put(
						tableName, 
						primaryKeyResSet.getString(4));
			}
			res.close();
			
			
			tableColumns = new HashMap<String, List<String>>(); // Liste over kolonnenavnene til hver tabell:
				
			for (String tabName : tables) {
				
				res = getQuery(tabName);
				ResultSetMetaData resMeta = res.getMetaData();
				
				tableColumns.put(tabName, new ArrayList<String>());
				
				int columnsSize = resMeta.getColumnCount();
				for (int i = 1; i <= columnsSize; i++) {
					
					if (resMeta.isWritable(i))
						tableColumns.get(tabName).add( resMeta.getColumnLabel(i));
				}
			}
				
		} catch (SQLException ex) {
			// TODO
			
			// Kanskje Core.ConnectionFailed(); som ikke krasjer programmet? -Sindre
			
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			
			throw ex;
			
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
		
	public ResultSet getQuery(String DBName, String... columns) throws SQLException {
		
		if (columns.length < 1)
			return executeSQL("SELECT * FROM "+ DBName);
		
		StringBuilder sBuild = new StringBuilder();
		int columnI = 0;
		for (; columnI < columns.length - 1; columnI++) {
			
			sBuild.append(columns[columnI]);
			sBuild.append(", ");
		}
		sBuild.append(columns[columnI]);
		
		return executeSQL(String.format("SELECT %s FROM %s", sBuild, DBName));
		
	}	
	
	public ResultSet getQueryJoined(String DBName, String DB2Name, 
			String matchingColumn1, String matchingColumn2, String... columns) throws SQLException {
		// TODO
		String SQLString = "";
		
		// Gir en Resultset med tabellradene hvor matchingColumn1 == matchingColumn1
		// * columns-argumentene som skal vises må skrives på form 'table.column'  -Sindre
		
		return executeSQL(SQLString);
		
	}
	
	
	public void insertRow(String DBName, Object... fields) throws SQLException {
		
		if (fields.length < 1) {
			executeSQL("INSERT INTO "+ DBName + "DEFAULT VALUES");
			return;
		}

		StringBuilder sBuild = new StringBuilder();
		int columnI = 0; // columnI-deklarasjonen er utenfor for-løkken slik at den kan brukes rett etter -Sindre
		for (; columnI < fields.length - 1; columnI++) {
			
			sBuild.append(fields[columnI]);
			sBuild.append(", ");
		}
		sBuild.append(fields[columnI]);
		
		executeSQL(String.format("INSERT INTO %s VALUES (%s)", DBName, sBuild));
		
	}
	
	
	public void deleteRow(String table, int ID) throws SQLException {
		
		executeSQL(String.format("DELETE FROM %s WHERE %s", table, ID));
		
	}

	
	public void editRow(String DBName, Object primaryKey, Object... writableFields) throws SQLException {
		
		if (writableFields.length < 1) {
			throw new SQLException("writableFields must have more than 0 elements");
		}

		List<String> columns = tableColumns.get(DBName);
		
		StringBuilder sBuild = new StringBuilder();
		
		int columnI = 0;
		for (; columnI < writableFields.length - 1; columnI++) {
			
			sBuild.append(columns.get(columnI));
			sBuild.append("=");
			sBuild.append(writableFields[columnI]);
			sBuild.append(", ");
		}
		sBuild.append(columns.get(columnI));
		sBuild.append("=");
		sBuild.append(writableFields[columnI]);
		
		executeSQL(String.format("UPDATE %s SET %s WHERE %s = %s", 
				DBName, sBuild, primaryKeys.get(DBName), primaryKey));
		
	}
	
	private ResultSet executeSQL(String SQLString) throws SQLException { 
		Statement statement = null;
		ResultSet result = null;
		
		try {
			
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			if (statement.execute(SQLString)) {     // Returnerer 1 om query (SELECT), 0 om noe blir endret (eks. INSERT) -Sindre 
				result = statement.getResultSet();
			}
			
		} catch (SQLException ex) {
			// TODO: Kanskje Core.DBConnectionFailure() som i konstruktøren? -Sindre
			
			throw (new SQLException(
					" SQLException: " + ex.getMessage() + 
					"\n SQLState: " + ex.getSQLState() + 
					"\n VendorError: " + ex.getErrorCode()));	    
		}
		
		return result;
	}

	
	public static void main(String[] args) throws SQLException {
		DBConnector dbc = new DBConnector(new Core(),
				Core.DBhostAddress, Core.DBUserName, Core.DBPassword);
		
		for (String key : dbc.primaryKeys.keySet()) {
			System.out.println(dbc.primaryKeys.get(key));
		}
		System.out.println();

		ResultSet res = dbc.getQuery("koie");
		ResultSetMetaData resMeta = res.getMetaData();
		
		int columnCount = resMeta.getColumnCount();
		for (int columnI = 1; columnI <= columnCount; columnI++) {
			//System.out.print(((com.mysql.jdbc.ResultSetMetaData) resMeta).getField(0).isPrimaryKey()+"  ");
			System.out.println(resMeta.getColumnTypeName(columnI));
		}
	}
}
