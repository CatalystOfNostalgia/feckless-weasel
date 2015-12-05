package com.fecklessweasel.service.objectmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.io.InputStream;

import com.fecklessweasel.service.datatier.*;
import com.fecklessweasel.service.objectmodel.*;

/**
 * Unit tests for object model User.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CourseTable.class,
                 DepartmentTable.class,
                 FileMetadataTable.class,
                 UniversityTable.class,
                 UserHasRoleTable.class,
                 UserRoleTable.class,
                 UserTable.class,
                 FavoritesTable.class,
                 StoredFile.class,
                 Course.class,
                 University.class,
                 Department.class})
public class test_User {
    private Connection mockConnection;
    private InputStream mockInputStream;
    private Course testCourse;
    private StoredFile testFile;
    private Department testDepartment;
    private University testUniversity;
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

        return Course.create(
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

    private StoredFile createTestFile() throws Exception {
    // public static StoredFile create(Connection sql, User user,Course course,String title,
        // String description,String tag,String extension,InputStream fileData) throws ServiceException {
        String title = "FileTitle";
        String description = "This is a description";
        String extension = "pdf";

        PowerMockito.when(FileMetadataTable.insertFileData(
                eq(mockConnection),
                anyInt(),       //uid
                anyInt(),       //courseId
                anyString(),    //title
                anyString(),    //description
                any(Date.class),
                anyString(),    //tag
                anyString()    //extension
                )).thenReturn(1);

        PowerMockito.when(StoredFile.class, "saveFile",
                any(InputStream.class),
                anyString()).thenReturn(true);

        return StoredFile.create(mockConnection, this.testUser, this.testCourse,
                                 title, description, "tag", extension, mockInputStream);
    }

    @Before
    public void setup() throws Exception {
        this.mockConnection = mock(Connection.class);
        this.mockInputStream = mock(InputStream.class);
        PowerMockito.mockStatic(UserTable.class);
        PowerMockito.mockStatic(UserHasRoleTable.class);
        PowerMockito.mockStatic(UserRoleTable.class);
        PowerMockito.mockStatic(FavoritesTable.class);
        PowerMockito.mockStatic(FileMetadataTable.class);
        PowerMockito.mockStatic(UniversityTable.class);
        PowerMockito.mockStatic(DepartmentTable.class);
        PowerMockito.mockStatic(CourseTable.class);
        PowerMockito.mockStatic(StoredFile.class);
        // PowerMockito.mockStatic(University.class);
        // // PowerMockito.mockStatic(Department.class);
        // PowerMockito.mockStatic(Course.class);

        this.testUser = createTestUser();
        this.testUniversity = createTestUni();
        this.testDepartment = createTestDept();
        this.testCourse = createTestCourse();

        // this.testFile = createTestFile();
    }

    @Test
    public void test_create_NullSQL_Exception() throws Exception {
        try {
            User.create(null,
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_NullUser_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        null,
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_NullPassword_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        null,
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_NullEmail_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_TooShortUser_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gunde",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USER_LENGTH, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_TooLongUser_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZA",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USER_LENGTH, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_UserWithSpaces_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gunderman c",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USERNAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_UserWithSymbols_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gunderman$c",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USERNAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_PasswordTooShort_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "12345",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_PASS_LENGTH, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_PasswordWithSpaces_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha nice try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_PASSWORD, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@gmail.");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail2_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail3_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@sfsf@sfsf.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail4_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_EmailTooLong_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMN@PQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDE.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_Success() throws Exception {
        PowerMockito.when(UserTable.insertUser(this.mockConnection,
                                  "gundermanc",
                                  "haha_nice_try",
                                  new Date(),
                                  new InternetAddress("gundermanc@gmail.com"))).thenReturn(1);

        PowerMockito.doNothing().when(UserRoleTable.class, "insertUserRole",
                                      any(Connection.class),
                                      any(String.class),
                                      any(String.class));

        // Make sure no exceptions are thrown in proper case.
        User user = User.create(this.mockConnection,
                                "gundermanc",
                                "haha_nice_try",
                                "gundermanc@gmail.com");

        //assertEquals(1, user.getID());
        assertEquals("gundermanc", user.getUsername());
        assertEquals(OMUtil.sha256("haha_nice_try"), user.getPasswordHash());
        assertEquals("gundermanc@gmail.com", user.getEmail());
        assertTrue(user.isRole("ROLE_USER"));
    }

    @Test
    public void test_lookup_NullSQL_Exception() throws Exception {
        try {
            User.lookup(null,
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_lookup_NullUser_Exception() throws Exception {
        try {
            User.lookup(mock(Connection.class),
                        null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_lookup_UserNotExist() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(false);
        PowerMockito.when(UserTable.lookupUserWithRoles(this.mockConnection,
                                                        "gundermanc")).thenReturn(mockResultSet);

        try {
            User.lookup(this.mockConnection,
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_USER_NOT_EXIST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_lookup_SQLException() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenThrow(new SQLException());
        PowerMockito.when(UserTable.lookupUserWithRoles(this.mockConnection,
                                                        "gundermanc")).thenReturn(mockResultSet);
        try {
            // Make sure no exceptions are thrown in proper case.
            User user = User.lookup(this.mockConnection,
                                    "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
            return;
        }

        fail("no exception was thrown");
    }

    @Test
    public void test_lookup_Success() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(false);
        when(mockResultSet.getInt("uid")).thenReturn(2);
        when(mockResultSet.getString("user")).thenReturn("gundermanc");
        when(mockResultSet.getString("pass")).thenReturn("haha_nice_try");
        when(mockResultSet.getDate("join_date")).thenReturn(date);
        when(mockResultSet.getString("email")).thenReturn("gundermanc@gmail.com");
        when(mockResultSet.getString("role")).thenReturn(UserRoleTable.ROLE_USER_ID);
        when(mockResultSet.getString("description")).thenReturn(UserRoleTable.ROLE_USER_DESCRIPTION);

        PowerMockito.when(UserTable.lookupUserWithRoles(this.mockConnection,
                                                        "gundermanc")).thenReturn(mockResultSet);

        // Make sure no exceptions are thrown in proper case.
        User user = User.lookup(this.mockConnection,
                                "gundermanc");

        assertEquals("gundermanc", user.getUsername());
        assertEquals("haha_nice_try", user.getPasswordHash());
        assertEquals("gundermanc@gmail.com", user.getEmail());
        assertEquals(date, user.getJoinDate());
    }

    @Test
    public void test_lookupById_NullSQL_Exception() throws Exception {
        try {
            User.lookupById(null,
                            1);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_lookupById_UserNotExist() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(false);
        PowerMockito.when(UserTable.lookupUserWithRolesById(this.mockConnection,
                                                            2)).thenReturn(mockResultSet);

        try {
            User.lookupById(this.mockConnection,
                            2);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_USER_NOT_EXIST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_lookupById_SQLException() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenThrow(new SQLException());
        PowerMockito.when(UserTable.lookupUserWithRolesById(this.mockConnection,
                                                            1)).thenReturn(mockResultSet);
        try {
            // Make sure no exceptions are thrown in proper case.
            User user = User.lookupById(this.mockConnection,
                                    1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
            return;
        }

        fail("no exception was thrown");
    }

    @Test
    public void test_lookupById_Success() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(false);
        when(mockResultSet.getInt("uid")).thenReturn(2);
        when(mockResultSet.getString("user")).thenReturn("gundermanc");
        when(mockResultSet.getString("pass")).thenReturn("haha_nice_try");
        when(mockResultSet.getDate("join_date")).thenReturn(date);
        when(mockResultSet.getString("email")).thenReturn("gundermanc@gmail.com");
        when(mockResultSet.getString("role")).thenReturn(UserRoleTable.ROLE_USER_ID);
        when(mockResultSet.getString("description")).thenReturn(UserRoleTable.ROLE_USER_DESCRIPTION);

        PowerMockito.when(UserTable.lookupUserWithRolesById(this.mockConnection,
                                                            4)).thenReturn(mockResultSet);

        // Make sure no exceptions are thrown in proper case.
        User user = User.lookupById(this.mockConnection,
                                    4);

        assertEquals("gundermanc", user.getUsername());
        assertEquals("haha_nice_try", user.getPasswordHash());
        assertEquals("gundermanc@gmail.com", user.getEmail());
        assertEquals(date, user.getJoinDate());
    }
    
    @Test
    public void test_deleteStatic_NullSQL_Exception() throws Exception {
        try {
            User.delete(null,
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_deleteStatic_NullUsername_Exception() throws Exception {
        try {
            User.delete(this.mockConnection,
                        null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_deleteStatic_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class, "deleteUser",
                                      this.mockConnection, "gundermanc");
        User.delete(this.mockConnection,
                    "gundermanc");
    }

    @Test
    public void test_delete_NullSQL_Exception() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").delete(null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_delete_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class,
                                      "deleteUser",
                                      this.mockConnection,
                                      "gundermanc");
        new User(1,
                 "gundermanc",
                 "hashes",
                 new Date(),
                 "email@gc.com").delete(this.mockConnection);
    }

    @Test
    public void test_updatePassword_NullConnection() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updatePassword(null, "old_pass", "new_pass");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_NullOldPassword() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updatePassword(this.mockConnection, null, "new_pass");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_NullNewPassword() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updatePassword(this.mockConnection, "old_pass", null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_InvalidPassword() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").updatePassword(this.mockConnection, "not_old_pass", "new_password");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_INVALID_PASSWORD);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class,
                                      "updatePassword",
                                      this.mockConnection,
                                      1,
                                      OMUtil.sha256("new_password"));
        User user = new User(1,
                             "gundermanc",
                             OMUtil.sha256("old_pass"),
                             new Date(),
                             "email@gc.com");

        user.updatePassword(this.mockConnection, "old_pass", "new_password");

        assertEquals(user.getPasswordHash(), OMUtil.sha256("new_password"));
    }

    @Test
    public void test_updateEmail_NullConnection() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updateEmail(null, "newemail@gc.com");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updateEmail_NullEmail() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updateEmail(this.mockConnection, null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updateEmail_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class,
                                      "updateEmail",
                                      this.mockConnection,
                                      1,
                                      new InternetAddress("email@gc.com"));
        User user = new User(1,
                             "gundermanc",
                             OMUtil.sha256("old_pass"),
                             new Date(),
                             "email@gc.com");

        user.updateEmail(this.mockConnection,
                         "gundermanc@gmail.com");

        assertEquals(user.getEmail(), "gundermanc@gmail.com");
    }

    @Test
    public void test_equals_True() throws Exception {
        assertEquals(new User(1,
                              "gundermanc",
                              OMUtil.sha256("old_pass"),
                              new Date(),
                              "email@gc.com"),
                     new User(1,
                              "gundermanc_different",
                              OMUtil.sha256("new_pass"),
                              new Date(),
                              "email2@gc.com"));
    }

    @Test
    public void test_equals_False() throws Exception {
        assertNotEquals(new User(3,
                                 "gundermanc",
                                 OMUtil.sha256("old_pass"),
                                 new Date(),
                                 "email@gc.com"),
                        new User(1,
                                 "gundermanc",
                                 OMUtil.sha256("old_pass"),
                                 new Date(),
                                 "email@gc.com"));
    }

    @Test
    public void test_addRole_NullConnection() {
        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").addRole(null, "ROLE_USER");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_addRole_NullRole() {
        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").addRole(this.mockConnection, null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_addRole_DuplicateInUserHasRoleTable() throws Exception {
        PowerMockito.doThrow(new ServiceException(ServiceStatus.APP_USER_HAS_ROLE_DUPLICATE))
            .when(UserHasRoleTable.class, "insertUserHasRole", this.mockConnection, 3, "ROLE_USER");

        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").addRole(this.mockConnection, "ROLE_USER");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_USER_HAS_ROLE_DUPLICATE);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_addRole_RoleNotExistInRolesTable() throws Exception {
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(false);

        PowerMockito.doNothing()
            .when(UserHasRoleTable.class, "insertUserHasRole", this.mockConnection, 3, "ROLE_USER");
        PowerMockito.when(UserRoleTable.lookupUserRole(this.mockConnection, "ROLE_USER"))
            .thenReturn(mockResultSet);

        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").addRole(this.mockConnection, "ROLE_USER");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_USER_HAS_ROLE_DUPLICATE);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_addRole_SQLException() throws Exception {
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenThrow(new SQLException());

        PowerMockito.doNothing()
            .when(UserHasRoleTable.class, "insertUserHasRole", this.mockConnection, 3, "ROLE_USER");
        PowerMockito.when(UserRoleTable.lookupUserRole(this.mockConnection, "ROLE_USER"))
            .thenReturn(mockResultSet);

        try {
            new User(3,
                     "gundermanc",
                     "old_pass",
                     new Date(),
                     "email@gc.com").addRole(this.mockConnection, "ROLE_USER");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_addRole_Success() throws Exception {
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.getString("role")).thenReturn("ROLE_USER");
        when(mockResultSet.getString("description")).thenReturn("A standard user.");
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        PowerMockito.doNothing()
            .when(UserHasRoleTable.class, "insertUserHasRole", this.mockConnection, 3, "ROLE_USER");
        PowerMockito.when(UserRoleTable.lookupUserRole(this.mockConnection, "ROLE_USER"))
            .thenReturn(mockResultSet);

        User.Role role = new User(3,
                                  "gundermanc",
                                  "old_pass",
                                  new Date(),
                                  "email@gc.com").addRole(this.mockConnection, "ROLE_USER");

        assertEquals(role.id, "ROLE_USER");
        assertEquals(role.description, "A standard user.");
    }

    @Test
    public void test_removeRole_NullConnection() {
        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").removeRole(null, "ROLE_USER");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_removeRole_NullRole() {
        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").removeRole(this.mockConnection, null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_removeRole_UserDoesNotHaveRole() throws Exception {
        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").removeRole(this.mockConnection, "ROLE_USER");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_USER_NOT_HAVE_ROLE);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_removeRole_Success() throws Exception {

        PowerMockito.doNothing().when(UserHasRoleTable.class,
                                      "deleteUserHasRole",
                                      this.mockConnection,
                                      3,
                                      "ROLE_USER");

        User user = new User(3,
                             "gundermanc",
                             "old_pass",
                             new Date(),
                             "email@gc.com");

        user.roles.add(new User.Role("ROLE_USER", "desc"));
        user.removeRole(this.mockConnection, "ROLE_USER");
    }

    @Test
    public void test_role_create_NullConnection() throws Exception {
        try {
            User.Role.create(null, "ROLE_USER", "description");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_create_NullRole() throws Exception {
        try {
            User.Role.create(this.mockConnection, null, "description");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_create_NullDescription() throws Exception {
        try {
            User.Role.create(this.mockConnection, "ROLE_USER", null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_create_RoleTooShort() throws Exception {
        try {
            User.Role.create(this.mockConnection, "ROLE_A", "description");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_INVALID_ROLE_LENGTH);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_create_RoleTooLong() throws Exception {
        try {
            User.Role.create(this.mockConnection, "ROLE_ABCDEFGHIJKLMNOPQRSTUVWXYZ", "description");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_INVALID_ROLE_LENGTH);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_create_RoleInvalid() throws Exception {
        try {
            User.Role.create(this.mockConnection, "INVALIDPREFIX_ABC", "description");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_INVALID_ROLE);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_create_DescriptionTooLong() throws Exception {
        try {
            User.Role.create(this.mockConnection, "ROLE_ABCD",
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                             "ABCDEFGHIJKLMNOPQRSTUV");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_INVALID_ROLE_DESC_LENGTH);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_delete_NullConnection() throws Exception {
        try {
            User.Role.delete(null, "ROLE_FOO");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_delete_NullRole() throws Exception {
        try {
            User.Role.delete(this.mockConnection, null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_role_delete_Success() throws Exception {
        PowerMockito.doNothing().when(UserRoleTable.class,
                                      "deleteUserRole",
                                      this.mockConnection,
                                      "ROLE_FOO");
        User.Role.delete(this.mockConnection, "ROLE_FOO");
    }

    @Test
    public void test_getFavoriteCourses_NullConnection() {
        try {
            new User(3,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").getFavoriteCourses(null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }
}
