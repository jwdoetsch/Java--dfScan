package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.Rectangle;
import java.awt.Button;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JComboBox;
import java.awt.Component;
import javax.swing.JTabbedPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTable;

public class MainAppWindow extends JFrame {

	private JDesktopPane desktopPane;
	private JMenuBar menuBar;
	private JInternalFrame profileFrame;
	private JMenu fileMenu;
	private JMenu windowMenu;
	private JMenu helpMenu;
	private JMenuItem updateMenuItem;
	private JMenuItem aboutMenuItem;
	private JSeparator helpMenuSeparator;
	private JMenuItem logMenuItem;
	private JMenuItem tutorialMenuItem;
	private JMenuItem exitMenuItem;
	private Box profileSelectBox;
	private Box horizontalBox_1;
	private JComboBox profileSelectComboBox;
	private JButton profileUpdateButton;
	private Box startScanBox;
	private JButton startScanButton;
	private Component leftGlue;
	private Component rightGlue;
	private JTabbedPane profileTabPane;
	private JPanel summaryPanel;
	private JPanel targetsPanel;
	private JPanel filtersPanel;
	private JButton profileSaveAsButton;
	private JPanel namePanel;
	private JPanel descriptionPanel;
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel descriptionLabel;
	private JScrollPane descriptionScrollPane;
	private JTextArea descriptionTextArea;
	private Box indexingOptionsBox;
	private JCheckBox indexRecursivelyCheckBox;
	private Box targetFoldersBox;
	private Box targetFoldersControlBox;
	private JButton addFolderButton;
	private JButton removeFolderButton;
	private JScrollPane targetFoldersScrollPane;
	private Component indexingOptionsBottomGlue;
	private Component indexingOptionsTopGlue;
	private JCheckBox indexHiddenFoldersCheckBox;
	private JCheckBox indexReadOnlyFoldersCheckBox;
	private JButton profileImportButton;
	private JList targetFoldersList;
	private Box filtersBox;
	private Box filterOptionsBox;
	private Box filtersControlBox;
	private JButton addFilterButton;
	private JButton removeFilterButton;
	private JScrollPane filtersScrollPane;
	private JList filtersList;
	private JButton moveUpButton;
	private JButton moveDownButton;
	private JCheckBox indexInclusivelyCheckBox;
	private Component filterOptionsTopGlue;
	private Component filterOptionsBottonGlue;
	private Component filtersLeftGlue;
	private Component filtersRightGlue;
	private Component filtersMiddleGlue;
	private JInternalFrame mainFrame;
	private JMenuItem resetMenuItem;
	private JSeparator separator;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JPanel panel_1;
	private Box horizontalBox;
	private JScrollPane scrollPane;
	private JTable table;
	private JComboBox comboBox;
	private Component horizontalGlue;
	private JComboBox comboBox_1;

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
					MainAppWindow frame = new MainAppWindow();
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
	public MainAppWindow () {
		initComponents();
	}
	private void initComponents() {
		setTitle("dfScan v0.10");
		//setPreferredSize(new Dimension(1020, 720));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		
		windowMenu = new JMenu("Window");
		menuBar.add(windowMenu);
		
		logMenuItem = new JMenuItem("Log");
		windowMenu.add(logMenuItem);
		
		separator = new JSeparator();
		windowMenu.add(separator);
		
		resetMenuItem = new JMenuItem("Reset Windows");
		windowMenu.add(resetMenuItem);
		
		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		updateMenuItem = new JMenuItem("Update");
		updateMenuItem.setEnabled(false);
		helpMenu.add(updateMenuItem);
		
		helpMenuSeparator = new JSeparator();
		helpMenu.add(helpMenuSeparator);
		
		tutorialMenuItem = new JMenuItem("User Guide");
		helpMenu.add(tutorialMenuItem);
		
		aboutMenuItem = new JMenuItem("About");
		helpMenu.add(aboutMenuItem);

		desktopPane = new JDesktopPane();
		setContentPane(desktopPane);
		
		profileFrame = new JInternalFrame("Profile", false, false, false, true);
		profileFrame.setBounds(6, 402, 996, 300);
		profileFrame.setVisible(true);
		JPanel profileFrameContentPane = new JPanel();
		profileFrame.setContentPane(profileFrameContentPane);
		profileFrameContentPane.setLayout(new BorderLayout(0, 0));
		
		profileSelectBox = Box.createHorizontalBox();
		profileSelectBox.setBorder(new EmptyBorder(6, 6, 6, 6));
		profileFrameContentPane.add(profileSelectBox, BorderLayout.NORTH);
		
		profileSelectComboBox = new JComboBox();
		profileSelectComboBox.setModel(new DefaultComboBoxModel(new String[] {"Custom..."}));
		profileSelectBox.add(profileSelectComboBox);
		
		profileUpdateButton = new JButton("Update");
		profileSelectBox.add(profileUpdateButton);
		
		profileSaveAsButton = new JButton("Save As...");
		profileSelectBox.add(profileSaveAsButton);
		
		profileImportButton = new JButton("Import...");
		profileSelectBox.add(profileImportButton);
		
		startScanBox = Box.createHorizontalBox();
		startScanBox.setBorder(new EmptyBorder(0, 6, 6, 6));
		profileFrameContentPane.add(startScanBox, BorderLayout.SOUTH);
		
		leftGlue = Box.createHorizontalGlue();
		startScanBox.add(leftGlue);
		
		startScanButton = new JButton("Start Scan");
		startScanBox.add(startScanButton);
		
		rightGlue = Box.createHorizontalGlue();
		startScanBox.add(rightGlue);
		
		profileTabPane = new JTabbedPane(JTabbedPane.TOP);
		profileFrameContentPane.add(profileTabPane, BorderLayout.CENTER);
		
		summaryPanel = new JPanel();
		summaryPanel.setBorder(new EmptyBorder(6, 6, 0, 6));
		
		profileTabPane.addTab("Summary", null, summaryPanel, null);
		summaryPanel.setLayout(new BorderLayout(0, 0));
		
		namePanel = new JPanel();
		summaryPanel.add(namePanel, BorderLayout.NORTH);
		namePanel.setLayout(new BorderLayout(0, 0));
		
		nameLabel = new JLabel("Profile Name");
		namePanel.add(nameLabel, BorderLayout.NORTH);
		
		nameField = new JTextField();
		namePanel.add(nameField, BorderLayout.SOUTH);
		nameField.setColumns(10);
		
		descriptionPanel = new JPanel();
		summaryPanel.add(descriptionPanel, BorderLayout.CENTER);
		descriptionPanel.setLayout(new BorderLayout(0, 0));
		
		descriptionLabel = new JLabel("Profile Description");
		descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);
		
