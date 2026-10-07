/**
 * CColor.java
 * 
 * This class defines an enumeration for console colors.
 * It provides color codes for resetting, red, green, and yellow text.
 */
package com.gcu.utilities;

/**
 * This class defines an enumeration for console colors.
 * It provides color codes for resetting, red, green, and yellow text.
 */
public enum CColor {

	/**
	 * RESET color
	 */
	RESET("\033[0m"),
	RED("\033[0;31m"),
	GREEN("\033[0;32m"),
	YELLOW("\033[0;33m");
	
	
	private final String code;

	
	/**
	 * Color string code
	 * @param code
	 */
    CColor(String code) {					
        this.code = code;
    }

    /**
     * Color toString override
     */
    @Override
    public String toString() {				
        return code;
    }
}
