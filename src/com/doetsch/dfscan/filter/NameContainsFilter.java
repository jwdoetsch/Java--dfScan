package com.doetsch.dfscan.filter;

import com.doetsch.dfscan.util.HashableFile;

/**
 * NameContainsFilter defines a content index filter with enforcement 
 * behavior that returns the HashableFile contents of the source index that
 * represent file-system files with file names that contain an occurrence
 * of the given string.
 *  
 * @author Jacob Wesley Doetsch
 */
public class NameContainsFilter extends ContentIndexFilter {

	/**
	 * Creates a NameContainsFilter instance.
	 * 
	 * @param value the String value to test file names for
	 */
	public NameContainsFilter (String value, boolean inclusive) {
		super(new FilterQualifier<String>(value) {

			@Override
			public boolean qualify (HashableFile sourceFile) {
				return sourceFile.getName().contains((CharSequence) super.getQualifierValue());
			}
			
		}, inclusive);
	}
	
	@Override
	public String getDescription () {
		String description = (super.isInclusive() ? "Inclusive " : "Exclusive ");
		description += "File Name Contains \"" + super.getQualifierValue().toString()
				+ "\" Filter";
		return description;
	}
	
	public String getType () {
		return "name-contains";
	}
	
}
