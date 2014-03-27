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
