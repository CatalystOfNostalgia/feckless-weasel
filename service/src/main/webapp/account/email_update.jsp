<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
        <title>Feckless Weasel Email Update</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/login.css" rel="stylesheet">
        <script language="javascript">
            function validate(event) {
                var emailInput = document.getElementById('email');

                if (emailInput.value.length === 0) {
                    alert("Email cannot be empty.");
                    return false;
                }

                if (emailInput.value.length > <%= User.EMAIL_MAX %>) {
                    alert("Email should be no greater than <%= User.EMAIL_MAX %> characters.")
                    return false;
                }

              return true;
            }
        </script>
    </head>
    <body>
        <%@ include file="/header.jsp" %>
        <%-- Check user is logged in. --%>
        <%
            if (authSession == null) {
                throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
            }
        %>
        <div class="container">
            <form class="form-signin" action="/servlet/user" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return validate()">
                <h2 class="form-signin-heading">Update Email</h2>
                <input type="hidden" name="action" value="update_email" />
                <input type="hidden" class="text-large" id="username" name="username" placeholder="username" value="<%= authSession.getUser().getUsername() %>" />
                <input type="text" class="form-control" id="email" name="email" placeholder="email" />
                <input type="submit" class="btn btn-lg btn-warning btn-block id="submit" name="submit" value="Update Email" \>
            </form>
        </div>
    </body>
</html>
