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

public class test_Course {

    private Connection mockConnection;
    private Department mockDepartment;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockDepartment = mock(Department.class);
    }
}
