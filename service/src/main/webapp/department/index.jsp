<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Department</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
        <body>
        <jsp:include page="/header.jsp"/>
        <%
           final Department department = DepartmentUtil.findDepartment(request);
           ArrayList<Course> courses =
           SQLSource.interact(new SQLInteractionInterface<ArrayList<Course>>() {
               @Override
               public ArrayList<Course> run(Connection connection) throws ServiceException {
                   return (ArrayList<Course>) department.getAllCourses(connection);
               }
           });
           request.setAttribute("department", department);
           request.setAttribute("courses", courses);
        %>
        <div class="jumbotron">
            <div class="container">
                <h1>${department.getDeptName()}</h1>
                <h2>${courses.size()} total Courses</h2>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <c:forEach var="courseObj" items="${courses}">
                    <div class="row">
                        <div class="col-md-6">
                            <h2><a href="/course/index.jsp?cid=${courseObj.getID()}">${courseObj.getCourseNum()}</a></h2>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="row">
                <div class="col-md-6">
                        <h2>Create a Course</h2>
                        <form class="form-inline" action="/servlet/course" method="post" enctype="application/x-www-form-urlencoded">
                             <input type="hidden" class="text-large" id="" name="department" placeholder="get input">
                             <input type="text" class="form-control input-lg" id="course" name="course" placeholder="Course Number">
                             <div>
                                <button type="submit" class="btn btn-default" id="submit" name="submit">Create</button>
                             </div>
                        </form>
                </div>
            </div>
>>>>>>> origin/master
        </div>
    </body>
</html>