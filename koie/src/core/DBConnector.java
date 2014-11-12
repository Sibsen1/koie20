package core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnector {
	private Core CoreClass;
	
	String DBhostAddress;
	String DBUserName;
	String DBPassword;
	
	public Connection connection;
	
	List<String>                     tables;
	Map<String, List<String>>        tableColumnNames;
	Map<String, Map<String, String>> tableColumnTypes;
	Map<String, String>              tablePrimaryKey;
	Map<String, String>              tablePrimaryKeyType;
	
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
			tablePrimaryKey = new HashMap<String, String>(); // Liste over hver tabell sin primary key
			tablePrimaryKeyType = new HashMap<String, String>(); // Liste over typen til hver tabell sin primary key
			
			DatabaseMetaData dataMeta = connection.getMetaData();
			
			String tableName;
			while (res.next()) {
				tableName = res.getString(3);
				tables.add(tableName);
				
				ResultSet primaryKeyResSet = dataMeta.getPrimaryKeys(null, null, tableName);
				primaryKeyResSet.next();
				
				tablePrimaryKey.put(
						tableName, 
						primaryKeyResSet.getString(4));
				
				int primaryKeyColumn = Integer.valueOf(primaryKeyResSet.getString(5));
				tablePrimaryKeyType.put(
						tableName, 
						res.getMetaData().getColumnTypeName(primaryKeyColumn));
				
			}
			res.close();
			
			
			tableColumnNames = new HashMap<String, List<String>>(); // Liste over kolonnenavnene til hver tabell:
			tableColumnTypes = new HashMap<String, Map<String, String>>(); // Liste over typene til hver kolonne
				
			for (String tabName : tables) {
				
				res = getQuery(tabName);
				ResultSetMetaData resMeta = res.getMetaData();
				
				tableColumnNames.put(tabName, new ArrayList<String>());
				tableColumnTypes.put(tabName, new HashMap<String, String>());
				
				int columnsSize = resMeta.getColumnCount();
				for (int i = 1; i <= columnsSize; i++) {
					
					if (resMeta.isWritable(i)) {
						String columnName = resMeta.getColumnLabel(i);
						tableColumnNames.get(tabName).add( columnName);
						tableColumnTypes.get(tabName).put(columnName, resMeta.getColumnTypeName(i));
					}
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
			} catch (NullPointerException e) {
				
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
	
	
	public ResultSet getQuery(String table, String... columns) throws SQLException {
		
		if (columns.length < 1)
			return executeSQL("SELECT * FROM "+ table);
		
		StringBuilder sBuild = new StringBuilder();
		int columnI = 0;
		for (; columnI < columns.length - 1; columnI++) {
			
			sBuild.append(columns[columnI]);
			sBuild.append(", ");
		}
		sBuild.append(columns[columnI]);
		
		return executeSQL(String.format("SELECT %s FROM %s", sBuild, table));
		
	}
	
	
	public ResultSet getQueryCondition(String table, String rowIDColumn, Object rowID, String... columns) throws SQLException {
		if (rowIDColumn == null) {
			rowIDColumn = tablePrimaryKey.get(table);
		}
		//System.out.println("rowID:" +rowID.toString());
		String rowIDString = "";
		
		switch (tableColumnTypes.get(table).get(rowIDColumn)) {
		
			case "DATE":
				Calendar rowIDCal = (Calendar) rowID;
				
				rowIDString = "DATE('"
						+ rowIDCal.get(Calendar.YEAR) + "-"
						+ (rowIDCal.get(Calendar.MONTH) + 1) + "-"
						+ rowIDCal.get(Calendar.DAY_OF_MONTH)
						+ "')";
				break;
			case "VARCHAR":
				rowIDString = "'" + rowID + "'";
				break;
			default:
				rowIDString = "" + rowID;		
		}
		
		//System.out.println(String.format("SELECT * FROM %s WHERE %s=%s", table, rowIDColumn, rowIDString));
		if (columns.length < 1)
			return executeSQL(String.format("SELECT * FROM %s WHERE %s=%s", table, rowIDColumn, rowIDString));
		
		StringBuilder sBuild = new StringBuilder();
		int columnI = 0;
		for (; columnI < columns.length - 1; columnI++) {
			
			sBuild.append(columns[columnI]);
			sBuild.append(", ");
		}
		sBuild.append(columns[columnI]);
		System.out.println("RowIDSTRING: "+rowIDString);
		//System.out.println((String.format("SELECT %s FROM %s WHERE %s=%s", sBuild, table, rowIDColumn, rowIDString)));
		return executeSQL(String.format("SELECT %s FROM %s WHERE %s=%s", sBuild, table, rowIDColumn, rowIDString));
	}
	

	public ResultSet getQueryJoined(String table1, String table2, String matchingColumn1,String matchingColumn2, String... columns) throws SQLException {

		if (columns.length < 1)
			return executeSQL(String.format("SELECT * FROM %s INNER JOIN %s ON %s=%s", table1, table2, matchingColumn1, matchingColumn2));
		
		
		StringBuilder sBuild = new StringBuilder();
		int columnI = 0;
		for (; columnI < columns.length - 1; columnI++) {
			String column = columns[columnI];
			
			if (!column.contains("."))
				throw new SQLException("'"+column+"' invalid argument. Must specify which table each column is from: <table>.<column>");
			
			sBuild.append(column);
			sBuild.append(", ");
		}
		sBuild.append(columns[columnI]);
		
		return executeSQL(String.format("SELECT %s FROM %s INNER JOIN %s ON %s=%s", sBuild, table1, table2, matchingColumn1, matchingColumn2));
		
	}
	
	
	public void insertRow(String table, Object... writableFields) throws SQLException {
		
		int WFLength = writableFields.length;		
		List<String> columnNames = tableColumnNames.get(table);
		
		if (WFLength < 1) {
			executeSQL("INSERT INTO (NULL) "+ table + " (NULL)");
			return;
			
		} else if (WFLength != columnNames.size()) {			
			throw new SQLException("Must have exactly 0 or "+ WFLength + " field-arguments "
					+ "(can have fields which are null, if auto-incrementing).");
		}
		
		Map<String, String> columnTypes = tableColumnTypes.get(table);
		
		List<String> columnsToAdd = new ArrayList<String>(columnNames);		
		
		StringBuilder sBuild = new StringBuilder();
		
		int columnI = 0;
		while (columnI < writableFields.length) {
			Object argument = writableFields[columnI];
					
			if (argument == null) {
				columnsToAdd.remove(columnNames.get(columnI));
				columnI++;
				
			} else {
				switch (columnTypes.get(columnNames.get(columnI))) {
				
				case "DATE":
					Calendar dateArg = (Calendar) argument;
	
					sBuild.append("DATE('");
					sBuild.append(dateArg.get(Calendar.YEAR)); sBuild.append("-");
					sBuild.append(dateArg.get(Calendar.MONTH) + 1); sBuild.append("-");
					sBuild.append(dateArg.get(Calendar.DAY_OF_MONTH));
					sBuild.append("')");
					break;
					
				case "TINYINT":
					sBuild.append(writableFields[columnI]);
					break;
				case "INT":
					sBuild.append(writableFields[columnI]);
					break;
					
				default:
					sBuild.append("'");
					sBuild.append(writableFields[columnI]);
					sBuild.append("'");
					break;
				}
				
				columnI++;
				if (columnI < writableFields.length)
					sBuild.append(", ");
			}
		}
		String columnsAdded = String.join(", ", columnsToAdd);
		
		//System.out.printf("INSERT INTO %s (%s) VALUES (%s)\n\n", table, columnsAdded, sBuild);
		executeSQL(String.format("INSERT INTO %s (%s) VALUES (%s)", table, columnsAdded, sBuild));
		
	}
	
	
	public void deleteRow(String table, Object primaryKey) throws SQLException {

		switch (tablePrimaryKeyType.get(table)) {
		case "INT": break;
		case "TINYINT": break;
		default:
			primaryKey = "'"+primaryKey+"'";
		}
		
		executeSQL(String.format("DELETE FROM %s WHERE %s=%s", table, tablePrimaryKey.get(table), primaryKey));
		
	}

	
	public void editRow(String table, Object primaryKey, Object... wFields) throws SQLException {
		List<String> columnNames = tableColumnNames.get(table);
		ArrayList<Object> writableFields = new ArrayList<Object>(Arrays.asList(wFields));
		if (writableFields.size() != columnNames.size()) {
			System.out.println(writableFields.size()+"-"+columnNames.size());
			throw new SQLException("Must have exactly "+ writableFields.size() + " field-arguments "
					+ "(can have fields which are null).");
		}
		
		Map<String, String> columnTypes = tableColumnTypes.get(table);
		StringBuilder sBuild = new StringBuilder();
		System.out.println(writableFields);
		int columnI = 0;
		while (columnI < writableFields.size()) {
			Object argument = writableFields.get(columnI);
					
			if (argument == null) {
				columnI++;
				
				continue;
			}
			sBuild.append(columnNames.get(columnI));
			sBuild.append(" = ");
				
			
			switch (columnTypes.get(columnNames.get(columnI))) {
			
			case "DATE":
				Calendar dateArg = (Calendar) argument;

				sBuild.append("DATE('");
				sBuild.append(dateArg.get(Calendar.YEAR)); sBuild.append("-");
				sBuild.append(dateArg.get(Calendar.MONTH) + 1); sBuild.append("-");
				sBuild.append(dateArg.get(Calendar.DAY_OF_MONTH));
				sBuild.append("')");
				break;
				
			case "TINYINT":
				sBuild.append(writableFields.get(columnI));
				break;
			case "INT":
				sBuild.append(writableFields.get(columnI));
				break;
				
			default:
				sBuild.append("'");
				sBuild.append(writableFields.get(columnI));
				sBuild.append("'");
				break;
			}
			
			columnI++;
			if (columnI < writableFields.size())
				sBuild.append(", ");
		
		}
		if (sBuild.substring(sBuild.length()-2, sBuild.length()).equals(", ")){
			sBuild.deleteCharAt(sBuild.length()-2);
		}
		switch (tablePrimaryKeyType.get(table)) {
		case "INT": break;
		case "TINYINT": break;
		default:
			primaryKey = "'"+primaryKey+"'";
		}
		
		System.out.printf("UPDATE %s SET %s WHERE %s = %s", table, sBuild, tablePrimaryKey.get(table), primaryKey);
		executeSQL(String.format("UPDATE %s SET %s WHERE %s = %s", table, sBuild, tablePrimaryKey.get(table), primaryKey));
	}
	

	private ResultSet executeSQL(String SQLString) throws SQLException { 
		Statement statement = null;
		ResultSet result = null;
		
		try {
			
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			//System.out.println(SQLString);
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

	public static void printResultSet(ResultSet res) throws SQLException {
		ResultSetMetaData resMeta = res.getMetaData();
		int columnCount = resMeta.getColumnCount();
		
		while (res.next()) {
			for (int i = 1; i <= columnCount; i++) {
				System.out.print(res.getObject(i)+" ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws SQLException {
		DBConnector dbc = new DBConnector(new Core(),
				Core.DBhostAddress, Core.DBUserName, Core.DBPassword);
		
		System.out.println("Primary Keys:");
		for (String key : dbc.tablePrimaryKey.keySet()) {
			System.out.printf(" %-14s"+dbc.tablePrimaryKey.get(key) + "\n", key+": ");
		}
		
		System.out.println("\nTable Fields - table (rows):");		
		for (String table: dbc.tables) {
			ResultSet res = dbc.getQuery(table);
			ResultSetMetaData resMeta = res.getMetaData();
			res.last();
			
			System.out.printf("%-20s", " "+table+" ("+res.getRow()+"): ");
			int columnCount = resMeta.getColumnCount();
			for (int columnI = 1; columnI <= columnCount; columnI++) {
				System.out.print(resMeta.getColumnName(columnI)+" ("+resMeta.getColumnTypeName(columnI)+"), ");
			}
			System.out.println();
		}

		System.out.println("\nWritable Table Fields:");		
		for (String table: dbc.tables) {
			
			Map<String, String> tableTypes = dbc.tableColumnTypes.get(table);
			
			System.out.printf("%-15s", " "+table+": ");
			for (String columnName : dbc.tableColumnNames.get(table)) {
				System.out.print(columnName+" ("+tableTypes.get(columnName)+"), ");
			}
			System.out.println();
		}
		System.out.println();
		
		printResultSet(dbc.getQuery("user"));
		
		printResultSet(dbc.getQuery("user"));
		
		int i = 1;
		Object x = (Object) i;
	}
}
