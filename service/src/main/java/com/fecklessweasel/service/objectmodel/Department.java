package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

import com.fecklessweasel.service.datatier.DepartmentTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all information about a school's department.
 * @author Elliot Essman
 */
public class Department {

    /** ID in the database table. */
    private int id;
    /** The university this department is in. */
    private University university;
    /** Acronym or short name of this department. */
    private String acronym;
    /** Official name of the department. */
    private String deptName;

    /** Max character length of the official name. */
    private static int DEPTNAME_MAX = 50;
    /** Min character length of the official name. */
    private static int DEPTNAME_MIN = 4;

    /**
     * Private constructor. Should be created from the database or create
     * method.
     */
    private Department(int id, University university, String deptName, String acronym) {
        this.id = id;
        this.university = university;
        this.deptName = deptName;
        this.acronym = acronym;
    }

    /**
     * Creates a department in the database.
     * @param conn A connection to the database.
     * @param university The university this department is in.
     * @param deptname The official name of the department.
     * @param acronym The acronym of the department.
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
        return new Department(id, university, deptName, acronym);
    }

    /**
     *@Author Hayden Schmackpfeffer
     *@param connection the SQL Connection
     *@param univ University object model
     *@return all departments belonging to passed in university
     */
    public static ArrayList<Department> getAllInUniversity(Connection conn, University univ)
        throws ServiceException {

        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(univ);

        ArrayList<Department> depts = new ArrayList<Department>();
        ResultSet results = DepartmentTable.lookupAllInUniversity(conn, univ.getID());

        try{
            while (results.next()) {
                Department dept = new Department(results.getInt("id"),
                                    univ,
                                    results.getString("deptName"),
                                    results.getString("acronym"));
                depts.add(dept);
            }
            results.close();
            return depts;
        } catch (SQLException ex){
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Gets the database ID of the department.
     * @return The database ID of the department.
     */
    protected int getID() {
        return this.id;
    }

    /**
     * Gets The university this department is in.
     * @return The university this department is in.
     */
    public University getUniversity() {
        return this.university;
    }

    /**
     * Gets the official department name.
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
}
