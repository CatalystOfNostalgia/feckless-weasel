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

public class test_Database {

	private Connection mockConnection;
	
	@Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }
	
    @Test
    public void test_insertUniversity() throws Exception{
    	UniversityTable.insert(this.mockConnection, "uname","uacro","city","state","country");
    }
}