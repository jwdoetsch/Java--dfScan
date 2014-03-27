package com.doetsch.dfscan.window;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import com.doetsch.oxide.OxideFrame;
import com.doetsch.oxide.OxideComponentFactory;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.UIManager;

public class ProgressIconSandBox extends OxideFrame {
	private final OxideComponentFactory oxideComponentFactory = new OxideComponentFactory();
	private JLabel label;
	private JButton button;
	
	private int iconNum = 0;

	/**
	 * Launch the application.
	 */
	public static void main (String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				try {
					ProgressIconSandBox frame = new ProgressIconSandBox();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProgressIconSandBox () {
		super(false);

		initComponents();
	}
	private void initComponents() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		label = oxideComponentFactory.createLabel("");
		label.setBounds(126, 48, 216, 126);
		getContentPane().add(label);
		
		button = oxideComponentFactory.createButton();
		button.setBounds(8, 73, 76, 53);
		getContentPane().add(button);
		
		button.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {
				setIcon();
			}
			
		});
		
		setIcon ();
	}
	
	private void setIcon() {
		
		switch (iconNum) {
			case 0:
				iconNum++;
				label.setIcon(
						new ImageIcon("src/main/resources/icons/status_icon_1.gif"));
				break;
				
			case 1:
				iconNum++;
				label.setIcon(
						new ImageIcon("src/main/resources/icons/status_icon_2.gif"));
				break;
				
			case 2:
				iconNum++;
				label.setIcon(
						new ImageIcon("src/main/resources/icons/status_icon_3.gif"));
				break;
				
			case 3:
				iconNum++;
				label.setIcon(
						new ImageIcon("src/main/resources/icons/status_icon_4.gif"));
				break;
				
			case 4:
				iconNum++;
				label.setIcon(
						new ImageIcon("src/main/resources/icons/status_icon_5.gif"));
				break;
				
			case 5:
				iconNum = 0;
				break;
		}
		
	}
}
