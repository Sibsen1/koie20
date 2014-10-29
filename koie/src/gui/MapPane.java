package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import core.Core;

import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.*;

public class MapPane extends JPanel {
	public MapPane(GUI g) {
		this.setSize(600, 500);
		JMapViewer mapPanel = new JMapViewer(); 
		mapPanel.setBounds(10, 11, 400, 359);
		double lat = 63.13;
		double lon = 10.43;
		mapPanel.setDisplayPositionByLatLon(lat, lon, 8);

		DefaultMapController mapController = new DefaultMapController(mapPanel);
		mapController.setMovementMouseButton(MouseEvent.BUTTON1);
	
		this.setLayout(null);
		this.add(mapPanel);
		
		JTextPane labKoier = new JTextPane();
		labKoier.setText("Koier:");
		labKoier.setBounds(443, 38, 113, 20);
		labKoier.setBackground(this.getBackground());
		add(labKoier);
		
		JPanel pnlKoier = new JPanel(); 
		pnlKoier.setBounds(420, 59, 121, 352);
		add(pnlKoier);
		
		JTextPane labKoieInfo = new JTextPane();
		labKoieInfo.setText("Velg en koie for informasjon:");
		labKoieInfo.setBounds(60, 381, 281, 49);
		labKoieInfo.setBackground(this.getBackground());
		add(labKoieInfo);
		

		List<String>names = new ArrayList<String>();
		for (List<Object> o:g.CoreClass.getDataBaseColumns("koie", "koienavn")){
			names.add(o.get(0).toString());
		}
		//names.add("fdhghf");
		//names.add("fdfhgh");
		//names.add("fdsdadf");
		ButtonGroup group = new ButtonGroup();
		ArrayList<JRadioButton> buttons = new ArrayList();
		for (Object o:names){
			buttons.add(new JRadioButton(o.toString()));
			group.add(buttons.get(buttons.size()-1));
			pnlKoier.add(buttons.get(buttons.size()-1));
			buttons.get(buttons.size()-1).addActionListener(new ActionListener(){ //Skal vise kort info om valgt koie
				@Override
				public void actionPerformed(ActionEvent arg0) {
					labKoieInfo.setText(o.toString()); 
				}
			});
		}
		
	
		ArrayList<List<Object>> koie_cords = g.CoreClass.getDataBaseColumns("koie","latitude","longitude");
		//double[][] koie_cords = {{lat,lon}};
		int d = 0;
		for (List<Object> k: koie_cords){
			mapPanel.addMapMarker(new MapMarkerDot(null, names.get(d), Float.valueOf(k.get(0).toString()), Float.valueOf(k.get(1).toString())));
			d++;
		}
		mapPanel.repaint();
	}
}
