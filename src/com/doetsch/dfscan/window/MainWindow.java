package com.doetsch.dfscan.window;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.UIManager;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JLabel;

import java.awt.CardLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.Profile;
import com.doetsch.dfscan.filter.NameContainsFilter;
import com.doetsch.dfscan.util.ContentIndex;
import com.doetsch.dfscan.util.HashableFile;
import com.doetsch.oxide.*;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.JEditorPane;

public class MainWindow extends OxideFrame {

	private OxideMenuButton buttonHelp;
	private OxideMenuButton buttonScan;
	private OxideMenuButton buttonHistory;
	private JLabel labelMenuBackground;
	private JLabel labelMenuSeparator;
	private JPanel panelDeck;
	private JPanel panelHelp;
	private JPanel panelScan;
	private JPanel panelHistory;
	
	private final String[] panelID = {"home", "scan", "history"};
	private JButton buttonEdit;
	private JButton buttonAdd;
	private JComboBox<ProfileEntry> comboBoxProfile;
	private JPanel panelProfileDetails;
	private JButton buttonStart;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JScrollPane scrollPaneHistory;
	private JTable tableHistory;
	private JScrollPane scrollPaneHelp;
	private JEditorPane editorPaneHelp;

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
			public void run () {
				try {
					MainWindow frame = new MainWindow();
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
	public MainWindow () {
		super(true, new OxideDefaultSkin());
	
		initComponents();
		defineBehavior();
	}
	
	private void initComponents() {
		setTitle("dfscan v0.01");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 450);
		centerInViewport();
		//super.centerInViewport();
		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan.png"))).getImage());
		
		OxideComponentFactory oxideComponentFactory = new OxideComponentFactory(getOxideSkin());
		
		buttonHelp = new OxideMenuButton((String) null, getOxideSkin());
		buttonHelp.setToolTipText("Opens the dfScan User Guide.");
		buttonHelp.setText("Help");
		buttonHelp.setBounds(0, 0, 120, 90);
		getContentPane().add(buttonHelp);
		
		buttonScan = new OxideMenuButton((String) null, getOxideSkin());
		buttonScan.setText("Scan");
		buttonScan.setBounds(0, 90, 120, 90);
		getContentPane().add(buttonScan);
		
		buttonHistory = new OxideMenuButton((String) null, getOxideSkin());
		buttonHistory.setText("History");
		buttonHistory.setBounds(0, 180, 120, 90);
		getContentPane().add(buttonHistory);
		
		labelMenuBackground = new JLabel("");
		labelMenuBackground.setOpaque(true);
		labelMenuBackground.setBackground(getOxideSkin().getShadeColor1());
		labelMenuBackground.setBounds(0, 0, 120, 450);
		getContentPane().add(labelMenuBackground);
		
		labelMenuSeparator = new JLabel("");
		labelMenuSeparator.setBackground(getOxideSkin().getDecorationBorderColor());
		labelMenuSeparator.setOpaque(true);
		labelMenuSeparator.setBounds(120, 0, 6, 450);
		getContentPane().add(labelMenuSeparator);
		
		panelDeck = new JPanel();
		panelDeck.setBounds(126, 0, 594, 450);
		getContentPane().add(panelDeck);
		panelDeck.setLayout(new CardLayout(0, 0));
		
		panelHelp = new JPanel();
		panelDeck.add(panelHelp, panelID[0]);
		panelHelp.setLayout(null);
		
		scrollPaneHelp = new JScrollPane();
		scrollPaneHelp.setBounds(12, 12, 570, 426);
		panelHelp.add(scrollPaneHelp);
		
		editorPaneHelp = new JEditorPane();
		editorPaneHelp.setEditable(false);
		try {
			editorPaneHelp.setPage(DFScan.class.getResource("resources/docs/help.html"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		scrollPaneHelp.setViewportView(editorPaneHelp);
		
		panelScan = new JPanel();
		panelDeck.add(panelScan, panelID[1]);
		panelScan.setLayout(null);
		
		buttonEdit = oxideComponentFactory.createButton();
		buttonEdit.setText("Edit");
		buttonEdit.setBounds(438, 12, 66, 24);
		panelScan.add(buttonEdit);
		
		buttonAdd = oxideComponentFactory.createButton();
		buttonAdd.setText("Add");
		buttonAdd.setBounds(516, 12, 66, 24);
		panelScan.add(buttonAdd);
		
		comboBoxProfile = oxideComponentFactory.createComboBox();
		comboBoxProfile.setModel(new DefaultComboBoxModel<ProfileEntry>(new ProfileEntry[] {}));
		comboBoxProfile.setBounds(12, 12, 414, 24);
		panelScan.add(comboBoxProfile);
		
		panelProfileDetails = oxideComponentFactory.createTitledPanel("Profile Details");
		panelProfileDetails.setBounds(12, 48, 570, 354);
		panelScan.add(panelProfileDetails);
		panelProfileDetails.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 30, 546, 312);
		panelProfileDetails.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(getOxideSkin().getControlFontFace());
		textArea.setForeground(getOxideSkin().getControlFontColor());
		scrollPane.setViewportView(textArea);
		
		buttonStart = oxideComponentFactory.createButton();
		buttonStart.setText("Start");
		buttonStart.setBounds(12, 414, 570, 24);
		panelScan.add(buttonStart);
		
		panelHistory = new JPanel();
		panelDeck.add(panelHistory, panelID[2]);
		panelHistory.setLayout(null);
		
		scrollPaneHistory = new JScrollPane();
		scrollPaneHistory.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneHistory.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneHistory.setBounds(12, 12, 570, 426);
		panelHistory.add(scrollPaneHistory);
		
		tableHistory = new JTable();
		tableHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableHistory.setShowVerticalLines(false);
		tableHistory.setFillsViewportHeight(true);
		tableHistory.setModel(new DefaultTableModel(
			new Object[][] {
				{"March 28, 2014, 7:37 PM CST", "Found 100 duplicate files (in 35 groups)", "D:\\TV and Movies, D:\\Music, D:\\FlashGet", "March 28, 2014, 7:56 PM CST", "15 minutes, 30 seconds"},
			},
			new String[] {
				"Start Time", "Results", "Target Folders", "Finish Time", "Elapsed Time"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableHistory.getColumnModel().getColumn(0).setPreferredWidth(179);
		tableHistory.getColumnModel().getColumn(0).setMinWidth(179);
		tableHistory.getColumnModel().getColumn(1).setPreferredWidth(228);
		tableHistory.getColumnModel().getColumn(1).setMinWidth(228);
		tableHistory.getColumnModel().getColumn(2).setPreferredWidth(93);
		tableHistory.getColumnModel().getColumn(2).setMinWidth(93);
		tableHistory.getColumnModel().getColumn(3).setPreferredWidth(168);
		tableHistory.getColumnModel().getColumn(3).setMinWidth(75);
		tableHistory.getColumnModel().getColumn(4).setPreferredWidth(107);
		tableHistory.getColumnModel().getColumn(4).setMinWidth(75);
		scrollPaneHistory.setViewportView(tableHistory);
		
		setMenuButtonColors(0);		
	}
	
	private void defineBehavior () {

		//Stop the application when the main window close button is pressed
		setCloseButtonBehavior(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				System.exit(0);
			}
			
		}); 

		buttonHelp.setActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				setMenuButtonColors(0);
			}
			
		});
		
		buttonScan.setActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				setMenuButtonColors(1);
			}
			
		});
		
		buttonHistory.setActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				setMenuButtonColors(2);
			}
			
		});
		
		buttonStart.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {
				
		
				if (comboBoxProfile.getSelectedIndex() > -1) {
					new ProgressWindow(comboBoxProfile.getItemAt(comboBoxProfile.getSelectedIndex()).getProfile());
				}
				
			}
			
		});
		
		comboBoxProfile.addActionListener(new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent e) {
				
				if (e.getActionCommand().equals("comboBoxChanged")) {
					
					populateProfileDetails();
				}
				
			}
			
		});
		
		buttonEdit.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {
				
				editProfile();			
			}
			
		});
		
		buttonAdd.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {

				addProfile();
			}
			
		});
		
	}

	
	private void setMenuButtonColors (int selected) {
		
		((CardLayout)panelDeck.getLayout()).show(panelDeck, panelID[selected]);
		
		if (selected == 0) {
			buttonHelp.setSelected(true);
			buttonScan.setSelected(false);
			buttonHistory.setSelected(false);
			
		} else if (selected == 1) {

			buttonHelp.setSelected(false);
			buttonScan.setSelected(true);
			buttonHistory.setSelected(false);
			populateComboBoxProfiles();			
			
		} else if (selected == 2) {
			buttonHelp.setSelected(false);
			buttonScan.setSelected(false);
			buttonHistory.setSelected(true);
		}
	}
	
	/*
	 * Loads the detection scan profiles from the resources/profiles folder
	 */
	public void populateComboBoxProfiles () {
		
		ContentIndex sourceIndex = new ContentIndex("profiles/");
		ContentIndex profileIndex = (new NameContainsFilter(".dfscan.profile.xml", true)).enforce(sourceIndex);
		Profile profile;
		
		comboBoxProfile.setModel(new DefaultComboBoxModel<ProfileEntry>() );
		
		for (HashableFile file : profileIndex) {

			try {
				profile = Profile.load(file.getPath());
				((DefaultComboBoxModel<ProfileEntry>)comboBoxProfile.getModel()).addElement(new ProfileEntry(profile));	

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
	
	private void populateProfileDetails () {
		
		if (comboBoxProfile.getSelectedIndex() > -1) {
			
			textArea.setText("");
			textArea.append(comboBoxProfile.getModel().getElementAt(
					comboBoxProfile.getSelectedIndex()).getProfile().getDetailedDescription());
		}
		
	}
	
	private void editProfile () {
		
		
		
		if (comboBoxProfile.getSelectedIndex() > -1) {
			ProfileEditorWindow editor = new ProfileEditorWindow(this, comboBoxProfile.getItemAt(comboBoxProfile.getSelectedIndex()).getProfile());
						
		}
	}
	
	private void addProfile () {
		
		ProfileEditorWindow editor = new ProfileEditorWindow(this);
		
	}
}
