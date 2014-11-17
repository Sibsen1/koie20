package gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.ScrollPane;

import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Font;

public class AdminPane extends JPanel {
	JPanel cards;
	GUI g;
	private JTextField txtAdmin;
	private JTextPane labOutput;
	private JTable tblRapport;
	List<String> names;
	List<String> woods;
	private JTextPane labWarning;
	private JScrollPane jspW;
	ArrayList<List<Object>> rapport;
	private JList lstKoier;
	private JList lstWood;
	private JScrollPane jspL;
	private JPanel lists;
	private JLabel labImgWarning;
	private Map<String,Integer> warnings;
	private CardLayout cardLayout;
	private JLabel labEdit;
	private JPanel start;
	private JPanel editKoie;
	private List<Object> koieInfo;
	private DefaultTableModel editModel;
	private String[] newColumns;
	private String[] formats;
	private List<String> lFormats;
	private JLabel labEditFeil;
	private JLabel labName;
	private JButton btnAdmin;
	private JLabel labIntr;
	public AdminPane(GUI gui) {
		this.g = gui;
		cards = new JPanel(new CardLayout());
		start = new JPanel();
		editKoie = new JPanel();
		cardLayout = (CardLayout) cards.getLayout();
		editKoie.setLayout(null);
		start.setLayout(null);
		setLayout(null);
		this.setSize(600, 500);
		cards.setSize(600, 500);
		start.setSize(600,500);
		editKoie.setSize(600,500);
		warnings = new HashMap<String,Integer>();
		
		//---------------------- Start
				labWarning = new JTextPane();
				labWarning.setEditable(false);
				labWarning.setForeground(Color.RED);
				jspW = new JScrollPane(labWarning);
				jspW.setBounds(289, 385, 281, 50);
				jspW.setVisible(false);
				start.add(jspW);
				labImgWarning = new JLabel();
				labImgWarning.setBounds(263, 385, 26, 50);
				labImgWarning.setVisible(false);;
				try {
					labImgWarning.setIcon(new ImageIcon(ImageIO.read(new File("resources/warning.png"))));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				start.add(labImgWarning);
				
				names = new ArrayList<String>();
				woods = new ArrayList<String>();
				updateInfo();
				
				lstKoier.setCellRenderer(new IconListRenderer());
				lstWood.setCellRenderer(new IconListRenderer());
				lstWood.setSelectionModel(new DisabledItemSelectionModel());
				
				lists = new JPanel();
				lists.add(lstKoier);
				lists.add(lstWood);
				jspL = new JScrollPane(lists);
				jspL.setBorder(new LineBorder(new Color(0, 0, 0)));
				jspL.setBounds(45, 44, 146, 330);
				start.add(jspL);
				
				
				JButton btnEdit = new JButton("Rediger");
				btnEdit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!lstKoier.isSelectionEmpty()){
							cardLayout.show(cards, "Edit");
							editMode();
							
						}
						else labEdit.setText("Velg en koie å redigere.");
					}
				});
				btnEdit.setBounds(201, 41, 89, 23);
				start.add(btnEdit);
				
				JLabel labKoier = new JLabel();
				labKoier.setText("Koier -");
				labKoier.setBounds(45, 13, 52, 20);
				labKoier.setBackground(this.getBackground());
				start.add(labKoier);
				JLabel labVed = new JLabel();
				labVed.setText(" - Ved");
				labVed.setBounds(120, 13, 52, 20);
				labVed.setBackground(this.getBackground());
				start.add(labVed);
				
				JLabel labNewAdmin = new JLabel();
				labNewAdmin.setText("Legg til ny admin:");
				labNewAdmin.setBounds(348, 13, 112, 20);
				labNewAdmin.setBackground(this.getBackground());
				start.add(labNewAdmin);
				
				txtAdmin = new JTextField();
				txtAdmin.setBounds(348, 42, 160, 22);
				start.add(txtAdmin);
				txtAdmin.setColumns(10);
				
				JButton btnAddAdmin = new JButton("+ Legg til");
				btnAddAdmin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String[] parts = txtAdmin.getText().split("@");
						if (parts.length == 2 && parts[1].indexOf(".")!=-1){
							if (g.CoreClass.editUser(txtAdmin.getText(),true)){ 
								txtAdmin.setText("");
								labOutput.setText("OK!");
							}
						}
						else {
							labOutput.setText("Oppgi gyldig email.");
						}
					}
				});
				btnAddAdmin.setBounds(348, 75, 89, 23);
				start.add(btnAddAdmin);
				
				String column_names[]= {"Bruker:","Koie:","Rapport:"};
				DefaultTableModel table_model = new DefaultTableModel(column_names,0);//rapport.get(0).size()); //Lager headers og setter rowcount til antall rapporter
				
				rapport = new ArrayList<List<Object>>();
				rapport = g.CoreClass.getReports("rapport.user_mail","koie.koienavn","rapport.rapporttext");
				for (List<Object> o : rapport){
					table_model.addRow(o.toArray());
				} 
				tblRapport = new JTable(table_model);
				tblRapport.getTableHeader().setReorderingAllowed(false);
				tblRapport.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			        public void valueChanged(ListSelectionEvent event) {
			            // do some actions here, for example
			            // print first column value from selected row
			        	JOptionPane.showMessageDialog(null, "<html><p style='width: 200px;'>"+tblRapport.getValueAt(tblRapport.getSelectedRow(), 2).toString()+"</html>");
			        }
			    });
				
				JScrollPane jspB = new JScrollPane(tblRapport);
				jspB.setBounds(218, 200, 352, 175);
				start.add(jspB);
				
				JLabel labRapport = new JLabel();
				labRapport.setText("<html>Liste over rapporter:<br>Velg en rapport for tekst</html>");
				labRapport.setBounds(218, 161, 192, 28);
				labRapport.setBackground(this.getBackground());
				start.add(labRapport);
				
				
				labOutput = new JTextPane();
				labOutput.setBackground(this.getBackground());
				labOutput.setBounds(447, 78, 126, 50);
				start.add(labOutput);
				
				JButton btnOppdater = new JButton("Oppdater");
				btnOppdater.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						lstKoier.removeAll();
						lstWood.removeAll();
						updateInfo();
						jspL = new JScrollPane(lists);
					}
				});
				btnOppdater.setBounds(481, 166, 89, 23);
				start.add(btnOppdater);
				
				labEdit = new JLabel();
				labEdit.setBounds(200, 68, 89, 20);
				start.add(labEdit);
				cards.add(start,"Start");
				//------------------- Start end
		
		//------------------- Edit
		
		
		
		newColumns = new String[] {"Koie nummer:","Koienavn:","Sengeplasser:","Bordplasser:","Bygget i:","Terreng:","Sykkel:","Topptur:","Jakt/fiske:","Gitar:","Vaffeljern:","Spesialiteter:","Latitude:","Longtitude:","Sekker ved:","Må fraktes:"};
		formats = new String[] {"","","Heltall","Heltall","Firesifret årstall","[S]kog, [T]regrense: S,S/T,T","'x'-ja, '-'-nei","'x'-ja, '-'-nei","x/x,x/- osv eller '-' for ingen","'x'-ja, '-'-nei","'x'-ja, '-'-nei","Liste skilt med ','","Desimal tall med '.'","Desimal tall med '.'", "Tall mellom 0-10","Ting som må fraktes"};
		lFormats = Arrays.asList(formats);
		String[] editHeaders = {"Felt","Verdi","Format"};
		editModel = new DefaultTableModel(editHeaders,0) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		    	 return (column == 1);
		    }
		};
		JTable tblEdit = new JTable(editModel);
		JScrollPane jspET = new JScrollPane(tblEdit);
		jspET.setBounds(31, 37, 334, 287);
		editKoie.add(jspET);
		tblEdit.getColumnModel().getColumn(0).setMaxWidth(90);
		tblEdit.getColumnModel().getColumn(1).setMaxWidth(90);
		
		
		cards.add(editKoie,"Edit");
		
		JButton btnConfirm = new JButton("Bekreft");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Object> tableItems = new ArrayList<Object>();
				for (int i = 0; i < newColumns.length-2;i++){
					tableItems.add(editModel.getValueAt(i, 1));
				}
				String edit = validateEdit(tableItems);
				if (edit.equals("true")){
					List<String> utstyr = new ArrayList<String>();
					
					if (!tableItems.get(7).toString().equals(koieInfo.get(9))){
						if (tableItems.get(7).toString().equals("x")){
							utstyr.add("gitar");	
						}
					}
					if (!tableItems.get(8).toString().equals(koieInfo.get(10))){
						if (tableItems.get(8).toString().equals("x")){
							utstyr.add("vaffeljern");	
						}
					} 
					if (!tableItems.get(9).toString().equals(koieInfo.get(11))){
						String[] s1 = tableItems.get(9).toString().split(",");
						for (int i = 0; i < s1.length; i++)
						    s1[i] = s1[i].trim();
						
						String[] s2 = koieInfo.get(11).toString().split(",");
						for (int i = 0; i < s2.length; i++)
						    s2[i] = s2[i].trim();
						
						List<String> s1List = new ArrayList(Arrays.asList(s1));
				        for (String s : s2) {
				            if (s1List.contains(s)) {
				                s1List.remove(s);
				            }
				        }
				        utstyr.addAll(s1List);
					}
					System.out.println(utstyr);
					String frakt = tableItems.get(13).toString()+",";
					if (utstyr.size() > 0)
						for (String s: utstyr)
							frakt+=s+",";
					g.CoreClass.editKoie(lstKoier.getSelectedValue().toString(), Integer.parseInt(tableItems.get(0).toString()), Integer.parseInt(tableItems.get(1).toString()), tableItems.get(2).toString(), tableItems.get(3).toString(), tableItems.get(4).toString(),tableItems.get(5).toString(), tableItems.get(6).toString(), tableItems.get(7).toString(), tableItems.get(8).toString(),tableItems.get(9).toString(), Float.parseFloat(tableItems.get(10).toString()), Float.parseFloat(tableItems.get(11).toString()), Integer.parseInt(tableItems.get(12).toString()),frakt);
					labEditFeil.setText("OK");
				}
				else{
					labEditFeil.setText("Feil på formatering på noen felter: "+edit);
				}
			}
		});
		btnConfirm.setBounds(31, 339, 89, 23);
		editKoie.add(btnConfirm);
		
		labEditFeil = new JLabel();
		labEditFeil.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labEditFeil.setBounds(31, 351, 438, 77);
		editKoie.add(labEditFeil);
		
		labName = new JLabel("New label");
		labName.setFont(new Font("Tahoma", Font.BOLD, 15));
		labName.setBounds(370, 37, 201, 47);
		editKoie.add(labName);
		
		btnAdmin = new JButton("Tilbake");
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cards, "Start");
				
			}
		});
		btnAdmin.setBounds(501, 339, 89, 23);
		editKoie.add(btnAdmin);
		
		labIntr = new JLabel("<html>For å endre på en koie forandrer du på verdien i 'Verdi' kolonnen. Husk at verdien må være på riktig format. En 'x' vil si Ja og '-' i de fleste tilfellene der de brukes.<br><br>Når du endrer på spesialiteter, vaffeljern eller gitar vil brukere som reserverer denne koien blir informert at dette utstyret må fraktes til koien.<br><br>Når det er kommet inn rapport at der fraktet fjerner du det fra 'Må fraktes' raden.</html>");
		labIntr.setBounds(375, 65, 215, 247);
		editKoie.add(labIntr);
		//------------------- Edit end
		add(cards);
	}
	
	public void editMode(){
		if (editModel.getRowCount() > 0) {
		    for (int i = editModel.getRowCount() - 1; i > -1; i--) {
		    	editModel.removeRow(i);
		    }
		}
		koieInfo = g.CoreClass.getDataBaseRow("koie", Integer.toString(g.CoreClass.getKoieID(lstKoier.getSelectedValue().toString())));
		for (int i = 0;i < newColumns.length;i++){
			editModel.addRow(new Object[]{newColumns[i],koieInfo.get(i),lFormats.get(i)});
		} 
		editModel.removeRow(0);
		editModel.removeRow(0);
		labName.setText(lstKoier.getSelectedValue().toString());
	}
	
	
		
	public String validateEdit(List<Object> tableItems){
		try {
		    int value1 = Integer.parseInt(tableItems.get(0).toString());
		    int value2 = Integer.parseInt(tableItems.get(1).toString());
		    if (tableItems.get(2).toString().length() == 4){
		    	int value3 = Integer.parseInt(tableItems.get(2).toString());
		    }
		    else return "År";
		    if (!(tableItems.get(3).toString().equals("S") || tableItems.get(3).toString().equals("T") || tableItems.get(3).toString().equals("S/T")))
		    	return "Terreng";
		    if (!(tableItems.get(4).toString().equals("x") ||tableItems.get(4).toString().equals("-")))
		    	return "Sykkel";
		    if (!(tableItems.get(5).toString().equals("x") ||tableItems.get(5).toString().equals("-")))
		    	return "Topptup";
		    if (!(tableItems.get(6).toString().equals("x/-") ||tableItems.get(6).toString().equals("-") ||tableItems.get(6).toString().equals("-/x")||tableItems.get(6).toString().equals("x/x")))
		    	return "Jakt/Fiske";
		    if (!(tableItems.get(7).toString().equals("x") ||tableItems.get(7).toString().equals( "-")))
		    	return "Gitar";
		    if (!(tableItems.get(8).toString().equals("x") ||tableItems.get(8).toString().equals("-")))
		    	return "Vaffeljern";
		    float value4 = Float.parseFloat(tableItems.get(10).toString());
		    float value5 = Float.parseFloat(tableItems.get(11).toString());
		    int value6 = Integer.parseInt(tableItems.get(12).toString());
		    if (value6 > 10)
		    	return "Ved";
		    
		} catch (NumberFormatException nfe) {
		    return "Tall feil";
		}
		return "true";
	}
	public void updateInfo(){
		names.clear();
		woods.clear();
		
		for (List<Object> o:g.CoreClass.getDataBaseColumns("koie", "koienavn")){
			names.add(o.get(0).toString());
		}
		
		woods = new ArrayList<String>();
		for (Object o: names){
			woods.add(Integer.toString(g.CoreClass.getWoodStatus(o.toString())));
		}
		System.out.println("");
		labWarning.setText("");
		for (int i = 0;i < woods.size();i++){
			if (Integer.parseInt(woods.get(i)) < 3){
				warningWood(names.get(i));
				warnings.put(names.get(i), Integer.parseInt(woods.get(i)));
			}
		}
		g.displayWarnings(warnings);
		lstKoier = new JList(names.toArray());
		lstWood = new JList(woods.toArray());
	}
	
	
	public void warningWood(String name){
		jspW.setVisible(true);
		labWarning.setText(labWarning.getText()+ name + " har manglende ved! - ");
		labImgWarning.setVisible(true);
		//g.CoreClass.warningMail(name); Sender mail om manglende ved
	}
	
	
	
	public class IconListRenderer extends DefaultListCellRenderer {
	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        Icon icon = this.getIcon(list, value, index, isSelected, cellHasFocus, label);
	        label.setIcon(icon);
	        return label;
	    }
	    protected Icon getIcon(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus, JLabel label) {
	    	try {
	    		if (value.toString().length() < 3){
	    			if (Integer.parseInt(value.toString()) < 3){
	    				label.setForeground(Color.RED);
	    				label.setBorder(new LineBorder(Color.RED));
	    				return new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("warning.png")));}
	    			else return null;
	    		}
	    		else return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	    }
	}
	class DisabledItemSelectionModel extends DefaultListSelectionModel {

	    @Override
	    public void setSelectionInterval(int index0, int index1) {
	        super.setSelectionInterval(-1, -1);
	    }
	}
}
