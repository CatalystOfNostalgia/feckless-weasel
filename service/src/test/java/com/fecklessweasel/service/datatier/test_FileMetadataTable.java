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

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_NullUser_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     null,
                                     "EECS 393",
                                     "university",
                                     new Date(),
                                     0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_NullCourse_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "user",
                                     null,
                                     "university",
                                     new Date(),
                                     0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_NullUniversity_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "user",
                                     "course",
                                     null,
                                     new Date(),
                                     0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_NullDate_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "user",
                                     "course",
                                     "university",
                                     null,
                                     0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_EmptyUser_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "",
                                     "course",
                                     "university",
                                     new Date(),
                                     0);
  }
  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_EmptyCourse_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "user",
                                     "",
                                     "university",
                                     new Date(),
                                     0);
  }
  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_EmptyUniversity_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "user",
                                     "course",
                                     "",
                                     new Date(),
                                     0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_WhitespaceUser_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "       ",
                                     "course",
                                     "university",
                                     new Date(),
                                     0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_WhitespaceCourse_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "user",
                                     "      ",
                                     "university",
                                     new Date(),
                                     0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_InsertFile_WhitespaceUniversity_Exception() throws Exception{
    FileMetadataTable.insertFileData(this.mockConnection,
                                     "user",
                                     "course",
                                     "          ",
                                     new Date(),
                                     0);
  }

  @Test
  public void test_InsertFile_SQLConstraintException_ServiceException() throws Exception {
    // Throw exception when insertFileData called next (since ResultSet should have only 1 file in it)
    when(mockConnection.prepareStatement(FileMetadataTable.INSERT_FILE_QUERY,
                                        Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
    try {
      FileMetadataTable.insertFileData(this.mockConnection,
                                      "user",
                                      "course",
                                      "university",
                                      new Date(),
                                      0);
    } catch (ServiceException ex) {
      assert(ex.status == ServiceStatus.DATABASE_ERROR);
    }
  }
}
