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
import java.util.List;
import java.util.ArrayList;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores all information about a University.
 * @author Elliot Essman
 */
public class University {

    /**
     * Id in the database table.
     */
    private int id;
    /**
     * Official name of the university.
     */
    private String longName;
    /**
     * Acronym or short name of the university.
     */
    private String acronym;
    /**
     * City the university is in.
     */
    private String city;
    /**
     * State the university is in.
     */
    private String state;
    /**
     * Country the university is in.
     */
    private String country;

    /**
     * Max character length for longName.
     */
    private final static int NAME_MAX = 50;
    /**
     * Min character length for longName.
     */
    private final static int NAME_MIN = 5;
    /**
     * Max character length for acronym.
     */
    private final static int ACRONYM_MAX = 5;
    /**
     * Min character length for acronym.
     */
    private final static int ACRONYM_MIN = 2;
    /**
     * Max character length for city.
     */
    private final static int CITY_MAX = 30;
    /**
     * Min character length for city.
     */
    private final static int CITY_MIN = 3;
    /**
     * Max character length for country.
     */
    private final static int COUNTRY_MAX = 30;
    /**
     * Min character length for country.
     */
    private final static int COUNTRY_MIN = 3;

    /**
     * Private countructor for a University. Should be created from the database
     * or create method.
     */
    private University(int id, String longName, String acronym, String city, String state, String country) {
        this.id = id;
        this.longName = longName;
        this.acronym = acronym;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    /**
     * Creates a university in the database.
     *
     * @param conn     Connection to the database.
     * @param longName The official name of the university.
     * @param acronym  The acronym of the university.
     * @param city     The city of the university.
     * @param state    The state of the university.
     * @param country  The country of the university.
     * @return A university object which has been added to the database.
     */
    public static University create(Connection conn, String longName, String acronym, String city, String state,
                                    String country) throws ServiceException {

        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(longName);
        OMUtil.nullCheck(acronym);
        OMUtil.nullCheck(city);
        OMUtil.nullCheck(state);// TODO null check state only when country is
        // USA
        OMUtil.nullCheck(country);

        // university name length
        if (longName.length() > NAME_MAX || longName.length() < NAME_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_UNIV_NAME_LENGTH);
        }
        // university name characters
        if (!OMUtil.isValidName(longName)) {
            throw new ServiceException(ServiceStatus.APP_INVALID_UNIV_NAME_CHARS);
        }

        // university acronym length
        if (acronym.length() > ACRONYM_MAX || acronym.length() < ACRONYM_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_UNIV_ACRONYM_LENGTH);
        }
        // university acronym characters
        if (!acronym.matches("^[a-zA-Z&]*$")) {
            throw new ServiceException(ServiceStatus.APP_INVALID_ACRONYM_CHARS);
        }

        // university city length
        if (city.length() > CITY_MAX || city.length() < CITY_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_CITY_LENGTH);
        }
        // university city characters
        if (!OMUtil.isValidName(city)) {
            throw new ServiceException(ServiceStatus.APP_INVALID_CITY_CHARS);
        }

        // university state
        if (!state.matches("^[A-Z]{2}$")) {
            throw new ServiceException(ServiceStatus.APP_INVALID_STATE);
        }

        // university country length
        if (country.length() > COUNTRY_MAX || country.length() < COUNTRY_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_COUNTRY_LENGTH);
        }
        // university country characters
        if (!OMUtil.isValidName(country)) {
            throw new ServiceException(ServiceStatus.APP_INVALID_COUNTRY_CHARS);
        }

        int id = UniversityTable.insertUniversity(conn, longName, acronym, city, state, country);
        return new University(id, longName, acronym, city, state, country);
    }

    /**
     * Gets the database ID of the university.
     *
     * @return The database ID of the university.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Gets the official name of the university.
     *
     * @return The official name of the university.
     */
    public String getLongName() {
        return this.longName;
    }

    /**
     * Gets the acronym of the university.
     *
     * @return The acronym of the university.
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * Gets the city of the university.
     *
     * @return The city of the university.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Gets the state of the university.
     *
     * @return The state of the university.
     */
    public String getState() {
        return this.state;
    }

    /**
     * Gets the country of the university.
     *
     * @return The country of the university.
     */
    public String getCountry() {
        return this.country;
    }


    /**
     * Gets the University from the database
     * @throws ServiceException Thrown if unable to find the specified university
     * or if another error occurs.
     * @param uid Unique university ID.
     * @return The university object.
     */
    public static University lookup(Connection connection, int uid)
            throws ServiceException {

        // Null check everything:
        OMUtil.sqlCheck(connection);

        //look up university with name
        ResultSet result = UniversityTable.lookupUniversityID(connection, uid);

        // Build University object.
        try {
            if (!result.next()) {
                //unable to find university
                throw new ServiceException(ServiceStatus.APP_USER_NOT_EXIST);
            }

            University university = new University(result.getInt("id"),
                                                   result.getString("longName"),
                                                   result.getString("acronym"),
                                                   result.getString("city"),
                                                   result.getString("state"),
                                                   result.getString("country"));
            result.close();
            return university;
        }
        catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Look up all rows in the university, but add int bounds
     * @param connection MySQL connection
     * @param offset The first x Results to skip
     * @param amt The max amount of University objects returned
     * @throws ServiceException Thrown upon error.
     * @return List of University Objects
     */
    public static List<University> lookUpPaginated(Connection connection, int offset, int amt)
            throws ServiceException {

        OMUtil.sqlCheck(connection);
        ResultSet results = UniversityTable.lookUpPaginated(connection, offset, amt);
        List<University> univs = new ArrayList<University>();

        try {
            while(results.next()) {
                University univ = new University(results.getInt("id"),
                                                 results.getString("longName"),
                                                 results.getString("acronym"),
                                                 results.getString("city"),
                                                 results.getString("state"),
                                                 results.getString("country"));
                univs.add(univ);
            }
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
        return univs;
    }

    /**
     * Return ALL universities
     * @param connection MySQL connection
     * @throws ServiceException Thrown upon error
     * @return List<University> contains ALL universities in the list
     */
    public static List<University> lookUpAll(Connection connection) throws ServiceException {
        // This gets all records from the 0 to the # of records in the table, or 2147483647, whichever is smaller...
        return lookUpPaginated(connection, 0, 2147483647 );
    }

    /**
     * Get all Departments between offset and offset + amt that belong to this University
     * @param connection MySQL database Collection
     * @param offset The first n rows to skip in the table select statement
     * @param amt the amount of Department objects to return (or less if  < amt records exist)
     * @return A List of Departments belonging to this university within the bounds of offset and amt
     * @throws A Service Exception if there is a database error
     */
    public List<Department> getAllDeptsPaginated(Connection connection, int offset, int amt)
        throws ServiceException {
            return Department.lookUpPaginated(connection, this.getID(), offset, amt);
    }

    /**
     * Get all Departments belonging to this university
     * @param connection MySQL database connection
     * @return a List of All Department Objects associated with this university
     */
    public List<Department> getAllDepts(Connection connection) throws ServiceException {
        return this.getAllDeptsPaginated(connection, 0, 2147483647);
    }
}


