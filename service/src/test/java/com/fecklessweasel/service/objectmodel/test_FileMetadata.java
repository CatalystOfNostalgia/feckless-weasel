package com.fecklessweasel.service.objectmodel;

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
import java.util.Date;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
  * Unit tests for the object model FileMetadata
  */
public class test_FileMetadata {
    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }
/** TODO: UPDATE TO REFLECT THAT YOU CREATE WITH A USER INSTEAD OF AN INT
    public void test_create_NullSQL_Exception() throws Exception {
        try {
            FileMetadata.create(null,
                      1,
                      393,
                      new Date());
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }
  */
}
