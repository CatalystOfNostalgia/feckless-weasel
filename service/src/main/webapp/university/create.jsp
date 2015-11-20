<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*, com.fecklessweasel.service.objectmodel.University, com.fecklessweasel.service.UniversityUtil"%>
<html>
    <head>
        <title>Feckless Weasel University Create Page</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/login.css" rel="stylesheet">
    </head>
    <body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="container">
            <form class="form-signin" action="/servlet/university" method="post" enctype="application/x-www-form-urlencoded">
                <h2 class="form-signin-heading">Don't see your university?</h2>
                <p>Add it today! Don't let your dreams be dreams.</p>
                <input type="text" class="form-control" id="longName" name="longName" placeholder="name">
                <input type="text" class="form-control" id="acronym" name="acronym" placeholder="acronym">
                <input type="text" class="form-control" id="city" name="city" placeholder="city">
                <input type="text" class="form-control" id="state" name="state" placeholder="state">
                <input type="text" class="form-control" id="country" name="country" placeholder="country">
                <div>
                    <button type="submit" class="btn btn-lg btn-warning btn-blocke" id="submit" name="submit" onclick = "listUni()">Create</button>
                </div>
            </form>
        </div>
    </body>
</html>
