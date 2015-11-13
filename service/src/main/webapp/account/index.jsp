<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*,com.fecklessweasel.service.*,com.fecklessweasel.service.objectmodel.*,com.fecklessweasel.service.datatier.*" %>
<html>
    <head>
        <title>Feckless Weasel User Profile</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
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

            boolean isCurrentUsersProfile
                = authSession != null && (user.equals(authSession.getUser()));
         %>
         <div class="jumbotron">
            <div class="container">
                <h1><%= user.getUsername() %>'s Profile</h1>
            </div>
        </div>
        <div class="container">
            <ul>
                <li>Joined on: <%= user.getJoinDate() %></li>
                <li>
                    Contact via: <%= user.getEmail() %>
                    <%= isCurrentUsersProfile ? "<a href='/account/email_update.jsp'>Update Email</a>" : "" %>
                </li>

                <%-- If this is the current users profile, display a password reset link. --%>
                <%= isCurrentUsersProfile ? "<li><a href='/account/password_update.jsp'>Change password</a></li>" : "" %>
            </ul>
        </div>
    </body>
</html>
