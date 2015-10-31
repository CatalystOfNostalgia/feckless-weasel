<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*,com.fecklessweasel.service.*,com.fecklessweasel.service.objectmodel.*,com.fecklessweasel.service.datatier.*" %>
<html>
    <head>
        <title>Feckless Weasel User Profile</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <%@ include file="/header.jsp" %>

        <%--
            Determines user who's info we'll display on this page. Looks first
            at "user" query param for a username. If none is given we revert to
            the currently logged in user's page. If no user is logged in we
            redirect to the error page with a NOT_AUTHENTICATED error.
        --%>
        <%
            final String username = request.getParameter("user");
            User user = SQLSource.interact(new SQLInteractionInterface<User>() {
                @Override
                public User run(Connection connection) throws ServiceException, SQLException {
                     if (username != null) {
                         return User.lookup(connection, username);
                     } else if (authSession == null) {
                         throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
                     }

                     return authSession.getUser();
                }
             });
         %>
        <div class="column-beta">
            <h1><%= user.getUsername() %>'s Profile</h1>
            <ul>
                <li>Joined on: <%= user.getJoinDate() %></li>
                <li>Contact via: <%= user.getEmail() %></li>

                <%-- TODO: Add more to the profile page --%>
            </ul>
        </div>
    </body>
</html>
