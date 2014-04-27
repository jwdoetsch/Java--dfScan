/*
 * Copyright (C) 2014 Jacob Wesley Doetsch
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.doetsch.dfscan.core;

/**
 * SettingsContainer encapsulates the indexing settings of a detection
 * profile.
 * 
 * @author Jacob Wesley Doetsch
 */
public class SettingsContainer {
	
	private boolean indexInclusively;
	private boolean indexRecursively;
	private boolean indexHiddenFolders;
	private boolean indexReadOnlyFolders;
	
	/**
	 * Creates a SettingsContainer instance encapsulating default indexing
	 * values; primary indexing will be undertaken inclusively, sub-folders
	 * within the target folders will be recursively scanned, and hidden and
	 * read-only folders will be skipped. 
	 */
	public SettingsContainer () {
		setIndexInclusively(false);
		setIndexRecursively(true);
		setIndexHiddenFolders(false);
		setIndexReadOnlyFolders(false);
	}
	
	/**
	 * Creates a SettingsContainer instance encapsulating the specified
	 * indexing options.
	 * 
	 * @param indexInclusively whether or not indexing will take place inclusively
	 * (true) or exclusively (false)
	 * @param indexRecursively whether or not to recursively index and scan sub-folders
	 * @param indexHiddenFolders whether or not to index hidden folders and sub-folders
	 * @param indexReadOnlyFolders whether or not to index read only folders and sub-folders
	 */
	public SettingsContainer (boolean indexInclusively, boolean indexRecursively,
			boolean indexHiddenFolders, boolean indexReadOnlyFolders) {
		this.setIndexInclusively(indexInclusively);
		this.setIndexRecursively(indexRecursively);
		this.setIndexHiddenFolders(indexHiddenFolders);
		this.setIndexReadOnlyFolders(indexReadOnlyFolders);
	}

	/**
	 * @return the indexInclusively
	 */
	public boolean getIndexInclusively() {
		return indexInclusively;
	}

	/**
	 * @param indexInclusively the indexInclusively to set
	 */
	public void setIndexInclusively(boolean indexInclusively) {
		this.indexInclusively = indexInclusively;
	}

	/**
	 * @return the indexRecursively
	 */
	public boolean getIndexRecursively() {
		return indexRecursively;
	}

	/**
	 * @param indexRecursively the indexRecursively to set
	 */
	public void setIndexRecursively(boolean indexRecursively) {
		this.indexRecursively = indexRecursively;
	}

	/**
	 * @return the indexHiddenFolders
	 */
	public boolean getIndexHiddenFolders() {
		return indexHiddenFolders;
	}

	/**
	 * @param indexHiddenFolders the indexHiddenFolders to set
	 */
	public void setIndexHiddenFolders(boolean indexHiddenFolders) {
		this.indexHiddenFolders = indexHiddenFolders;
	}

	/**
	 * @return the indexReadOnlyFolders
	 */
	public boolean getIndexReadOnlyFolders() {
		return indexReadOnlyFolders;
	}

	/**
	 * @param indexReadOnlyFolders the indexReadOnlyFolders to set
	 */
	public void setIndexReadOnlyFolders(boolean indexReadOnlyFolders) {
		this.indexReadOnlyFolders = indexReadOnlyFolders;
	}

	public String toString () {
		String returnString = "";
//	       <indexing_mode value="exclusive" />
//	       <index_recursively value="on" />
//	       <index_hidden_folders value="off" />
//	       <index_readonly_folders value="off" />
		returnString += "index_inclusively: " + (indexInclusively ? "yes" : "no") + "\n";
		returnString += "index_recursively: " + (indexRecursively ? "yes" : "no") + "\n";
		returnString += "index_hidden_folders: " + (indexHiddenFolders ? "yes" : "no") + "\n";
		returnString += "index_readonly_folders: " + (indexReadOnlyFolders ? "yes" : "no") + "\n";
		return returnString;
	}
	
}
