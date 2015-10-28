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
        <h2><%= request.getAttribute("longName")%></h3>
         <a href="/pdfobject.jsp">PDF</a>
        <embed src="samplepdf.pdf" width="550" height="750"></embed>
            </div>
        </div>
    </body>>
</html>c