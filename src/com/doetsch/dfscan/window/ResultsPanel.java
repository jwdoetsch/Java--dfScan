package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.Report;
import com.doetsch.dfscan.util.ContentIndex;
import com.doetsch.dfscan.util.FolderChooser;
import com.doetsch.dfscan.util.HashableFile;
import com.doetsch.oxide.OxideComponentFactory;
import com.doetsch.oxide.OxideDefaultSkin;
import com.doetsch.oxide.OxideFrame;

import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.BevelBorder;

import java.awt.FlowLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;

public class ResultsPanel extends TabbedPanel {

	/*
	 * A table cell renderer that defines a cell's background color
	 * by the boolean value of the row's first element; true is 
	 * purple, false is green.
	 */
	class GroupCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus, int row,
	            int column) {

	        super.getTableCellRendererComponent(table,
	                value, isSelected, hasFocus, row, column);
	        
	        if (table.getModel().getValueAt(row, 1) == (Boolean) true) {
	        
		        if (table.getModel().getValueAt(row, 0) == (Boolean)true) {
		            setBackground(new Color(207, 233, 255));
		        } else {
		            setBackground(new Color(255, 237, 216));
		        }
		        
	        } else {
	        	setBackground(new Color(255, 91, 91));
	        }
	        
	        if (isSelected) {
	        	setBackground(new Color(128, 196, 128));
	        }
	        
	        return this;
		}
		
	}
	
	/*
	 * GroupTableBuilder provides a method, build(), which generates and
	 * returns a JTable configured for and filled with the duplicate file
	 * indices contents.
	 */
	private class GroupTableBuilder {
		
		private ArrayList<ContentIndex> indices;
		
		private GroupTableBuilder (ArrayList<ContentIndex> indices) {
			this.indices = indices;
		}
		
		private JTable build () {
			JTable table = new JTable();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.setShowHorizontalLines(true);
			
			/*
			 * Disables dragging of columns by overriding moveColumn.
			 */
			table.setColumnModel(new DefaultTableColumnModel() {
				public void moveColumn (int column, int targetColumn) {
				}
			});
			table.setRowSelectionAllowed(false);
			table.setModel(new GroupTableModel());
			
			((JLabel)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
			
			scrollPaneDuplicateFiles.setViewportView(table);
			
			boolean alternator = true;
			
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			table.setDefaultRenderer(Object.class, new GroupCellRenderer());
			
			/*
			 * Populate the results table
			 */
			for (ContentIndex index : indices) {
				alternator = !alternator;
				for (int i = 0; i < index.getSize(); i++) {
					Object[] rowEntry = {alternator,
							index.getContents().get(i).exists(),
							(i == 0 ? false : true),
							index.getContents().get(i).getName(),
							index.getContents().get(i).getPath(),
							sizeString(index.getContents().get(i).length())};
					
					tableModel.addRow(rowEntry);				
				}
			}
			
			setColumnProperties(table, 0, false, 0);
			setColumnProperties(table, 1, false, 0);
			
			setColumnProperties(table, 2, false, 24);
			setColumnProperties(table, 3, true, 350);
			setColumnProperties(table, 4, true, 650);
			setColumnProperties(table, 5, false, 100);
			
			return table;
		}
		
		private String sizeString (long length) {
			if (length > 1024)  {
				return String.valueOf((int) (length / 1024)) + " KB";
				
			} else {
				return length + " B";
			}
		}

		private void setColumnProperties (JTable table, int index, boolean isResizable, int width) {
			TableColumn column = table.getColumnModel().getColumn(index);
			column.setResizable(isResizable);
			column.setMinWidth(0);
			column.setPreferredWidth(width);
			column.setMaxWidth(1024 * 1024);
			
		}
				
	}
	
	/*
	 * 
	 */
	private class GroupTableModel extends DefaultTableModel {
		
		Class[] columnTypes = new Class[] {
				Boolean.class, Boolean.class, Boolean.class, String.class, String.class, String.class};
		boolean[] columnEditables = new boolean[] {false, false, true, false, false, false};
		
		
		private GroupTableModel() {
			super(new Object[][] {},
				new String[] {"Group Color", "Exists", "", "File Name", "Path", "Size"});
		}
		
		public Class getColumnClass(int columnIndex) {
			return columnTypes[columnIndex];
		}
		
		public boolean isCellEditable(int row, int column) {
			return columnEditables[column];
		}
		
	}
	
	//private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPaneDuplicateFiles;
	private Report resultsReport;
	private JButton moveButton;
	private JComboBox<String> sortingComboBox;
	private JLabel scanDetailsLabel;
	private JLabel scanResultsLabel;
	private JComboBox<String> selectorComboBox;
	private JButton deleteButton;
	private Box handleControlBox;
	private Box headerBox;
	private Box controlBox;
	private Component controlBoxGlue;
	private JTabbedPane parentPane;
	private Component leftGlue;
	private Component rightGlue;
	private JButton saveButton;
	private Component glue;
	
	/**
	 * Create the frame.
	 */
	public ResultsPanel(Report resultsReport, JTabbedPane parentPane) {

		super("Results: " + resultsReport.getProfileName());
		setBorder(new EmptyBorder(6, 6, 6, 6));
		
		this.parentPane = parentPane;
		this.resultsReport = resultsReport;
		
		initComponents();
		setBehavior();
		setDefaultValues();
		
	}

	protected void initComponents() {
		
//		setTitle("Results for scan completed on " + resultsReport.getFinishDate()
//				+ " at " + resultsReport.getFinishTime());
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());

//		super.setTabTitle("Results " + resultsReport.getFinishDate()
//				+ " at " + resultsReport.getFinishTime());
		
		//setBounds(100, 100, 864, 552);
		setLayout(new BorderLayout(0, 0));
		
		parentPane.addTab("", null, this, null);
		parentPane.setTabComponentAt(
				parentPane.indexOfComponent(this), this.getTabAsComponent());
		parentPane.setSelectedComponent(this);
		
		
		scrollPaneDuplicateFiles = new JScrollPane();
		scrollPaneDuplicateFiles.setBounds(12, 126, 840, 354);
		add(scrollPaneDuplicateFiles, BorderLayout.CENTER);
		
		int fileCount = 0;
		for (ContentIndex index : resultsReport.getGroups()) {
			for (HashableFile file : index) {
				fileCount++;
			}
		}
		
		headerBox = Box.createVerticalBox();
		add(headerBox, BorderLayout.NORTH);
		
		scanDetailsLabel = new JLabel("");
		headerBox.add(scanDetailsLabel);
				scanDetailsLabel.setText("Scan started by " + resultsReport.getUser()
				+ " on host " + resultsReport.getHost()
				+ " at " + resultsReport.getStartTime()
				+ " on " + resultsReport.getStartDate());
		
		scanResultsLabel = new JLabel("");
		headerBox.add(scanResultsLabel);
		scanResultsLabel.setText("Found " + fileCount + " duplicate files in "
				+ resultsReport.getGroups().size() + " common groups");
		
		controlBox = Box.createHorizontalBox();
		headerBox.add(controlBox);
		
		selectorComboBox = new JComboBox<String>();
		controlBox.add(selectorComboBox);
		selectorComboBox.setModel(new DefaultComboBoxModel(new String[] {"Default Selection", "Select All Entries", "Select None"}));
		selectorComboBox.setBounds(12, 60, 258, 24);
		
		controlBoxGlue = Box.createGlue();
		controlBox.add(controlBoxGlue);
		

		sortingComboBox = new JComboBox<String>();
		controlBox.add(sortingComboBox);
		sortingComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Sort By...", "Size (Ascending)", "Size (Descending)", "Name (Ascending)", "Name (Descending)"}));
		sortingComboBox.setBounds(282, 60, 570, 24);
		
		glue = Box.createGlue();
		controlBox.add(glue);
		
		saveButton = new JButton("Save Report");
		controlBox.add(saveButton);
		
		handleControlBox = Box.createHorizontalBox();
		add(handleControlBox, BorderLayout.SOUTH);
		
		leftGlue = Box.createHorizontalGlue();
		handleControlBox.add(leftGlue);
		
		deleteButton = new JButton("Delete Selected...");
		handleControlBox.add(deleteButton);
		deleteButton.setBounds(438, 492, 414, 24);
		
		moveButton = new JButton("Move Selected Files...");
		handleControlBox.add(moveButton);
		
		rightGlue = Box.createHorizontalGlue();
		handleControlBox.add(rightGlue);
		
		buildTable();
	}

	private void buildTable () {
		table = (new GroupTableBuilder(resultsReport.getGroups())).build();
		table.addMouseListener(new MouseAdapter() {

			public void mousePressed (MouseEvent e) {
				copyPathToClipboard(e);
			}
			
		});
		
		table.setShowVerticalLines(false);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
	}
	
	private void copyPathToClipboard (MouseEvent e) {
		
		String path;
		Point p = e.getPoint();
		int row = table.rowAtPoint(p);
		int col = table.columnAtPoint(p);

		if ((e.getClickCount() == 2)  &&
				((row > -1) && (col > -1))) {

			path = (String) ((DefaultTableModel)table.getModel()).getValueAt(row, 4);
		
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection clipBoardData = new StringSelection(path);
			clipboard.setContents(clipBoardData, clipBoardData);
			//labelStatusBar.setText("Copied selected path to clipboard: " + path);		
		}
	}
	
	private void setBehavior () {
		
		selectorComboBox.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {

				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				
				switch (selectorComboBox.getSelectedIndex()) {
					case 0:
						boolean groupID = true;
						for (int row = 0; row < tableModel.getRowCount(); row++) {
							if (groupID != (boolean) tableModel.getValueAt(row, 0)) {
								tableModel.setValueAt(false, row, 2);
								groupID = !groupID;
								
							} else {
								tableModel.setValueAt(true, row, 2);
							}
						}
						
						break;
						
					case 1:
						for (int row = 0; row < tableModel.getRowCount(); row++) {
							tableModel.setValueAt(true, row, 2);
						}						
						break;
						
					case 2:
						for (int row = 0; row < tableModel.getRowCount(); row++) {
							tableModel.setValueAt(false, row, 2);
						}
						break;
				}
			}
			
		});
		
		moveButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {

				File destinationPath = null;
				ArrayList<File> pathList = getSelectedEntries();
				FolderChooser folderChooser = new FolderChooser(ResultsPanel.this,
						new File(System.getProperty("user.home")),
								"Select destination folder...");
				
				if (folderChooser.getFolder()) {
					destinationPath = folderChooser.getSelectedFile();
				}
				
				if (destinationPath == null) {
					return;
				}
				
				for (File path : pathList) {

					try {
						Files.move(FileSystems.getDefault().getPath(path.getPath()),
								FileSystems.getDefault().getPath(destinationPath.getPath() + "/" + path.getName()));
						//labelStatusBar.setText("Moved " + path.getPath() + ".");

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				buildTable();
				
			}
			
		});
		
		deleteButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {
				File destinationPath = null;
				ArrayList<File> pathList = getSelectedEntries();
								
				for (File path : pathList) {
					
					
					try {
						Files.deleteIfExists(FileSystems.getDefault().getPath(path.getPath()));
						//labelStatusBar.setText("Deleted " + path.getPath() + ".");

						
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
				
				buildTable();
				
			}
			
		});
		
	}

	private ArrayList<File> getSelectedEntries () {
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		ArrayList<File> pathList = new ArrayList<File>();
		
		for (int row = 0; row < tableModel.getRowCount(); row++) {
			
			if (((boolean) tableModel.getValueAt(row, 2)) &&
				((boolean) tableModel.getValueAt(row, 1))) {
				
				pathList.add(new File((String) tableModel.getValueAt(row, 4)));
			}
		}
		
		return pathList;
	}
	
	private void setDefaultValues () {
//		setVisible(true);
		
		int fileCount = 0;
		for (ContentIndex i : resultsReport.getGroups()) {
			for (HashableFile f : i) {
				fileCount++;
			}
		}
		
		
	}

	@Override
	public void tabCloseButtonAction () {
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
