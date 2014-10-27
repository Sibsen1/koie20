package core;

import gui.GUI;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;

	
// Merknad: Alle individuelle kommentarer må være signert,
// * med mindre de henger sammen, som dette. -Sindre

public class Core {
	
	public String userEmail; // Vi trenger ingen brukerklasse før det viser 
	public boolean isAdmin;	 // -> seg at disse to ikke er alt vi trenger.
	
	final static String DBhostAddress = "jdbc:mysql://r.saggau.no:3306/koie20";
	final static String DBUserName = "user";
	final static String DBPassword = "koie20pw";
	
	private GUI GUIClass;
	private DBConnector DBClass;
	
	public Core() throws SQLException {
		GUIClass = new GUI(this);
		DBClass = new DBConnector(this, DBhostAddress, DBUserName, DBPassword);
	}

	public void login(String email) {
		this.userEmail = email;
		//this.isAdmin = DBClass.checkAdmin(email);
	}
	
	public void reserve(String koie, String email, int persons, Date date, int days) {
		// TODO
	}
	
	
	public ArrayList<List<String>> getReports() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	
	public ArrayList<List<String>> insertReservation() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	public ArrayList<List<String>> insertUser() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	public ArrayList<List<String>> insertKoie() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	public ArrayList<List<String>> insertReport() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	
	
	public void deleteFromTable(String table, Object primaryKey) {
		try {
			DBClass.deleteRow(table, primaryKey);
		} catch (SQLException e) {
			DBFailure(e);
		}
	}
	
	
	public ArrayList<List<String>> editReservation() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	public ArrayList<List<String>> editUser() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	public ArrayList<List<String>> editKoie() {
		// TODO
		return new ArrayList<List<String>>();
	}
	
	
	public static ArrayList<List<String>> resToList(ResultSet resultSet) {
		
		ArrayList<List<String>> results = new ArrayList<>();
		try {
			int columnCount = resultSet.getMetaData().getColumnCount();
			while (resultSet.next()) {
				List<String> row = new ArrayList<>(columnCount);
				int i = 1;
				while (i <= columnCount) {
					row.add(resultSet.getString(i++));
				}
				results.add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
		
	}
	
	public void DBFailure(Exception... e) {
		// TODO Her påkaller den DBConnector disconnect og GUI shutdown
		e[0].printStackTrace();
	}

	public ArrayList<String> getDataBaseRow(String table, String primaryKey, String... columns) {
		return resToList(getQueryCondition(table, null, primaryKey)).get(0);
	}


	public ArrayList<List<String>> getDataBaseColumns(String table, String... columns) {
		try {
			return (resToList( DBClass.getQuery(table, columns)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<List<String>>();
	}
	
	// Husk! All bruk av DBConnector må gå igjennom en metode her først. -Sindre
	
	public static void main(String[] args) {
		try {
			Core core = new Core();
			System.out.println(core.getDataBaseColumns("koie", "koienavn"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
