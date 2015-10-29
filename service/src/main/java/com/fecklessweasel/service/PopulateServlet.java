package com.fecklessweasel.service;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.User;
import com.fecklessweasel.service.objectmodel.UserSession;
import com.fecklessweasel.service.objectmodel.Course;
import com.fecklessweasel.service.objectmodel.Department;
import com.fecklessweasel.service.objectmodel.University;
import com.fecklessweasel.service.objectmodel.FileMetadata;

/**
 * A servlet that will fill the Database with testing data when posted to
 * new users.
 * @author Hayden Schmackpfeffer
 */
@WebServlet("/servlet/populate")
public final class PopulateServlet extends HttpServlet {
    //Test User data
    public static final String[] usernames = {"hschmack", "person1",
                          "SteveJobs27", "Drake13"};
    public static final String[] passwords = {"password1", "pass_word2",
                          "PassWord3", "P455W0RD4"};
    public static final String[] emails = {"hayden@case.edu", "generic@gmail.com",
                          "jobs@apple.com", "hotlinebling@yahoo.com"};
    //Test University data
    public static final String[] univ_name = {"Case Western Reserve University", "University of Rochester",
                                                "University of Michigan", "Boston University"};
    public static final String[] univ_acron = {"CWRU", "UR", "UM", "BU"};
    public static final String[] univ_city = {"Cleveland", "Rochester", "Ann arbor", "Boston"};
    public static final String[] univ_state= {"OH", "NY", "MI", "MA"};

    //Test Dept data
    public static final String[] dept_names = {"Electrical Engineering and Computer Science" , "History",
                                              "Philosophy", "Physics"};
    public static final String[] dept_acro = {"EECS", "HIST", "PHIL", "PHYS"};

    //Test Course data
    public static final int[] course_nums = {397, 233, 390, 201, 102, 300};


    public void doGet(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
        // set the attribute in the request to access it on the JSP
        final ArrayList<User> users = new ArrayList<User>();
        final ArrayList<University> univs = new ArrayList<University>();
        final ArrayList<Department> depts = new ArrayList<Department>();
        final ArrayList<Course> courses = new ArrayList<Course>();
        SQLSource.interact(new SQLInteractionInterface<Integer>() {

                /**
                 * Performs SQL Interactions.
                 * @param connection The SQL connection. Connection is released
                 * upon return.
                 * @throws ServiceException Exception is thrown up to calling
                 * function.
                 * @throws SQLException Exception is thrown if there is an
                 * unhandled SQLException.
                 * @return Returns 0 because generic interfaces have to return
                 * something.
                 */
                @Override
                public Integer run(Connection connection)
                    throws ServiceException, SQLException {

                    // Create the test users
                    for (int i = 0; i < usernames.length; i++){
                        User user = User.create(connection,
                                            usernames[i],
                                            passwords[i],
                                            emails[i]);

                        users.add(user);
                    }
                    // Create the test Universities
                    for (int i = 0; i < univ_name.length; i++){
                        University univ = University.create(connection,
                                            univ_name[i],
                                            univ_acron[i],
                                            univ_city[i],
                                            univ_state[i],
                                            "USA");

                        univs.add(univ);
                    }
                    //create test Departments
                    for (int i = 0; i < dept_names.length; i++){
                        //fill out 2 departments for two universities for testing purposes
                        University univ = univs.get(i%2);

                        Department dept = Department.create(connection,
                                            univ,
                                            dept_names[i],
                                            dept_acro[i]);

                        depts.add(dept);
                    }

                    //create test Courses
                    for (int i = 0; i < course_nums.length; i++){
                        //using only two departments to make it simpler
                        Department dept;
                        if (i < 3) {
                            dept = depts.get(0); //CWRU EECS
                        } else {
                            dept = depts.get(2); //CWRU PHIL
                        }

                        Course course = Course.create(connection,
                                            dept,
                                            course_nums[i]);

                        courses.add(course);
                    }
                    // Have to return something.
                    return 0;
                }
            });
        request.setAttribute("users", users);
        request.setAttribute("univs", univs);
        request.setAttribute("depts", depts);
        request.setAttribute("courses", courses);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/populatedb.jsp");
        rd.forward(request, response);
    }
}
