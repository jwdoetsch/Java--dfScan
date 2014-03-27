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
