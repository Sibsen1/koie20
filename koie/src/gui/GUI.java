package gui;

import core.Core;

public class GUI {

	private Core CoreClass; 		// All kommunikasjon med resten av programmet skjer med denne. -Sindre
									
	private PageHub pageHub;
	private IniPage iniPage;
	
	private boolean userIsAdmin;
	
	public GUI(Core CoreClass) { // GUI() tar i mot all informasjon som er nødvendig ved start for hvert GUI-element -Sindre
		this.CoreClass = CoreClass;
		pageHub = new PageHub();
		}
		
	public boolean login(String email) {
		if (CoreClass.login(email)) {
			userIsAdmin = CoreClass.isAdmin;
			return true;
		}
		
		return false;
	}
}

