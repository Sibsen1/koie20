package core;

import gui.GUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
	
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

	public ArrayList<String> getRow(String table, int rowNr) {
		// TODO
		return new ArrayList<String>();
	}

	// Returnerer 2D-ArrayList av én eller flere kolonner: -Sindre
	public ArrayList<List<String>> getDataBaseColumns(String table, String... attributeName) {
		// TODO
		return new ArrayList<List<String>>();
	}

	// Dersom vi trenger det: Denne gir en enkelt-celles verdi -Sindre
	public String getKoieAttribute(int koieID, String attributeName) {
		// TODO
		return "";
	}
	
	// Husk! All bruk av DBConnector må gå igjennom en metode her først. -Sindre 
}
