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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_NullConnection_Exception() throws Exception {
        FileMetadataTable.insertFileData(null,
                             1,
                             1,
                             "CourseName",
                             "Course Desc",
                             new Date(),
                             "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_NullTitle_Exception() throws Exception {
        FileMetadataTable.insertFileData(this.mockConnection,
                             1,
                             1,
                             null,
                             "Course Desc",
                             new Date(),
                             "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_NullDesc_Exception() throws Exception {
        FileMetadataTable.insertFileData(this.mockConnection,
                             1,
                             1,
                             "CourseName",
                             null,
                             new Date(),
                             "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_NullDate_Exception() throws Exception {
        FileMetadataTable.insertFileData(this.mockConnection,
                             1,
                             1,
                             "CourseName",
                             "Course Desc",
                             null,
                             "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_EmptyTitle_Exception() throws Exception {
        FileMetadataTable.insertFileData(this.mockConnection,
                             1,
                             1,
                             "",
                             "Course Desc",
                             new Date(),
                             "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_EmptyDesc_Exception() throws Exception {
        FileMetadataTable.insertFileData(this.mockConnection,
                             1,
                             1,
                             "CourseName",
                             "",
                             new Date(),
                             "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_WhiteSpaceTitle_Exception() throws Exception {
        FileMetadataTable.insertFileData(this.mockConnection,
                             1,
                             1,
                             "           ",
                             "Course Desc",
                             new Date(),
                             "notes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertFileData_WhiteSpaceDescription_Exception() throws Exception {
        FileMetadataTable.insertFileData(this.mockConnection,
                             1,
                             1,
                             "CourseName",
                             "          ",
                             new Date(),
                             "notes");
    }

    @Test
    public void test_InsertUser_SQLConstraintException_ServiceException() throws Exception {
        // Throw exception when insertUser called next:
        when(mockConnection.prepareStatement(FileMetadataTable.INSERT_FILE_QUERY,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenThrow(new SQLException());

        try {
            FileMetadataTable.insertFileData(this.mockConnection,
                                 1,
                                 1,
                                 "CourseName",
                                 "Course Description",
                                 new Date(),
                                 "notes");
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_InsertFileData_SuccessCase() throws Exception {
        final int id = 12345;

        // Throw exception when insertUser called next:
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.getInt(1)).thenReturn(id);

        when(mockPreparedStatement.getGeneratedKeys())
            .thenReturn(mockResultSet);

        when(mockConnection.prepareStatement(FileMetadataTable.INSERT_FILE_QUERY,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenReturn(mockPreparedStatement);


        assert(FileMetadataTable.insertFileData(this.mockConnection,
                                    1,
                                    1,
                                    "CourseName",
                                    "CourseDesc",
                                    new Date(), 
                                    "notes") == id);

        // Verify next was called once to get the generated key.
        verify(mockResultSet, times(1)).next();

        // Verify statement was closed.
        verify(mockPreparedStatement, times(1)).close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_LookUpFile_NullConnection() throws Exception {
        FileMetadataTable.lookUpFile(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_LookUpUserFiles_NullConnection() throws Exception {
        FileMetadataTable.lookUpUserFiles(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_LookUpCourseFiles_NullConnection() throws Exception {
        FileMetadataTable.lookUpCourseFiles(null, 1);
    }

    @Test
    public void test_LookUpFile_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.LOOKUP_FILE_QUERY))
            .thenThrow(new SQLException());

        try {
            FileMetadataTable.lookUpFile(this.mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_LookUpUserFiles_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.LOOKUP_FILES_FROM_USER_QUERY))
            .thenThrow(new SQLException());

        try {
            FileMetadataTable.lookUpUserFiles(this.mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_LookUpCourseFiles_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.LOOKUP_FILES_FROM_COURSE_QUERY))
            .thenThrow(new SQLException());

        try {
            FileMetadataTable.lookUpCourseFiles(this.mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_LookUpFile_SuccessCase() throws Exception {
        final int fileID = 1;

        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.LOOKUP_FILE_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);

        assert(FileMetadataTable.lookUpFile(this.mockConnection,
                                             1) == mockResultSet);

        verify(mockPreparedStatement, times(1)).setInt(1, fileID);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    public void test_LookUpUserFiles_SuccessCase() throws Exception {
        final int userID = 1;

        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.LOOKUP_FILES_FROM_USER_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);

        assert(FileMetadataTable.lookUpUserFiles(this.mockConnection,
                                             1) == mockResultSet);

        verify(mockPreparedStatement, times(1)).setInt(1, userID);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }
    
    @Test
    public void test_LookUpCourseFiles_SuccessCase() throws Exception {
        final int courseID = 1;

        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.LOOKUP_FILES_FROM_COURSE_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);

        assert(FileMetadataTable.lookUpCourseFiles(this.mockConnection,
                                             1) == mockResultSet);

        verify(mockPreparedStatement, times(1)).setInt(1, courseID);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_deleteFile_NullConnection() throws Exception {
        FileMetadataTable.deleteFile(null, 1);
    }

    @Test
    public void test_DeleteFile_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.DELETE_FILE_QUERY))
            .thenThrow(new SQLException());

        try {
            FileMetadataTable.deleteFile(this.mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteFile_FileNotExist() throws Exception {
        final int fileID = 1;
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.DELETE_FILE_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate())
            .thenReturn(0);

        try {
            FileMetadataTable.deleteFile(this.mockConnection, fileID);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_FILE_NOT_EXIST, ex.status);

            verify(mockPreparedStatement, times(1)).setInt(1, fileID);
            verify(mockPreparedStatement, times(1)).executeUpdate();
            return;
        }
        fail("No exception was thrown");
    }

    @Test
    public void test_DeleteFile_SuccessCase() throws Exception {
        final int fileID = 1;
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(FileMetadataTable.DELETE_FILE_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate())
            .thenReturn(1);

        FileMetadataTable.deleteFile(this.mockConnection, fileID);

        verify(mockPreparedStatement, times(1)).setInt(1, fileID);
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).close();
    }
}
