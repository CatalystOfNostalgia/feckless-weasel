<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="com.fecklessweasel.service.objectmodel.UserSession,com.fecklessweasel.service.UserSessionUtil" %>

<div class="header">
    <div class="column-center">
        <input type="text" class="text-large" id="college" name="college" placeholder="Enter your university">
        <input type="text" class="text-large" id="class" name="class" placeholder="Enter your class (i.e. EECS-390)">
        <button type="submit" class="button-small" id="search-submit" name="search-submit">Go</button>

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
</div>
