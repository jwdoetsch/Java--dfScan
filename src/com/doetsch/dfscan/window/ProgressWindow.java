package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextPane;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.DetectionTask;
import com.doetsch.dfscan.core.Profile;
import com.doetsch.dfscan.core.Report;
import com.doetsch.oxide.OxideComponentFactory;
import com.doetsch.oxide.OxideDefaultSkin;
import com.doetsch.oxide.OxideFrame;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;

public class ProgressWindow extends OxideFrame {

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
			public void run() {
				try {
					ProgressWindow frame = new ProgressWindow(Profile.load("profiles/eBooks.dfscan.profile.xml"));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Profile detectionProfile;
	
	private JScrollPane scrollPane;
	private JButton buttonAbort;
	private JTextArea textAreaLog;
	private JLabel labelIndexingStatus;
	private JLabel labelFilteringStatus;
	private JLabel labelHashingStatus;
	private JLabel labelGroupingStatus;
	private JLabel labelElapsedTime;
	private DetectionTask detectionTask;

	/**
	 * Create the frame.
	 */
	public ProgressWindow (Profile detectionProfile) {
		super(false, new OxideDefaultSkin());
		
		this.detectionProfile = detectionProfile;
		
		initComponents();
		setBehavior();
		setDefaultValues();
		startDetectionTask();

		
	}

	private void initComponents () {
		setTitle("Progress");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 528, 354);
		centerInViewport();
		
		OxideComponentFactory oxideComponentFactory = new OxideComponentFactory(getOxideSkin());
		
		labelIndexingStatus = oxideComponentFactory.createLabel("Indexing");
		labelIndexingStatus.setBounds(12, 42, 114, 24);
		getContentPane().add(labelIndexingStatus);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 78, 504, 264);
		getContentPane().add(scrollPane);
		
		textAreaLog = new JTextArea();
		scrollPane.setViewportView(textAreaLog);
		
		//buttonAbort = new OxideButton("Abort");
		buttonAbort = oxideComponentFactory.createButton();
		buttonAbort.setText("Abort");
		buttonAbort.setBounds(432, 12, 84, 24);
		getContentPane().add(buttonAbort);
		
		//labelFilteringStatus = new OxideLabel((String) null);
		labelFilteringStatus = oxideComponentFactory.createLabel("Filtering");
		//labelFilteringStatus.setText("Filtering");
		labelFilteringStatus.setBounds(138, 42, 114, 24);
		getContentPane().add(labelFilteringStatus);
		
		//labelHashingStatus = new OxideLabel((String) null);
		labelHashingStatus = oxideComponentFactory.createLabel("Hashing");
		//labelHashingStatus.setText("Hashing");
		labelHashingStatus.setBounds(264, 42, 114, 24);
		getContentPane().add(labelHashingStatus);
		
		//labelGroupingStatus = new OxideLabel((String) null);
		labelGroupingStatus = oxideComponentFactory.createLabel("Grouping");
		//labelGroupingStatus.setText("Grouping");
		labelGroupingStatus.setBounds(390, 42, 114, 24);
		getContentPane().add(labelGroupingStatus);
		
		//labelElapsedTime = new OxideLabel((String) null);
		labelElapsedTime = oxideComponentFactory.createLabel("Elapsed: ");
		//labelElapsedTime.setText("Elapsed: ");
		labelElapsedTime.setBounds(12, 12, 408, 24);
		getContentPane().add(labelElapsedTime);
		
	}
	
	private void setBehavior () {
		
		setCloseButtonBehavior(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				cancelDetectionTask();
			}
			
		});
		
		buttonAbort.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				cancelDetectionTask();
			}
			
		});
		
//		setMinimizeButtonBehavior(new AbstractAction() {
//			
//			@Override
//			public void actionPerformed (ActionEvent arg0) {
//				setExtendedState(ICONIFIED);
//			}
//			
//		});
	}
	
	private void setDefaultValues () {
		labelIndexingStatus.setEnabled(false);
		labelFilteringStatus.setEnabled(false);
		labelHashingStatus.setEnabled(false);
		labelGroupingStatus.setEnabled(false);
		labelIndexingStatus.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
		labelFilteringStatus.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
		labelHashingStatus.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
		labelGroupingStatus.setIcon(new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png")));
		
	}

	private void cancelDetectionTask () {
		detectionTask.cancel(false);
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
						
						/*
						 * Dispose the ProgressWindow since the detection task is finished
						 */
						ProgressWindow.this.dispose();
						
					}
					
				}
			}
			
		});
		
		detectionTask.execute();
		
	}
	

	/**
	 * @return the scrollPane
	 */
	public JScrollPane getScrollPane () {
		return scrollPane;
	}


	/**
	 * @return the labelIndexingStatus
	 */
	public JLabel getLabelIndexingStatus () {
		return labelIndexingStatus;
	}





	/**
	 * @param labelIndexingStatus the labelIndexingStatus to set
	 */
	public void setLabelIndexingStatus (JLabel labelIndexingStatus) {
		this.labelIndexingStatus = labelIndexingStatus;
	}





	/**
	 * @return the labelFilteringStatus
	 */
	public JLabel getLabelFilteringStatus () {
		return labelFilteringStatus;
	}





	/**
	 * @param labelFilteringStatus the labelFilteringStatus to set
	 */
	public void setLabelFilteringStatus (JLabel labelFilteringStatus) {
		this.labelFilteringStatus = labelFilteringStatus;
	}





	/**
	 * @return the labelHashingStatus
	 */
	public JLabel getLabelHashingStatus () {
		return labelHashingStatus;
	}





	/**
	 * @param labelHashingStatus the labelHashingStatus to set
	 */
	public void setLabelHashingStatus (JLabel labelHashingStatus) {
		this.labelHashingStatus = labelHashingStatus;
	}





	/**
	 * @return the labelGroupingStatus
	 */
	public JLabel getLabelGroupingStatus () {
		return labelGroupingStatus;
	}





	/**
	 * @param labelGroupingStatus the labelGroupingStatus to set
	 */
	public void setLabelGroupingStatus (JLabel labelGroupingStatus) {
		this.labelGroupingStatus = labelGroupingStatus;
	}





	/**
	 * @return the labelElapsedTime
	 */
	public JLabel getLabelTimeElapsed () {
		return labelElapsedTime;
	}





	/**
	 * @param labelElapsedTime the labelElapsedTime to set
	 */
	public void setLabelTimeElapsed (JLabel labelElapsedTime) {
		this.labelElapsedTime = labelElapsedTime;
	}





	/**
	 * @return the textAreaLog
	 */
	public JTextArea getTextAreaLog () {
		return textAreaLog;
	}





	/**
	 * @param textAreaLog the textAreaLog to set
	 */
	public void setTextAreaLog (JTextArea textAreaLog) {
		this.textAreaLog = textAreaLog;
	}
}
