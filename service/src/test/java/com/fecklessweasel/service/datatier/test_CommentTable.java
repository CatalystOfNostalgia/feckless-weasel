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

public class test_CommentTable {

    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullConnection() throws Exception {
        CommentTable.addComment(null, 9, 9);
    }

}