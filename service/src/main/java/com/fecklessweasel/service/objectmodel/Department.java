package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

/**
 * Stores all information about a school's department. Created from database.
 * @author Elliot Essman
 */

public class Department {
	
	private int id;
	private int univId;
	private String acronym;
	private String deptName;
	
	private int DEPTNAME_MAX = 20;
	private int DEPTNAME_MIN = 4;
	
	private Department(int id, int univId, String deptName, String acronym){
		this.id = id;
		this.univId = univId;
		this.deptName = deptName;
		this.acronym = acronym;
	}
	
	public Department create(Connection conn, int univId, String deptName, String acronym){
		OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(univId);
        OMUtil.nullCheck(deptName);
        
        //department name length
        if (deptName.length() > DEPTNAME_MAX || deptName.length() < DEPTNAME_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_DEPTNAME_LENGTH);
        }
        //department name characters
        if(!OMUtil.isValidName(deptName)){
            throw new ServiceException(ServiceStatus.APP_INVALID_DEPTNAME_CHARS);
        }
        //check acronym
        if (!acronym.matches("^[A-Z]{4}$")) {
            throw new ServiceException(ServiceStatus.APP_INVALID_DEPT_ACRONYM);
        }
        
        long id = DepartmentTable.insertDepartment(conn ,univId, deptName, acronym);
        return new Department(id, univId, deptName, acronym);
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

