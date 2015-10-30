<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.*,com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
        <title>Feckless Weasel User Profile</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <%@ include file="/header.jsp" %>
        <%
            // Redirect to not authenticated page.
            if (authSession == null) {
                throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
            }

            User user = authSession.getUser();
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
