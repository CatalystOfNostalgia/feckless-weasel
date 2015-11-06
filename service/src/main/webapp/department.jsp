<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>${longName}d</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <jsp:include page="header.jsp"/>
        <div class="column-beta">
<<<<<<< HEAD:service/src/main/webapp/department.jsp
            <h1><%= request.getAttribute("longName")%></h1>
            <h2><%= request.getAttribute("state")%></h2>
        </div>
        <p>
        </p>
=======
        <h2><%= request.getAttribute("longName")%></h3>
         <a href="/pdfobject.jsp">PDF</a>
        <embed src="samplepdf.pdf" width="550" height="750"></embed>
            </div>
            <br>
                <h1>EECS 393</h1>
                <h2>Case Western Reserve University</h2>
            </br>
            <!--code for embedding pdf into html-->
            <a href="/pdfobject.jsp">PDF</a>
            <embed src="samplepdf.pdf" width="550" height="750"></embed>
        </div>
>>>>>>> b424721e93c1567fbd6f6594265bdf9761275b29:service/src/main/webapp/createCoursePages/classroom.jsp
    </body>
</html>
