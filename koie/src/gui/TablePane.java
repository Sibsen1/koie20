package gui;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	JTextPane labInfo;
	List<String> colnames;
	String koiename;
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
		for (int i = 0; i < g.CoreClass.getDataBaseColumns("koie","koienavn").size();i++){
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
		//cards.add(start,"Start");
		//--------------
		cards.add(koie,"Koie");
		koie.setLayout(null);
		
		labKoienavn = new JLabel();
		labKoienavn.setText("koienavn");
		labKoienavn.setBounds(48, 49, 169, 44);
		koie.add(labKoienavn);
		
		labInfo = new JTextPane();
		labInfo.setContentType("text/html");
		labInfo.setBounds(48, 102, 169, 257);
		koie.add(labInfo);
		
		JTextArea txtRapport = new JTextArea();
		txtRapport.setLineWrap(true);
		txtRapport.setBounds(349, 188, 214, 171);
		koie.add(txtRapport);
		
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
		String texts = "";
		List<Object> thisKoie = new ArrayList<Object>();
		for (List<Object> o: koieInfo){
			if (o.get(0).equals( name)){
				thisKoie = o;
				int k = 0;
				for (String s:colnames){
					texts += s +"    "+ thisKoie.get(k).toString() + "<br><br>";
					k++;
				}
				labInfo.setText(texts);
				break;
			}
		}
		
	}
}