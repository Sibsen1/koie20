package gui;
import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.*;

import com.alee.laf.WebLookAndFeel;

import core.Core;

import java.awt.*;
import java.awt.event.*;

public class PageHub extends JFrame{
	
		JTabbedPane contentPanel = new JTabbedPane();
		ReservePane reservePane = new ReservePane();
		MapPane mapPane = new MapPane();
		
						
		public PageHub() {
			getContentPane().add(contentPanel);
			
			contentPanel.add("Reserver",reservePane);
			contentPanel.add("Kart",mapPane);
			
			this.setSize(600, 500);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
}
