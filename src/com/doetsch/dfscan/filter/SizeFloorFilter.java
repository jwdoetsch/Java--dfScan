package com.doetsch.dfscan.filter;

import com.doetsch.dfscan.util.HashableFile;

/**
 * SizeFloorFilter defines a content index filter with enforcement 
 * behavior that returns the HashableFile contents of the source index that
 * represent file-system files with sizes greater than or equal to the given
 * floor value.
 *  
 * @author Jacob Wesley Doetsch
 */
public class SizeFloorFilter extends ContentIndexFilter {

	/**
	 * Creates a SizeFloorFilter instance.
	 * 
	 * @param ceilingValue the minimum file size by which to filter against
	 */
	public SizeFloorFilter(Long floorValue, boolean inclusive) {
		super(new FilterQualifier<Long>(floorValue) {

			@Override
			public boolean qualify (HashableFile sourceFile) {
				return (sourceFile.length() >= super.getQualifierValue());
			}
			
		}, inclusive);
	}
	
	@Override
	public String getDescription () {
		String description = (super.isInclusive() ? "Inclusive " : "Exclusive ");
		description += "File Size Is >=" + ((long) super.getQualifierValue())
				+ " Filter";
		return description;
	}	
	
	public String getType () {
		return "size-floor";
	}

}
