package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JComboBox;
import javax.swing.Box;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Component;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.doetsch.dfscan.DFScan;

public class MainAppWindow2 extends JFrame {

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
						int i = parentPane.indexOfTabComponent(TabLabel.this);
						System.out.println(i);
						if (i > -1) {
							parentPane.remove(i);
						}
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

		private final JTabbedPane parentPane;
		
		private TabLabel (JTabbedPane parentPane) {
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));
			this.parentPane = parentPane;
			init();
		}
		
		private void init() {

			
			JLabel titleLabel = new JLabel() {
				@Override
				public String getText () {
					int i = parentPane.indexOfTabComponent(TabLabel.this);
					if (i > -1) {
						return parentPane.getTitleAt(i);
					} else {
						return "";
					}					
				}
			};
			//titleLabel.setSize(64, 12);
			titleLabel.setOpaque(true);
			
			this.add(titleLabel);
			this.add(new TabLabelCloseButton());
			
		}

	}
	
	
	private JPanel contentPane;
	private JSplitPane splitPane; 
	private JPanel resultsPanel;
	private JPanel profilePanel;
	private JMenuBar menuBar;
	private Box profileSelectionBox;
	private JComboBox profileComboBox;
	private JButton updateButton;
	private JButton saveAsButton;
	private JButton importButton;
	private Box startScanControlBox;
	private JButton startScanButton;
	private Component startScanRightGlue;
	private Component startScanLeftGlue;
	private JScrollPane summaryScrollPane;
	private JTextPane summaryTextPane;
	private JTabbedPane profileTabbedPane;
	private JPanel summaryPanel;
	private JTabbedPane resultsTabbedPane;
	private JPanel results1;
	private JMenu fileMenu;
	private JPanel foldersPanel;
	private Box foldersBox;
	private Box indexingOptionsBox;
	private JCheckBox scanSubFoldersCheckBox;
	private JCheckBox scanHiddenFoldersCheckBox;
	private JCheckBox scanReadOnlyCheckBox;
	private Component indexingOptionsTopGlue;
	private Component indexingOptionsBottonGlue;
	private Box foldersControlBox;
	private JButton addFolderButton;
	private JButton removeFolderButton;
	private JScrollPane foldersScrollPane;
	private JList foldersList;
	private JPanel panel;
	private Box verticalBox;
	private Box horizontalBox;
	private Component horizontalGlue;
	private JButton button;
	private JButton button_1;
	private Component horizontalGlue_1;
	private JScrollPane scrollPane;
	private JPanel filtersPanel;
	private Box filtersBox;
	private Box filteringOptionsBox;
	private Box filtersControlBox;
	private JScrollPane filtersScrollPane;
	private JList filtersList;
	private JButton addFilterButton;
	private JButton removeFilterButton;
	private JCheckBox chckbxNewCheckBox;
	private Component filteringOptionsTopGlue;
	private Component filteringOptionsBottomGlue;
	private JMenuItem openResultsMenuItem;
	private JMenuItem mntmNewMenuItem;
	private JSeparator separator;
	private JMenu helpMenu;
	private JMenuItem tutorialMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem updateMenuItem;
	private JSeparator helpMenuSeparator;
	private JMenu viewMenu;
	private JMenuItem logMenuItem;
	private JMenuItem guideMenuItem;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private Component verticalStrut;

	/**
	 * Launch the application.
	 */
	public static void main (String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				try {
					MainAppWindow2 frame = new MainAppWindow2();
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
	public MainAppWindow2 () {
		initComponents();
	}
	private void initComponents() {
		setTitle("dfScan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1026, 768);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		openResultsMenuItem = new JMenuItem("Open Results...");
		fileMenu.add(openResultsMenuItem);
		
		separator = new JSeparator();
		fileMenu.add(separator);
		
		mntmNewMenuItem = new JMenuItem("Exit");
		fileMenu.add(mntmNewMenuItem);
		
		viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		
		logMenuItem = new JMenuItem("Log");
		viewMenu.add(logMenuItem);
		
		guideMenuItem = new JMenuItem("Usage Guide");
		viewMenu.add(guideMenuItem);
		
		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		tutorialMenuItem = new JMenuItem("Usage Guide");
		helpMenu.add(tutorialMenuItem);
		
		updateMenuItem = new JMenuItem("Check for Updates...");
		helpMenu.add(updateMenuItem);
		
		helpMenuSeparator = new JSeparator();
		helpMenu.add(helpMenuSeparator);
		
		aboutMenuItem = new JMenuItem("About dfScan");
		helpMenu.add(aboutMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(12, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		resultsPanel = new JPanel();
		resultsPanel.setPreferredSize(new Dimension(10, 400));
		splitPane.setLeftComponent(resultsPanel);
		resultsPanel.setLayout(new BorderLayout(0, 0));
		
		resultsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		resultsPanel.add(resultsTabbedPane, BorderLayout.CENTER);
		
		//results1 = new JPanel();
		results1 = new TabPanel();
		results1.setVisible(true);
		resultsTabbedPane.addTab("Scan in progress...", null, results1, null);
		
		
		resultsTabbedPane.setTabComponentAt(0, new TabLabel(resultsTabbedPane));
		
		profilePanel = new JPanel();
		splitPane.setRightComponent(profilePanel);
		profilePanel.setLayout(new BorderLayout(0, 0));
		
		profileSelectionBox = Box.createHorizontalBox();
		profileSelectionBox.setBorder(new EmptyBorder(6, 6, 6, 6));
		profilePanel.add(profileSelectionBox, BorderLayout.NORTH);
		
		profileComboBox = new JComboBox();
		profileSelectionBox.add(profileComboBox);
		
		updateButton = new JButton("Update");
		profileSelectionBox.add(updateButton);
		
		saveAsButton = new JButton("Save As...");
		profileSelectionBox.add(saveAsButton);
		
		importButton = new JButton("Import");
		profileSelectionBox.add(importButton);
		
		profileTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		profilePanel.add(profileTabbedPane, BorderLayout.CENTER);
		
		summaryPanel = new JPanel();
		summaryPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
		profileTabbedPane.addTab("Summary", null, summaryPanel, null);
		summaryPanel.setLayout(new BorderLayout(0, 0));
		
		summaryScrollPane = new JScrollPane();
		summaryPanel.add(summaryScrollPane, BorderLayout.CENTER);
		
		summaryTextPane = new JTextPane();
		summaryTextPane.setEditable(false);
		summaryScrollPane.setViewportView(summaryTextPane);
		
		foldersPanel = new JPanel();
		foldersPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
		profileTabbedPane.addTab("Target Folders", null, foldersPanel, null);
		foldersPanel.setLayout(new BorderLayout(0, 0));
		
		foldersBox = Box.createVerticalBox();
		foldersPanel.add(foldersBox, BorderLayout.CENTER);
		
		foldersControlBox = Box.createHorizontalBox();
		foldersBox.add(foldersControlBox);
		
		addFolderButton = new JButton("Add Folder");
		foldersControlBox.add(addFolderButton);
		
		removeFolderButton = new JButton("Remove Folder");
		foldersControlBox.add(removeFolderButton);
		
		foldersScrollPane = new JScrollPane();
		foldersBox.add(foldersScrollPane);
		
		foldersList = new JList();
		foldersScrollPane.setViewportView(foldersList);
		
		indexingOptionsBox = Box.createVerticalBox();
		foldersPanel.add(indexingOptionsBox, BorderLayout.EAST);
		
		indexingOptionsTopGlue = Box.createVerticalGlue();
		indexingOptionsBox.add(indexingOptionsTopGlue);
		
		scanSubFoldersCheckBox = new JCheckBox("Scan sub-folders");
		indexingOptionsBox.add(scanSubFoldersCheckBox);
		
		scanHiddenFoldersCheckBox = new JCheckBox("Scan hidden folders");
		indexingOptionsBox.add(scanHiddenFoldersCheckBox);
		
		scanReadOnlyCheckBox = new JCheckBox("Scan read-only folders");
		indexingOptionsBox.add(scanReadOnlyCheckBox);
		
		indexingOptionsBottonGlue = Box.createVerticalGlue();
		indexingOptionsBox.add(indexingOptionsBottonGlue);
		
		filtersPanel = new JPanel();
		filtersPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
		profileTabbedPane.addTab("Filters", null, filtersPanel, null);
		filtersPanel.setLayout(new BorderLayout(0, 0));
		
		filtersBox = Box.createVerticalBox();
		filtersPanel.add(filtersBox, BorderLayout.CENTER);
		
		filtersControlBox = Box.createHorizontalBox();
		filtersBox.add(filtersControlBox);
		
		addFilterButton = new JButton("Add Filter");
		filtersControlBox.add(addFilterButton);
		
		removeFilterButton = new JButton("Remove Filter");
		filtersControlBox.add(removeFilterButton);
		
		filtersScrollPane = new JScrollPane();
		filtersBox.add(filtersScrollPane);
		
		filtersList = new JList();
		filtersScrollPane.setViewportView(filtersList);
		
		filteringOptionsBox = Box.createVerticalBox();
		filtersPanel.add(filteringOptionsBox, BorderLayout.EAST);
		
		filteringOptionsTopGlue = Box.createVerticalGlue();
		filteringOptionsBox.add(filteringOptionsTopGlue);
		
		chckbxNewCheckBox = new JCheckBox("Index inclusively");
		filteringOptionsBox.add(chckbxNewCheckBox);
		
		verticalStrut = Box.createVerticalStrut(20);
		filteringOptionsBox.add(verticalStrut);
		
		btnNewButton = new JButton("Move Up");
		filteringOptionsBox.add(btnNewButton);
		
		btnNewButton_1 = new JButton("Move Down");
		filteringOptionsBox.add(btnNewButton_1);
		
		filteringOptionsBottomGlue = Box.createVerticalGlue();
		filteringOptionsBox.add(filteringOptionsBottomGlue);
		
		startScanControlBox = Box.createHorizontalBox();
		startScanControlBox.setBorder(new EmptyBorder(0, 0, 6, 0));
		profilePanel.add(startScanControlBox, BorderLayout.SOUTH);
		
		startScanLeftGlue = Box.createHorizontalGlue();
		startScanControlBox.add(startScanLeftGlue);
		
		startScanButton = new JButton("Start Scan");
		startScanControlBox.add(startScanButton);
		
		startScanRightGlue = Box.createHorizontalGlue();
		startScanControlBox.add(startScanRightGlue);
	}

}
