package com.doetsch.dfscan.filter;

import com.doetsch.dfscan.util.HashableFile;

/**
 * ReadOnlyFileFilter defines a content index filter with enforcement
 * behavior that returns the HashableFile contents of the source index that
 * represent readable (not read-only) file-system file.   
 * 
 * @author Jacob Wesley Doetsch
 */
public class ReadOnlyFileFilter extends ContentIndexFilter {

	/**
	 * Creates a ReadOnlyFileFilter instance.
	 */
	public ReadOnlyFileFilter(boolean inclusive) {
		super(new FilterQualifier<String>("") {

			@Override
			public boolean qualify (HashableFile sourceFile) {
				return sourceFile.canWrite();
			}
			
		}, inclusive);
	}
	
	@Override
	public String getDescription () {
		String description = (super.isInclusive() ? "Include" : "Exclude");
		description += " read-only files ";
		description += (super.isInclusive() ? "while" : "from") + " scanning";
		return description;
	}
	
	public String getType () {
		return "is-readonly";
	}
	
}
