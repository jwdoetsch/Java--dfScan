package com.doetsch.dfscan.filter;

import com.doetsch.dfscan.util.HashableFile;

/**
 * PathContainsFilter defines a content index filter with enforcement 
 * behavior that returns the HashableFile contents of the source index that
 * represent file-system files with file-system paths that contain an occurrence
 * of the given string.
 *  
 * @author Jacob Wesley Doetsch
 */
public class PathContainsFilter extends ContentIndexFilter {

	/**
	 * Creates a NameContainsFilter instance.
	 * 
	 * @param value the String value to test file-system paths for
	 */
	public PathContainsFilter (String value, boolean inclusive) {
		super(new FilterQualifier<String>(value) {

			@Override
			public boolean qualify (HashableFile sourceFile) {
				return sourceFile.getPath().contains((CharSequence) super.getQualifierValue());
			}
			
		}, inclusive);
	}
	
	@Override
	public String getDescription () {
		String description = (super.isInclusive() ? "Inclusive " : "Exclusive ");
		description += "File Path Contains \"" + super.getQualifierValue().toString()
				+ "\" Filter";
		return description;
	}
	
	public String getType () {
		return "path-contains";
	}
	
}
