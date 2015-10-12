package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;

import com.fecklessweasel.service.objectmodel.ServiceException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class test_UniversityTable {

    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUser_nullConnection() throws Exception {
        UniversityTable.insertUniversity(null, "uname", "uacro", "city", "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUser_nullLongname() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, null, "uacro", "city", "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUser_nullAcronym() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", null, "city", "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUser_nullCity() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", "uacro", null, "state", "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUser_nullState() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", "uacro", "city", null, "country");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertUser_nullCountry() throws Exception {
        UniversityTable.insertUniversity(this.mockConnection, "uname", "uacro", "city", "state", null);
    }
}