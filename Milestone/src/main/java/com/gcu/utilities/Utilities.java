/**
 * Utlities.java
 * This class contains utility methods that can be used throughout the application.
 */
package com.gcu.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class contains utility methods that can be used throughout the application.
 */
public class Utilities {
	
	/**
	 * This method returns the current date and time in a specific format.
	 * 2025-04-03T15:50:58.387358 format
	 * https://www.w3schools.com/java/java_date.asp for more formatting options
	 * @return The current date and time as a string.
	 */
	public static String getCurrentTime() {
		
		LocalDateTime curDateTime = LocalDateTime.now();									
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");	
		return curDateTime.format(formatter);											
	}
	/**
	 * This method formats a given date string from "yyyy-MM-dd HH:mm:ss" to "MM/dd/yyyy HH:mm:ss".
	 * @param date The date string to be formatted.
	 * @return The formatted date string.
	 */
	public static String formatDate(String date) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		 
		 return dateTime.format(outputFormatter);
	}
}