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

import java.awt.BorderLayout;
import java.awt.TrayIcon;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JLabel;

import java.awt.Component;

import javax.swing.JButton;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.DetectionTask;
import com.doetsch.dfscan.core.Profile;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;

/**
 * 
 * 
 * @author Jacob Wesley Doetsch
 */
public class ProgressPanel extends TabbedPanel {
	
	private Box statusLabelBox;
	private JScrollPane logScrollPane;
	private JTextArea logTextPane;
	private JLabel indexingLabel;
	private JLabel filteringLabel;
	private JLabel hashingLabel;
	private JLabel groupingLabel;
	private Component indexingStrut;
	private Component filteringStrut;
	private Component hashingStrut;
	private Component rightGlue;
	private Component groupingStrut;
	private JButton abortButton;
	
	private Profile detectionProfile;
	private DetectionTask detectionTask;
	private JTabbedPane parentPane;
	private Component leftGlue;
	private JButton logButton;
	private TrayIcon trayIcon;

	/**
	 * Create the panel.
	 */
	public ProgressPanel (Profile detectionProfile, JTabbedPane parentPane, TrayIcon trayIcon) {
		super("" + detectionProfile.getName(),
				new ImageIcon(DFScan.class.getResource("resources/icons/scan_icon.gif")), trayIcon);
		setBorder(new EmptyBorder(6, 6, 6, 6));
		
		this.trayIcon = trayIcon;
		this.detectionProfile = detectionProfile;
		this.parentPane = parentPane;

		initComponents();
		setBehavior();		
		setDefaultValues();
		startDetectionTask();
	}
	
	private void initComponents () {
		setLayout(new BorderLayout(0, 0));
		
		parentPane.addTab("", null, this, null);
		parentPane.setTabComponentAt(
				parentPane.indexOfComponent(this), this.getTabAsComponent());
		parentPane.setSelectedComponent(this);

		
		statusLabelBox = Box.createHorizontalBox();
		statusLabelBox.setBorder(new EmptyBorder(0, 0, 6, 0));
		add(statusLabelBox, BorderLayout.NORTH);
		
		leftGlue = Box.createHorizontalGlue();
		statusLabelBox.add(leftGlue);
		
		indexingLabel = new JLabel("Indexing");
		statusLabelBox.add(indexingLabel);
		
		indexingStrut = Box.createHorizontalStrut(20);
		indexingStrut.setPreferredSize(new Dimension(36, 0));
		statusLabelBox.add(indexingStrut);
		
		filteringLabel = new JLabel("Filtering");
		statusLabelBox.add(filteringLabel);
		
		filteringStrut = Box.createHorizontalStrut(20);
		filteringStrut.setPreferredSize(new Dimension(36, 0));
		statusLabelBox.add(filteringStrut);
		
		hashingLabel = new JLabel("Hashing");
		statusLabelBox.add(hashingLabel);
		
		hashingStrut = Box.createHorizontalStrut(20);
		hashingStrut.setPreferredSize(new Dimension(36, 0));
		statusLabelBox.add(hashingStrut);
		
		groupingLabel = new JLabel("Grouping");
		statusLabelBox.add(groupingLabel);
		
		groupingStrut = Box.createHorizontalStrut(20);
		groupingStrut.setPreferredSize(new Dimension(180, 0));
		groupingStrut.setMaximumSize(new Dimension(48, 32767));
		groupingStrut.setMinimumSize(new Dimension(48, 0));
		statusLabelBox.add(groupingStrut);
		
		abortButton = new JButton("Abort");
		statusLabelBox.add(abortButton);
		
		logButton = new JButton("Log");
		logButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		statusLabelBox.add(logButton);
		
		rightGlue = Box.createHorizontalGlue();
		statusLabelBox.add(rightGlue);
		
		logScrollPane = new JScrollPane();
		logScrollPane.setVisible(false);
		add(logScrollPane, BorderLayout.CENTER);
		
		logTextPane = new JTextArea();
		logScrollPane.setViewportView(logTextPane);
		
	}
	
