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

public class test_Rating {

    private Connection mockConnection;
    private User mockUser;
    private FileMetadata mockFile;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockUser = mock(User.class);
        this.mockFile = mock(FileMetadata.class);
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullConnection() throws Exception {
        Rating.Create(null, this.mockUser, this.mockFile, 3);
    }
    
    @Test(expected = ServiceException.class)
    public void test_create_nullUser() throws Exception {
        Rating.Create(this.mockConnection, null, this.mockFile, 3);
    }
    
    @Test(expected = ServiceException.class)
    public void test_create_nullFile() throws Exception {
        Rating.Create(this.mockConnection, this.mockUser, null, 3);
    }
    
    @Test(expected = ServiceException.class)
    public void test_create_negative_rating() throws Exception {
        Rating.Create(this.mockConnection, this.mockUser, this.mockFile, -3);
    }

}