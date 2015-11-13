<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*"%>
<html>
    <head>
        <title>Course</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
        <body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <%Course course = CourseUtil.findCourse(request);%>
            <%Department department = DepartmentUtil.findDepartmentID(course.getDeptID());%>
            <h1><%=department.getAcronym()%>
            <h2><%=course.getCourseNum()%></h2>
        </div>
    </body>
</html>