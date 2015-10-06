package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

import com.fecklessweasel.service.datatier.ClassTable;

/**
 * Stores all information about a class at a university.
 * @author Elliot Essman
 */

public class Class {

	/** ID in the database table. */
	private int id;
	/** Department this class is in. */
	private Department department;
	/** University this class is in. */
	private University university;
	/** Class number. */
	private int classNum;

	/** Max class number. */
	private static int NUM_MAX = 999;
	/** Min class number. */
	private static int NUM_MIN = 1;

	/** Private constructor. Should be created for the database for create method. */
	private Class(int id, Department department, int classNum) {
		this.id = id;
		this.university = department.getUniversity();
		this.department = department;
		this.classNum = classNum;
	}

	/**
	 * Creates a class in the database.
	 * @param conn A connection to the database.
	 * @param department The department this class is in.
	 * @param classnum The number of this class.
	 * @return A class object.
	 */
	public static Class create(Connection conn, Department department, int classNum) throws ServiceException{
		OMUtil.sqlCheck(conn);
		OMUtil.nullCheck(department);
		OMUtil.nullCheck(classNum);
		University university = department.getUniversity();

		if (classNum > NUM_MAX || classNum < NUM_MIN) {
			throw new ServiceException(ServiceStatus.APP_INVALID_CLASS_NUMBER);
		}
		int id = ClassTable.insertClass(conn, university.getID(), department.getID(), classNum);
		return new Class(id, department, classNum);
	}

	/** 
	 * Gets the database ID of the class.
	 * @return The database ID of the class.
	 */
	protected int getID() {
		return this.id;
	}

	/** 
	 * Gets the department of the class.
	 * @return The deparmtent of the class.
	 */
	public Department getDepartment() {
		return this.department;
	}
	
	/** 
	 * Gets the university of the class.
	 * @return The university of the class.
	 */
	public University getUniversity() {
		return this.university;
	}

	/** 
	 * Gets the number of the class.
	 * @return The number of the class.
	 */
	public int getClassNum() {
		return this.classNum;
	}
}
