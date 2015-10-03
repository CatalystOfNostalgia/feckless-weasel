package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

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
	
	private int NAME_MAX = 30;
	private int NAME_MIN = 5;
	private int ACRONYM_MAX = 5;
	private int ACRONYM_MIN = 2;
	private int CITY_MAX = 30;
	private int CITY_MIN = 3;
	private int COUNTRY_MAX = 30;
	private int COUNTRY_MIN = 3;
	
	private University(int id, String longname, String acronym, String city, String state, String country){
		this.id = id;
		this.longname = longname;
		this.acronym = acronym;
		this.city = city;
		this.state = state;
		this.country = country;
	}
	
	/**
	 * Creates a university in the database.
	 * @param conn Connection to the database
	 * @param longname The official name of the university
	 * @param acronym The acronym/short name of the university
	 * @param city The city of the university
	 * @param state The state of the University. Can be Null.
	 * @param the The country of the University
	 */
	public University create(Connection conn, String longname, String acronym, String city, String state, String country) throws ServiceException{
		
		//TODO null check state only when country is USA
		OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(longname);
        OMUtil.nullCheck(acronym);
        OMUtil.nullCheck(city);
        OMUtil.nullCheck(country);
        
        //university name length
        if (longname.length() > NAME_MAX || longname.length() < NAME_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_UNIV_NAME_LENGTH);
        }
        //university name characters
        if(!OMUtil.isValidName(longname)){
            throw new ServiceException(ServiceStatus.APP_INVALID_UNIV_NAME_CHARS);
        }
        
        //university acronym length
        if (acronym.length() > ACRONYM_MAX || acronym.length() < ACRONYM_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_ACRONYM_LENGTH);
        }
        //university acronym characters
        if(!acronym.matches("^[a-zA-Z&]*$")){
            throw new ServiceException(ServiceStatus.APP_INVALID_ACRONYM_CHARS);
        }
        
        //university city length
        if (city.length() > CITY_MAX || city.length() < CITY_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_CITY_LENGTH);
        }
        //university city characters
        if (!OMUtil.isValidName(city)) {
            throw new ServiceException(ServiceStatus.APP_INVALID_CITY_CHARS);
        }
        
        //university state
        if (!state.matches("^[A-Z]{2}$")) {
            throw new ServiceException(ServiceStatus.APP_INVALID_STATE);
        }
        
        //university country length
        if (country.length() > COUNTRY_MAX || country.length() < COUNTRY_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_COUNTRY_LENGTH);
        }
        //university country characters
        if (!OMUtil.isValidName(country)) {
            throw new ServiceException(ServiceStatus.APP_INVALID_COUNTRY_CHARS);
        }
        
        long id = UniversityTable.insertUniversity(conn, longname, acronym, city, state, country);
        return new User(id, longname, acronym, city, state, country);
        
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

