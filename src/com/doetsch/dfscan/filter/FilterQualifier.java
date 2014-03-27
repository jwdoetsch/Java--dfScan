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
