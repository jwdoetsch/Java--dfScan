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

import com.doetsch.dfscan.util.ContentIndex;
import com.doetsch.dfscan.util.HashableFile;

/**
 * ContentIndexFilter is the prototype class for ContentIndex filter
 * classes. It declares a method, enforce(), the implementation of
 * which will be defined by each filter.
 * 
 * @author Jacob Wesley Doetsch
 */
public abstract class ContentIndexFilter {

	/*
	 * The FilterQualifier by which the source index's contents will
	 * be qualified against and filtered by. 
	 */
	private FilterQualifier<?> qualifier;
	private boolean inclusive;
	
	/**
	 * Creates a ContentIndexFilter instance encapsulating the given FilterQualifier.
	 * 
	 * @param qualifier the FilterQualifier rule which will determine the qualification
	 * of HashableFiles while the filter is enforced
	 */
	public ContentIndexFilter (FilterQualifier<?> qualifier, boolean inclusive) {
		this.qualifier = qualifier;
		this.inclusive = inclusive;
	}
	
	/**
	 * Returns a ContentIndex representation of the contents of the given source index
	 * that qualify to be filtered by the encapsulated FilterQualifier rule.
	 * 
	 * @param sourceIndex the ContentIndex to filter
	 * @return a ContentIndex representation of the source index's filtered contents
	 */
	public ContentIndex enforce (ContentIndex sourceIndex) {
		ContentIndex filteredIndex = new ContentIndex();
		
		/*
		 * Iterate through the contents of the given source index, while determining
		 * whether or not the current HashableFile qualifies to be filtered, adding
		 * qualifying HashableFile instances to the return index.
		 */
		for (HashableFile file : sourceIndex) {
			if (qualifier.qualify(file)) {
				filteredIndex.add(file);
			}
		}
		
		return filteredIndex;
	}
	
	/**
	 * Returns the inclusivity of the filter; true indicates the 
	 * filter is inclusive, false indicates it's exclusive
	 * 
	 * @return true if inclusive, false if exclusive
	 */
	public boolean isInclusive () {
		return inclusive;
	}

	/**
	 * Sets the inclusivity of the filter; true indicates the
	 * filter is inclusive, false indicates it's exclusive.
	 * 
	 * @param inclusive true if inclusive, false if exclusive
	 */
	public void setInclusive (boolean inclusive) {
		this.inclusive = inclusive;
	}
	
	/**
	 * Returns the filter's qualifying value.
	 * 
	 * @return the filter's qualifying value
	 */
	public Object getQualifierValue () {
		return this.qualifier.getQualifierValue();
	}

	/**
	 * Returns a string representation of the filter.
	 * 
	 * @return a String representation of the filter
	 */
	public String toString () {
		return "filter: " 
				+ " mode=" + (inclusive ? "inclusive" : "exclusive")
				+ " value=" + qualifier.getQualifierValue();
	}
	
	/**
	 * Returns a description of the filter.
	 * 
	 * @return a String representation of the filter's description
	 */
	public abstract String getDescription ();
	
	public abstract String getType ();
	
}
