package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

import com.fecklessweasel.service.datatier.DepartmentTable;
/**
 * Stores all information about a school's department.
 * @author Elliot Essman
 */

public class Department {
	
	private long id;
	private int univId;
	private String acronym;
	private String deptName;
	
	private static int DEPTNAME_MAX = 20;
	private static int DEPTNAME_MIN = 4;
	
	private Department(long id, int univId, String deptName, String acronym){
		this.id = id;
		this.univId = univId;
		this.deptName = deptName;
		this.acronym = acronym;
	}
	
	/**
	 * Creates a department in the database
	 * @param conn A connection to the database
	 * @param univid Id of the university this department is in
	 * @param deptname The official name of the department
	 * @param acronym The acronym of the department
	 * @return A department object
	 */
	public static Department create(Connection conn, int univId, String deptName, String acronym) throws ServiceException{
		OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(univId);
        OMUtil.nullCheck(deptName);
        OMUtil.nullCheck(acronym);
        
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
	
	public long getId(){
		return this.id;
	}
	public int getUnivId(){
		return this.univId;
	}
	public String getDeptName(){
		return this.deptName;
	}
}

