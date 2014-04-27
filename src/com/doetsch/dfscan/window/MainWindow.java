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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.Profile;
import com.doetsch.dfscan.core.SettingsContainer;
import com.doetsch.dfscan.filter.ContentIndexFilter;
import com.doetsch.dfscan.filter.NameContainsFilter;
import com.doetsch.dfscan.util.ContentIndex;
import com.doetsch.dfscan.util.FolderChooser;
import com.doetsch.dfscan.util.HashableFile;

import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.TitledBorder;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JSplitPane splitPane; 
	private JPanel reportPanel;
	private JPanel profilePanel;
	private JMenuBar menuBar;
	private Box profileSelectionBox;
	private JComboBox<ProfileEntry> profileComboBox;
	private JButton saveButton;
	private Box startScanControlBox;
	private JButton startScanButton;
	private Component startScanRightGlue;
	private Component startScanLeftGlue;
	private JScrollPane summaryScrollPane;
	private JTextArea summaryTextArea;
	private JTabbedPane profileTabbedPane;
	private JPanel summaryPanel;
	private JTabbedPane reportTabbedPane;
	private JMenu fileMenu;
	private JPanel foldersPanel;
	private Box foldersBox;
	private Box indexingOptionsBox;
	private JCheckBox scanSubFoldersCheckBox;
	private JCheckBox scanHiddenFoldersCheckBox;
	private JCheckBox scanReadOnlyFoldersCheckBox;
	private Component indexingOptionsTopGlue;
	private Component indexingOptionsBottonGlue;
	private Box foldersControlBox;
	private JButton addFolderButton;
	private JButton removeFolderButton;
	private JScrollPane foldersScrollPane;
	private JList<String> foldersList;
	private DefaultListModel<String> foldersListModel;
	private JPanel filtersPanel;
	private Box filtersBox;
	private Box filteringOptionsBox;
	private Box filtersControlBox;
	private JScrollPane filtersScrollPane;
	private JList<FilterListEntry> filtersList;
	private DefaultListModel<FilterListEntry> filtersListModel;
	private JButton addFilterButton;
	private JButton removeFilterButton;
	private JCheckBox indexInclusivelyCheckBox;
	private Component filteringOptionsTopGlue;
	private Component filteringOptionsBottomGlue;
	private JMenuItem openResultsMenuItem;
	private JMenuItem exitMenuItem;
	private JSeparator fileMenuSeparator;
	private JMenu helpMenu;
	private JMenuItem updateMenuItem;
	private JSeparator helpMenuSeparator;
	private JMenuItem guideMenuItem;
	private JButton moveUpButton;
	private JButton moveDownButton;
	private Component verticalStrut;
	private JButton refreshButton;
	private JPanel headerPanel;
	private JTextField nameTextField;
	private JTextArea descriptionTextArea;
	private JMenuItem wizardMenuItem;
	private JMenuItem donateMenuItem;
	private TrayIcon systemTrayIcon;

	/**
	 * Create the frame.
	 */
	public MainWindow () {
		initComponents();
		setBehavior();
		setDefaultValues();
	}
	
	private void initComponents() {
		setTitle("dfScan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan3.png"))).getImage());
		setSize(1026, 768);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		wizardMenuItem = new JMenuItem("Wizard");
		wizardMenuItem.setEnabled(false);
		fileMenu.add(wizardMenuItem);
		
		openResultsMenuItem = new JMenuItem("Open Results...");
		fileMenu.add(openResultsMenuItem);
		
		fileMenuSeparator = new JSeparator();
		fileMenu.add(fileMenuSeparator);
		
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		
		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		guideMenuItem = new JMenuItem("User Guide");
		helpMenu.add(guideMenuItem);
		
		donateMenuItem = new JMenuItem("Donate");
		donateMenuItem.setToolTipText("A donation would go a long way to supporting us, and we'd love you forever!");
		helpMenu.add(donateMenuItem);
		
		helpMenuSeparator = new JSeparator();
		helpMenu.add(helpMenuSeparator);
		
		updateMenuItem = new JMenuItem("Update");
		helpMenu.add(updateMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(12, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		reportPanel = new JPanel();
		reportPanel.setPreferredSize(new Dimension(10, 400));
		splitPane.setLeftComponent(reportPanel);
		reportPanel.setLayout(new BorderLayout(0, 0));
		
		reportTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		reportTabbedPane.setPreferredSize(new Dimension(0, 400));
		reportPanel.add(reportTabbedPane, BorderLayout.CENTER);

		profilePanel = new JPanel();
		splitPane.setRightComponent(profilePanel);
		profilePanel.setLayout(new BorderLayout(0, 0));
		
		profileSelectionBox = Box.createHorizontalBox();
		profileSelectionBox.setBorder(new EmptyBorder(6, 6, 6, 6));
		profilePanel.add(profileSelectionBox, BorderLayout.NORTH);
		
		profileComboBox = new JComboBox<ProfileEntry>();
		profileSelectionBox.add(profileComboBox);
		
		refreshButton = new JButton("Refresh");
		profileSelectionBox.add(refreshButton);
		
		saveButton = new JButton("Save Current Settings & Options");
		profileSelectionBox.add(saveButton);
		
		profileTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		profilePanel.add(profileTabbedPane, BorderLayout.CENTER);
		
		summaryPanel = new JPanel();
		summaryPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
		profileTabbedPane.addTab("Summary", null, summaryPanel, null);
		summaryPanel.setLayout(new BorderLayout(0, 0));
		
		summaryScrollPane = new JScrollPane();
		summaryPanel.add(summaryScrollPane, BorderLayout.CENTER);
		
		summaryTextArea = new JTextArea();
		summaryTextArea.setEditable(false);
		summaryScrollPane.setViewportView(summaryTextArea);
		
		headerPanel = new JPanel();
		headerPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
		profileTabbedPane.addTab("Header", null, headerPanel, null);
		headerPanel.setLayout(new BorderLayout(0, 0));
		
		nameTextField = new JTextField();
		headerPanel.add(nameTextField, BorderLayout.NORTH);
		nameTextField.setColumns(10);
		
		descriptionTextArea = new JTextArea();
		headerPanel.add(descriptionTextArea, BorderLayout.CENTER);
		
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
		
		foldersList = new JList<String>();
		foldersScrollPane.setViewportView(foldersList);
		
		foldersListModel = new DefaultListModel<String>();
		foldersList.setModel(foldersListModel);
		
		indexingOptionsBox = Box.createVerticalBox();
		foldersPanel.add(indexingOptionsBox, BorderLayout.EAST);
		
		indexingOptionsTopGlue = Box.createVerticalGlue();
		indexingOptionsBox.add(indexingOptionsTopGlue);
		
		scanSubFoldersCheckBox = new JCheckBox("Scan sub-folders");
		indexingOptionsBox.add(scanSubFoldersCheckBox);
		
		scanHiddenFoldersCheckBox = new JCheckBox("Scan hidden folders");
		indexingOptionsBox.add(scanHiddenFoldersCheckBox);
		
		scanReadOnlyFoldersCheckBox = new JCheckBox("Scan read-only folders");
		indexingOptionsBox.add(scanReadOnlyFoldersCheckBox);

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
		
		filtersList = new JList<FilterListEntry>();
		filtersScrollPane.setViewportView(filtersList);
		
		filtersListModel = new DefaultListModel<FilterListEntry>();
		filtersList.setModel(filtersListModel);
		
		filteringOptionsBox = Box.createVerticalBox();
		filtersPanel.add(filteringOptionsBox, BorderLayout.EAST);
		
		filteringOptionsTopGlue = Box.createVerticalGlue();
		filteringOptionsBox.add(filteringOptionsTopGlue);
		
		indexInclusivelyCheckBox = new JCheckBox("Index inclusively");
		filteringOptionsBox.add(indexInclusivelyCheckBox);
		
		verticalStrut = Box.createVerticalStrut(20);
		filteringOptionsBox.add(verticalStrut);
		
		moveUpButton = new JButton("Move Up");
		filteringOptionsBox.add(moveUpButton);
		
		moveDownButton = new JButton("Move Down");
		filteringOptionsBox.add(moveDownButton);
		
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
		splitPane.setDividerLocation(400);
		
		showUserGuide();
		
		if (SystemTray.isSupported()) {
		
			systemTrayIcon = new TrayIcon((new ImageIcon(DFScan.class.getResource("resources/icons/tray_icon.png"))).getImage());
			
			PopupMenu trayMenu = new PopupMenu();
			MenuItem exitMenuItem = new MenuItem("Exit");
			exitMenuItem.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed (ActionEvent arg0) {
					closeApplication();
				}
				
			});
			
			trayMenu.add(exitMenuItem);
			systemTrayIcon.setPopupMenu(trayMenu);
			
			try {
				SystemTray.getSystemTray().add(systemTrayIcon);
	
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void setBehavior () {
		
		/*
		 * Behavior for selecting the Exit menu item; 
		 */
		exitMenuItem.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {
				closeApplication();
			}
			
		});
		
		/*
		 * Define the behavior of the UI components within the profile pane
		 * portion of the split pane.		
		 */
		refreshButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {
				populateProfileDetails();
			}
			
		});
		
