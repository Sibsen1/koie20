package core;

import gui.GUI;

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
	
	public Core() {
		GUIClass = new GUI(this);
		DBClass = new DBConnector(this, DBhostAddress, DBUserName, DBPassword);
	}

	public boolean login(String email) {
		String[] parts = email.split("@");
		if (parts.length == 2 && parts[1].indexOf(".")!=-1){ // Check for only one @ and atleast one. -Ole
			
			isAdmin = true;
			userEmail = email;
			// TODO
			
			return true;
		}
		return false;
	}

	public ArrayList<String> getKoieProperties(int koieID) {
		// TODO
		return new ArrayList<String>();
	}

	// Returnerer 2D-ArrayList av én eller flere kolonner:
	// * Husk når vi lager ArrayList: "List l = ArrayList". Derfor er det Array<List> som må gjøres om til ArrayList<ArrayList> når den skapes -Sindre
	public ArrayList<List<String>> getDataBaseColumns(String DBName, String... attributeName) {
		// TODO
		return new ArrayList<List<String>>();
	}

	// Dersom vi trenger det: Denne gir en enkelt-celles verdi -Sindre
	public String getKoieAttribute(int koieID, String attributeName) {
		// TODO
		return "";
	}
	
	// Husk! All DBConnector-påkallelser må gå igjennom en metode her først. -Sindre 
}
