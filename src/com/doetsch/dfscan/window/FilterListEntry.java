package com.doetsch.dfscan.window;

import com.doetsch.dfscan.filter.*;

public class FilterListEntry {

	private String filterType;
	private String filterValue;
	private boolean isInclusive;
	private ContentIndexFilter filter;

	public FilterListEntry (ContentIndexFilter filter) {
		this.filter = filter;
	}

	/*
	 * Creates a FilterEntry instance 
	 * 
	 * @param filterType
	 * @param filterValue
	 * @param isInclusive
	 */
	public FilterListEntry (String filterType, String filterValue, boolean isInclusive) {

		this.filterType = filterType;
		this.filterValue = filterValue;
		this.isInclusive = isInclusive;
		this.filter = null;

	}

	/*
	 * @return the filterType
	 */
	public String getFilterType () {
		return filterType;
	}

	/*
	 * @param filterType the filterType to set
	 */
	public void setFilterType (String filterType) {
		this.filterType = filterType;
	}

	/*
	 * @return the filterValue
	 */
	public String getFilterValue () {
		return filterValue;
	}

	/*
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue (String filterValue) {
		this.filterValue = filterValue;
	}

	/*
	 * @return the isInclusive
	 */
	public boolean isInclusive () {
		return isInclusive;
	}

	/*
	 * @param isInclusive the isInclusive to set
	 */
	public void setInclusive (boolean isInclusive) {
		this.isInclusive = isInclusive;
	}

	/*
	 * Generates and returns a description of the filter entry
	 * 
	 * @return a String representation of the description
	 */
	public String getDescription () {
		return "";
	}

	public ContentIndexFilter getContentIndexFilter () {

		if (filter != null) {
			return filter;
		}

		switch (filterType) {
		case "name-contains":
			filter = new NameContainsFilter(filterValue, isInclusive);
			break;

		case "path-contains":
			filter = new PathContainsFilter(filterValue, isInclusive);
			break;

		case "is-hidden":
			filter = new HiddenFileFilter(isInclusive);
			break;

		case "is-readonly":
			filter = new ReadOnlyFileFilter(isInclusive);
			break;

		case "size-ceiling":
			filter = new SizeCeilingFilter(Long.valueOf(filterValue), isInclusive);
			break;

		case "size-floor":
			filter = new SizeFloorFilter(Long.valueOf(filterValue), isInclusive);
			break;
		}

		return filter;
	}
	
	public String toString () {
		return filter.getDescription();
	}

}