//		saveButton.addActionListener(new AbstractAction() {
//
//			@Override
//			public void actionPerformed (ActionEvent arg0) {
//				populateProfileDetails();
//			}
//			
//		});
		
		profileTabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged (ChangeEvent e) {

				if (reportTabbedPane.getSelectedIndex() == 0) {
					populateProfileSummary();
				}
			}
			
		});
		
		startScanButton.addActionListener(new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				
				ProgressPanel progressPanel = new ProgressPanel(buildCurrentDetectionProfile(), reportTabbedPane, systemTrayIcon);
			}
			
		});
		
		profileComboBox.addActionListener(new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent e) {
				
				if (e.getActionCommand().equals("comboBoxChanged")) {
					
					populateProfileDetails();
				}
				
			}
			
		});
		
		saveButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				
				Profile profile = buildCurrentDetectionProfile();				
				String checkMessage = "Are you sure you'd like to save the current settings"
						+ " and options under the profile name \"" + profile.getName() + "\"?"; 
				String checkTitle = "Save Confirmation";
				int currentProfileIndex = profileComboBox.getSelectedIndex();				
				
				if (JOptionPane.showConfirmDialog(MainWindow.this, checkMessage,
						checkTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					
					try {
						Profile.save(profile);

					} catch (TransformerFactoryConfigurationError e) {
						e.printStackTrace();

					} catch (TransformerException e) {
						e.printStackTrace();
					}
					
					populateComboBoxProfiles();
					profileComboBox.setSelectedIndex(currentProfileIndex);
					
				}
			}
			
		});
		
		addFilterButton.addActionListener(new AbstractAction () {
			
			@Override
			public void actionPerformed (ActionEvent e) {
				
				FilterBuilderDialog filterBuilderWindow =
						new FilterBuilderDialog(MainWindow.this, filtersListModel);
				//populateProfileSummary();
				
			}
			
		});
		
		removeFilterButton.addActionListener(new AbstractAction () {

			/*
			 * Removes the filter list element at the selected index, provided
			 * one is selected and available.
			 */
			@Override
			public void actionPerformed (ActionEvent e) {
				
				int index = filtersList.getSelectedIndex();
				
				if (index > -1) {
					filtersListModel.removeElementAt(index);
				}
				populateProfileSummary();
			}
			
		});
		
		moveUpButton.addActionListener(new AbstractAction () {

			/*
			 * Move the selected filter entry up the list
			 */
			@Override
			public void actionPerformed (ActionEvent e) {
				
				int index = filtersList.getSelectedIndex();
				
				if ((index < 0) || (filtersListModel.size() < 2) || (index < 1)) {
					return;
				}
				
				FilterListEntry entry = filtersListModel.remove(index);
								
				filtersListModel.insertElementAt(entry, index - 1);
				filtersList.setSelectedIndex(index - 1);
				populateProfileSummary();
			}
			
		});
		
		moveDownButton.addActionListener(new AbstractAction () {

			/*
			 * Move the selected filter entry down the list
			 */
			@Override
			public void actionPerformed (ActionEvent e) {
			
				
				int index = filtersList.getSelectedIndex();
				
				if ((index < 0) || (filtersListModel.size() < 2) || (index > (filtersListModel.size() - 2))) {
					return;
				}
				
				FilterListEntry entry = filtersListModel.remove(index);
				filtersListModel.insertElementAt(entry, index + 1);
				filtersList.setSelectedIndex(index + 1);
				populateProfileSummary();
			}
			
		});
		
		addFolderButton.addActionListener(new AbstractAction () {
			
			/*
			 * Displays a folder choosing dialog box and adds the selected folder
			 * to the target folder list
			 */
			@Override
			public void actionPerformed (ActionEvent e) {
				
				FolderChooser folderChooser = new FolderChooser(
						MainWindow.this, new File(System.getProperty("user.home")), "Select a target folder");
				
				if (folderChooser.getFolder() == true) {
					foldersListModel.addElement(folderChooser.getSelectedFile().getPath());
				}
				
				populateProfileSummary();				
			}
			
		});
		
		removeFolderButton.addActionListener(new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent e) {

				int index = foldersList.getSelectedIndex();
				
				if (index > -1) {
					foldersListModel.removeElementAt(index);
				}
				
				populateProfileSummary();
			}
			
		});
		
		/*
		 * Defines the behavior of the menu bar items.
		 */
		guideMenuItem.addActionListener(new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				showUserGuide();
			}
			
		});
		
		/*
		 * Defines the behavior upon the main window closing down.
		 */
		this.addWindowListener(new WindowAdapter() {
			
			/*
			 * Removes the system tray icon upon normal application closure.
			 */
			@Override
			public void windowClosing (WindowEvent e) {
				if (systemTrayIcon != null) {
					SystemTray.getSystemTray().remove(systemTrayIcon);
				}
			}
			
		});
		
	}
	
	private void setDefaultValues () {
		populateComboBoxProfiles();
	}
	
	/**
	 * 
	 */
	private void closeApplication () {
		this.processWindowEvent(new WindowEvent(MainWindow.this, WindowEvent.WINDOW_CLOSING));		
	}
	
	private Profile buildCurrentDetectionProfile () {
		Profile selectedProfile;
		Profile detectionProfile;
		
		/*
		 * While the profile within the profile selector ComboBox could be used,
		 * the user is able to change the options once the profile is loaded. So the
		 * current options & settings need to be used.
		 */
		if (profileComboBox.getSelectedIndex() > -1) {
			selectedProfile = ((ProfileEntry) profileComboBox.getSelectedItem()).getProfile();
			detectionProfile = new Profile(selectedProfile.getName(), selectedProfile.getDescription());					
		} else {
			detectionProfile = new Profile("Custom Profile", "Custom Profile Decsription");
		}
		
		//Set the indexing and scanning settings of the detection profile
		detectionProfile.setSettings(new SettingsContainer(
				indexInclusivelyCheckBox.isSelected(), scanSubFoldersCheckBox.isSelected(),
				scanReadOnlyFoldersCheckBox.isSelected(), scanHiddenFoldersCheckBox.isSelected()));
		
		detectionProfile.setName(nameTextField.getText());
		detectionProfile.setDescription(descriptionTextArea.getText());
		
		//Add the current entries within the filter list to the detection profile
		for (int i = 0; i < filtersListModel.getSize(); i++) {
			detectionProfile.getFilters().add(filtersListModel.get(i).getContentIndexFilter());
		}
		
		//Add the current entries within the folder list to the detection profile
		for (int i = 0; i < foldersListModel.getSize(); i++) {
			detectionProfile.getTargets().add(foldersListModel.get(i));
		}
		
		return detectionProfile;
	}

	private void showUserGuide () {
		UserGuidePanel userGuidePanel = new UserGuidePanel(reportTabbedPane, systemTrayIcon);
		reportTabbedPane.addTab("", null, userGuidePanel, "Welcome Page");
		reportTabbedPane.setTabComponentAt(
				reportTabbedPane.indexOfComponent(userGuidePanel), userGuidePanel.getTabAsComponent());
		reportTabbedPane.setSelectedComponent(userGuidePanel);
	}
	
	/*
	 * Loads the detection scan profiles from the resources/profiles folder
	 */
	public void populateComboBoxProfiles () {
		
		ContentIndex sourceIndex = new ContentIndex("profiles/");
		ContentIndex profileIndex = (new NameContainsFilter(".dfscan.profile.xml", true)).enforce(sourceIndex);
		Profile profile;
		
		profileComboBox.setModel(new DefaultComboBoxModel<ProfileEntry>() );
		
		((DefaultComboBoxModel<ProfileEntry>)profileComboBox.getModel()).addElement(
				new ProfileEntry(new Profile("Select a profile or define your own...", "Does Something Wonderful")));
		
		for (HashableFile file : profileIndex) {

			try {
				profile = Profile.load(file.getPath());
				((DefaultComboBoxModel<ProfileEntry>)profileComboBox.getModel()).addElement(new ProfileEntry(profile));	

			} catch (SAXException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			
		}
		
		populateProfileDetails();
		
	}
	
	private void populateProfileSummary () {
		summaryTextArea.setText("");
		summaryTextArea.append(buildCurrentDetectionProfile().getDetailedDescription());
		summaryTextArea.setCaretPosition(0);
	}
	
	private void populateProfileDetails () {
		
		if (profileComboBox.getSelectedIndex() > -1) {
			
			Profile profile = profileComboBox.getModel().getElementAt(profileComboBox.getSelectedIndex())
					.getProfile();
			
			nameTextField.setText(profile.getName());
			descriptionTextArea.setText(profile.getDescription());
			scanSubFoldersCheckBox.setSelected(profile.getSettings().getIndexRecursively());
			scanHiddenFoldersCheckBox.setSelected(profile.getSettings().getIndexHiddenFolders());
			scanReadOnlyFoldersCheckBox.setSelected(profile.getSettings().getIndexReadOnlyFolders());
			indexInclusivelyCheckBox.setSelected(profile.getSettings().getIndexInclusively());			
			
			foldersListModel = new DefaultListModel<String>();
			foldersList.setModel(foldersListModel);
			for (String targetPath : profile.getTargets()) {
				foldersListModel.addElement(targetPath);
			}
			
			filtersListModel = new DefaultListModel<FilterListEntry>();
			filtersList.setModel(filtersListModel);
			for (ContentIndexFilter filter : profile.getFilters()) {
				filtersListModel.addElement(new FilterListEntry(filter));
			}
			
			populateProfileSummary();
			
		}
		
	}
	
}
