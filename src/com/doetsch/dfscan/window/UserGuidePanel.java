package com.doetsch.dfscan.window;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.awt.Component;
import java.io.IOException;

import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

import com.doetsch.dfscan.DFScan;

public class UserGuidePanel extends TabbedPanel {

	
	private JTabbedPane parentPane;
	private Box horizontalBox;
	private JScrollPane scrollPane;
	private JTextPane textPane;
	
	/**
	 * Create the panel.
	 */
	public UserGuidePanel (JTabbedPane parentPane) {
		super("Welcome to dfScan!",
				new ImageIcon(DFScan.class.getResource("resources/icons/userguide_icon.png")));
		this.parentPane = parentPane;

		initComponents();
	}
	private void initComponents() {
		setBorder(new EmptyBorder(6, 6, 6, 6));
		setLayout(new BorderLayout(0, 0));
		
		horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBorder(new EmptyBorder(0, 0, 6, 0));
		add(horizontalBox, BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		textPane = new JTextPane();
		try {
			textPane.setPage(DFScan.class.getResource("resources/docs/help.html"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
	}

	@Override
	public void tabCloseButtonAction () {
		closePanel();
	}

	@Override
	public void closePanel () {
		int tabIndex = parentPane.indexOfComponent(this);
		
		if (tabIndex > -1) {
			parentPane.remove(tabIndex);
		}
	}

}
