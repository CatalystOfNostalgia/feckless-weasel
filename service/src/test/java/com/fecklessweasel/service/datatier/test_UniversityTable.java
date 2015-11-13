package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fecklessweasel.service.objectmodel.ServiceException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.mysql.jdbc.Statement;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class test_UniversityTable {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockPreparedStatement = mock(PreparedStatement.class);
        this.mockResultSet = mock(ResultSet.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUniversity_nullConnection() throws Exception {
        UniversityTable.insertUniversity(null, "uname", "uacro", "city", "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUniversity_nullLongname() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, null, "uacro", "city", "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUniversity_nullAcronym() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", null, "city", "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUniversity_nullCity() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", "uacro", null, "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUniversity_nullState() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", "uacro", "city", null, "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUniversity_nullCountry() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", "uacro", "city", "state", null);
    }

    @Test
    public void test_insertUniversity_SQLException() throws Exception {
        when(this.mockConnection.prepareStatement(UniversityTable.INSERT_ROW,
                                                  Statement.RETURN_GENERATED_KEYS))
                .thenThrow(new SQLException());

        try {
            UniversityTable.insertUniversity(this.mockConnection,
                                             "uname",
                                             "uacro",
                                             "city",
                                             "state",
                                             "country");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_insertUniversity_Success() throws Exception {
        String universityName = "uname";
        String universityAcro = "uacro";
        String city = "city";
        String state = "state";
        String country = "country";

        when(this.mockConnection.prepareStatement(UniversityTable.INSERT_ROW,
                                                  Statement.RETURN_GENERATED_KEYS))
                .thenReturn(this.mockPreparedStatement);
        when(this.mockPreparedStatement.getGeneratedKeys())
                .thenReturn(this.mockResultSet);

        int id = UniversityTable.insertUniversity(this.mockConnection,
                                                  universityName,
                                                  universityAcro,
                                                  city,
                                                  state,
                                                  country);

        verify(this.mockPreparedStatement, times(1)).setString(1, universityName);
        verify(this.mockPreparedStatement, times(1)).setString(2, universityAcro);
        verify(this.mockPreparedStatement, times(1)).setString(3, city);
        verify(this.mockPreparedStatement, times(1)).setString(4, state);
        verify(this.mockPreparedStatement, times(1)).setString(5, country);
        assertEquals(id, 0);
    }

    @Test
    public void test_lookupUniversityID_SQLException() throws Exception {
        when(this.mockConnection.prepareStatement(UniversityTable
                                                  .LOOKUP_UNIVERSITY_ID_QUERY))
                .thenThrow(new SQLException());

        try {
            UniversityTable.lookupUniversityID(this.mockConnection, 1);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_lookupUniversityID_Success() throws Exception {
        int univid = 1;
        when(this.mockConnection.prepareStatement(UniversityTable
                                                  .LOOKUP_UNIVERSITY_ID_QUERY))
                .thenReturn(this.mockPreparedStatement);
        when(this.mockPreparedStatement.executeQuery())
                .thenReturn(this.mockResultSet);

        ResultSet result = UniversityTable.lookupUniversityID(this.mockConnection, univid);

        verify(this.mockPreparedStatement, times(1)).setInt(1, univid);
        assertEquals(result, this.mockResultSet);
    }

    @Test
    public void test_lookupPaginated_SQLExcpetion() throws Exception {
        when(this.mockConnection.prepareStatement(UniversityTable .SELECT_PAGINATED))
                .thenThrow(new SQLException());

        try {
            UniversityTable.lookUpPaginated(this.mockConnection, 0, 1);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_lookupPaginated_Success() throws Exception {
        int offset = 0;
        int amt = 1;
        when(this.mockConnection.prepareStatement(UniversityTable.SELECT_PAGINATED))
                .thenReturn(this.mockPreparedStatement);
        when(this.mockPreparedStatement.executeQuery())
                .thenReturn(this.mockResultSet);

        ResultSet result = UniversityTable.lookUpPaginated(this.mockConnection, offset, amt);

        verify(this.mockPreparedStatement, times(1)).setInt(1, offset);
        verify(this.mockPreparedStatement, times(1)).setInt(2, amt);
        assertEquals(result, this.mockResultSet);
    }
}