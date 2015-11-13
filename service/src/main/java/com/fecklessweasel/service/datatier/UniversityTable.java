package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Date;
import javax.mail.internet.InternetAddress;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Class to represent the university table in the database.
 *
 * @author Elliot Essman
 */
public class UniversityTable {

    private static String INSERT_ROW = "insert into University (longName, acronym, city, state, country) values (?,?,?,?,?)";
    /**
     * Lookup user query.
     */
    public static String LOOKUP_UNIVERSITY_ID_QUERY =
            "SELECT * FROM University WHERE University.id=?";

    /**
     * Insert a university into the table.
     *
     * @param conn     The connection tot he database.
     * @param longName The official name of the university.
     * @param acronym  The acronym of the university.
     * @param city     The city the university is in.
     * @param state    The state the university is ine.
     * @param country  The country the university is in.
     * @return The id of the new university.
     */
    public static int insertUniversity(Connection conn, String longName, String acronym, String city, String state,
                                       String country) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNullOrEmptyOrWhitespace(longName, "longName");
        CodeContract.assertNotNullOrEmptyOrWhitespace(acronym, "acronym");
        CodeContract.assertNotNullOrEmptyOrWhitespace(city, "city");
        CodeContract.assertNotNullOrEmptyOrWhitespace(state, "state");
        CodeContract.assertNotNullOrEmptyOrWhitespace(country, "country");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, longName);
            preparedStatement.setString(2, acronym);
            preparedStatement.setString(3, city);
            preparedStatement.setString(4, state);
            preparedStatement.setString(5, country);
            preparedStatement.executeUpdate();

            // Get new id
            ResultSet result = preparedStatement.getGeneratedKeys();
            result.next();
            int id = result.getInt(1);
            preparedStatement.close();
            return id;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up university given ID
     *
     * @param connection
     * @param univid
     * @return
     * @throws ServiceException
     */
    public static ResultSet lookupUniversityID(Connection connection, int univid)
            throws ServiceException {

        CodeContract.assertNotNull(connection, "connection");
        try {
            PreparedStatement lookupStatement =
                    connection.prepareStatement(LOOKUP_UNIVERSITY_ID_QUERY);
            lookupStatement.setInt(1, univid);

            return lookupStatement.executeQuery();

        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

}
