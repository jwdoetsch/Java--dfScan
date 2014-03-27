package com.doetsch.dfscan.util;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * FolderChooser is an extension of JFileChooser, configured to 
 * allow only the selection of folders. It's getFolder() method
 * displays a configured JFileChooser and returns and stores the
 * String representation of the path of the selected folder. 
 * 
 * @author Jacob Wesley Doetsch
 */
public class FolderChooser extends JFileChooser {
	
	private Component parent;
	
	/**
	 * Create a FolderChooser instance with the given default folder
	 * and parent component.
	 * 
	 * @param parent the FolderChooser's parent component which will adopt
	 * the getFolder() pop-up
	 * @param defaultFolder the default folder in which to browse with the
	 * JFileChooser pop-up
	 */
	public FolderChooser (Component parent, File defaultFolder, String title) {
		super();
		this.parent = parent;
		setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		super.setDialogTitle(title);
		
		/*
		 * If the given default folder doesn't exist then the user's home
		 * folder will be used 
		 */
		setCurrentDirectory(
				(defaultFolder.exists() ? defaultFolder : null));
	}

	/**
	 * Shows a folder selection dialog and returns the selected folder.
	 * A valid folder must be already exist and be readable.
	 * 
	 * @return true if a valid folder was selected, false otherwise
	 */
	public boolean getFolder () {
		
		//Iff a folder was actually selected and okayed...
		if (showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			
			//Update the chosen folder reference according to the selected File
			File chosenFolder = getSelectedFile();
			
			//If the folder doesn't exist then display a pop-up message and bail
			if (!chosenFolder.exists()) {
				JOptionPane.showMessageDialog(parent, chosenFolder.getPath()
						+ " doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			
			//If the folder can't be read then display a pop-up message and bail
			} else if (!chosenFolder.canRead()) {
				JOptionPane.showMessageDialog(parent, chosenFolder.getPath()
						+ " can't be read. Check permissions.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
				
			//If the folder exists and can be read then update and return the chosen folder
			} else {
				//this.chosenFolder = this.getSelectedFile();
				return true;
			}
		
		//If no folder was selected and okayed then bail
		} else {
			return false;
		}
		
	}
	
}
