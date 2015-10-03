package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

/**
 * Stores all information about a Class. Created from database.
 * 
 * @author Elliot Essman
 */

public class Class {

	private int id;
	private int deptId;
	private int univId;
	private int classNum;

	private int NUM_MAX = 999;
	private int NUM_MIN = 90;

	private Class(int id, int univId, int deptId, int classNum) {
		this.id = id;
		this.univId = univId;
		this.deptId = deptId;
		this.classNum = classNum;
	}

	public Class create(Connection conn, int univId, int deptId, int classNum) {
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

	public int getId() {
		return this.id;
	}

	public int getDeptId() {
		return this.deptId;
	}

	public int getclassNum() {
		return this.classNum;
	}
}
