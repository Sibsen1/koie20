package gui;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;

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
		String column_names[]= {"<html>Koie<br>navn:</html>","<html>Senge<br>plasser:</html>","<html>Bord<br>plasser:</html>", "År:", "Terreng:", "<html>Topp<br>tur:</html>", "<html>Jakt<br>fiske:</html>", "Spesialiteter:", "<html>Sekker<br>ved:</html>"};
		System.out.println(g.CoreClass.getDataBaseColumns("koie","koienavn").size());
		for (int i = 0; i < g.CoreClass.getDataBaseColumns("koie","koienavn").size();i++){
		 koieInfo.add(g.CoreClass.getDataBaseRow("koie", Integer.toString(i), "koienavn","sengeplasser","bordplasser","aar","terreng","topptur","jaktfiske","spesialiteter","vedstatus"));//g.CoreClass.getDataBaseColumns("koie", null);
		}
		DefaultTableModel table_model = new DefaultTableModel(column_names,0);//rapport.get(0).size()); //Lager headers og setter rowcount til antall rapporter
		for (List<Object> o : koieInfo){
			table_model.addRow(o.toArray());
		} 
		tblKoier = new JTable(table_model);
		tblKoier.enable(false);
		jspB = new JScrollPane(tblKoier);
		jspB.setBounds(29, 26, 540, 399);
		start.add(jspB);
		//cards.add(start,"Start");
		//--------------
		cards.add(koie,"Koie");
		koie.setLayout(null);
		
		JTextPane txtpnKoienavn = new JTextPane();
		txtpnKoienavn.setEditable(false);
		txtpnKoienavn.setText("koienavn");
		txtpnKoienavn.setBounds(48, 49, 169, 44);
		koie.add(txtpnKoienavn);
		
		JTextPane labInfo = new JTextPane();
		labInfo.setEditable(false);
		labInfo.setBounds(48, 136, 176, 257);
		koie.add(labInfo);
		
		JTextArea txtRapport = new JTextArea();
		txtRapport.setLineWrap(true);
		txtRapport.setBounds(349, 188, 214, 171);
		koie.add(txtRapport);
		
		JTextPane labSkrivRapport = new JTextPane();
		labSkrivRapport.setEditable(false);
		labSkrivRapport.setText("Skriv rapport:");
		labSkrivRapport.setBounds(349, 153, 124, 20);
		koie.add(labSkrivRapport);
		
		JButton btnSendRapport = new JButton("Send rapport");
		btnSendRapport.setBounds(349, 401, 124, 23);
		koie.add(btnSendRapport);
		
		JTextPane labAntallSekkerVed = new JTextPane();
		labAntallSekkerVed.setEditable(false);
		labAntallSekkerVed.setText("Antall sekker ved igjen:");
		labAntallSekkerVed.setBounds(349, 370, 136, 20);
		koie.add(labAntallSekkerVed);
		
		comboBox = new JComboBox();
		for (int i = 1; i < 11;i++ ){
			comboBox.addItem(Integer.toString(i));
		}
		comboBox.setBounds(483, 370, 81, 20);
		koie.add(comboBox);
		
		JTextPane labFeil = new JTextPane();
		labFeil.setBounds(483, 401, 81, 20);
		koie.add(labFeil);
		add(cards);
	}
	
	public void showKoie(String name){
		cardLayout.show(cards,"Koie");
		comboBox.setSelectedIndex(g.CoreClass.getWoodStatus(name));
	}
}