<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Department</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <script language="javascript">
            function validate(event) {
                var courseNameInput = document.getElementById('courseName');
                var courseNumInput  = document.getElementById('course');

                var courseNum = parseInt(courseNumInput.value);
                //ensure that the input is a number
                if (isNaN(courseNum)) {
                    alert("Course number must be a number");
                    return false;
                }
                //ensure that the input is a valid number
                if (courseNum < <%= Course.NUM_MIN %>|| courseNum > <%= Course.NUM_MAX %>) {
                    alert("Course number must be between <%= Course.NUM_MIN %> and <%= Course.NUM_MAX %>.");
                    return false;
                }
                if (courseNameInput.value.length <= 0 || courseNameInput.value.length > <%= Course.NAME_MAX %>) {
                    alert("Course name must be shorter than <%= Course.NAME_MAX %> characters.");
                    return false;
                }
                return true;
            }
        </script>
    </head>
        <body>
        <jsp:include page="/header.jsp"/>
        <%
           Tuple3<University, Department, List<Course>> tuple =
           SQLSource.interact(new SQLInteractionInterface<Tuple3<University, Department, List<Course>>>() {
               @Override
               public Tuple3<University, Department, List<Course>> run(Connection connection) throws ServiceException {
                   final Department department = Department.lookup(connection,
                       OMUtil.parseInt(request.getParameter("did")));
                   return new Tuple3(department.lookupUniversity(connection), department, department.getAllCourses(connection));
               }
           });
           request.setAttribute("university", tuple.value1);
           request.setAttribute("department", tuple.value2);
           request.setAttribute("courses", tuple.value3);
        %>
        <div class="jumbotron" style="margin-bottom: 0px">
            <div class="container">
                <h1>${department.getDeptName()}</h1>
                <h2>
                    <span>${department.getAcronym()} at </span>
                    <a style="color: #f0ad4e" href="/university?uid=${university.getID()}" title="${university.getLongName()}">${university.getAcronym()}</a>
                </h2>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <jsp:include page="/sidebar.jsp"/>
                <div class="container">
                    <div class="col-md-6">
                        <c:forEach var="courseObj" items="${courses}">
                            <h2><a style="color: #f0ad4e;" href="/course/index.jsp?cid=${courseObj.getID()}">
                                ${department.getAcronym()} ${courseObj.getCourseNum()}: ${courseObj.getCourseName()}
                            </a></h2>
                        </c:forEach>
                    </div>
                    <div class="col-md-6">
                        <h2>Create a Course</h2>
                        <form class="form-signin" action="/servlet/course" method="post" enctype="application/x-www-form-urlencoded" 
                            onsubmit="return validate()">
                             <input type="hidden" class = "text-large" id="method" name="method" value="handleCourseCreate">
                             <input type="hidden" class="text-large" id="department" name="department" placeholder= "department" value='${department.getID()}'>
                             <input type="text" class="form-control input-lg" id="course" name="course" placeholder="Course Number">
                             <input type="text" class="form-control input-lg" id="courseName" name="courseName" placeholder="Course Name">
                             <div>
                                <button type="submit" class="btn btn-warning" id="submit" name="submit">Create</button>
                             </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
