/*
 * Copyright (C) 2014 Jacob Wesley Doetsch
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

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
