package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
import com.doetsch.dfscan.window.*;
import com.doetsch.oxide.OxideComponentFactory;
import com.doetsch.oxide.OxideDefaultSkin;
import com.doetsch.oxide.OxideFrame;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.UIManager;

public class FilterBuilderWindow extends OxideFrame {

	private JPanel contentPane;
	private JComboBox<String> comboBoxFilterType;
	private JTextField textFieldFilterValue;
	private JButton buttonAdd;
	private JLabel labelFilterValue;
	private JRadioButton radioButtonFilterInclusively;
	private JRadioButton radioButtonFilterExclusively;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private ContentIndexFilter newFilter;
	private JButton buttonClose;
	private DefaultListModel<FilterListEntry> listModelFilters;
	private Component parentFrame;
	
	/**
	 * Create the frame.
	 */
	public FilterBuilderWindow (Component mainAppWindow2, DefaultListModel<FilterListEntry> listModel) {
		super(false, new OxideDefaultSkin());
		
		initComponents();
		setBehavior();
		
		this.parentFrame = mainAppWindow2;
		this.listModelFilters = listModel;
		newFilter = null;
		mainAppWindow2.setEnabled(false);
	}

	private void initComponents () {
		setTitle("Build filter...");
		setBounds(100, 100, 480, 156);
		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());

		centerInViewport();

		setVisible(true);
		contentPane = this.getContentPane();
		
		OxideComponentFactory oxideComponentFactory = new OxideComponentFactory(getOxideSkin());
		
		comboBoxFilterType = oxideComponentFactory.createComboBox();
		comboBoxFilterType.setModel(
				new DefaultComboBoxModel<String>(new String[] {
						"File name contains ...", "File path contains ...",
						"File size is less than or equal to ...",
						"File size is greater than or equal to ...", 
						"File is hidden", "File is read-only"}));
		comboBoxFilterType.setBounds(12, 12, 456, 24);
		contentPane.add(comboBoxFilterType);
		
		textFieldFilterValue = oxideComponentFactory.createTextField();
		textFieldFilterValue.setBounds(156, 48, 312, 24);
		contentPane.add(textFieldFilterValue);
		textFieldFilterValue.setColumns(10);
		
		buttonAdd = oxideComponentFactory.createButton();
		buttonAdd.setText("Add");
		buttonAdd.setBounds(12, 120, 222, 24);
		contentPane.add(buttonAdd);
		
		labelFilterValue = oxideComponentFactory.createLabel("Qualifying Value:");
		labelFilterValue.setBounds(12, 48, 138, 24);
		contentPane.add(labelFilterValue);
		
		radioButtonFilterInclusively = oxideComponentFactory.createRadioButton();
		radioButtonFilterInclusively.setText("Apply filter inclusively");
		radioButtonFilterInclusively.setSelected(true);
		buttonGroup.add(radioButtonFilterInclusively);
		radioButtonFilterInclusively.setHorizontalAlignment(SwingConstants.CENTER);
		radioButtonFilterInclusively.setBounds(12, 84, 228, 24);
		contentPane.add(radioButtonFilterInclusively);
		
		radioButtonFilterExclusively = oxideComponentFactory.createRadioButton();
		radioButtonFilterExclusively.setText("Apply filter exclusively");
		buttonGroup.add(radioButtonFilterExclusively);
		radioButtonFilterExclusively.setHorizontalAlignment(SwingConstants.CENTER);
		radioButtonFilterExclusively.setBounds(240, 84, 228, 24);
		contentPane.add(radioButtonFilterExclusively);
		
		ButtonGroup radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(radioButtonFilterExclusively);
		radioButtonGroup.add(radioButtonFilterInclusively);
		
		buttonClose = oxideComponentFactory.createButton();
		buttonClose.setText("Close");
		buttonClose.setBounds(246, 120, 222, 24);
		getContentPane().add(buttonClose);
		
	}
	
	private void setBehavior () {
		
		setCloseButtonBehavior(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent e) {

				parentFrame.setEnabled(true);
				dispose();
			}
			
		});
		
		buttonAdd.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				saveFilter();
			}
			
		});
		
		buttonClose.addActionListener(getCloseButtonBehavior());
		
	}
	
	private void saveFilter () {
		
		int filterType = comboBoxFilterType.getSelectedIndex();
		String filterValue = textFieldFilterValue.getText();		
		boolean isInclusive = radioButtonFilterInclusively.isSelected();
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
		
		getCloseButtonBehavior().actionPerformed(null);
	}

	public ContentIndexFilter getNewFilter () {
		return newFilter;
	}
}
