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

package com.doetsch.dfscan.window;

import com.doetsch.dfscan.core.Profile;

/**
 * ProfileEntry is a container class that encapsulates a Profile
 * instance. 
 * 
 * @author Jacob Wesley Doetsch
 */
public class ProfileEntry {
	
	private Profile profile;
	
	/**
	 * Creates a new ProfileEntry instance encapsulating the given Profile.
	 * 
	 * @param profile the Profile to encapsulate
	 */
	public ProfileEntry (Profile profile) {
		this.profile = profile;
	}
	
	/**
	 * Returns the name of the encapsulated profile.
	 */
	@Override
	public String toString () {
		return profile.getName();
	}
	
	/**
	 * Returns the encapsulated Profile.
	 * 
	 * @return the encapsulated Profile
	 */
	public Profile getProfile () {
		return this.profile;
	}
	
	/**
	 * Sets the encapsulated Profile.
	 * 
	 * @param profile the Profile to encapsulate.
	 */
	public void setProfile (Profile profile) {
		this.profile = profile;
	}

}
