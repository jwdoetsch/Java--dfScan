package com.doetsch.dfscan.filter;

import com.doetsch.dfscan.util.HashableFile;

/**
 * SizeCeilingFilter defines a content index filter with enforcement 
 * behavior that returns the HashableFile contents of the source index that
 * represent file-system files with sizes less than or equal to the given
 * ceiling value.
 *  
 * @author Jacob Wesley Doetsch
 */
public class SizeCeilingFilter extends ContentIndexFilter {

	/**
	 * Creates a SizeCeilingFilter instance.
	 * 
	 * @param ceilingValue the maximum file size by which to filter against
	 */
	public SizeCeilingFilter(Long ceilingValue, boolean inclusive) {
		super(new FilterQualifier<Long>(ceilingValue) {

			@Override
			public boolean qualify (HashableFile sourceFile) {
				return (sourceFile.length() <= super.getQualifierValue());
			}
			
		}, inclusive);
	}
	
	@Override
	public String getDescription () {
		String description = (super.isInclusive() ? "Include" : "Exclude");
		description += " files with sizes less than or equal to " + ((long) super.getQualifierValue()) + " bytes ";
		description += (super.isInclusive() ? "while" : "from") + " scanning";
		return description;
	}
	
	public String getType () {
		return "size-ceiling";
	}
	
}
