package com.doetsch.dfscan.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The realization of an index that encapsulates a collection
 * of HashableFile objects, representing the file contents of
 * all or part of a file-system.
 * 
 * @author Jacob Wesley Doetsch
 */
public class ContentIndex implements Iterable<HashableFile> {
	
	private ArrayList<HashableFile> contents;
	
	/**
	 * Creates an empty ContentIndex.
	 */
	public ContentIndex () {
		contents = new ArrayList<HashableFile>();
	}
	
	/**
	 * Creates a ContentIndex encapsulating a representation of the files
	 * contained within the folder represented by the given String path;
	 * @param string
	 */
	public ContentIndex(String path) {
		contents = buildIndex(path);
	}

	private ArrayList<HashableFile> buildIndex (String folderPath) {
		System.out.println(folderPath);
		ArrayList<HashableFile> index = new ArrayList<HashableFile>();
		
		
		File[] files = (new File(folderPath)).listFiles();
		
		if (files == null) {
			return index;
		}
		
		for (File file : files) {
			if (file.isFile()) {
				index.add(new HashableFile(file.getPath()));
			}
		}
		
		return index;		
	}
	
	public String toString () {
		String returnString = "{\n";
		
		for (HashableFile file : contents) {
			returnString += file.getHash() + ": " + file.getPath() + ",\n";
		}
		
		returnString += "}";
		
		return returnString;
	}
	
	/**
	 * Adds a HashableFile to the ContentIndex.
	 * 
	 * @param file the HashableFile to add to the index.
	 */
	public void add (HashableFile file) {
		contents.add(file);
	}
	
	/**
	 * Adds a HashableFile to the ContentIndex.
	 * 
	 * @param file the path of the HashableFile to add to the index.
	 */
	public void add (String file) {
		contents.add(new HashableFile(file));
	}
	
	/**
	 * Removes the HashableFile representing the file at the given
	 * path from the ContentIndex.
	 *  
	 * @param path the path of the file to remove.
	 */
	public void remove (String path) {
		
		if (contains(path) != -1) {
			contents.remove(contains(path));
		}
		
	}
	
	/**
	 * Determines whether or not a HashableFile representing the file
	 * at the given path is present in the ContentIndex.
	 * 
	 * @param path the path to check for
	 * @return the index of the HashableFile representation of the file
	 * at the given path, -1 if it isn't present.
	 */
	public int contains (String path) {
		for (int i = 0; i < contents.size(); i++) {
			if (contents.get(i).getPath().equals(path)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Merges the contents of the sourceIndex into the ContentIndex.
	 * 
	 * @param sourceIndex the index to merge
	 */
	public void merge (ContentIndex sourceIndex) {
		
		/*
		 * Iterate through the contents of the source index.
		 */
		for (HashableFile file : sourceIndex) {
			
			/*
			 * If the current HashableFile isn't present in this ContentIndex
			 * then add it.
			 */
			if (contains(file.getPath()) == -1) {
				add(file);
			}
		}
	}

	/**
	 * Returns the size of the ContentIndex; the number of HashableFile
	 * representations encapsulated by the ContentIndex. 
	 * 
	 * @return the size of the ContentIndex
	 */
	public int getSize () {
		return contents.size();
	}
	
	/*
	 * Returns the Iterator of the collection encapsulating the index
	 * of HashableFile representations.
	 */
	@Override
	public Iterator<HashableFile> iterator() {
		
		/*
		 * Return the iterator of the HashableFile collection.
		 */
		return contents.iterator();
	}

	public static void main (String[] args) {
		ContentIndex index = new ContentIndex("D:\\Music");		
		
		System.out.println(index.toString());
		
		
	}

	public void merge(HashableFile hashableFile) {
		if (contains(hashableFile.getPath()) == -1) {
			add(hashableFile);
		}
	}

	public ContentIndex copy() {
		ContentIndex copyIndex = new ContentIndex();
		
		for (HashableFile f : contents) {
			copyIndex.add(f.copy());
		}
		
		return copyIndex;
	}
	
	public ArrayList<HashableFile> getContents() {
		return contents;
	}
		
}
