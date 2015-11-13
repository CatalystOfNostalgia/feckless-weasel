<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*, com.fecklessweasel.service.objectmodel.University, com.fecklessweasel.service.UniversityUtil"%>
<html>
    <head>
        <title>Feckless Weasel University Create Page</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="jumbotron">
            <div class="container">
                <h1>Create a University</h1>
            </div>
        </div>
        <div class="container">
            <form action="/servlet/university" method="post" enctype="application/x-www-form-urlencoded">
                <input type="text" class="form-control input-lg" id="longName" name="longName" placeholder="name">
                <input type="text" class="form-control input-lg" id="acronym" name="acronym" placeholder="acronym">
                <input type="text" class="form-control input-lg" id="city" name="city" placeholder="city">
                <input type="text" class="form-control input-lg" id="state" name="state" placeholder="state">
                <input type="text" class="form-control input-lg" id="country" name="country" placeholder="country">
                <div>
                    <button type="submit" class="btn btn-default" id="submit" name="submit" onclick = "listUni()">Create</button>
                </div>
            </form>
        </div>
    </body>
</html>