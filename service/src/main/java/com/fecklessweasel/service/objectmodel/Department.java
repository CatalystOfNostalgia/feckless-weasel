package com.fecklessweasel.service.objectmodel;

/**
 * Stores all information about a school's department. Created from database.
 * @author Elliot Essman
 */

public class Department {
	
	private int id;
	private int univId;
	private String deptName;
	
	private Department(int id, int univId, String deptName){
		this.id = id;
		this.univId = univId;
		this.deptName = deptName;
	}
	
	public int getId(){
		return this.id;
	}
	public int getUnivId(){
		return this.univId;
	}
	public String getDeptName(){
		return this.deptName;
	}
}

