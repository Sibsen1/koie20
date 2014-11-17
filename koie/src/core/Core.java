package core;

import gui.GUI;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.sql.ResultSet;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import com.sun.mail.smtp.SMTPTransport;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Core {
	
	public String userEmail; 
	public boolean isAdmin;	
	
	final static String DBhostAddress = "jdbc:mysql://r.saggau.no:3306/koie20";
	final static String DBUserName = "user";
	final static String DBPassword = "koie20pw";

	final static String EMAIL_USER = "koie20it1901";
	final static String EMAIL_PASSWORD = "koie20koie20";

	final static String KOIER = "koie";
	final static String REPORTS = "rapport";
	final static String RESERVATIONS = "reservations";
	final static String USERS = "user";
	
	private GUI GUIClass;
	public DBConnector DBClass;
	
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
	
	public boolean insertReservation(String koie, String email, Date dato, int antallPersoner) {
		Calendar date = Calendar.getInstance();
		date.setTime(dato);
		
		int reservations = 0;
		ArrayList<List<Object>> reservationsList = getDataBaseColumns(RESERVATIONS, "koie_idkoie", "date","antall_personer");
		
		for (int i = 0; i < reservationsList.size(); i++) {
			
			if ((int) reservationsList.get(i).get(0) == getKoieID(koie)) {
				Calendar cal = new GregorianCalendar();
				cal.setTime((Date) reservationsList.get(i).get(1));
				
				if (cal.get(Calendar.YEAR) == date.get(Calendar.YEAR) && (cal.get(Calendar.MONTH))+1 == date.get(Calendar.MONTH)+1 
						&& cal.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
					
					reservations += (int) reservationsList.get(i).get(2);
					
				}
			}
		}
		if (reservations + antallPersoner <= (int) getDataBaseRow(KOIER, getKoieID(koie), "sengeplasser").get(0)) {
			
			try {
				DBClass.insertRow(RESERVATIONS, null, getKoieID(koie), email, date, antallPersoner);
				return true;
			} catch (SQLException e) {
				DBFailure(e);
				return false;
			}
			
		}
		else {
			return false; 
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
			//System.out.println("Koienavn:"+koie);
			return (int) resToList(DBClass.getQueryCondition("koie", "koienavn", koie, "vedstatus")).get(0).get(0);			
		} catch (SQLException e) {
			DBFailure(e);
			return -1;
		} catch (IndexOutOfBoundsException e) {
			DBFailure(new Exception("getWoodStatus('" + koie + "') returnerer en tom liste. "));
			return -1;
		}
		
	}

	public void setWoodStatus(String koie, int woodSacks) {
		//System.out.println("setWW");
		try {
			DBClass.editRow("koie", getKoieID(koie), null, null, null, null, null, null, null, null, null, null, null, null, null, null, woodSacks,null);
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
	
	public boolean editUser(String email, boolean isAdmin) {
		try {
			ArrayList<List<Object>> userList = getDataBaseColumns(USERS);
			for (List<Object> user: userList) {
				String emailStr = (String) user.get(0);
				if (email.equals(emailStr)) {
					DBClass.editRow("user", email, email, isAdmin);
					return true;
				}
			}		
		} catch (SQLException e) {
			DBFailure(e);
		}
		try {
			DBClass.insertRow(USERS, email, isAdmin);
			return true;
			
		} catch (SQLException e) {
			DBFailure(e);
			return false;
		}
		
	}
	
	public void editKoie(String koie, int sengeplasser, int bordplasser, String aar, String terreng, String sykkel, String topptur, String jaktfiske, String gitar, String vaffeljern, String spesialiteter, float latitude, float longitude, int vedstatus, String fraktes) {
		try {
			DBClass.editRow(KOIER, getKoieID(koie),null,koie, sengeplasser, bordplasser, aar, terreng, sykkel, topptur, jaktfiske, gitar, vaffeljern, spesialiteter, latitude, longitude, vedstatus, fraktes);
		} catch (SQLException e) {
			DBFailure(e);
		}
	}
	

	public ArrayList<Object> getDataBaseRow(String table, Object primaryKey, String... columns) {
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
	
	
	public boolean SendEmailToReservations(String koieName, int resYear, int resMonth, int resDay, String title, String message) {
		
		int idkoie = getKoieID(koieName);
		
		List<List<Object>> reservationEmails;
		try {
			reservationEmails = resToList(DBClass.getQueryCondition(RESERVATIONS, "date", new GregorianCalendar(resYear, resMonth-1, resDay), "koie_idkoie", "user_mail"));
		} catch (SQLException e) {
			DBFailure(e);
			return false;
		}
		
		if (reservationEmails.isEmpty())
			return false;
		
		List<String> recipientEmails = new ArrayList<String>();
		for (List<Object> recpList: reservationEmails ) {

			if ((int) recpList.get(0) == idkoie) {
				recipientEmails.add((String) recpList.get(1));
			}
		}
		
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");


        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        final MimeMessage msg = new MimeMessage(session);

        
        try {
			msg.setFrom(new InternetAddress(EMAIL_USER + "@gmail.com"));
        
	        for (String recipientEmail : recipientEmails) {
	        	msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
	        }
	
	        msg.setSubject(title);
	        msg.setText(message, "utf-8");
	        msg.setSentDate(new Date());
	
	        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
	
	        t.connect("smtp.gmail.com", EMAIL_USER, EMAIL_PASSWORD);
	        t.sendMessage(msg, msg.getAllRecipients());      
	        t.close();
	        return true;
	        
        } catch (MessagingException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        	return false;
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
