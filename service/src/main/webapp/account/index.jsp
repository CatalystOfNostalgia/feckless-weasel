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
        <div class="jumbotron" style="margin-bottom: 0px;">
            <div class="container">
                <h1><%= user.getUsername() %>'s Profile</h1>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <jsp:include page="/sidebar.jsp"/>
                <div class="container">
                    <h2>Contact Information</h2>
                    <p><b>Email:</b> <%= user.getEmail() %> <%= isCurrentUsersProfile ? "<a href='/account/email_update.jsp'>Update Email</a>" : "" %><p>
                    <h2>General Information</h2>
                    <p><b>Password:</b> ************ <%= isCurrentUsersProfile ? "<a href='/account/password_update.jsp'>Change Password</a>" : "" %>
                </div> 
            </div>
        </div>
    </body>
</html>
