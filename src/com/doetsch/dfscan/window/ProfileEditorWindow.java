package com.doetsch.dfscan.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EtchedBorder;
import javax.swing.DefaultListModel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JTextArea;

import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.core.Profile;
import com.doetsch.dfscan.filter.ContentIndexFilter;
import com.doetsch.dfscan.filter.HiddenFileFilter;
import com.doetsch.dfscan.filter.NameContainsFilter;
import com.doetsch.dfscan.filter.PathContainsFilter;
import com.doetsch.dfscan.filter.ReadOnlyFileFilter;
import com.doetsch.dfscan.filter.SizeCeilingFilter;
import com.doetsch.dfscan.filter.SizeFloorFilter;
import com.doetsch.dfscan.util.FolderChooser;
import com.doetsch.oxide.OxideComponentFactory;
import com.doetsch.oxide.OxideDefaultSkin;
import com.doetsch.oxide.OxideFrame;

import javax.swing.ListSelectionModel;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class ProfileEditorWindow extends OxideFrame {
	
	public class FilterBuiltListener implements ComponentListener {
		
		private ContentIndexFilter filter;
		
		public FilterBuiltListener (FilterBuilderWindow filterBuilder) {
			filterBuilder.setVisible(true);
			filter = filterBuilder.getNewFilter();
			System.out.println("Constructor, filter=" + filter);
		}

		@Override
		public void componentHidden (ComponentEvent e) {
			
			System.out.println("FilterBuilderWindow hidden");
			System.out.println(filter);
			
			if (filter != null) {
				listModelFilters.addElement(new FilterListEntry(filter));
			}
		}

		@Override
		public void componentMoved (ComponentEvent e) {			
		}

		@Override
		public void componentResized (ComponentEvent e) {			
		}

		@Override
		public void componentShown (ComponentEvent e) {			
		}
		
	}
	

	
	
	private JPanel panelScanningOptions;
	private JCheckBox checkBoxIndexInclusively;
	private JCheckBox checkBoxIndexRecursively;
	private JCheckBox checkBoxIndexHiddenFolders;
	private JCheckBox checkBoxIndexReadOnlyFolders;
	private JPanel panelFolders;
	private JPanel panelFilters;
	private JButton buttonSave;
	private JScrollPane scrollPaneFolders;
	private JList<String> listFolders;
	private JButton buttonAddFolder;
	private JButton buttonRemoveFolder;
	private JScrollPane scrollPaneFilters;
	private JButton buttonAddFilter;
	private JButton buttonRemoveFilter;
	private DefaultListModel<FilterListEntry> listModelFilters;
	private DefaultListModel<String> listModelFolders;
	private JList<FilterListEntry> listFilters;
	private JPanel panelName;
	private JPanel panelDescription;
	private JTextField textFieldName;
	private JScrollPane scrollPaneDescription;
	private JTextArea textAreaDescription;
	private JButton buttonMoveUp;
	private JButton buttonMoveDown;
	private OxideFrame parentFrame;


	/**
	 * Create the frame.
	 * @wbp.parser.constructor
	 */
	public ProfileEditorWindow (OxideFrame parentFrame) {
		super(false, new OxideDefaultSkin());

		initComponents();
		setBehavior();
		
		this.parentFrame = parentFrame;
		this.parentFrame.setEnabled(false);
	}
	
	public ProfileEditorWindow (OxideFrame parentFrame, Profile profile) {
		super (false, new OxideDefaultSkin());
		
		initComponents();
		setBehavior();
		
		this.parentFrame = parentFrame;
		this.parentFrame.setEnabled(false);	
		populateProfileFields(profile);
	}
	
	private void populateProfileFields (Profile profile) {
		textFieldName.setText(profile.getName());
		textAreaDescription.setText(profile.getDescription());
		
		checkBoxIndexInclusively.setSelected(profile.getSettings().getIndexInclusively());
		checkBoxIndexRecursively.setSelected(profile.getSettings().getIndexRecursively());
		checkBoxIndexHiddenFolders.setSelected(profile.getSettings().getIndexHiddenFolders());
		checkBoxIndexReadOnlyFolders.setSelected(profile.getSettings().getIndexReadOnlyFolders());
		
		for (String targetPath : profile.getTargets()) {
			DefaultListModel<String> foldersListModel = new DefaultListModel<String>();
			listFolders.setModel(foldersListModel);
			//((DefaultListModel<String>)listFolders.getModel()).addElement(targetPath);
			foldersListModel.addElement(targetPath);
		}
		
		for (ContentIndexFilter filter : profile.getFilters()) {
			((DefaultListModel<FilterListEntry>)listFilters.getModel()).addElement(new FilterListEntry(filter));
		}
		
	}
	
	private void initComponents() {
		
//		setTitle("Results - " + resultsReport.getStartDate() + " at " + resultsReport.getStartTime());
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());
//
//		setBounds(100, 100, 864, 552);
//		
//		centerInViewport();
//		contentPane = this.getContentPane();
		
		setTitle("Build Profile...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage((new ImageIcon(DFScan.class.getResource("resources/icons/dfscan2.png"))).getImage());

		setBounds(100, 100, 696, 498);

		centerInViewport();
		JPanel contentPane = this.getContentPane();
		
		
		OxideComponentFactory oxideComponentFactory = new OxideComponentFactory(getOxideSkin());
		
		panelScanningOptions = oxideComponentFactory.createTitledPanel("Scanning Options");
		panelScanningOptions.setBounds(354, 12, 330, 162);
		contentPane.add(panelScanningOptions);
		panelScanningOptions.setLayout(null);
		
		checkBoxIndexInclusively = oxideComponentFactory.createCheckBox();
		checkBoxIndexInclusively.setText("Scan inclusively");
		checkBoxIndexInclusively.setBounds(6, 40, 312, 18);
		panelScanningOptions.add(checkBoxIndexInclusively);
		
		checkBoxIndexRecursively = oxideComponentFactory.createCheckBox();
		checkBoxIndexRecursively.setText("Scan recursively");
		checkBoxIndexRecursively.setBounds(6, 66, 312, 18);
		panelScanningOptions.add(checkBoxIndexRecursively);
		
		checkBoxIndexHiddenFolders = oxideComponentFactory.createCheckBox();
		checkBoxIndexHiddenFolders.setText("Scan hidden folders");
		checkBoxIndexHiddenFolders.setBounds(6, 90, 312, 18);
		panelScanningOptions.add(checkBoxIndexHiddenFolders);
		
		checkBoxIndexReadOnlyFolders = oxideComponentFactory.createCheckBox();
		checkBoxIndexReadOnlyFolders.setText("Scan read-only folders");
		checkBoxIndexReadOnlyFolders.setBounds(6, 114, 312, 18);
		panelScanningOptions.add(checkBoxIndexReadOnlyFolders);
		
		panelFolders = oxideComponentFactory.createTitledPanel("Target Folders");
		panelFolders.setBounds(12, 180, 330, 270);
		contentPane.add(panelFolders);
		panelFolders.setLayout(null);
		
		scrollPaneFolders = new JScrollPane();
		scrollPaneFolders.setBounds(12, 54, 306, 204);
		panelFolders.add(scrollPaneFolders);
		
		listModelFolders = new DefaultListModel<String>();
		//listFolders = new JList<String>();
		listFolders = oxideComponentFactory.createList();
		listFolders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFolders.setModel(listModelFolders);
		scrollPaneFolders.setViewportView(listFolders);
		
		buttonAddFolder = oxideComponentFactory.createButton();
		buttonAddFolder.setText("Add");
		buttonAddFolder.setBounds(12, 24, 150, 24);
		panelFolders.add(buttonAddFolder);
		
		buttonRemoveFolder = oxideComponentFactory.createButton();
		buttonRemoveFolder.setText("Remove");
		buttonRemoveFolder.setBounds(168, 24, 150, 24);
		panelFolders.add(buttonRemoveFolder);
		
		panelFilters = oxideComponentFactory.createTitledPanel("Detection Filters");
		panelFilters.setBounds(354, 180, 330, 270);
		contentPane.add(panelFilters);
		panelFilters.setLayout(null);
		
		scrollPaneFilters = new JScrollPane();
		scrollPaneFilters.setBounds(12, 54, 306, 174);
		panelFilters.add(scrollPaneFilters);
		
		listModelFilters = new DefaultListModel<FilterListEntry>();
		//listFilters = new JList<FilterListEntry>();
		listFilters = oxideComponentFactory.createList();
		listFilters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFilters.setModel(listModelFilters);
		scrollPaneFilters.setViewportView(listFilters);
		
		buttonAddFilter = oxideComponentFactory.createButton();
		buttonAddFilter.setText("Add");
		buttonAddFilter.setBounds(12, 24, 150, 24);
		panelFilters.add(buttonAddFilter);
		
		buttonRemoveFilter = oxideComponentFactory.createButton();
		buttonRemoveFilter.setText("Remove");
		buttonRemoveFilter.setBounds(168, 24, 150, 24);
		panelFilters.add(buttonRemoveFilter);
		
		buttonMoveUp = oxideComponentFactory.createButton();
		buttonMoveUp.setText("\u2191 Move Up \u2191");
		buttonMoveUp.setBounds(12, 234, 150, 24);
		panelFilters.add(buttonMoveUp);
		
		buttonMoveDown = oxideComponentFactory.createButton();
		buttonMoveDown.setText("\u2193 Move Down \u2193");
		buttonMoveDown.setBounds(168, 234, 150, 24);
		panelFilters.add(buttonMoveDown);
		
		buttonSave = oxideComponentFactory.createButton();
		buttonSave.setText("Save");
		buttonSave.setBounds(12, 462, 672, 24);
		contentPane.add(buttonSave);
		
		panelName = oxideComponentFactory.createTitledPanel("Profile Name");
		panelName.setBounds(12, 12, 330, 54);
		contentPane.add(panelName);
		
		textFieldName = oxideComponentFactory.createTextField();
		textFieldName.setBounds(12, 18, 306, 24);
		panelName.add(textFieldName);
		
		panelDescription = oxideComponentFactory.createTitledPanel("Description");
		panelDescription.setBounds(12, 72, 330, 102);
		contentPane.add(panelDescription);
		
		scrollPaneDescription = new JScrollPane();
		scrollPaneDescription.setBounds(12, 18, 306, 72);
		panelDescription.add(scrollPaneDescription);
		
		textAreaDescription = oxideComponentFactory.createTextArea();
		textAreaDescription.setLineWrap(true);
		scrollPaneDescription.setViewportView(textAreaDescription);
		
		
	}
	
	/**
	 * Defines behavior of UI components.
	 */
	public void setBehavior () {
		
		this.setCloseButtonBehavior(new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent e) {
				parentFrame.setEnabled(true);
				((MainWindow) parentFrame).populateComboBoxProfiles();
				dispose();
			}
			
		});
		
		/*
		 * Defines the behavior of the save button
		 */
		buttonSave.addActionListener(new AbstractAction () {

			@Override
			public void actionPerformed (ActionEvent e) {
				saveProfile();
			}
			
		});
		
		
		/*
		 * Define the behavior of the filter panel components
		 */
		{
		
			buttonAddFilter.addActionListener(new AbstractAction () {
	
				@Override
				public void actionPerformed (ActionEvent e) {
					
					FilterBuilderWindow filterBuilderWindow =
							new FilterBuilderWindow(ProfileEditorWindow.this, listModelFilters);
	
				}
				
			});
			
			buttonRemoveFilter.addActionListener(new AbstractAction () {
	
				/*
				 * Removes the filter list element at the selected index, provided
				 * one is selected and available.
				 */
				@Override
				public void actionPerformed (ActionEvent e) {
					
					int index = listFilters.getSelectedIndex();
					
					if (index > -1) {
						listModelFilters.removeElementAt(index);
					}
				}
				
			});
			
			buttonMoveUp.addActionListener(new AbstractAction () {
	
				/*
				 * Move the selected filter entry up the list
				 */
				@Override
				public void actionPerformed (ActionEvent e) {
					
					int index = listFilters.getSelectedIndex();
					
					if ((index < 0) || (listModelFilters.size() < 2) || (index < 1)) {
						return;
					}
					
					FilterListEntry entry = listModelFilters.remove(index);
									
					listModelFilters.insertElementAt(entry, index - 1);
					listFilters.setSelectedIndex(index - 1);
				}
				
			});
			
			buttonMoveDown.addActionListener(new AbstractAction () {
	
				/*
				 * Move the selected filter entry down the list
				 */
				@Override
				public void actionPerformed (ActionEvent e) {
				
					
					int index = listFilters.getSelectedIndex();
					
					if ((index < 0) || (listModelFilters.size() < 2) || (index > (listModelFilters.size() - 2))) {
						return;
					}
					
					FilterListEntry entry = listModelFilters.remove(index);
					listModelFilters.insertElementAt(entry, index + 1);
					listFilters.setSelectedIndex(index + 1);
					
					
				}
				
			});
			
		}
		
		
		/*
		 * Define the behavior for target folder panel components
		 */
		{
			
			buttonAddFolder.addActionListener(new AbstractAction () {
	
				/*
				 * Displays a folder choosing dialog box and adds the selected folder
				 * to the target folder list
				 */
				@Override
				public void actionPerformed (ActionEvent e) {
					
					FolderChooser folderChooser = new FolderChooser(
							ProfileEditorWindow.this, new File(System.getProperty("user.home")), "Select a target folder");
					
					if (folderChooser.getFolder() == true) {
						listModelFolders.addElement(folderChooser.getSelectedFile().getPath());
					}
					
				}
				
			});
			
			buttonRemoveFolder.addActionListener(new AbstractAction () {
	
				@Override
				public void actionPerformed (ActionEvent e) {
	
					int index = listFolders.getSelectedIndex();
					
					if (index > -1) {
						listModelFolders.removeElementAt(index);
					}
				}
				
			});
			
		}
				
	}
	
	/*
	 * Builds a Profile representation of the new profile options and settings
	 * and saves an XML representation of the Profile.
	 */
	private void saveProfile () {
		
		Profile newProfile = new Profile(textFieldName.getText(), textAreaDescription.getText());
		String targetFolder;
		FilterListEntry filterListEntry;
		
		/*
		 * Add name, description, and indexing settings to the new detection profile
		 */
		newProfile.setName(textFieldName.getText());
		newProfile.setDescription(textAreaDescription.getText());
		
		newProfile.getSettings().setIndexInclusively(checkBoxIndexInclusively.isSelected());
		newProfile.getSettings().setIndexRecursively(checkBoxIndexRecursively.isSelected());
		newProfile.getSettings().setIndexHiddenFolders(checkBoxIndexHiddenFolders.isSelected());
		newProfile.getSettings().setIndexReadOnlyFolders(checkBoxIndexReadOnlyFolders.isSelected());
		
		/*
		 * Add target folders to the profile from the folder list 
		 */
		
		for (int i = 0; i < listFolders.getModel().getSize(); i++) {
			
			targetFolder = listFolders.getModel().getElementAt(i);
			newProfile.addTarget(targetFolder);
		}
		
		/*
		 * Add filters to the profile from the filter list
		 */
		for (int i = 0; i < listFilters.getModel().getSize(); i++) {
			
			filterListEntry = listFilters.getModel().getElementAt(i);
			
			if (filterListEntry.getContentIndexFilter() != null) {
				newProfile.addFilter(filterListEntry.getContentIndexFilter());
			}
		}
		
		try {
			Profile.save(newProfile);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return;
						
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			return;
			
		} catch (TransformerException e) {
			e.printStackTrace();
			return;
		}
		
		getCloseButtonBehavior().actionPerformed(null);	

	}

	/**
	 * @return the checkBoxIndexInclusively
	 */
	private JCheckBox getCheckBoxIndexInclusively () {
		return checkBoxIndexInclusively;
	}

	/**
	 * @param checkBoxIndexInclusively the checkBoxIndexInclusively to set
	 */
	private void setCheckBoxIndexInclusively (JCheckBox checkBoxIndexInclusively) {
		this.checkBoxIndexInclusively = checkBoxIndexInclusively;
	}

	/**
	 * @return the checkBoxIndexRecursively
	 */
	private JCheckBox getCheckBoxIndexRecursively () {
		return checkBoxIndexRecursively;
	}

	/**
	 * @param checkBoxIndexRecursively the checkBoxIndexRecursively to set
	 */
	private void setCheckBoxIndexRecursively (JCheckBox checkBoxIndexRecursively) {
		this.checkBoxIndexRecursively = checkBoxIndexRecursively;
	}

	/**
	 * @return the checkBoxIndexHiddenFolders
	 */
	private JCheckBox getCheckBoxIndexHiddenFolders () {
		return checkBoxIndexHiddenFolders;
	}

	/**
	 * @param checkBoxIndexHiddenFolders the checkBoxIndexHiddenFolders to set
	 */
	private void setCheckBoxIndexHiddenFolders (JCheckBox checkBoxIndexHiddenFolders) {
		this.checkBoxIndexHiddenFolders = checkBoxIndexHiddenFolders;
	}

	/**
	 * @return the checkBoxIndexReadOnlyFolders
	 */
	private JCheckBox getCheckBoxIndexReadOnlyFolders () {
		return checkBoxIndexReadOnlyFolders;
	}

	/**
	 * @param checkBoxIndexReadOnlyFolders the checkBoxIndexReadOnlyFolders to set
	 */
	private void setCheckBoxIndexReadOnlyFolders (
			JCheckBox checkBoxIndexReadOnlyFolders) {
		this.checkBoxIndexReadOnlyFolders = checkBoxIndexReadOnlyFolders;
	}

	/**
	 * @return the listModelFilters
	 */
	private DefaultListModel<FilterListEntry> getListModelFilters () {
		return listModelFilters;
	}

	/**
	 * @param listModelFilters the listModelFilters to set
	 */
	private void setListModelFilters (
			DefaultListModel<FilterListEntry> listModelFilters) {
		this.listModelFilters = listModelFilters;
	}

	/**
	 * @return the listModelFolders
	 */
	private DefaultListModel<String> getListModelFolders () {
		return listModelFolders;
	}

	/**
	 * @param listModelFolders the listModelFolders to set
	 */
	private void setListModelFolders (DefaultListModel<String> listModelFolders) {
		this.listModelFolders = listModelFolders;
	}

	/**
	 * @return the textFieldName
	 */
	private JTextField getTextFieldName () {
		return textFieldName;
	}

	/**
	 * @param textFieldName the textFieldName to set
	 */
	private void setTextFieldName (JTextField textFieldName) {
		this.textFieldName = textFieldName;
	}

	/**
	 * @return the textAreaDescription
	 */
	private JTextArea getTextAreaDescription () {
		return textAreaDescription;
	}

	/**
	 * @param textAreaDescription the textAreaDescription to set
	 */
	private void setTextAreaDescription (JTextArea textAreaDescription) {
		this.textAreaDescription = textAreaDescription;
	}
}
