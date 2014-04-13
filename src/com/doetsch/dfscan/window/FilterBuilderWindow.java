package com.doetsch.dfscan.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

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
import javax.swing.Box;

import java.awt.Window.Type;
import java.awt.Dimension;

public class FilterBuilderWindow extends JFrame {

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
	private Box verticalBox;
	private Box horizontalBox;
	private Box horizontalBox_1;
	private Box horizontalBox_2;
	private Box horizontalBox_3;
	private Component rigidArea;
	private Component rigidArea_1;
	
	/**
	 * Create the frame.
	 */
	public FilterBuilderWindow (Component parentFrame, DefaultListModel<FilterListEntry> listModel) {

		this.parentFrame = parentFrame;		
		this.listModelFilters = listModel;
		newFilter = null;

		initComponents();
		setBehavior();
		
		parentFrame.setEnabled(false);
	}

	private void initComponents () {
		setTitle("Build filter...");
		setBounds(100, 100, 498, 160);
		setLocationRelativeTo(parentFrame);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());

		//centerInViewport();

		setVisible(true);
		
		ButtonGroup radioButtonGroup = new ButtonGroup();
		
		verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new EmptyBorder(6, 6, 6, 6));
		getContentPane().add(verticalBox, BorderLayout.NORTH);
		
		horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBorder(new EmptyBorder(0, 0, 6, 0));
		verticalBox.add(horizontalBox);
		
		
		comboBoxFilterType = new JComboBox<String>();
		horizontalBox.add(comboBoxFilterType);
		comboBoxFilterType.setModel(
				new DefaultComboBoxModel<String>(new String[] {
						"File name contains ...", "File path contains ...",
						"File size is less than or equal to ...",
						"File size is greater than or equal to ...", 
						"File is hidden", "File is read-only"}));
		comboBoxFilterType.setBounds(12, 12, 456, 24);
		
		horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setBorder(new EmptyBorder(0, 0, 6, 0));
		verticalBox.add(horizontalBox_1);
		
		labelFilterValue = new JLabel("Qualifying Value: ");
		horizontalBox_1.add(labelFilterValue);
		labelFilterValue.setBounds(12, 48, 138, 24);
		
		textFieldFilterValue = new JTextField();
		horizontalBox_1.add(textFieldFilterValue);
		textFieldFilterValue.setBounds(156, 48, 312, 24);
		textFieldFilterValue.setColumns(10);
		
		horizontalBox_2 = Box.createHorizontalBox();
		horizontalBox_2.setBorder(new EmptyBorder(0, 0, 6, 0));
		verticalBox.add(horizontalBox_2);
		
		radioButtonFilterInclusively = new JRadioButton();
		horizontalBox_2.add(radioButtonFilterInclusively);
		radioButtonFilterInclusively.setText("Apply filter inclusively");
		radioButtonFilterInclusively.setSelected(true);
		buttonGroup.add(radioButtonFilterInclusively);
		radioButtonFilterInclusively.setHorizontalAlignment(SwingConstants.CENTER);
		radioButtonFilterInclusively.setBounds(12, 84, 228, 24);
		radioButtonGroup.add(radioButtonFilterInclusively);
		
		rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_2.add(rigidArea_1);
		
		radioButtonFilterExclusively = new JRadioButton();
		horizontalBox_2.add(radioButtonFilterExclusively);
		radioButtonFilterExclusively.setText("Apply filter exclusively");
		buttonGroup.add(radioButtonFilterExclusively);
		radioButtonFilterExclusively.setHorizontalAlignment(SwingConstants.CENTER);
		radioButtonFilterExclusively.setBounds(240, 84, 228, 24);
		radioButtonGroup.add(radioButtonFilterExclusively);
		
		horizontalBox_3 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_3);
		
		buttonAdd = new JButton("Add");
		horizontalBox_3.add(buttonAdd);
		buttonAdd.setBounds(12, 120, 222, 24);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox_3.add(rigidArea);
		
		buttonClose = new JButton("Close");
		horizontalBox_3.add(buttonClose);
		buttonClose.setBounds(246, 120, 222, 24);
		
	}
	
	private void setBehavior () {
		
//		setCloseButtonBehavior(new AbstractAction() {
//
//			@Override
//			public void actionPerformed (ActionEvent e) {
//
//				parentFrame.setEnabled(true);
//				dispose();
//			}
//			
//		});
//		
		
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated (WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed (WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing (WindowEvent arg0) {
				closeAndUnlock();
			}

			@Override
			public void windowDeactivated (WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified (WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified (WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened (WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		buttonAdd.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				saveFilter();
			}
			
		});
		
		buttonClose.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed (ActionEvent arg0) {
				closeAndUnlock();
			}
			
		});
		
	}
	
	private void closeAndUnlock () {
		parentFrame.setEnabled(true);
		dispose();
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
		
	}

	public ContentIndexFilter getNewFilter () {
		return newFilter;
	}
}
