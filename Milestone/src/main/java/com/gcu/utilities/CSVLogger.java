/**
 * CSVLogger.java
 * This class provides a utility for logging application events to an external CSV file.
 */
package com.gcu.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CSVLogger {

	private static final Path LOG_PATH = Paths.get("C:/listit-logs/app-events.csv");
	
	/**
	 * Static block to initialize the CSV log file.
	 * Creates the necessary directories and the log file if they do not exist.
	 * Writes the header row to the CSV file if it is newly created.
	 */
	static {
		try {
			Files.createDirectories(LOG_PATH.getParent());
			
			if(!Files.exists(LOG_PATH)) {
				Files.createFile(LOG_PATH);
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH.toFile(), true))) {
                    writer.write("timestamp,userId,layer,eventType,message,errorMessage");
                    writer.newLine();
                }
			}
		} catch (IOException e) {
			System.err.println("Failed to initialize CSV log file: " + e.getMessage());
		}
	}
	
	/**
	 * Logs an event to the CSV file.
	 * 
	 * @param userId The ID of the user associated with the event (can be null).
	 * @param layer The application layer where the event occurred (e.g., "DATA", "SERVICE").
	 * @param eventType The type of event (e.g., "ERROR", "INFO").
	 * @param message A descriptive message about the event.
	 * @param errorMessage An optional error message if the event is an error (can be null).
	 */
	public static void log(
			Integer userId,
			String layer,
			String eventType,
			String message,
			String errorMessage
			) {
		
		String timestamp = Utilities.getCurrentTime();
		
		// format log entry as a single CSV line
		String csvLine = String.format("%s,%s,%s,%s,\"%s\",\"%s\"",
                timestamp,
                userId == null ? "" : userId,
                layer,
                eventType,
                message.replace("\"", "'"),
                errorMessage == null ? "" : errorMessage.replace("\"", "'")
        );
		
		// logs string into buffer to write to CSV file
		try (BufferedWriter writer = Files.newBufferedWriter(
		        LOG_PATH,
		        StandardOpenOption.APPEND
		)) {
		    writer.write(csvLine);
		    writer.newLine();
		} catch (IOException e) {
		    System.err.println("Failed to write to CSV log file: " + e.getMessage());
		}
	}
}