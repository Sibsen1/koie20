package core;

import gui.GUI;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;

public class Core {
	
	public String userEmail; 
	public boolean isAdmin;	
	
	final static String DBhostAddress = "jdbc:mysql://r.saggau.no:3306/koie20";
	final static String DBUserName = "user";
	final static String DBPassword = "koie20pw";

	final static String KOIER = "koie";
	final static String REPORTS = "rapport";
	final static String RESERVATIONS = "reservations";
	final static String USERS = "user";
	
	private GUI GUIClass;
	private DBConnector DBClass;
	
	public Core() throws SQLException {
		DBClass = new DBConnector(this, DBhostAddress, DBUserName, DBPassword);
		GUIClass = new GUI(this);
	}
	
	//Husk på å kopiere getKoieID, getWoodStatus og setWoodStatus insertuser
	
	public void showGUI(String email){
		GUIClass.login(email);
		GUIClass.show();
	}
	
	public int getKoieID(String koie) {
		try {
			return (int) resToList(DBClass.getQueryCondition(KOIER, "koienavn", koie, "idkoie")).get(0).get(0);			
		} catch (SQLException e) {
			DBFailure(e);
			return -1;
		}
	}

	public boolean login(String emailArg) {
		ArrayList<List<Object>> userList = getDataBaseColumns(USERS);
		
		for (List<Object> user: userList) {
			String emailStr = (String) user.get(0);
			if (emailArg.equals(emailStr)) {
				this.userEmail = emailStr;
				this.isAdmin = (Boolean) user.get(1);
				
				return true;
			}
		}		
		try {
			DBClass.insertRow(USERS, emailArg, false);
			this.userEmail = emailArg;
			this.isAdmin = false;
			return true;
			
		} catch (SQLException e) {
			DBFailure(e);
			return false;
		}
	}
	
	public void insertReservation(int koieID, String email, Date startDate, int days) {
		Calendar date = Calendar.getInstance();
		date.setTime(startDate);
		
		try {
			for (int i = 0; i < days; i++) {

				DBClass.insertRow(RESERVATIONS,null, koieID, email, date);
				date.add(Calendar.DAY_OF_MONTH, 1);
			}

		} catch (SQLException e) {
			DBFailure(e);
		}
	}
	
	public void insertReport(String email, String koie, String text, int vedStatus) {
		
		try {
			DBClass.insertRow(REPORTS, null, email, getKoieID(koie), text);
			setWoodStatus(koie, vedStatus);
		} catch (SQLException e) {
			DBFailure(e);
		}
		
	}
	
	public void insertUser(String email, boolean isAdmin) {
		try {
			DBClass.insertRow(USERS, email, isAdmin);
		} catch (SQLException e) {
			DBFailure(e);
		}		
	}
	
	
	public void insertKoie(String koieName, int beds, int tables, int year, String terrain, boolean hasBike, boolean isToppTur, boolean hasHuntingFishing, boolean hasGuitar, boolean hasWaffleIron, String specialities, float latitude, float longitude, String woodStatus) {
		// TODO
		
	}
	
	
	
	public ArrayList<List<Object>> getReports(String... columns) {
		try {
			return resToList(DBClass.getQueryJoined(REPORTS, KOIER, "koie.idkoie","rapport.koie_idkoie", columns));
			
		} catch (SQLException e) {
			DBFailure(e);
			return null;
		}
	}
	
	public int getWoodStatus(String koie) {
		try {
			return (int) resToList(DBClass.getQueryCondition("koie", "koienavn", koie, "vedstatus")).get(0).get(0);			
		} catch (SQLException e) {
			DBFailure(e);
			return -1;
		}
	}

	public void setWoodStatus(String koie, int woodSacks) {
		try {
			DBClass.editRow("koie", getKoieID(koie), null, null, null, null, null, null, null, null, null, null, null, null, null, null, woodSacks);
		} catch (SQLException e) {
			DBFailure(e);
		}
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
	
	public void editUser(String email, boolean isAdmin) {
		try {
			DBClass.editRow("user", email, isAdmin );
		} catch (SQLException e) {
			DBFailure(e);
		}
	}
	
	public void editKoie(String koie, int sengeplasser, int bordplasser, int aar, String terreng, boolean sykkel, boolean topptur, String jaktfiske, boolean gitar, boolean vaffeljern, String spesialiteter, float latitude, float longitude, int vedstatus) {
		try {
			DBClass.editRow(KOIER, getKoieID(koie), sengeplasser, bordplasser, aar, terreng, sykkel, topptur, jaktfiske, gitar, vaffeljern, spesialiteter, latitude, longitude, vedstatus);
		} catch (SQLException e) {
			DBFailure(e);
		}
	}
	

	public ArrayList<Object> getDataBaseRow(String table, String primaryKey, String... columns) {
		try {
			return (ArrayList<Object>) resToList(DBClass.getQueryCondition(table, null, primaryKey,columns)).get(0);
			
		} catch (SQLException e) {
			DBFailure(e);
			return null;
		}
	}


	public ArrayList<List<Object>> getDataBaseColumns(String table, String... columns) {
		try {
			return (resToList( DBClass.getQuery(table, columns)));
		} catch (SQLException e) {
			DBFailure(e);
			return null;
		}
	}
	
	
	public void DBFailure(Exception e) {
		// TODO Her påkaller den DBConnector disconnect og GUI shutdown
		System.out.println("DBFailure():");
		System.out.println("Her er programmet ment å krasje/endre tilstand! Årsak:");
		
		e.printStackTrace();
	}
	
	
	public static ArrayList<List<Object>> resToList(ResultSet resultSet) {
		
		ArrayList<List<Object>> results = new ArrayList<List<Object>>();
		try {
			int columnCount = resultSet.getMetaData().getColumnCount();
			
			while (resultSet.next()) {
				ArrayList<Object> row = new ArrayList<Object>(columnCount);
				
				int i = 1;
				while (i <= columnCount) {
					row.add(resultSet.getObject(i++));
				}
				results.add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
		
	}
	

	public static void main(String[] args) {
		try {
			Core core = new Core();
			System.out.println(core.getDataBaseColumns("user"));
			core.insertUser("test@test.co.uk", false);
			System.out.println(core.getDataBaseColumns("user"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
