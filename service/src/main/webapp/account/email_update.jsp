<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
        <title>Feckless Weasel Email Update</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
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
    <body class="four-column">
        <%@ include file="/header.jsp" %>
        <%-- Check user is logged in. --%>
        <%
            if (authSession == null) {
                throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
            }
        %>
        <div class="column-beta">
            <h1>Update Email</h1>
            <form class="form-default" action="/servlet/user" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return validate()">
                <input type="hidden" name="action" value="update_email" />
                <input type="hidden" class="text-large" id="username" name="username" placeholder="username" value="<%= authSession.getUser().getUsername() %>" />
                <input type="text" class="text-large" id="email" name="email" placeholder="email" />
                <div>
                    <input type="submit" class="button-large" id="submit" name="submit" value="Update Email" \>
                </div>
            </form>
        </div>
    </body>
</html>
