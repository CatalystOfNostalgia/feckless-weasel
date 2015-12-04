package com.fecklessweasel.service.objectmodel;

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
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CourseTable.class, DepartmentTable.class, FileMetadataTable.class, UniversityTable.class, UserHasRoleTable.class, UserRoleTable.class, UserTable.class})
public class test_StoredFile {
    private Connection mockConnection;
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
        }
    }
}
