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
import com.fecklessweasel.service.objectmodel.*;

@WebServlet("/servlet/university")
public final class UniversityServlet extends HttpServlet {

	/**
	 * Handles post requests to this end point. Performs the creation
	 * of universities given a set of parameters.
	 * **/
	String longName;
	@Override
	protected void doPost(final HttpServletRequest request,
						  final HttpServletResponse response)
			throws ServletException, IOException {

		SQLSource.interact(new SQLInteractionInterface<Integer>() {
			@Override
			public Integer run(Connection connection)
					throws ServiceException, SQLException {
					 longName = request.getParameter("longName");
					String acronym = request.getParameter("acronym");
					String city = request.getParameter("city");
					String state = request.getParameter("state");
					String country = request.getParameter("country");
					// Create university
					University university = University.create(connection, longName, acronym, city, state, country);
					// return int value
					return 0;

				}
			}

			);

			// Redirect to homepage.
			//response.sendRedirect("/");
		request.setAttribute("longName", longName);
		request.getRequestDispatcher("/classroom.jsp").forward(request, response);
	}
	}
