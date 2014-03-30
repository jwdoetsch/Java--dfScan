package com.doetsch.dfscan;

import java.awt.EventQueue;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import com.doetsch.dfscan.window.MainWindow;

public class DFScan {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
