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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.doetsch.dfscan.DFScan;

public abstract class TabbedPanel extends JPanel {
	
	private class TabLabel extends JPanel {
		
		private class TabLabelCloseButton extends JLabel {
			
			private TabLabelCloseButton () {
				super();
				init();
			}
			
			private void init () {
				this.setPreferredSize(new Dimension(20, 20));
				this.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/close_icon_18x18.png")));
				this.setHorizontalAlignment(SwingConstants.CENTER);
				this.setVerticalAlignment(SwingConstants.CENTER);
				
				this.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked (MouseEvent e) {
						buttonAction.actionPerformed(null);
					}
					
					@Override
					public void mouseEntered (MouseEvent e) {
						TabLabelCloseButton.this.setBorder(new LineBorder(new Color(155, 155, 155)));					
					}

					@Override
					public void mouseExited (MouseEvent e) {
						TabLabelCloseButton.this.setBorder(null);
					}

					@Override
					public void mousePressed (MouseEvent e) {
						TabLabelCloseButton.this.setBorder(new LineBorder(new Color(75, 75, 75), 2));					
					}

					@Override
					public void mouseReleased (MouseEvent e) {
						mouseEntered(e);
					}
					
				});
				
			}
			
		}
		
		String title;
		JLabel tabTitleLabel;
		ImageIcon tabIcon;
		
		public TabLabel (String title, ImageIcon tabIcon) {
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));
			this.title = title;
			this.tabIcon = tabIcon;
			
			init();
		}
		
		private void init() {
			this.setOpaque(false);
			this.setBorder(new EmptyBorder(6, 0, 0, 0));
			
			tabTitleLabel = new JLabel();
			tabTitleLabel.setOpaque(false);
			tabTitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
			setTabTitle(title);
						
			if (tabIcon != null) {
				JLabel iconLabel = new JLabel(tabIcon);
				//Position the extra space to the right of the icon
				iconLabel.setHorizontalAlignment(JLabel.LEADING);
				iconLabel.setPreferredSize(new Dimension(24, 18));
				this.add(iconLabel);
				
			}
			
			this.add(tabTitleLabel);
			
			TabLabelCloseButton closeLabel = new TabLabelCloseButton();
			this.add(closeLabel);
		}
		
		private String getTabTitle () {
			return title;
		}
		
		private void setTabTitle (String title) {
			this.title = title;
			tabTitleLabel.setText(title + " ");
			tabTitleLabel.repaint();
		}
	}

	
	private TabLabel tab;
	private AbstractAction buttonAction;
	private ImageIcon tabIcon;
	private TrayIcon trayIcon;
	
	public TabbedPanel (String title, ImageIcon tabIcon, TrayIcon trayIcon) {
		this.tab = new TabLabel(title, tabIcon);
		this.trayIcon = trayIcon;

		init();
	}
	
	private void init () {
		
		this.buttonAction = new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent e) {
				tabCloseButtonAction();		
			}
			
		};
	}
	
//	public abstract void tabButtonAction ();
	
	/**
	 * Defines the default behavior of the tab's close button as
	 * a call to closePanel(); 
	 */
	public void tabCloseButtonAction () {
		closePanel();
	}
	
	public void setTabTitle (String title) {
		this.tab.setTabTitle(title);
	}
	
	public String getTabTitle () {
		return this.tab.getTabTitle();
	}
	
	public Component getTabAsComponent () {
		return tab;
	}
	
	public abstract void closePanel ();
	
	public TrayIcon getTrayIcon () {
		return trayIcon;
	}

}
