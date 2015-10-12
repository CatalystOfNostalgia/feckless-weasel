package com.fecklessweasel.service.objectmodel;

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

public class test_University {

    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullConnection() throws Exception {
        University.create(null, "longname", "ACRO", "city", "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullLongname() throws Exception {
        University.create(this.mockConnection, null, "ACRO", "city", "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullAcronym() throws Exception {
        University.create(this.mockConnection, "longname", null, "city", "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullCity() throws Exception {
        University.create(this.mockConnection, "longname", "ACRO", null, "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullState() throws Exception {
        University.create(this.mockConnection, "longname", "ACRO", "city", null, "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullCountry() throws Exception {
        University.create(this.mockConnection, "longname", "ACRO", "city", "OH", null);
    }

    @Test(expected = ServiceException.class)
    public void test_create_badAcronym() throws Exception {
        University.create(this.mockConnection, "longname", "ACROISTOOLONG", "city", "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_badState() throws Exception {
        University.create(this.mockConnection, "longname", "ACRO", "city", "OHithink", "country here");
    }

}