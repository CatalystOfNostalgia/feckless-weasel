package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.fecklessweasel.service.objectmodel.ServiceException;

public class FileMetadataTable extends SQLSource{

  private class Insert implements SQLInteractionInterface{
    private String query = "INSERT INTO FileMetadata (UserID, Course, Department, University, Date) VALUES (?,?,?,?,?)";

    private String userId;
    private String course;
    private String department;
    private String university;
    private String date;

    public Insert(String userId, String course, String department, String university, String date){
         this.userId = userId;
         this.course = course;
         this.department = department;
         this.university = university;
         this.date = date;
     }

     public void run(Connection connection){
        //convert date to a format suitable for our table
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date date = sdf.parse(this.date); //assuming date is passed in with correct format
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, this.userId);
        preparedStatement.setString(2, this.course);
        preparedStatement.setString(3, this.department);
        preparedStatement.setString(4, this.university);
        preparedStatement.setDate(5, sqlDate);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }
  }

  private class Create implements SQLInteractionInterface{
    private static String query = "CREATE TABLE FileMetadata" +
                                  "(" +
                                  "FileID int NOT NULL AUTO_INCREMENT," +
                                  "UserID varchar(255)," +
                                  "Class varchar(255)," +
                                  "Department varchar(255)," +
                                  "University varchar(255)," +
                                  "Date datetime" +
                                  ")";

    public void run(Connection connection){
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.executeUpdate();
      preparedStatement.close();
      connection.close();
    }

  }

}
