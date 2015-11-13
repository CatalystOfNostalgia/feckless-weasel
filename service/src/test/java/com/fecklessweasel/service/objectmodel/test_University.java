package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

@PrepareForTest({UniversityTable.class})
@RunWith(PowerMockRunner.class)
public class test_University {

    private Connection mockConnection;
    private ResultSet mockResultSet;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockResultSet = mock(ResultSet.class);
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullConnection() throws Exception {
        University.create(null, "longname", "ACRO", "city", "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullLongname() throws Exception {
        University.create(this.mockConnection, null, "ACRO", "city", "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullAcronym() throws Exception {
        University.create(this.mockConnection, "longname", null, "city", "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullCity() throws Exception {
        University.create(this.mockConnection, "longname", "ACRO", null, "OH", "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullState() throws Exception {
        University.create(this.mockConnection, "longname", "ACRO", "city", null, "country here");
    }

    @Test(expected = ServiceException.class)
    public void test_create_nullCountry() throws Exception {
        University.create(this.mockConnection, "longname", "ACRO", "city", "OH", null);
    }

    @Test
    public void test_create_uniNameTooLong() throws Exception {
        try {
            University.create(this.mockConnection,
                    "THISISASUPERLONGNAMETHATWILLTAKEUPWAYTOOMANYCHARACTERSYAY",
                    "ACRO",
                    "city",
                    "OH",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_UNIV_NAME_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_uniNameTooShort() throws Exception {
        try {
            University.create(this.mockConnection,
                    "A",
                    "ACRO",
                    "city",
                    "OH",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_UNIV_NAME_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_invalidUniName() throws Exception {
        try {
            University.create(this.mockConnection,
                    "THIS IS INVALID!!!!!!",
                    "ACRO",
                    "city",
                    "OH",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_UNIV_NAME_CHARS, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_invalidAcronymName() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR!",
                    "city",
                    "OH",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_ACRONYM_CHARS, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_acronymTooLong() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "TOOLONG",
                    "city",
                    "OH",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_UNIV_ACRONYM_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_acronymTooShort() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "A",
                    "city",
                    "OH",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_UNIV_ACRONYM_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_cityNameTooLong() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR",
                    "THISCITYNAMEISWAYTOOLONGSOITWILLTHROWANERRORHOPEFULLYANDPASS",
                    "PA",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_CITY_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_cityNameTooShort() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR",
                    "a",
                    "AP",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_CITY_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_invalidCityName() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR",
                    "Mars!",
                    "AP",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_CITY_CHARS, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_invalidStateName() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR",
                    "city",
                    "NOPE",
                    "country here");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_STATE, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_countryNameTooLong() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR",
                    "city",
                    "PA",
                    "this country has way too many characters to be a real country so it throws something");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_COUNTRY_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_countryNameTooShort() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR",
                    "city",
                    "PA",
                    "US");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_COUNTRY_LENGTH, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_invalidCountryName() throws Exception {
        try {
            University.create(this.mockConnection,
                    "longname",
                    "ACR",
                    "city",
                    "PA",
                    "USA!");
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.APP_INVALID_COUNTRY_CHARS, e.status);
            return;
        }

        fail("Exception not thrown.");
    }

    @Test
    public void test_create_success() throws Exception {
        PowerMockito.mockStatic(UniversityTable.class);

        String longname = "longname";
        String acronym = "ACR";
        String city = "city";
        String state = "PA";
        String country = "USA";

        when(UniversityTable.insertUniversity(this.mockConnection, longname, acronym, city, state, country))
                .thenReturn(1);

        University uni = University.create(this.mockConnection, longname, acronym, city, state, country);

        assertEquals(uni.getID(), 1);
        assertEquals(uni.getLongName(), longname);
        assertEquals(uni.getAcronym(), acronym);
        assertEquals(uni.getCity(), city);
        assertEquals(uni.getState(), state);
        assertEquals(uni.getCountry(), country);
    }

    @Test
    public void test_lookup_uniNotFound() throws Exception {
        PowerMockito.mockStatic(UniversityTable.class);

        int uid = 1;

        when(UniversityTable.lookupUniversityID(this.mockConnection, uid))
                .thenReturn(this.mockResultSet);

        when(this.mockResultSet.next()).thenReturn(false);

        try {
            University.lookup(this.mockConnection, uid);
        } catch(ServiceException e) {
            assertEquals(ServiceStatus.APP_USER_NOT_EXIST, e.status);
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void test_lookup_SQLException() throws Exception {
        PowerMockito.mockStatic(UniversityTable.class);

        int uid = 1;

        when(UniversityTable.lookupUniversityID(this.mockConnection, uid))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next()).thenThrow(new SQLException());

        try {
            University.lookup(this.mockConnection, uid);
        } catch(ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void test_lookup_success() throws Exception {
        PowerMockito.mockStatic(UniversityTable.class);

        int uid = 1;
        String longname = "longname";
        String acronym = "ACR";
        String city = "city";
        String state = "PA";
        String country = "USA";

        when(UniversityTable.lookupUniversityID(this.mockConnection, uid))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next()).thenReturn(true);
        when(this.mockResultSet.getInt("id")).thenReturn(uid);
        when(this.mockResultSet.getString("longName")).thenReturn(longname);
        when(this.mockResultSet.getString("acronym")).thenReturn(acronym);
        when(this.mockResultSet.getString("city")).thenReturn(city);
        when(this.mockResultSet.getString("state")).thenReturn(state);
        when(this.mockResultSet.getString("country")).thenReturn(country);

        University uni = University.lookup(this.mockConnection, uid);

        assertEquals(uni.getID(), 1);
        assertEquals(uni.getLongName(), longname);
        assertEquals(uni.getAcronym(), acronym);
        assertEquals(uni.getCity(), city);
        assertEquals(uni.getState(), state);
        assertEquals(uni.getCountry(), country);
    }

    @Test
    public void test_lookupPaginated_SQLException() throws Exception {
        PowerMockito.mockStatic(UniversityTable.class);

        int offset = 0;
        int amt = 1;

        when(UniversityTable.lookUpPaginated(this.mockConnection, offset, amt))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next()).thenThrow(new SQLException());

        try {
            University.lookUpPaginated(this.mockConnection, offset, amt);
        } catch(ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void test_lookupPaginated_success() throws Exception {
        PowerMockito.mockStatic(UniversityTable.class);

        int offset = 0;
        int amt = 1;
        int uid = 1;
        String longname = "longname";
        String acronym = "ACR";
        String city = "city";
        String state = "PA";
        String country = "USA";

        when(UniversityTable.lookUpPaginated(this.mockConnection, offset, amt))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);
        when(this.mockResultSet.getInt("id")).thenReturn(uid);
        when(this.mockResultSet.getString("longName")).thenReturn(longname);
        when(this.mockResultSet.getString("acronym")).thenReturn(acronym);
        when(this.mockResultSet.getString("city")).thenReturn(city);
        when(this.mockResultSet.getString("state")).thenReturn(state);
        when(this.mockResultSet.getString("country")).thenReturn(country);

        List<University> uniList = University.lookUpPaginated(this.mockConnection, offset, amt);

        University uni = uniList.get(0);
        assertEquals(uni.getID(), 1);
        assertEquals(uni.getLongName(), longname);
        assertEquals(uni.getAcronym(), acronym);
        assertEquals(uni.getCity(), city);
        assertEquals(uni.getState(), state);
        assertEquals(uni.getCountry(), country);
    }

    @Test
    public void test_lookupAll() throws Exception {
        PowerMockito.mockStatic(UniversityTable.class);

        int offset = 0;
        int amt = Integer.MAX_VALUE;
        int uid = 1;
        String longname = "longname";
        String acronym = "ACR";
        String city = "city";
        String state = "PA";
        String country = "USA";

        when(UniversityTable.lookUpPaginated(this.mockConnection, offset, amt))
                .thenReturn(this.mockResultSet);
        when(this.mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);
        when(this.mockResultSet.getInt("id")).thenReturn(uid);
        when(this.mockResultSet.getString("longName")).thenReturn(longname);
        when(this.mockResultSet.getString("acronym")).thenReturn(acronym);
        when(this.mockResultSet.getString("city")).thenReturn(city);
        when(this.mockResultSet.getString("state")).thenReturn(state);
        when(this.mockResultSet.getString("country")).thenReturn(country);
        List<University> uniList = University.lookUpAll(this.mockConnection);

        University uni = uniList.get(0);
        assertEquals(uni.getID(), 1);
        assertEquals(uni.getLongName(), longname);
        assertEquals(uni.getAcronym(), acronym);
        assertEquals(uni.getCity(), city);
        assertEquals(uni.getState(), state);
        assertEquals(uni.getCountry(), country);
    }
}
