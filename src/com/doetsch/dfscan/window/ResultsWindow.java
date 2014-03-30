package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
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
import javax.swing.ImageIcon;
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
	private JPanel oxideTitledPanel;
	private JLabel labelDetailsTop;
	private JLabel labelDetailsBottom;
	private JLabel labelDetailsStartTime;
	private JLabel labelDetailsFinishTime;

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
		
		setTitle("Results - " + resultsReport.getStartDate() + " at " + resultsReport.getStartTime());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());

		setBounds(100, 100, 864, 552);
		
		centerInViewport();
		contentPane = this.getContentPane();
		
		OxideComponentFactory oxideComponentFactory = new OxideComponentFactory(getOxideSkin());
		
		scrollPaneDuplicateFiles = new JScrollPane();
		scrollPaneDuplicateFiles.setBounds(12, 186, 840, 318);
		contentPane.add(scrollPaneDuplicateFiles);
		

		buttonSelectAll = oxideComponentFactory.createButton();
		buttonSelectAll.setText("Select All");
		buttonSelectAll.setBounds(12, 150, 114, 24);
		contentPane.add(buttonSelectAll);
		

		buttonSelectNone = oxideComponentFactory.createButton();
		buttonSelectNone.setText("Select None");
		buttonSelectNone.setBounds(138, 150, 132, 24);
		contentPane.add(buttonSelectNone);
		
		buttonHandleSelectedFiles = oxideComponentFactory.createButton();
		buttonHandleSelectedFiles.setText("Handle Selected Files");
		buttonHandleSelectedFiles.setBounds(12, 516, 840, 24);
		contentPane.add(buttonHandleSelectedFiles);
		

		comboBoxSortBy = oxideComponentFactory.createComboBox();
		comboBoxSortBy.setEnabled(false);
		comboBoxSortBy.setModel(new DefaultComboBoxModel<String>(new String[] {"Sort By...", "Size (Ascending)", "Size (Descending)", "Name (Ascending)", "Name (Descending)"}));
		comboBoxSortBy.setBounds(282, 150, 570, 24);
		contentPane.add(comboBoxSortBy);
		
		oxideTitledPanel = oxideComponentFactory.createTitledPanel("Details");
		oxideTitledPanel.setBounds(12, 12, 840, 126);
		getContentPane().add(oxideTitledPanel);
		oxideTitledPanel.setLayout(null);
		
		labelDetailsTop = oxideComponentFactory.createLabel("");
		labelDetailsTop.setText("");
		labelDetailsTop.setBounds(12, 24, 528, 18);
		oxideTitledPanel.add(labelDetailsTop);
		
		labelDetailsStartTime = oxideComponentFactory.createLabel("");
		labelDetailsStartTime.setText("");
		labelDetailsStartTime.setBounds(12, 48, 528, 18);
		oxideTitledPanel.add(labelDetailsStartTime);
		
		labelDetailsBottom = oxideComponentFactory.createLabel("");
		labelDetailsBottom.setText("");
		labelDetailsBottom.setBounds(12, 96, 528, 18);
		oxideTitledPanel.add(labelDetailsBottom);
		
		labelDetailsFinishTime = oxideComponentFactory.createLabel("");
		labelDetailsFinishTime.setText("");
		labelDetailsFinishTime.setBounds(12, 72, 528, 18);
		oxideTitledPanel.add(labelDetailsFinishTime);
		
		
		
		table = (new GroupTableBuilder(resultsReport.getGroups())).build();

	}

	private void setBehavior () {
		// TODO Auto-generated method stub
	}

	private void setDefaultValues () {
		setVisible(true);
		setResizable(false);
		
		labelDetailsTop.setText("Report generated by " + resultsReport.getUser()
				+ " on " + resultsReport.getHost());
		
		
		labelDetailsStartTime.setText("Started On: " + resultsReport.getStartDate()
				+ " " + resultsReport.getStartTime());
				
		labelDetailsFinishTime.setText("Finished On: " + resultsReport.getFinishDate()
				+ " " + resultsReport.getFinishTime());
		
		int fileCount = 0;
		for (ContentIndex i : resultsReport.getGroups()) {
			for (HashableFile f : i) {
				fileCount++;
			}
		}
		
		labelDetailsBottom.setText("Found " + resultsReport.getGroups().size()
				+ " groups of duplicate files containing " + fileCount + " files");
		
		
	}
}
