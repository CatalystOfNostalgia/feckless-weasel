<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Populate Database</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
	<body>
        <jsp:include page="/header.jsp"/>
        <h1>Database is populated  with the following information</h1>
        <div>
            <table class="db-table">
                <caption>User Table Additions</caption>
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                </tr>
                <c:forEach var="element" items="${users}">
                    <tr>
                        <td>${element.getUsername()}</td>
                        <td>${element.getEmail()}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br></br>
        <div>
            <table class="db-table">
                <caption>University Table Additions</caption>
                <tr>
                    <th>Long Name</th>
                    <th>Acronym</th>
                    <th>City</th>
                    <th>State</th>
                    <th>Country</th>
                </tr>
                <c:forEach var="element" items="${univs}">
                    <tr>
                        <td>${element.getLongName()}</td>
                        <td>${element.getAcronym()}</td>
                        <td>${element.getCity()}</td>
                        <td>${element.getState()}</td>
                        <td>${element.getCountry()}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br></br>
        <div>
            <table class="db-table">
                <caption>Department Table Additions</caption>
                <tr>
                    <th>University</th>
                    <th>Department Name</th>
                    <th>Acronym</th>
                </tr>
                <c:forEach var="department" items="${depts}">
                    <tr>
                        <td>${department.lookupUniversity().getlongName()}</td>
                        <td>${department.getDeptName()}</td>
                        <td>${department.getAcronym()}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br></br>
        <div>
            <p>Note the University is not represented in the Course table, just included here for clarity</p>
            <table class="db-table">
                <caption>Course Table Additions</caption>
                <tr>
                    <th>University</th>
                    <th>Dept</th>
                    <th>Number</th>
                </tr>
                <c:forEach var="element" items="${courses}">
                    <tr>
                        <td>${element.getDepartment().getUniversity().getlongName()}</td>
                        <td>${element.getDepartment().getAcronym()}</td>
                        <td>${element.getCourseNum()}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</html>