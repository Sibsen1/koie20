package core;

public class Koie {
	
	private final int koieID;
	private String koieNavn;
	private String koieBeskrivelse;
	private int sengePlasser;
	private int bordPlasser;
	private final int aar;
	private String terreng;
	
	public Koie(int koieID, String koieNavn, String koieBeskrivelse, int sengePlasser, int bordPlasser, int aar, String terreng) {
		this.koieID = koieID;
		this.koieNavn = koieNavn;
		this.koieBeskrivelse = koieBeskrivelse;
		this.sengePlasser = sengePlasser;
		this.bordPlasser = bordPlasser;
		this.aar = aar;
		this.terreng = terreng;
	}
	
	
	public int getKoieID() {
		return koieID;
	}
	
	/*public void setKoieID(int koieID) {
		this.koieID = koieID;
	}*/
	
	public String getKoieNavn() {
		return koieNavn;
	}
	
	public void setKoieNavn(String koieNavn) {
		this.koieNavn = koieNavn;
	}
	
	public String getKoieBeskrivelse() {
		return koieBeskrivelse;
	}
	
	public void setKoieBeskrivelse(String koieBeskrivelse) {
		this.koieBeskrivelse = koieBeskrivelse;
	}
	
	public int getSengePlasser() {
		return sengePlasser;
	}
	
	public void setSengePlasser(int sengePlasser) {
		this.sengePlasser = sengePlasser;
	}
	
	public int getBordPlasser() {
		return bordPlasser;
	}
	
	public void setBordPlasser(int bordPlasser) {
		this.bordPlasser = bordPlasser;
	}
	
	public int getAar() {
		return aar;
	}
	
	
	/*public void setAar(int aar) {
		this.aar = aar;
	}*/
	
	public String getTerreng() {
		return terreng;
	}
	
	public void setTerreng(String terreng) {
		this.terreng = terreng;
	}
}