package gui;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class TablePane extends JPanel{
	JTable tblKoier;
	ArrayList<List<Object>> koieInfo;
	JScrollPane jspB;
	JPanel cards;
	JPanel start;
	JPanel koie;
	CardLayout cardLayout;
	JComboBox comboBox;
	GUI g;
	JLabel labKoienavn;
	JLabel labInfo;
	List<String> colnames;
	String koiename;
	private List<String> ncolnames;
	private JTextArea txtRapport;
	public TablePane(GUI gui) {
		this.g = gui;
		setLayout(null);
		cards = new JPanel(new CardLayout());
		start = new JPanel();
		koie = new JPanel();
		this.setSize(600, 500);
		cards.setSize(600,500);
		cardLayout = (CardLayout) cards.getLayout();
		koieInfo = new ArrayList<List<Object>>();
		String column_names[]= {"<html>Koie<br>navn:</html>","<html>Senge<br>plasser:</html>","<html>Bord<br>plasser:</html>", "Terreng:", "<html>Topp<br>tur:</html>", "<html>Jakt<br>fiske:</html>", "Spesialiteter:"};
		String column[]= {"Koienavn:","Sengeplasser:","Bordplasser:","Terreng:","Topptur:","Jakt/fiske:","Spesialiteter"};
		colnames = Arrays.asList(column);
		int maxSize = g.CoreClass.getDataBaseColumns("koie","koienavn").size();
		for (int i = 0; i < maxSize;i++){
			koieInfo.add(g.CoreClass.getDataBaseRow("koie", Integer.toString(i), "koienavn","sengeplasser","bordplasser","terreng","topptur","jaktfiske","spesialiteter"));//g.CoreClass.getDataBaseColumns("koie", null);
		}
		
		
		DefaultTableModel table_model = new DefaultTableModel(column_names,0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		for (List<Object> o : koieInfo){
			table_model.addRow(o.toArray());
		} 
		tblKoier = new JTable(table_model);
		start.setLayout(null);
		jspB = new JScrollPane(tblKoier);
		jspB.setBounds(0, 0, 600, 446);
		start.add(jspB);
		cards.add(start,"Start");
		//--------------
		koieInfo.clear();
		for (int i = 0; i < maxSize;i++){
			koieInfo.add(g.CoreClass.getDataBaseRow("koie", Integer.toString(i)));//g.CoreClass.getDataBaseColumns("koie", null);
		}
		String[] newColumn = {"Koie nummer:","Koienavn:","Sengeplasser:","Bordplasser:","Bygget i:","Terreng:","Sykkel:","Topptur:","Jakt/fiske:","Gitar:","Vaffeljern:","Spesialiteter:","Latitude:","Longtitude:","Sekker ved:"};
		ncolnames = Arrays.asList(newColumn);
		cards.add(koie,"Koie");
		koie.setLayout(null);
		
		labKoienavn = new JLabel();
		labKoienavn.setFont(new Font("Tahoma", Font.BOLD, 18));
		labKoienavn.setText("koienavn");
		labKoienavn.setBounds(48, 11, 169, 44);
		koie.add(labKoienavn);
		
		labInfo = new JLabel();
		labInfo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labInfo.setBounds(48, 66, 169, 381);
		koie.add(labInfo);
		
		txtRapport = new JTextArea();
		txtRapport.setLineWrap(true);
		JScrollPane jspT = new JScrollPane(txtRapport);
		jspT.setBounds(349, 188, 214, 171);
		jspT.setBorder(new LineBorder(new java.awt.Color(0, 0, 0)));
		koie.add(jspT);
		
		JLabel labSkrivRapport = new JLabel();
		labSkrivRapport.setText("Skriv rapport:");
		labSkrivRapport.setBounds(349, 153, 124, 20);
		koie.add(labSkrivRapport);
		
		JButton btnSendRapport = new JButton("Send rapport");
		btnSendRapport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				g.CoreClass.insertReport(g.CoreClass.userEmail, koiename, txtRapport.getText(),comboBox.getSelectedIndex());
			}
		});
		btnSendRapport.setBounds(349, 401, 124, 23);
		koie.add(btnSendRapport);
		
		JLabel labAntallSekkerVed = new JLabel();
		labAntallSekkerVed.setText("Antall sekker ved igjen:");
		labAntallSekkerVed.setBounds(349, 370, 124, 20);
		koie.add(labAntallSekkerVed);
		
		comboBox = new JComboBox();
		for (int i = 0; i < 11;i++ ){
			comboBox.addItem(Integer.toString(i));
		}
		comboBox.setBounds(483, 370, 81, 20);
		koie.add(comboBox);
		
		JTextPane labFeil = new JTextPane();
		labFeil.setBounds(483, 401, 81, 20);
		labFeil.setEditable(false);
		koie.add(labFeil);
		
		JButton btnBack = new JButton("Tabell");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(cards, "Start");
			}
		});
		btnBack.setBounds(483, 11, 89, 23);
		koie.add(btnBack);
		add(cards);
	}
	
	public void showKoie(String name){
		this.koiename = name;
		cardLayout.show(cards,"Koie");
		labKoienavn.setText(name);
		comboBox.setSelectedIndex(g.CoreClass.getWoodStatus(name));
		String texts = "<html>";
		for (List<Object> o: koieInfo){
			if (o.get(1).equals( name)){
				int k = 0;
				for (String s:ncolnames){
					texts += s +"    "+ o.get(k).toString() + "<br>";
					k++;
				}
				labInfo.setText(texts+"</html>");
				break;
			}
		}
		
	}
}