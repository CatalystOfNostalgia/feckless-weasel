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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.fecklessweasel.service.datatier.DepartmentTable;

/**
 * Stores all information about a school's department.
 * @author Elliot Essman

*/
public final class Department {
    /**
     * ID in the database table.
     */
    private int id;
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
    private int univid;

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
     * Look up all rows in the Department belonging to a university
     * except for any row up to offset and after offset + amt
     * @param connection MySQL connection
     * @param offset The first x Results to skip
     * @param amt The max amount of Department objects returned
     * @throws ServiceException Thrown upon error.
     * @return List of Department Objects with univid = univid in table between offset & offset + amt
     */
    public static List<Department> lookUpPaginated (Connection sql, int univid, int offset, int amt)
        throws ServiceException {

        OMUtil.sqlCheck(sql);
        ResultSet results = DepartmentTable.lookUpPaginated(sql, univid, offset, amt);
        List<Department> depts = new ArrayList<Department>();

        try {
            while(results.next()) {
                Department dept = new Department(results.getInt("id"),
                                                 results.getInt("univid"),
                                                 results.getString("deptName"),
                                                 results.getString("acronym"));
                depts.add(dept);
            }
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
        return depts;
    }

    /**
     * Get all Courses between offset and offset + amt that belong to this Dept
     * @param connection MySQL database Collection
     * @param offset The first n rows to skip in the table select statement
     * @param amt the amount of Course objects to return (or less if  < amt records exist)
     * @return A List of Courses belonging to this Dept within the bounds of offset and amt
     * @throws A Service Exception if there is a database error
     */
    public List<Course> getAllCoursesPaginated(Connection connection, int offset, int amt)
        throws ServiceException {
            return Course.lookUpPaginated(connection, this.getID(), offset, amt);
    }

    /**
     * Get all Courses belonging to this Dept
     * @param connection MySQL database connection
     * @return a List of All Course Objects associated with this Dept
     */
    public List<Course> getAllCourses(Connection connection) throws ServiceException {
        return this.getAllCoursesPaginated(connection, 0, 2147483647);
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

