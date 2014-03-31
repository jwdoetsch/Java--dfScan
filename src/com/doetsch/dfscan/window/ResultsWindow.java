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
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.Report;
import com.doetsch.dfscan.util.ContentIndex;
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

public class ResultsWindow extends OxideFrame {

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
	
	private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPaneDuplicateFiles;
	private Report resultsReport;
	private JButton buttonSelectAll;
	private JButton buttonSelectNone;
	private JButton buttonHandleSelectedFiles;
	private JComboBox<String> comboBoxSortBy;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JPanel panel;
	private JLabel lblStatusBar;

	/**
	 * Create the frame.
	 */
	public ResultsWindow(Report resultsReport) {

		super(false, new OxideDefaultSkin());
		
		this.resultsReport = resultsReport;
		
		initComponents();
		setBehavior();
		setDefaultValues();
		
	}

	protected void initComponents() {
		
		setTitle("Results for scan completed on " + resultsReport.getFinishDate()
				+ " at " + resultsReport.getFinishTime());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());

		setBounds(100, 100, 864, 552);
		
		centerInViewport();
		contentPane = this.getContentPane();
		
		OxideComponentFactory oxideComponentFactory = new OxideComponentFactory(getOxideSkin());
		
		scrollPaneDuplicateFiles = new JScrollPane();
		scrollPaneDuplicateFiles.setBounds(12, 126, 840, 354);
		contentPane.add(scrollPaneDuplicateFiles);
		

		buttonSelectAll = oxideComponentFactory.createButton();
		buttonSelectAll.setText("Select All");
		buttonSelectAll.setBounds(12, 60, 114, 24);
		contentPane.add(buttonSelectAll);
		

		buttonSelectNone = oxideComponentFactory.createButton();
		buttonSelectNone.setText("Select None");
		buttonSelectNone.setBounds(138, 60, 132, 24);
		contentPane.add(buttonSelectNone);
		
		buttonHandleSelectedFiles = oxideComponentFactory.createButton();
		buttonHandleSelectedFiles.setText("Handle Selected Files");
		buttonHandleSelectedFiles.setBounds(12, 492, 840, 24);
		contentPane.add(buttonHandleSelectedFiles);
		

		comboBoxSortBy = oxideComponentFactory.createComboBox();
		comboBoxSortBy.setEnabled(false);
		comboBoxSortBy.setModel(new DefaultComboBoxModel<String>(new String[] {"Sort By...", "Size (Ascending)", "Size (Descending)", "Name (Ascending)", "Name (Descending)"}));
		comboBoxSortBy.setBounds(282, 60, 570, 24);
		contentPane.add(comboBoxSortBy);
		
		label = oxideComponentFactory.createLabel("");
		label.setOpaque(true);
		label.setBackground(getOxideSkin().getDecorationBorderColor());
		label.setBounds(0, 42, 864, 6);
		getContentPane().add(label);
		
		label_1 = oxideComponentFactory.createLabel("");
		label_1.setBounds(12, 12, 846, 18);
		label_1.setFont(new Font("Arial", Font.PLAIN, 15));
		getContentPane().add(label_1);
		label_1.setText("Scan started by " + resultsReport.getUser()
				+ " on host " + resultsReport.getHost()
				+ " at " + resultsReport.getStartTime()
				+ " on " + resultsReport.getStartDate());
		
		label_2 = oxideComponentFactory.createLabel("");
		label_2.setBounds(12, 96, 840, 18);
		label_2.setFont(new Font("Arial", Font.PLAIN, 15));
		getContentPane().add(label_2);
		
		int fileCount = 0;
		for (ContentIndex index : resultsReport.getGroups()) {
			for (HashableFile file : index) {
				fileCount++;
			}
		}
		
		label_2.setText("Found " + fileCount + " duplicate files in "
				+ resultsReport.getGroups().size() + " common groups");
		
		panel = new JPanel();
		panel.setBackground(new Color(216, 216, 216));
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(0, 528, 864, 24);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblStatusBar = oxideComponentFactory.createLabel("");
		lblStatusBar.setBounds(0, 0, 864, 24);
		panel.add(lblStatusBar);
		
		
		
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
			lblStatusBar.setText("Copied selected path to clipboard: " + path);		
		}
	}
	
	private void setBehavior () {

		buttonSelectAll.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {

				tableSelect(true);
			}
			
		});
		
		buttonSelectNone.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {

				tableSelect(false);
			}
			
		});
	}
	
	private void tableSelect (boolean all) {
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		
		for (int row = 0; row < tableModel.getRowCount(); row++) {
			tableModel.setValueAt(all, row, 2);
		}
	}

	private void setDefaultValues () {
		setVisible(true);
		setResizable(false);
		
		int fileCount = 0;
		for (ContentIndex i : resultsReport.getGroups()) {
			for (HashableFile f : i) {
				fileCount++;
			}
		}
		
		
	}
}
