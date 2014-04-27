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

package com.doetsch.dfscan.util;

import java.io.File;

/**
 * HashableFile is an extension of java.io.File that implements
 * an additional parameter representing the encapsulated file's
 * hash value.
 * 
 * @author Jacob Wesley Doetsch
 */
public class HashableFile extends File {

	private String hash = "";
	
	/**
	 * Create a new HashableFile instance representing the file at the
	 * given String path.
	 * 
	 * @param path the path of the file to represent
	 */
	public HashableFile (String path) {
		super(path);
	}
	
	/**
	 * Creates a new HashableFile instance representing the file at the
	 * given String path with the given hash value.
	 * 
	 * @param path the path of the file to represent
	 * @param hash the hash value to associate with the HashableFile
	 */
	public HashableFile (String path, String hash) {
		super(path);
		setHash(hash);
	}

	/**
	 * Returns the encapsulated File's hash value.
	 * 
	 * @return the hash
	 */
	public String getHash () {
		return hash;
	}

	/**
	 * Sets the encapsulated File's hash value.
	 * 
	 * @param hash the hash to set
	 */
	public void setHash (String hash) {
		this.hash = hash;
	}
	
	/**
	 * Returns a new instance of the HashableFile that encapsulates
	 * the same file-system file and hash as this HashableFile.
	 * @return
	 */
	public HashableFile copy () {
		HashableFile copyFile = new HashableFile(this.getPath());
		copyFile.setHash(this.getHash());
		return copyFile;
	}

}
