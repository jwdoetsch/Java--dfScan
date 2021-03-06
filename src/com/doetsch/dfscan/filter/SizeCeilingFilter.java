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
