package com.doetsch.dfscan.window;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.UIManager;
import javax.swing.JTextArea;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JLabel;

import java.awt.CardLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.Profile;
import com.doetsch.dfscan.core.Report;
import com.doetsch.dfscan.filter.NameContainsFilter;
import com.doetsch.dfscan.util.ContentIndex;
import com.doetsch.dfscan.util.HashableFile;
import com.doetsch.oxide.*;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.JEditorPane;

import java.awt.Cursor;

import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

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
	private ArrayList<Report> reportsHistory;
	
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
	private JLabel labelReportCount;

	/**
	 * Create the frame.
	 */
	public MainWindow () {
		super(true, new OxideDefaultSkin());
	
		initComponents();
		defineBehavior();
	}
	
	private void initComponents() {
		setTitle("dfScan v0.01");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 450);
		centerInViewport();
		//super.centerInViewport();
		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());
		
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
		labelMenuBackground.setVisible(false);
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
		
		//textArea = new JTextArea();
		textArea = oxideComponentFactory.createTextArea();
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
		
		labelReportCount = oxideComponentFactory.createLabel("");
		labelReportCount.setBounds(12, 12, 570, 18);
		panelHistory.add(labelReportCount);
		
		scrollPaneHistory = new JScrollPane();
		scrollPaneHistory.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneHistory.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneHistory.setBounds(12, 42, 570, 396);
		panelHistory.add(scrollPaneHistory);
		
		tableHistory = new JTable() {};
		tableHistory.setDoubleBuffered(true);
		tableHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableHistory.setShowVerticalLines(false);
		tableHistory.setFillsViewportHeight(true);
		populateHistoryTable();
		
		scrollPaneHistory.setViewportView(tableHistory);
		
	

		
		tableHistory.addMouseListener(new MouseAdapter() {

			public void mousePressed (MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point p = e.getPoint();
				int row = table.rowAtPoint(p);
				int col = table.columnAtPoint(p);
				if ((row > -1) && (col > -1)) {
					if (e.getClickCount() == 2) {
						
						new ResultsWindow(reportsHistory.get(row));
						
					}
				}
			}
			
		});
		
		
		setMenuButtonColors(0);		
	}
	

	private void populateHistoryTable () {
		
		
		
		ContentIndex sourceIndex = new ContentIndex("results/");
		ContentIndex reportIndex = (new NameContainsFilter(".dfscan.report.xml", true)).enforce(sourceIndex);
		int reportCount = 0;
		
		reportsHistory = new ArrayList<Report>();

		tableHistory.setColumnModel(new DefaultTableColumnModel() {
			public void moveColumn (int column, int targetColumn) {
			}
		});
		
		/*
		 * Build the table model
		 */
		DefaultTableModel tableModel = new DefaultTableModel( new Object[][] {}, new String[] {
				"User", " Host", "Started On", "Results", "Finished On", "Elapsed Time"}) {
			
			boolean[] columnEditables =
					new boolean[] {false, false, false, false, false, false};
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		tableHistory.setModel(tableModel);
		

		
		/*
		 * Set column sizes
		 */
		//int[] columnWidths = {96, 96, 192, 192, 192, 192};
		int[][] columnWidths = {{96, 96, 192, 192, 192, 192},
								{64, 64, 64, 64, 64, 64}};
		
		for (int i = 0; i < 6; i++) {
			tableHistory.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[0][i]);
			tableHistory.getColumnModel().getColumn(i).setMinWidth(columnWidths[1][i]);
		}
		

	

		/*
		 * Iterate through the report index. Load the Profile at the current
		 * file path and add the profile's details to the history table. 
		 */
		for (HashableFile file : reportIndex) {
			
			if (file.canRead()) {
				reportsHistory.add(Report.load(file));
				reportCount++;
			}
		}
		
		labelReportCount.setText("Found " + reportCount + " reports.");
		
		/*
		 * Populate the table with report data		
		 */
		for (Report report : reportsHistory) {
		
			int fileCount = 0;
			
			for (ContentIndex groupIndex : report.getGroups()) {
				fileCount += groupIndex.getSize();
			}
			
			String results = "" + fileCount + " files ("
					+ report.getGroups().size() + " groups)";
			
			tableModel.addRow(new Object[] {
				report.getUser(),
				report.getHost(),
				report.getStartDate() + "    " + report.getStartTime(),
				results,
				report.getFinishDate() + "    " + report.getFinishTime(),
				report.getDetectionTime()				
			});

		}
				
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
			populateHistoryTable();
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
