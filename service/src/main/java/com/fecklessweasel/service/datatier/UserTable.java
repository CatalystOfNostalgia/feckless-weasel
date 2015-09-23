package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import javax.mail.internet.InternetAddress;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

public abstract class UserTable {
    /** Create user query. */
    private static final String INSERT_USER_QUERY =
        "INSERT INTO User (user, pass, first_name, last_name," +
        "join_date, email)" +
        " VALUES (?,?,?,?,?,?)";

    public static int insertUser(Connection connection,
                                 String user,
                                 String pass,
                                 String firstName,
                                 String lastName,
                                 LocalDate joinDate,
                                 InternetAddress email)
        throws ServiceException {

        try {
            PreparedStatement insertStatement
                = connection.prepareStatement(INSERT_USER_QUERY);
            insertStatement.setString(1, user);
            insertStatement.setString(2, pass);
            insertStatement.setString(3, firstName);
            insertStatement.setString(4, lastName);
            insertStatement.setDate(5, new java.sql.Date(joinDate.getTime()));
            insertStatement.setString(6, email.getAddress());

            insertStatement.execute();

            // Get autoincrement row id.
            ResultSet result = insertStatement.getGeneratedKeys();
            result.next();

            int uid = result.getInt(1);

            insertStatement.close();

            return uid;
        } catch (SQLIntegrityConstraintViolationException ex) {
            // We have no foreign or unique keys other than primary
            // so this can only be thrown for duplicate users.
            throw new ServiceException(ServiceStatus.APP_USERNAME_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
