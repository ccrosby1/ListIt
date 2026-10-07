/**
 * RegistrationResult.java
 * This class represents the result of a registration attempt.
 * It contains a boolean indicating success or failure and a message providing additional information.
 */
package com.gcu.model;

public record RegistrationResult(boolean success, String message) {}