	private void setBehavior () {
		
		abortButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {

				tabCloseButtonAction();
			}
			
		});
		
		logButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {
		
				logScrollPane.setVisible(!logScrollPane.isVisible());
			}

		});
		
	}

	private void setDefaultValues () {
		indexingLabel.setEnabled(false);
		filteringLabel.setEnabled(false);
		hashingLabel.setEnabled(false);
		groupingLabel.setEnabled(false);
		indexingLabel.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
		filteringLabel.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
		hashingLabel.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
		groupingLabel.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
	}
	
	private void startDetectionTask () {
		detectionTask = new DetectionTask(detectionProfile, this);
		
		detectionTask.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange (PropertyChangeEvent e) {
				
				//System.out.println(e.getPropertyName() + ":" + e.getNewValue());
				
				/*
				 * If the thread state of the detection task changes...
				 */
				if (e.getPropertyName().toString().equals("state")) {
					
					/*
					 * If the detection task thread is finished...
					 */
					if (e.getNewValue().toString().equals("DONE")) {
						
						/*
						 * If the task is finished successfully then instantiate a ResultsWindow
						 * with the generated Report
						 */
						if (!detectionTask.isCancelled()) {
							try {
								ResultsPanel resultsPanel = new ResultsPanel(detectionTask.get(), parentPane, trayIcon);
								if (trayIcon != null) {
									trayIcon.displayMessage("Scan Finished",
											"A scan using the profile " + detectionProfile.getName() +
											" has finished. The results of the scan are ready.",
											TrayIcon.MessageType.INFO);
								}
								
							} catch (InterruptedException | ExecutionException e1) {
								//System.out.println("Report not retrievable from the detection task!");
								e1.printStackTrace();
							}
							
							
						/*
						 * If the task reports finished because it has been interrupted...
						 */	
						} else {
						
						}
						
//						/*
//						 * Dispose the ProgressWindow since the detection task is finished
//						 */
//						ProgressPanel.this.dispose();
						
						//disposePanel();
						//System.out.println("Task aborted/finished, removing panel...");
						tabCloseButtonAction();
						
					}
					
				}
			}
			
		});
		
		detectionTask.execute();
		
	}
	
	/**
	 * @return the indexingLabel
	 */
	public JLabel getIndexingLabel () {
		return indexingLabel;
	}

	/**
	 * @param indexingLabel the indexingLabel to set
	 */
	public void setIndexingLabel (JLabel indexingLabel) {
		this.indexingLabel = indexingLabel;
	}

	/**
	 * @return the filteringLabel
	 */
	public JLabel getFilteringLabel () {
		return filteringLabel;
	}

	/**
	 * @param filteringLabel the filteringLabel to set
	 */
	public void setFilteringLabel (JLabel filteringLabel) {
		this.filteringLabel = filteringLabel;
	}

	/**
	 * @return the hashingLabel
	 */
	public JLabel getHashingLabel () {
		return hashingLabel;
	}

	/**
	 * @param hashingLabel the hashingLabel to set
	 */
	public void setHashingLabel (JLabel hashingLabel) {
		this.hashingLabel = hashingLabel;
	}

	/**
	 * @return the groupingLabel
	 */
	public JLabel getGroupingLabel () {
		return groupingLabel;
	}

	/**
	 * @param groupingLabel the groupingLabel to set
	 */
	public void setGroupingLabel (JLabel groupingLabel) {
		this.groupingLabel = groupingLabel;
	}

//	/**
//	 * @return the timeElapsedLabel
//	 */
//	public JLabel getTimeElapsedLabel () {
//		return timeElapsedLabel;
//	}
//
//	/**
//	 * @param timeElapsedLabel the timeElapsedLabel to set
//	 */
//	public void setTimeElapsedLabel (JLabel timeElapsedLabel) {
//		this.timeElapsedLabel = timeElapsedLabel;
//	}

	/**
	 * @return the logTextPane
	 */
	public JTextArea getLogTextPane () {
		return logTextPane;
	}

	/**
	 * @param logTextPane the logTextPane to set
	 */
	public void setLogTextPane (JTextArea logTextPane) {
		this.logTextPane = logTextPane;
	}

	/**
	 * @return the parentPane
	 */
	public JTabbedPane getParentPane () {
		return parentPane;
	}

	/**
	 * @param parentPane the parentPane to set
	 */
	public void setParentPane (JTabbedPane parentPane) {
		this.parentPane = parentPane;
	}

	@Override
	public void tabCloseButtonAction () {
		detectionTask.cancel(false);
		closePanel();
	}
	
	public void setTabTitle (String title) {
		super.setTabTitle(title);
	}

	@Override
	public void closePanel () {
		int tabIndex = parentPane.indexOfComponent(this);
		
		if (tabIndex > -1) {
			parentPane.remove(tabIndex);
		}
	}
	
}
