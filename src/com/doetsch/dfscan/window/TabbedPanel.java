package com.doetsch.dfscan.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
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
		
		public TabLabel () {
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));

			init();
		}
		
		private void init() {

			
			JLabel titleLabel = new JLabel() {
				@Override
				public String getText () {
					int i = parentPane.indexOfComponent(TabbedPanel.this);
					if (i > -1) {
						return parentPane.getTitleAt(i);
					} else {
						return "";
					}					
				}
			};

			titleLabel.setOpaque(true);
			this.add(titleLabel);
			this.add(new TabLabelCloseButton());
		}
		
		public void setTitle (String title) {
			int i = parentPane.indexOfTabComponent(this);
			
			if (i > -1) {
				parentPane.setTitleAt(i, title);
			}
		}

	}

	
	private TabLabel tab;
	private JTabbedPane parentPane;
	private AbstractAction buttonAction;
	
	public TabbedPanel (String title, JTabbedPane parentPane) {
		this.parentPane = parentPane;
		this.tab = new TabLabel();
		this.buttonAction = new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent e) {
				tabButtonAction();		
			}
			
		};
	}
	
	public abstract void tabButtonAction ();
	
	public void setTabTitle (String title) {
		int tabIndex = parentPane.indexOfComponent(this);
		
		if (tabIndex > -1) {
			parentPane.setTitleAt(tabIndex, title);
		}
	}
	
	public String getTabTitle () {
		int tabIndex = parentPane.indexOfComponent(this);
		
		if (tabIndex > -1) {
			return parentPane.getTitleAt(tabIndex);
		}
		
		return "";
	}

	
	public Component getTab () {
		return tab;
	}
	
	public void closePanel () {
		int tabIndex = parentPane.indexOfComponent(this);
		
		if (tabIndex > -1) {
			//return parentPane.getTitleAt(tabIndex);
			parentPane.remove(tabIndex);
		}
	}
}
