<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    ArrayList<University> univs =
    SQLSource.interact(new SQLInteractionInterface<ArrayList<University>>() {
        @Override
        public ArrayList<University> run(Connection connection) throws ServiceException {
            return (ArrayList<University>) University.lookUpAll(connection);
        }
    });
    request.setAttribute("univs", univs);
%>

<html>
    <head>
        <title>Feckless Weasel Hello World Index Page</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class="jumbotron" style="margin-bottom: 0px;">
            <div class="container">
                <h1>Welcome to Feckless Weasel!</h1>
                <p>Let Feckless Weasel help you ace your next test!</p>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <jsp:include page="sidebar.jsp"/>
                   <div class="container">
                        <div class="container">
                            <div class="col-md-4">
                                <h2>Can't find your university?</h2>
                                <p>That's okay! Get your university started and add your university on Feckless Weasel today.</p>
                                <p>
                                    <a class="btn btn-warning" href="/university/create.jsp" role="button">Create</a>
                                </p>
                            </div>
                            <div class="col-md-4">
                                <c:forEach var="university" items="${univs}">
                                    <p><a href="/university/index.jsp?uid=${university.getID()}">${university.getLongName()}</a></p>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
