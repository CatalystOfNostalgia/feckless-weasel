package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import com.fecklessweasel.service.datatier.UniversityTable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.fecklessweasel.service.UniversityUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.fecklessweasel.service.datatier.DepartmentTable;

/**
 * Stores all information about a school's department.
 * @author Elliot Essman
<<<<<<< HEAD
*/
public final class Department {
    /**
     * ID in the database table.
     */
    public static int id;
    /**
     * The university this department is in.
     */
    private int uid;
    /**
     * Acronym or short name of this department.
     */
    private String acronym;
    /**
     * Official name of the department.
     */
    private String deptName;

    /**
     * Max character length of the official name.
     */
    private static int DEPTNAME_MAX = 50;
    /**
     * Min character length of the official name.
     */
    private static int DEPTNAME_MIN = 4;
    /**
     * University ID
     **/
    private static int univid;

    /**
     * Private constructor. Should be created from the database or create
     * method.
     */
    private Department(int id, int uid, String deptName, String acronym) {
        this.id = id;
        this.uid = uid;
        this.deptName = deptName;
        this.acronym = acronym;
    }

    /**
     * Creates a department in the database.
     *
     * @param conn       A connection to the database.
     * @param university The university this department is in.
     * @param deptname   The official name of the department.
     * @param acronym    The acronym of the department.
     * @return A department object.
     */
    public static Department create(Connection conn, University university, String deptName, String acronym)
            throws ServiceException {
        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(university);
        OMUtil.nullCheck(deptName);
        OMUtil.nullCheck(acronym);

        // Department name length
        if (deptName.length() > DEPTNAME_MAX || deptName.length() < DEPTNAME_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_DEPTNAME_LENGTH);
        }
        // Department name characters
        if (!OMUtil.isValidName(deptName)) {
            throw new ServiceException(ServiceStatus.APP_INVALID_DEPTNAME_CHARS);
        }
        // Check acronym
        if (!acronym.matches("^[A-Z]{4}$")) {
            throw new ServiceException(ServiceStatus.APP_INVALID_DEPT_ACRONYM);
        }

        int id = DepartmentTable.insertDepartment(conn, university.getID(), deptName, acronym);
        return new Department(id, university.getID(), deptName, acronym);
    }

    /**
     * Look up a specific department and return it in object form.
     *
     * @param sql The Sql connection to the FecklessWeaselDB.
     * @param did The department's unique identifier.
     * @return The Department's object representation.
     */

    public static Department lookup(Connection sql, int did)
            throws ServiceException {

        OMUtil.sqlCheck(sql);
        ResultSet result = DepartmentTable.lookupDepartment(sql, did);

        try {
            // If no tuples returned, throw error.
            if (!result.next()) {
                throw new ServiceException(ServiceStatus.APP_DEPARTMENT_NOT_EXIST);
            }

            Department dept = new Department(result.getInt("id"),
                    result.getInt("univid"),
                    result.getString("deptName"),
                    result.getString("acronym"));
            result.close();
            return dept;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Gets The university this department is in.
     *
     * @return The university this department is in.
     */
    public University lookupUniversity(Connection conn)
            throws ServiceException {

        OMUtil.sqlCheck(conn);
        return University.lookup(conn, this.uid);
    }

    /**
     * Gets the official department name.
     *
     * @return The official department name.
     */
    public String getDeptName() {
        return this.deptName;
    }

    /**
     * Gets the department acronym name.
     * @return The department acronym name.
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * Gets the database ID of the department.
     *
     * @return The database ID of the department.
     */
    public int getID() {
        return this.id;
    }
}

