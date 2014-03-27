package com.doetsch.dfscan.filter;

import com.doetsch.dfscan.util.HashableFile;

/**
 * HiddenFileFilter defines a content index filter with enforcement 
 * behavior that returns the HashableFile contents of the source index that
 * represent visible (not hidden) file-system files.
 *  
 * @author Jacob Wesley Doetsch
 */
public class HiddenFileFilter extends ContentIndexFilter {

	/**
	 * Creates a HiddenFileFilter instance.
	 */
	public HiddenFileFilter(boolean inclusive) {
		super(new FilterQualifier<String>("") {

			@Override
			public boolean qualify (HashableFile sourceFile) {
				return !sourceFile.isHidden();
			}
			
		}, inclusive);
	}

	@Override
	public String getDescription () {
		String description = (super.isInclusive() ? "Inclusive " : "Exclusive ");
		description += "Hidden File Filter";
		return description;
	}
	
	public String getType () {
		return "is-hidden";
	}

}
