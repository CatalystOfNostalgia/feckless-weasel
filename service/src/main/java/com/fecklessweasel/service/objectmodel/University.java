package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;

import com.fecklessweasel.service.datatier.UniversityTable;

/**
 * Stores all information about a University.
 * @author Elliot Essman
 */
public class University {

    /** Id in the database table. */
    private int id;
    /** Official name of the university. */
    private String longName;
    /** Acronym or short name of the university. */
    private String acronym;
    /** City the university is in. */
    private String city;
    /** State the university is in. */
    private String state;
    /** Country the university is in. */
    private String country;

    /** Max character length for longName. */
    private static int NAME_MAX = 50;
    /** Min character length for longName. */
    private static int NAME_MIN = 5;
    /** Max character length for acronym. */
    private static int ACRONYM_MAX = 5;
    /** Min character length for acronym. */
    private static int ACRONYM_MIN = 2;
    /** Max character length for city. */
    private static int CITY_MAX = 30;
    /** Min character length for city. */
    private static int CITY_MIN = 3;
    /** Max character length for country. */
    private static int COUNTRY_MAX = 30;
    /** Min character length for country. */
    private static int COUNTRY_MIN = 3;

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
     * @param conn Connection to the database.
     * @param longName The official name of the university.
     * @param acronym The acronym of the university.
     * @param city The city of the university.
     * @param state The state of the university.
     * @param country The country of the university.
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
     * @return The database ID of the university.
     */
    protected int getID() {
        return this.id;
    }

    /**
     * Gets the official name of the university.
     * @return The official name of the university.
     */
    public String getlongName() {
        return this.longName;
    }

    /**
     * Gets the acronym of the university.
     * @return The acronym of the university.
     */
    public String getAcronym() {
        return this.acronym;
    }

    /**
     * Gets the city of the university.
     * @return The city of the university.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Gets the state of the university.
     * @return The state of the university.
     */
    public String getState() {
        return this.state;
    }

    /**
     * Gets the country of the university.
     * @return The country of the university.
     */
    public String getCountry() {
        return this.country;
    }
}
