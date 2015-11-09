package com.fecklessweasel.service.datatier;

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
 * Unit tests for FileMetadataTable validation and exception handling
 */
public class test_FileMetadataTable {
    private Connection mockConnection;

    @Before
    public void setup(){
        this.mockConnection = mock(Connection.class);
    }

  // @Test(expected = IllegalArgumentException.class)
  // public void test_InsertFile_NullUser_Exception() throws Exception{
  //   FileMetadataTable.insertFileData(this.mockConnection,
  //                                    null,
  //                                    393,
  //                                    new Date());
  // }
  //
  // @Test(expected = IllegalArgumentException.class)
  // public void test_InsertFile_NullCourse_Exception() throws Exception{
  //   FileMetadataTable.insertFileData(this.mockConnection,
  //                                    1,
  //                                    null,
  //                                    new Date());
  // }



    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFile_NullDate_Exception() throws Exception{
        FileMetadataTable.insertFileData(this.mockConnection,
                                         1,
                                         393,
                                         null,
                                         "ex",
                                         "ex desc");
    }


    @Test
    public void test_InsertFile_SQLConstraintException_ServiceException() throws Exception {
        // Throw exception when insertFileData called next (since ResultSet should have only 1 file in it)
        when(mockConnection.prepareStatement(FileMetadataTable.INSERT_FILE_QUERY,
                                            Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
        try {
            FileMetadataTable.insertFileData(this.mockConnection,
                                                1,
                                                393,
                                                new Date(),
                                                "ex",
                                                "ex desc");
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.DATABASE_ERROR);
        }
    }
}
