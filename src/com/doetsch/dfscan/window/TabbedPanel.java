package com.doetsch.dfscan.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
				this.setPreferredSize(new Dimension(18, 18));
				this.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/close_icon.png")));
				this.setHorizontalAlignment(SwingConstants.CENTER);
				
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
	public ImageIcon tabIcon;
	
	public TabbedPanel (String title, ImageIcon tabIcon) {
		this.tab = new TabLabel(title, tabIcon);
		System.out.println(tabIcon);

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

}
