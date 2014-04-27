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
 * FilterQualifier is a prototype class implemented inside ContentIndexFilter
 * classes. The implementation of the validate() method determines if the 
 * given HashableFile qualifies to be filtered according to the given
 * matchParameter.
 * 
 * @author Jacob Wesley Doetsch
 *
 * @param <K> the class type of the qualifier value
 */
public abstract class FilterQualifier <K> {

	private K qualifierValue;
	
	/**
	 * Create a FilterQualifier to qualify against the given matchParam
	 * parameter.
	 * 
	 * @param qualifierValue the value against which to qualify HashableFile
	 * instances
	 */
	public FilterQualifier (K qualifierValue) {
		this.qualifierValue = qualifierValue;
	}
	
	/**
	 * Returns the value against which to qualify HashableFile instances.
	 * 
	 * @return
	 */
	public K getQualifierValue () {
		return this.qualifierValue;
	}
	
	/**
	 * Determines whether the given HashableFile qualifies to be filtered
	 * based on the underlying implementation.
	 * 
	 * @param sourceFile the HashableFile to determine qualification for
	 * @param matchParameter the ParameterType with which to measure the HashableFile
	 * against to determine it's qualification.
	 * @return true if the sourceFile qualifies against the matchParameter, false otherwise.
	 */
	public abstract boolean qualify (HashableFile sourceFile);
	
}
