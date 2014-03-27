package com.doetsch.dfscan.util;

public class TimeStringBuilder {
	
	public static String secondsToKindString (int seconds) {
		String returnString;
		
		int i = 0;
		
		//count total years
		while (seconds >= 31536000) {
			i++;
			seconds -= 31536000;
		}

		returnString = i + " years, ";
		i = 0;
		
		
		//count total days
		while (seconds >= 86400) {
			i++;
			seconds -= 86400;
		}
		
		returnString += i + " days, ";
		i = 0;
		
		//count total hours
		while (seconds >= 3600) {
			i++;
			seconds -= 3600;
		}
		
		returnString += i + " hours, ";
		i = 0;
		
		//count total minutes
		while (seconds >= 60) {
			i++;
			seconds -= 60;
		}
		
		returnString += i + " minutes, ";
		i = 0;
		
		//count total seconds remaining
		while (seconds >= 1) {
			i++;
			seconds -= 1;
		}
		
		returnString += i + " seconds";
		
		return returnString;
	}
	
	public static void main (String[] args) {
		
		System.out.println(TimeStringBuilder.secondsToKindString(86400 + 3600 + 60 + 2));
		
	}

}
