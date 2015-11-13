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
                <h2><%= department.getAcronym()%></h2>
                <h3>Create a Course</h3>
                <form class="form-default" action="/servlet/course" method="post" enctype="application/x-www-form-urlencoded">
                     <input type="hidden" class="text-large" id="department" name="department" value='<%=department.getID()%>'>
                     <input type="text" class="text-large" id="course" name="course" placeholder="Course Number">
                     <div>
                        <button type="submit" class="button-large" id="submit" name="submit">Create</button>
                     </div>
                </form>
        </div>
    </body>
</html>