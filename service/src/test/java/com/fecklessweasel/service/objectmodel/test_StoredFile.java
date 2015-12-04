package com.fecklessweasel.service.objectmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fecklessweasel.service.datatier.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.internet.InternetAddress;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CourseTable.class, DepartmentTable.class, FileMetadataTable.class, UniversityTable.class, UserHasRoleTable.class, UserRoleTable.class, UserTable.class})
public class test_StoredFile {
    private Connection mockConnection;
    private ResultSet mockResultSet;
    private InputStream mockInputStream;

    private Course testCourse;
    private User testUser;

    private University createTestUni() throws Exception {
        String longName = "Case Western";
        String acronym = "CWRU";
        String city = "Cleveland";
        String state = "OH";
        String country = "USA";

        PowerMockito.when(UniversityTable.insertUniversity(
                mockConnection, longName, acronym, city, state, country))
                .thenReturn(1);

        return University.create(
                mockConnection,
                "Case Western",
                "CWRU",
                "Cleveland",
                "OH",
                "USA"
        );
    }

    private Department createTestDept() throws Exception {
        int uniID = 1;
        String deptName = "Computer Science";
        String acronym = "EECS";

        PowerMockito.when(DepartmentTable.insertDepartment(
                mockConnection, uniID, deptName, acronym))
                .thenReturn(1);

        return Department.create(
                mockConnection,
                createTestUni(),
                deptName,
                acronym
        );
    }

    private Course createTestCourse() throws Exception {
        int courseNum = 111;
        String courseName = "Test Course";

        PowerMockito.when(CourseTable.insertCourse(
                mockConnection, 1, courseNum, courseName))
                .thenReturn(1);

        return this.testCourse = Course.create(
                mockConnection,
                createTestDept(),
                courseNum,
                courseName);
    }

    private User createTestUser() throws Exception {
        String username = "testuser";
        String password = "password";
        String email = "email@gmail.com";

        PowerMockito.when(UserTable.insertUser(
                mockConnection,
                username,
                password,
                new Date(),
                new InternetAddress(email)))
                .thenReturn(1);

        PowerMockito.doNothing().when(UserRoleTable.class, "insertUserRole",
                any(Connection.class),
                any(String.class),
                any(String.class));

        return User.create(
                mockConnection, "testuser", "password", "email@email.com"
        );
    }

    @Before
    public void setup() throws Exception {
        this.mockConnection = mock(Connection.class);
        this.mockResultSet = mock(ResultSet.class);
        this.mockInputStream = mock(InputStream.class);

        PowerMockito.mockStatic(CourseTable.class);
        PowerMockito.mockStatic(DepartmentTable.class);
        PowerMockito.mockStatic(FileMetadataTable.class);
        PowerMockito.mockStatic(UserTable.class);
        PowerMockito.mockStatic(UserRoleTable.class);
        PowerMockito.mockStatic(UserHasRoleTable.class);
        PowerMockito.mockStatic(UniversityTable.class);

        this.testCourse = createTestCourse();
        this.testUser = createTestUser();
    }

