/**
 * loginAccessInterface.java
 */
package com.gcu.data;

public interface LoginAccessInterface <T> {

	public T getById(T t);											
	public T getUserLoginById(int t);									
	public int getIdByUsername(String name);									
	public boolean create(T t);										
}