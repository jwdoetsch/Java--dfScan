package com.doetsch.dfscan.window;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
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
	private JLabel timeElapsedLabel;
	
	private Profile detectionProfile;
	private DetectionTask detectionTask;
	private Component timeElapsedStrut;
	private JTabbedPane parentPane;
	private Component leftGlue;

	/**
	 * Create the panel.
	 */
	public ProgressPanel (Profile detectionProfile, JTabbedPane parentPane) {
		super(detectionProfile.getName());
		
		this.detectionProfile = detectionProfile;
		this.parentPane = parentPane;

		initComponents();
		setBehavior();		
		setDefaultValues();
		startDetectionTask();
	}
	
	private void initComponents () {
		setLayout(new BorderLayout(0, 0));
		
		statusLabelBox = Box.createHorizontalBox();
		add(statusLabelBox, BorderLayout.NORTH);
		
		leftGlue = Box.createHorizontalGlue();
		statusLabelBox.add(leftGlue);
		
		timeElapsedLabel = new JLabel("Time Elapsed:");
		statusLabelBox.add(timeElapsedLabel);
		
		timeElapsedStrut = Box.createHorizontalStrut(20);
		timeElapsedStrut.setPreferredSize(new Dimension(36, 0));
		statusLabelBox.add(timeElapsedStrut);
		
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
		
		rightGlue = Box.createHorizontalGlue();
		statusLabelBox.add(rightGlue);
		
		logScrollPane = new JScrollPane();
		add(logScrollPane, BorderLayout.CENTER);
		
		logTextPane = new JTextArea();
		logScrollPane.setViewportView(logTextPane);
		
	}
	
	private void setBehavior () {
		
		abortButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {

				tabButtonAction();
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
								new ResultsWindow(detectionTask.get());
								
							} catch (InterruptedException | ExecutionException e1) {
								System.out.println("Report not retrievable from the detection task!");
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
						System.out.println("Task aborted/finished, removing panel...");
						tabButtonAction();
						
					}
					
				}
			}
			
		});
		
		detectionTask.execute();
		
	}
	
	@Override
	public void tabButtonAction () {
		detectionTask.cancel(false);
		closePanel();
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

	/**
	 * @return the timeElapsedLabel
	 */
	public JLabel getTimeElapsedLabel () {
		return timeElapsedLabel;
	}

	/**
	 * @param timeElapsedLabel the timeElapsedLabel to set
	 */
	public void setTimeElapsedLabel (JLabel timeElapsedLabel) {
		this.timeElapsedLabel = timeElapsedLabel;
	}

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
