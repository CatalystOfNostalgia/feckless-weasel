package com.fecklessweasel.service.objectmodel;

/**
 * Stores all information about a Class. Created from database.
 * @author Elliot Essman
 */

public class Class {
	
	private int id;
	private int deptId;
	private int classNum;
	
	private Class(int id, int deptId, int classNum){
		this.id = id;
		this.deptId = deptId;
		this.classNum = classNum;
	}
	
	public int getId(){
		return this.id;
	}
	public int getDeptId(){
		return this.deptId;
	}
	public int getclassNum(){
		return this.classNum;
	}
}

