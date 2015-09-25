package com.fecklessweasel.service.datatier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import javax.mail.internet.InternetAddress;
import java.util.Date;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for UserTable validation and exception handling. Does
 * not validate SQL queries.
 */
public class test_UserTable {
    private Connection mockConnection;
    
    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullConnection_Exception() throws Exception {
        UserTable.insertUser(null,
                             "user",
                             "pass",
                             "firstName",
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullUser_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             null,
                             "pass",
                             "firstName",
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullPass_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             null,
                             "firstName",
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullFirstName_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             null,
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullLastName_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             "firstName",
                             null,
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullDate_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             "firstName",
                             "lastName",
                             null,
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullEmail_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             "firstName",
                             "lastName",
                             new Date(),
                             null);
    }





    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_EmptyPass_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "",
                             "firstName",
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_EmptyFirstName_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             "",
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_EmptyLastName_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             "firstName",
                             "",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_WhitespacePass_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "  ",
                             "firstName",
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_WhitespaceFirstName_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             "  ",
                             "lastName",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_WhitespaceLastName_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             "firstName",
                             "  ",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test
    public void test_InsertUser_SQLConstraintException_ServiceException() throws Exception {
        // Throw exception when insertUser called next:
        when(mockConnection.prepareStatement(UserTable.INSERT_USER_QUERY,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenThrow(new SQLIntegrityConstraintViolationException());

        try {
            UserTable.insertUser(this.mockConnection,
                                 "user",
                                 "pass",
                                 "firstName",
                                 "lastName",
                                 new Date(),
                                 new InternetAddress("gundermanc@gmail.com"));
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.APP_USERNAME_TAKEN);
        }
    }

    @Test
    public void test_InsertUser_SQLException_ServiceException() throws Exception {
        // Throw exception when insertUser called next:
        when(mockConnection.prepareStatement(UserTable.INSERT_USER_QUERY,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenThrow(new SQLException());
        
        try {
            UserTable.insertUser(this.mockConnection,
                                 "user",
                                 "pass",
                                 "firstName",
                                 "lastName",
                                 new Date(),
                                 new InternetAddress("gundermanc@gmail.com"));
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.DATABASE_ERROR);
        }
    }
}
