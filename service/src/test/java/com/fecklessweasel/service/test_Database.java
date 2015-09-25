package com.fecklessweasel.service;
//import com.fecklessweasel.service.datatier.SQLSource;
//import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import java.sql.Connection;
import com.fecklessweasel.service.objectmodel.ServiceException;
import java.sql.SQLException;
import com.fecklessweasel.service.datatier.*;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class test_Database {

    @Test
    public void test_insertUniversity() throws Exception{
    	SQLSource.interact(new SQLInteractionInterface(){
    		public void run(Connection conn) throws ServiceException, SQLException {
    			UniversityTable.insert(conn, "uname","uacro","city","state","country");
    		}
    	});
    }
}