package com.fecklessweasel.service.objectmodel;

/**
 * Stores all information about a University. Created from database.
 * @author Elliot Essman
 */

public class University {
	
	private int id;
	private String longname;
	private String acronym;
	private String city;
	private String state;
	private String country;
	
	private University(int id, String longname, String acronym, String city, String state, String country){
		this.id = id;
		this.longname = longname;
		this.acronym = acronym;
		this.city = city;
		this.state = state;
		this.country = country;
	}
	
	public int getId(){
		return this.id;
	}
	public String getLongname(){
		return this.longname;
	}
	public String getAcronym(){
		return this.acronym;
	}
	public String getCity(){
		return this.city;
	}
	public String getState(){
		return this.state;
	}
	public String getCountry(){
		return this.country;
	}
}

