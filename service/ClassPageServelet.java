package com.fecklessweasel.service;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.Course;

/**This servelet handles checking to see if a class already exists and adding that class to the Class tables**/

public final class ClassPageServelet extends HttpServelet{

	String className;
	String universityName;
	public void doPost(HttpServletRequest request, HttpServeletRequest response)
		throws ServletException, IOException{

			className = request.getParameter("class");
			universityName = request.getParameter("college")


	}
	
	public void doGet(HttpServeletRequest request, HttpServeletRequest response)
		throws ServletException, IOException{
			
			
	}
	
}