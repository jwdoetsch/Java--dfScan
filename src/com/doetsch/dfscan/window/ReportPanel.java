package com.doetsch.dfscan.window;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLayeredPane;
import java.awt.CardLayout;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.DefaultComboBoxModel;
import java.awt.Dimension;

/**
 * 
 */
public class ReportPanel extends JPanel {
	
	private Box verticalBox;
	private Box horizontalBox_4;
	private Box horizontalBox_5;
	private JScrollPane scrollPane_2;
	private JTable table_1;
	private JButton btnNewButton_4;
	private JButton btnNewButton_5;
	private JComboBox comboBox_3;
	private JComboBox comboBox_4;
	private Component horizontalGlue_4;
	private JButton btnNewButton;

	/**
	 * Create the panel.
	 */
	public ReportPanel () {

		initComponents();
	}
	private void initComponents() {
		setBorder(new EmptyBorder(6, 6, 6, 6));
		
		setLayout(new BorderLayout(0, 0));
		
		verticalBox = Box.createVerticalBox();
		add(verticalBox, BorderLayout.NORTH);
		
		horizontalBox_4 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_4);
		
		horizontalBox_5 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_5);
		
		comboBox_3 = new JComboBox();
		comboBox_3.setMaximumSize(new Dimension(128, 32767));
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"Select Defaults", "Select All", "Select None"}));
		horizontalBox_5.add(comboBox_3);
		
		btnNewButton_4 = new JButton("Delete Selected");
		horizontalBox_5.add(btnNewButton_4);
		
		btnNewButton_5 = new JButton("Move Selected");
		horizontalBox_5.add(btnNewButton_5);
		
		horizontalGlue_4 = Box.createHorizontalGlue();
		horizontalBox_5.add(horizontalGlue_4);
		
		btnNewButton = new JButton("Refresh Report");
		horizontalBox_5.add(btnNewButton);
		
		comboBox_4 = new JComboBox();
		comboBox_4.setModel(new DefaultComboBoxModel(new String[] {"Sort By..."}));
		horizontalBox_5.add(comboBox_4);
		
		scrollPane_2 = new JScrollPane();
		add(scrollPane_2, BorderLayout.CENTER);
		
		table_1 = new JTable();
		table_1.setFillsViewportHeight(true);
		scrollPane_2.setViewportView(table_1);
	}

}
