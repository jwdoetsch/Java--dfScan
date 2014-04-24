package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;


import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.filter.ContentIndexFilter;
import com.doetsch.dfscan.filter.HiddenFileFilter;
import com.doetsch.dfscan.filter.NameContainsFilter;
import com.doetsch.dfscan.filter.PathContainsFilter;
import com.doetsch.dfscan.filter.ReadOnlyFileFilter;
import com.doetsch.dfscan.filter.SizeCeilingFilter;
import com.doetsch.dfscan.filter.SizeFloorFilter;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.Box;

import java.awt.Dimension;

public class FilterBuilderDialog extends JDialog {

	private JComboBox<String> ruleComboBox;
	private JTextField valueTextField;
	private JButton addButton;
	private JLabel valueLabel;
	private JRadioButton inclusiveRadioButton;
	private JRadioButton exclusiveRadioButton;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private ContentIndexFilter newFilter;
	private JButton closeButton;
	private DefaultListModel<FilterListEntry> listModelFilters;
	private Component parentFrame;
	private Box verticalBox;
	private Box ruleBox;
	private Box qualifyingValueBox;
	private Box applyBox;
	private Box dialogControlBox;
	private Component dialogControlGlue;
	private Component applyBoxGlue;
	
	/**
	 * Create the frame.
	 */
	public FilterBuilderDialog (JFrame parentFrame, DefaultListModel<FilterListEntry> listModel) {
		super(parentFrame, "Add Filters", ModalityType.MODELESS);
		this.parentFrame = parentFrame;		
		this.listModelFilters = listModel;
		newFilter = null;

		initComponents();
		setBehavior();
	}

	private void initComponents () {
		setBounds(100, 100, 498, 160);
		setLocationRelativeTo(parentFrame);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan3.png"))).getImage());
		setVisible(true);
		
		ButtonGroup radioButtonGroup = new ButtonGroup();
		
		verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new EmptyBorder(6, 6, 6, 6));
		getContentPane().add(verticalBox, BorderLayout.CENTER);
		
		ruleBox = Box.createHorizontalBox();
		ruleBox.setBorder(new EmptyBorder(0, 0, 6, 0));
		verticalBox.add(ruleBox);
		
		
		ruleComboBox = new JComboBox<String>();
		ruleBox.add(ruleComboBox);
		ruleComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] {
						"File name contains ...", "File path contains ...",
						"File size is less than or equal to ...",
						"File size is greater than or equal to ...", 
						"File is hidden", "File is read-only"}));
		ruleComboBox.setBounds(12, 12, 456, 24);
		
		qualifyingValueBox = Box.createHorizontalBox();
		qualifyingValueBox.setBorder(new EmptyBorder(0, 0, 6, 0));
		verticalBox.add(qualifyingValueBox);
		
		valueLabel = new JLabel("Qualifying Value: ");
		qualifyingValueBox.add(valueLabel);
		valueLabel.setBounds(12, 48, 138, 24);
		
		valueTextField = new JTextField();
		qualifyingValueBox.add(valueTextField);
		valueTextField.setBounds(156, 48, 312, 24);
		valueTextField.setColumns(10);
		
		applyBox = Box.createHorizontalBox();
		applyBox.setBorder(new EmptyBorder(0, 0, 6, 0));
		verticalBox.add(applyBox);
		
		inclusiveRadioButton = new JRadioButton();
		applyBox.add(inclusiveRadioButton);
		inclusiveRadioButton.setText("Apply filter inclusively");
		inclusiveRadioButton.setSelected(true);
		buttonGroup.add(inclusiveRadioButton);
		inclusiveRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		inclusiveRadioButton.setBounds(12, 84, 228, 24);
		radioButtonGroup.add(inclusiveRadioButton);
		
		applyBoxGlue = Box.createRigidArea(new Dimension(20, 20));
		applyBox.add(applyBoxGlue);
		
		exclusiveRadioButton = new JRadioButton();
		applyBox.add(exclusiveRadioButton);
		exclusiveRadioButton.setText("Apply filter exclusively");
		buttonGroup.add(exclusiveRadioButton);
		exclusiveRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		exclusiveRadioButton.setBounds(240, 84, 228, 24);
		radioButtonGroup.add(exclusiveRadioButton);
		
		dialogControlBox = Box.createHorizontalBox();
		verticalBox.add(dialogControlBox);
		
		addButton = new JButton("Add");
		dialogControlBox.add(addButton);
		addButton.setBounds(12, 120, 222, 24);
		
		dialogControlGlue = Box.createRigidArea(new Dimension(20, 20));
		dialogControlBox.add(dialogControlGlue);
		
		closeButton = new JButton("Close");
		dialogControlBox.add(closeButton);
		closeButton.setBounds(246, 120, 222, 24);
		//this.setModalityType(ModalityType.APPLICATION_MODAL);
	}
	
	private void setBehavior () {
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing (WindowEvent arg0) {
				closeDialog();
			}
		});
		
		addButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				saveFilter();
			}
			
		});
		
		closeButton.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				closeDialog();
			}
			
		});
		
	}
	
	private void closeDialog () {
		dispose();
	}
	
	private void saveFilter () {
		
		int filterType = ruleComboBox.getSelectedIndex();
		String filterValue = valueTextField.getText();		
		boolean isInclusive = inclusiveRadioButton.isSelected();
		long size = 0;
		
		if ((filterType < 4) && (filterValue.equals(""))) {
			JOptionPane.showMessageDialog(this, "The selected filter type requires a qualifying value.",
					"Hey, listen...", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		if ((filterType == 2) || (filterType == 3)) {
			
			try {
				
				size = Long.valueOf(filterValue);
				if (size < 0) {
					throw new NumberFormatException();
				}
				
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this,
						"A file size filter type requires a positive number as it's qualifying value.",
						"Hey, listen...", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		
		
		switch (filterType) {
			case 0:
				newFilter = new NameContainsFilter(filterValue, isInclusive);
				break;
				
			case 1:
				newFilter = new PathContainsFilter(filterValue, isInclusive);
				break;
				
			case 2:
				newFilter = new SizeCeilingFilter(size, isInclusive);
				break;
				
			case 3:
				newFilter = new SizeFloorFilter(size, isInclusive);
				break;
				
			case 4:
				newFilter = new HiddenFileFilter(isInclusive);
				break;
				
			case 5:
				newFilter = new ReadOnlyFileFilter(isInclusive);
				break;
		}
				
		
		listModelFilters.addElement(new FilterListEntry(newFilter));
		
	}

	public ContentIndexFilter getNewFilter () {
		return newFilter;
	}
}
