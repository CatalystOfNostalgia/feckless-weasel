<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="com.fecklessweasel.service.objectmodel.UserSession,com.fecklessweasel.service.UserSessionUtil" %>

<nav class="navbar navbar-fixed-top navbar-inverse">
    <div class="container">
        <form class="navbar-form navbar-middle">
            <div class="form-group">
                <input type="text" class="form-control" id="college" name="college" placeholder="Enter your university">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" id="class" name="class" placeholder="Enter your class (i.e. EECS-390)">
            </div>
            <button type="submit" class="btn btn-success" id="search-submit" name="search-submit">Go</button>
        </form>
            <%-- Instantiate the session if there is one. You can use this in pages that include this. --%>
            <%
                UserSession authSession = UserSessionUtil.resumeSession(request);
             %>

            <%-- Show signup and login or logout button --%>
            <%= authSession != null ? String.format(
                "<form class='form-default' action='/servlet/user_session' method='post'>" +
                "<input type='hidden' name='action' value='delete'/><input type='submit' " +
                "class='button-large' id='submit' name='submit' value='Logout %s'/></form>",
                    authSession.getUser().getUsername()) :
                "<a href='/account/create.jsp'>Sign up</a>" +
                " <a href='/account/login.jsp'>Login</a>"
                %>
    </div>
</nav>
