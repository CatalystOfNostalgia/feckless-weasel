<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*"%>
<html>
    <head>
        <title>Course</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
        <body class="four-column">
           <%
               final Course course = CourseUtil.findCourse(request);
               final Department department =DepartmentUtil.findDepartmentID(course.getDeptID());
               request.setAttribute("department", department);
               request.setAttribute("course", course);
         %>
        <jsp:include page="/header.jsp"/>
        <div class="jumbotron">
                    <div class="container">
                        <h1>${department.getDeptName()}</h1>
                        <h2>${course.getCourseNum()}</h2>
                    </div>
                </div>
            </body>
        </html>
    </body>
</html>