		descriptionScrollPane = new JScrollPane();
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);
		
		descriptionTextArea = new JTextArea();
		descriptionScrollPane.setViewportView(descriptionTextArea);
		
		targetsPanel = new JPanel();
		targetsPanel.setBorder(new EmptyBorder(6, 6, 0, 6));
		profileTabPane.addTab("Target Folders", null, targetsPanel, null);
		targetsPanel.setLayout(new BorderLayout(0, 0));
		
		indexingOptionsBox = Box.createVerticalBox();
		targetsPanel.add(indexingOptionsBox, BorderLayout.EAST);
		
		indexingOptionsTopGlue = Box.createVerticalGlue();
		indexingOptionsBox.add(indexingOptionsTopGlue);
		
		indexRecursivelyCheckBox = new JCheckBox("Scan sub-folders within the target folders");
		indexingOptionsBox.add(indexRecursivelyCheckBox);
		
		indexHiddenFoldersCheckBox = new JCheckBox("Scan hidden folders");
		indexingOptionsBox.add(indexHiddenFoldersCheckBox);
		
		indexReadOnlyFoldersCheckBox = new JCheckBox("Scan read-only folders");
		indexingOptionsBox.add(indexReadOnlyFoldersCheckBox);
		
		indexingOptionsBottomGlue = Box.createVerticalGlue();
		indexingOptionsBox.add(indexingOptionsBottomGlue);
		
		targetFoldersBox = Box.createVerticalBox();
		targetsPanel.add(targetFoldersBox, BorderLayout.CENTER);
		
		targetFoldersControlBox = Box.createHorizontalBox();
		targetFoldersBox.add(targetFoldersControlBox);
		
		addFolderButton = new JButton("Add Folder");
		targetFoldersControlBox.add(addFolderButton);
		
		removeFolderButton = new JButton("Remove Folder");
		targetFoldersControlBox.add(removeFolderButton);
		
		targetFoldersScrollPane = new JScrollPane();
		targetFoldersBox.add(targetFoldersScrollPane);
		
		targetFoldersList = new JList();
		targetFoldersScrollPane.setViewportView(targetFoldersList);
		
		filtersPanel = new JPanel();
		filtersPanel.setBorder(new EmptyBorder(6, 6, 0, 6));
		profileTabPane.addTab("Filters", null, filtersPanel, null);
		filtersPanel.setLayout(new BorderLayout(0, 0));
		
		filtersBox = Box.createVerticalBox();
		filtersPanel.add(filtersBox, BorderLayout.CENTER);
		
		filtersControlBox = Box.createHorizontalBox();
		filtersBox.add(filtersControlBox);
		
		filtersLeftGlue = Box.createHorizontalGlue();
		filtersControlBox.add(filtersLeftGlue);
		
		addFilterButton = new JButton("Add Filter");
		filtersControlBox.add(addFilterButton);
		
		removeFilterButton = new JButton("Remove Filter");
		filtersControlBox.add(removeFilterButton);
		
		filtersMiddleGlue = Box.createHorizontalGlue();
		filtersControlBox.add(filtersMiddleGlue);
		
		moveUpButton = new JButton("Move Up");
		filtersControlBox.add(moveUpButton);
		
		moveDownButton = new JButton("Move Down");
		filtersControlBox.add(moveDownButton);
		
		filtersRightGlue = Box.createHorizontalGlue();
		filtersControlBox.add(filtersRightGlue);
		
		filtersScrollPane = new JScrollPane();
		filtersBox.add(filtersScrollPane);
		
		filtersList = new JList();
		filtersScrollPane.setViewportView(filtersList);
		
		filterOptionsBox = Box.createVerticalBox();
		filtersPanel.add(filterOptionsBox, BorderLayout.EAST);
		
		filterOptionsTopGlue = Box.createVerticalGlue();
		filterOptionsBox.add(filterOptionsTopGlue);
		
		indexInclusivelyCheckBox = new JCheckBox("Index inclusively");
		filterOptionsBox.add(indexInclusivelyCheckBox);
		
		filterOptionsBottonGlue = Box.createVerticalGlue();
		filterOptionsBox.add(filterOptionsBottonGlue);
		
		desktopPane.add(profileFrame);
		
		
		
		
		mainFrame = new JInternalFrame("Main");
		mainFrame.setIconifiable(true);
		mainFrame.setResizable(true);
		mainFrame.setMaximizable(true);
		mainFrame.setBounds(6, 6, 996, 390);
		JPanel mainFrameContentPane = new JPanel();
		mainFrame.setContentPane(mainFrameContentPane);
		mainFrameContentPane.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainFrameContentPane.add(tabbedPane);
		
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(6, 6, 6, 6));
		tabbedPane.addTab("Scan In Progress... (20s)", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		horizontalBox = Box.createHorizontalBox();
		panel.add(horizontalBox, BorderLayout.NORTH);
		
		comboBox = new JComboBox();
		horizontalBox.add(comboBox);
		
		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		comboBox_1 = new JComboBox();
		horizontalBox.add(comboBox_1);
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		panel_1 = new JPanel();
		tabbedPane.addTab("Results - 5:16 PM March 7, 2014", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		desktopPane.add(mainFrame);
		mainFrame.setVisible(true);
	}
}
