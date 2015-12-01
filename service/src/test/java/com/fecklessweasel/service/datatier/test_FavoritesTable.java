package com.fecklessweasel.service.datatier;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class test_FavoritesTable {
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Before
    public void setup(){
        this.mockConnection = mock(Connection.class);
        this.mockPreparedStatement = mock(PreparedStatement.class);
        this.mockResultSet = mock(ResultSet.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertFavoriteCourse_nullConnection() throws Exception {
        FavoritesTable.insertFavoriteCourse(null, 1, 1);
    }

    @Test
    public void test_insertFavoriteCourse_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.INSERT_FAV_COURSE))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.insertFavoriteCourse(mockConnection, 1, 1);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
        }
    }

    @Test
    public void test_insertFavoriteCourse_success() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.INSERT_FAV_COURSE))
                .thenReturn(mockPreparedStatement);

        FavoritesTable.insertFavoriteCourse(mockConnection, 1, 1);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insertFavoriteFile_nullConnection() throws Exception {
        FavoritesTable.insertFavoriteFile(null, 1, 1);
    }

    @Test
    public void test_insertFavoriteFile_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.INSERT_FAV_FILE))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.insertFavoriteFile(mockConnection, 1, 1);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
        }
    }

    @Test
    public void test_insertFavoriteFile_success() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.INSERT_FAV_FILE))
                .thenReturn(mockPreparedStatement);

        FavoritesTable.insertFavoriteFile(mockConnection, 1, 1);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getFavoriteCourses_nullConnection() throws Exception {
        FavoritesTable.getFavoriteCourses(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getFavoriteFiles_nullConnection() throws Exception {
        FavoritesTable.getFavoriteFiles(null, 1);
    }

    @Test
    public void test_getFavoriteCourses_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.SELECT_COURSES))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.getFavoriteCourses(mockConnection, 1);
        } catch(ServiceException e) {
            assertEquals(e.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_getFavoriteFiles_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.SELECT_FILES))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.getFavoriteFiles(mockConnection, 1);
        } catch(ServiceException e) {
            assertEquals(e.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_getFavoriteCourses_success() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.SELECT_COURSES))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        ResultSet results = FavoritesTable.getFavoriteCourses(mockConnection, 1);

        assertEquals(results, mockResultSet);
    }

    @Test
    public void test_getFavoriteFiles_success() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.SELECT_FILES))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        ResultSet results = FavoritesTable.getFavoriteFiles(mockConnection, 1);

        assertEquals(results, mockResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_favoriteCourseExists_nullConnection() throws Exception {
        FavoritesTable.favoriteCourseExists(null, 1, 1);
    }

    @Test
    public void test_favoriteCourseExists_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.COURSE_FAV_EXISTS))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.favoriteCourseExists(mockConnection, 1, 1);
        } catch(ServiceException e) {
            assertEquals(e.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_favoriteCourseExists_true() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.COURSE_FAV_EXISTS))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);
        when(mockResultSet.getInt(1))
                .thenReturn(1);

        assertTrue(FavoritesTable.favoriteCourseExists(mockConnection, 1, 1));
    }

    @Test
    public void test_favoriteCourseExists_false() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.COURSE_FAV_EXISTS))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);
        when(mockResultSet.getInt(1))
                .thenReturn(0);

        assertFalse(FavoritesTable.favoriteCourseExists(mockConnection, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_favoriteFileExists_nullConnection() throws Exception {
        FavoritesTable.favoriteFileExists(null, 1, 1);
    }

    @Test
    public void test_favoriteFileExists_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.FILE_FAV_EXISTS))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.favoriteFileExists(mockConnection, 1, 1);
        } catch(ServiceException e) {
            assertEquals(e.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_favoriteFileExists_true() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.FILE_FAV_EXISTS))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);
        when(mockResultSet.getInt(1))
                .thenReturn(1);

        assertTrue(FavoritesTable.favoriteFileExists(mockConnection, 1, 1));
    }

    @Test
    public void test_favoriteFileExists_false() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.FILE_FAV_EXISTS))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);
        when(mockResultSet.getInt(1))
                .thenReturn(0);

        assertFalse(FavoritesTable.favoriteFileExists(mockConnection, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_deleteFavoriteCourse_nullConnection() throws Exception {
        FavoritesTable.deleteFavoriteCourse(null, 1, 1);
    }

    @Test
    public void test_deleteFavoriteCourse_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.COURSE_FAV_DELETE))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.deleteFavoriteCourse(mockConnection, 1, 1);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
        }
    }

    @Test
    public void test_deleteFavoriteCourse_success() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.COURSE_FAV_DELETE))
                .thenReturn(mockPreparedStatement);

        FavoritesTable.deleteFavoriteCourse(mockConnection, 1, 1);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_deleteFavoriteFile_nullConnection() throws Exception {
        FavoritesTable.deleteFavoriteFile(null, 1, 1);
    }

    @Test
    public void test_deleteFavoriteFile_SQLException() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.FILE_FAV_DELETE))
                .thenThrow(new SQLException());

        try {
            FavoritesTable.deleteFavoriteFile(mockConnection, 1, 1);
        } catch (ServiceException e) {
            assertEquals(ServiceStatus.DATABASE_ERROR, e.status);
        }
    }

    @Test
    public void test_deleteFavoriteFile_success() throws Exception {
        when(mockConnection.prepareStatement(FavoritesTable.FILE_FAV_DELETE))
                .thenReturn(mockPreparedStatement);

        FavoritesTable.deleteFavoriteFile(mockConnection, 1, 1);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
