<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*"%>
<html>
    <head>
        <title>${longName}</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
        <body class="four-column">
        <jsp:include page="header.jsp"/>
         <%
            University university = UniversityUtil.findUniversity(request);
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
        <div class="column-beta">
                <%University university = UniversityUtil.findUniversity(request);%>
                <h1>${university.getLongName()}</h1>
                <h2>${university.getState()}</h2>
                <h3>Create a Department</h3>
                <form class="form-default" action="/servlet/department" method="post" enctype="application/x-www-form-urlencoded">
                     <input type="text" class="text-large" id="university" name="university" placeholder="University">
                     <input type="text" class="text-large" id="deptName" name="deptName" placeholder="Department Name (i.e. Biology)">
                     <input type="text" class="text-large" id="acronym" name="acronym" placeholder="acronym (i.e. BIOL)">
                     <div>
                        <button type="submit" class="button-large" id="submit" name="submit">Create</button>
                     </div>
                </form>
        </div>
    </body>
</html>
