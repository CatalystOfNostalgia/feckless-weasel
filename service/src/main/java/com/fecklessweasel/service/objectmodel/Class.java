package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

import com.fecklessweasel.service.datatier.ClassTable;

/**
 * Stores all information about a class at a university.
 * 
 * @author Elliot Essman
 */

public class Class {

	private long id;
	private int deptId;
	private int univId;
	private int classNum;

	private static int NUM_MAX = 999;
	private static int NUM_MIN = 90;

	private Class(long id, int univId, int deptId, int classNum) {
		this.id = id;
		this.univId = univId;
		this.deptId = deptId;
		this.classNum = classNum;
	}

	/**
	 * Creates a class in the database
	 * @param conn A connection to the database
	 * @param univid Id of the university this class is at
	 * @param deptid The official name of the department
	 * @param classnum The number of this class
	 * @return A class object
	 */
	public static Class create(Connection conn, int univId, int deptId, int classNum) throws ServiceException{
		OMUtil.sqlCheck(conn);
		OMUtil.nullCheck(deptId);
		OMUtil.nullCheck(univId);
		OMUtil.nullCheck(classNum);

		if (classNum > NUM_MAX || classNum < NUM_MIN) {
			throw new ServiceException(ServiceStatus.APP_INVALID_CLASS_NUMBER);
		}
		long id = ClassTable.insertClass(conn, univId, deptId, classNum);
		return new Class(id, univId, deptId, classNum);
	}

	public long getId() {
		return this.id;
	}

	public int getDeptId() {
		return this.deptId;
	}

	public int getclassNum() {
		return this.classNum;
	}
}
