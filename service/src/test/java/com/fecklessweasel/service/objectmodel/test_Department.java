package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.fecklessweasel.service.datatier.DepartmentTable;
import com.fecklessweasel.service.datatier.UniversityTable;
import com.fecklessweasel.service.objectmodel.ServiceException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest({DepartmentTable.class})
@RunWith(PowerMockRunner.class)
public class test_Department {

    private Connection mockConnection;
    private University mockUniversity;
    private ResultSet mockResultSet;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockUniversity = mock(University.class);
        this.mockResultSet = mock(ResultSet.class);
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullConnection() throws Exception {
        Department.create(null, this.mockUniversity, "deptname here", "EECS");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullDeptName() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, null, "EECS");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullAcronym() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, "deptname here", null);
    }

    @Test(expected = ServiceException.class)
    public void test_create_badDeptname_numbers() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, "deptname1234", "EECS");
    }

    @Test(expected = ServiceException.class)
    public void test_create_badAcronym_numbers() throws Exception {
        Department.create(this.mockConnection, this.mockUniversity, "deptname here", "33C5");
    }

    @Test
    public void test_create_deptNameTooLong() throws Exception {
        try {
            Department.create(this.mockConnection,
                    this.mockUniversity,
                    "this dept name is way too long and will cause an error",
                    "EECS");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_DEPTNAME_LENGTH, e.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_deptNameTooShort() throws Exception {
        try {
            Department.create(this.mockConnection,
                    this.mockUniversity,
                    "A",
                    "EECS");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_DEPTNAME_LENGTH, e.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_create_success() throws Exception {
        PowerMockito.mockStatic(DepartmentTable.class);

        int uid = 1;
        int did = 2;
        String deptName = "Dept Name";
        String acronym = "EECS";

        when(DepartmentTable.insertDepartment(this.mockConnection, uid, deptName, acronym))
                .thenReturn(did);
        when(this.mockUniversity.getID()).thenReturn(uid);

        Department department = Department.create(this.mockConnection,
                                                  this.mockUniversity,
                                                  deptName,
                                                  acronym);

        assertEquals(department.getID(), did);
        assertEquals(department.getDeptName(), deptName);
        assertEquals(department.getAcronym(), acronym);
    }

    @Test
    public void test_lookup_deptNotFound() throws Exception {
        PowerMockito.mockStatic(DepartmentTable.class);

        int did = 1;

        when(DepartmentTable.lookupDepartment(this.mockConnection, did))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next()).thenReturn(false);

        try {
            Department.lookup(this.mockConnection, did);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_DEPARTMENT_NOT_EXIST, e.status);
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void test_lookup_SQLException() throws Exception {
        PowerMockito.mockStatic(DepartmentTable.class);

        int did = 1;

        when(DepartmentTable.lookupDepartment(this.mockConnection, did))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next()).thenThrow(new SQLException());

        try {
            Department.lookup(this.mockConnection, did);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void test_lookup_success() throws Exception {
        PowerMockito.mockStatic(DepartmentTable.class);

        int uid = 1;
        int did = 2;
        String deptName = "Dept Name";
        String acronym = "EECS";

        when(DepartmentTable.lookupDepartment(this.mockConnection, did))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next()).thenReturn(true);
        when(this.mockResultSet.getInt("id")).thenReturn(did);
        when(this.mockResultSet.getInt("univid")).thenReturn(uid);
        when(this.mockResultSet.getString("deptName")).thenReturn(deptName);
        when(this.mockResultSet.getString("acronym")).thenReturn(acronym);

        Department dept = Department.lookup(this.mockConnection, did);

        assertEquals(dept.getID(), did);
        assertEquals(dept.getAcronym(), acronym);
        assertEquals(dept.getDeptName(), deptName);
    }

    @Test
    public void test_lookupPaginated_SQLException() throws Exception {
        PowerMockito.mockStatic(DepartmentTable.class);

        int offset = 0;
        int amt = 1;
        int uid = 1;

        when(DepartmentTable.lookUpPaginated(this.mockConnection, uid, offset, amt))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next()).thenThrow(new SQLException());

        try {
            Department.lookUpPaginated(this.mockConnection, uid, offset, amt);
        } catch(ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void test_lookupPaginated_success() throws Exception {
        PowerMockito.mockStatic(DepartmentTable.class);

        int offset = 0;
        int amt = 1;
        int uid = 1;
        int did = 2;
        String deptName = "Dept Name";
        String acronym = "EECS";

        when(DepartmentTable.lookUpPaginated(this.mockConnection, uid, offset, amt))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);
        when(this.mockResultSet.getInt("id")).thenReturn(did);
        when(this.mockResultSet.getInt("univid")).thenReturn(uid);
        when(this.mockResultSet.getString("deptName")).thenReturn(deptName);
        when(this.mockResultSet.getString("acronym")).thenReturn(acronym);

        List<Department> deptList = Department.lookUpPaginated(this.mockConnection, uid, offset, amt);

        Department dept = deptList.get(0);
        assertEquals(dept.getID(), did);
        assertEquals(dept.getAcronym(), acronym);
        assertEquals(dept.getDeptName(), deptName);
    }
}