<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*"%>
<html>
    <head>
        <title>Department</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
        <body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
                <%Department department = DepartmentUtil.findDepartment(request);%>
                <h1><%= department.getDeptName()%></h1>
                <h3>Create a Department</h3>
                <form class="form-default" action="/servlet/course" method="post" enctype="application/x-www-form-urlencoded">
                     <input type="text" class="text-large" id="university" name="university" placeholder="University">
                     <input type="text" class="text-large" id="deptName" name="deptName" placeholder="Department Name (i.e. Biology)">
                     <input type="text" class="text-large" id="acronym" name="acronym" placeholder="Acronym (i.e. BIOL)">
                     <div>
                        <button type="submit" class="button-large" id="submit" name="submit">Create</button>
                     </div>
                </form>
        </div>
    </body>
</html>