    @Test
    public void test_create_nullConnection() throws Exception {
        try {
            StoredFile.create(
                    null,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "description",
                    "tag",
                    "ext",
                    mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_create_nullUser() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    null,
                    this.testCourse,
                    "title",
                    "description",
                    "tag",
                    "ext",
                    mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_create_nullCourse() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    null,
                    "title",
                    "description",
                    "tag",
                    "ext",
                    mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_create_nullTitle() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    null,
                    "description",
                    "tag",
                    "ext",
                    mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_create_nullDescription() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    null,
                    "tag",
                    "ext",
                    mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_create_nullExtension() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "description",
                    "tag",
                    null,
                    mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_create_nullFileStream() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "description",
                    "tag",
                    "ext",
                    null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_nullMarkdownTest() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "description",
                    null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No exception thrown");
    }


    @Test
    public void test_create_titleTooShort() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "",
                    "description",
                    "text");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_TITLE_LENGTH, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_titleTooLong() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "thistitleiswaytooooooooooooooooooooooooooooooooooooooooo" +
                    "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                    "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                    "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                    "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                    "oooooooooooooooooooooooooooooooooooooooooooooooooooooooolong",
                    "description",
                    "text");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_TITLE_LENGTH, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_descriptionTooShort() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "",
                    "text");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_DESCRIPTION_LENGTH, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_descriptionTooLong() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "thisdescriptioniswaytooooooooooooooooooooooooooooooooooooooooo" +
                            "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "oooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "oooooooooooooooooooooooooooooooooooooooooooooooooooooooolong",
                    "text");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_DESCRIPTION_LENGTH, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_extensionTooShort() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "description",
                    "tag",
                    "",
                    this.mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EXTENSION_LENGTH, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_extensionTooLong() throws Exception {
        try {
            StoredFile.create(
                    this.mockConnection,
                    this.testUser,
                    this.testCourse,
                    "title",
                    "description",
                    "tag",
                    "toolongofextension",
                    this.mockInputStream);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EXTENSION_LENGTH, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_lookup_nullConnection() throws Exception {
        try {
            StoredFile.lookup(null, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
        }
    }

    @Test
    public void test_lookup_SQLException() throws Exception {
        PowerMockito.when(FileMetadataTable.lookUpFile(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenThrow(new SQLException());

        try {
            StoredFile.lookup(mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookup_noFile() throws Exception {
        PowerMockito.when(FileMetadataTable.lookUpFile(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(false);

        try {
            StoredFile.lookup(mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_FILE_NOT_EXIST);
        }
    }

    @Test
    public void test_lookup_success() throws Exception {
        int fid = 1;
        int uid = 1;
        int cid = 1;
        java.sql.Date date = new java.sql.Date(1);
        String title = "title";
        String description = "description";
        String tag = "tag";
        String extension = "md";

        PowerMockito.when(FileMetadataTable.lookUpFile(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true);
        when(mockResultSet.getInt("fid")).thenReturn(fid);
        when(mockResultSet.getInt("uid")).thenReturn(uid);
        when(mockResultSet.getInt("cid")).thenReturn(cid);
        when(mockResultSet.getDate("creation_date")).thenReturn(date);
        when(mockResultSet.getString("title")).thenReturn(title);
        when(mockResultSet.getString("description")).thenReturn(description);
        when(mockResultSet.getString("tag")).thenReturn(tag);
        when(mockResultSet.getString("extension")).thenReturn(extension);

        StoredFile file = StoredFile.lookup(mockConnection, 1);
        assertEquals(fid, file.getID());
        assertEquals(title, file.getTitle());
        assertEquals(description, file.getDescription());
        assertEquals(tag, file.getTag());
        assertEquals(extension, file.getExtension());
    }

    @Test
    public void test_lookupCourseFiles_nullConnection() throws Exception {
        try {
            StoredFile.lookupCourseFiles(null, testCourse);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_lookupCourseFiles_nullCourse() throws Exception {
        try {
            StoredFile.lookupCourseFiles(mockConnection, null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("No exception thrown");

    }

    @Test
    public void test_lookupCourseFiles_SQLException() throws Exception {
        PowerMockito.when(FileMetadataTable.lookUpCourseFiles(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenThrow(new SQLException());

        try {
            StoredFile.lookupCourseFiles(mockConnection, testCourse);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookupUserFiles_nullConnection() throws Exception {
        try {
            StoredFile.lookupUserFiles(null, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_lookupUserFiles_SQLException() throws Exception {
        PowerMockito.when(FileMetadataTable.lookUpUserFiles(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenThrow(new SQLException());

        try {
            StoredFile.lookupUserFiles(mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookupUserFiles_success() throws Exception {
        int fid = 1;
        int uid = 1;
        int cid = 1;
        java.sql.Date date = new java.sql.Date(1);
        String title = "title";
        String description = "description";
        String tag = "tag";
        String extension = "md";

        PowerMockito.when(FileMetadataTable.lookUpUserFiles(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);
        when(mockResultSet.getInt("fid")).thenReturn(fid);
        when(mockResultSet.getInt("uid")).thenReturn(uid);
        when(mockResultSet.getInt("cid")).thenReturn(cid);
        when(mockResultSet.getDate("creation_date")).thenReturn(date);
        when(mockResultSet.getString("title")).thenReturn(title);
        when(mockResultSet.getString("description")).thenReturn(description);
        when(mockResultSet.getString("tag")).thenReturn(tag);
        when(mockResultSet.getString("extension")).thenReturn(extension);

        Iterable<StoredFile> fileList = StoredFile.lookupUserFiles(mockConnection, 1);
        StoredFile file = null;
        for (StoredFile f : fileList) {
            file = f;
        }
        assertEquals(fid, file.getID());
        assertEquals(title, file.getTitle());
        assertEquals(description, file.getDescription());
        assertEquals(tag, file.getTag());
        assertEquals(extension, file.getExtension());
    }

    @Test
    public void test_lookupUserNotes_nullConnection() throws Exception {
        try {
            StoredFile.lookupUserNotes(null, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_lookupUserNotes_SQLException() throws Exception {
        PowerMockito.when(FileMetadataTable.lookUpUserNotes(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenThrow(new SQLException());

        try {
            StoredFile.lookupUserNotes(mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookupUserNotes_success() throws Exception {
        int fid = 1;
        int uid = 1;
        int cid = 1;
        java.sql.Date date = new java.sql.Date(1);
        String title = "title";
        String description = "description";
        String tag = "tag";
        String extension = "md";

        PowerMockito.when(FileMetadataTable.lookUpUserNotes(mockConnection, 1))
                .thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);
        when(mockResultSet.getInt("fid")).thenReturn(fid);
        when(mockResultSet.getInt("uid")).thenReturn(uid);
        when(mockResultSet.getInt("cid")).thenReturn(cid);
        when(mockResultSet.getDate("creation_date")).thenReturn(date);
        when(mockResultSet.getString("title")).thenReturn(title);
        when(mockResultSet.getString("description")).thenReturn(description);
        when(mockResultSet.getString("tag")).thenReturn(tag);
        when(mockResultSet.getString("extension")).thenReturn(extension);

        Iterable<StoredFile> fileList = StoredFile.lookupUserNotes(mockConnection, 1);
        StoredFile file = null;
        for (StoredFile f : fileList) {
            file = f;
        }
        assertEquals(fid, file.getID());
        assertEquals(title, file.getTitle());
        assertEquals(description, file.getDescription());
        assertEquals(tag, file.getTag());
        assertEquals(extension, file.getExtension());
    }
}
