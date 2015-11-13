<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>${longName}</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class>
        <jsp:include page="/header.jsp"/>
        <%
           final University university = UniversityUtil.findUniversity(request);
           ArrayList<Department> depts =
           SQLSource.interact(new SQLInteractionInterface<ArrayList<Department>>() {
               @Override
               public ArrayList<Department> run(Connection connection) throws ServiceException {
                   return (ArrayList<Department>) university.getAllDepts(connection);
               }
           });
           request.setAttribute("university", university);
           request.setAttribute("depts", depts);
        %>
        <div class="jumbotron">
            <div class="container">
                <h1>${university.getLongName()}</h1>
                <h2>${depts.size()} total Departments</h2>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <c:forEach var="department" items="${depts}">
                    <div class="row">
                        <div class="col-md-6">
                            <h2><a href="/department/index.jsp?did=${department.getID()}">${department.getDeptName()}</a></h2>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="row">
                <div class=""col-md-6"">
                    <h3>Create a Department</h3>
                    <form class="form-inlines" action="/servlet/department" method="post" enctype="application/x-www-form-urlencoded">
                         <input type="hidden" class="form-control" id="university" name="university" placeholder="university" value="${university.getID()}">
                         <input type="text" class="form-control input-lg" id="deptName" name="deptName" placeholder="Department Name (i.e. Biology)">
                         <input type="text" class="form-control input-lg" id="acronym" name="acronym" placeholder="acronym (i.e. BIOL)">
                         <div>
                            <button type="submit" class="btn btn-default" id="submit" name="submit">Create</button>
                         </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
