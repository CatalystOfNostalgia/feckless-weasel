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

public class test_Department {

    private Connection mockConnection;
    private University mockUniversity;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockUniversity = mock(University.class);
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullConnection() throws Exception {
        Department.create(null, this.mockUniversity, "deptname here", "EECS");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullDeptName() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, null, "EECS");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullAcronym() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, "deptname here", null);
    }

    @Test(expected = ServiceException.class)
    public void test_create_badDeptname_numbers() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, "deptname1234", "EECS");
    }

    @Test(expected = ServiceException.class)
    public void test_create_badAcronym_numbers() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, "deptname here", "33C5");
    }

}