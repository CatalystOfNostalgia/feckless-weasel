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

public class test_Comment {

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
        Comment.Create(null, this.mockUser, this.mockFile, "hello");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullUser() throws Exception {
        Comment.Create(this.mockConnection, null, this.mockFile, "hello");
    }
    
    @Test(expected = ServiceException.class)
    public void test_create_nullFile() throws Exception {
        Comment.Create(this.mockConnection, this.mockUser, null, "hello");
    }
    
    @Test(expected = ServiceException.class)
    public void test_create_nullText() throws Exception {
        Comment.Create(this.mockConnection, this.mockUser, this.mockFile, null);
    }
    
    @Test(expected = ServiceException.class)
    public void test_create_emptyText() throws Exception {
        Comment.Create(this.mockConnection, this.mockUser, this.mockFile, "");
    }

}