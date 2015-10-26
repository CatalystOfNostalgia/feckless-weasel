<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>Populate Database</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
	<body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <h1>Populate the database with the following information</h1>
            <h2>User Table</h2>
            <table class="db-table">
				<caption>User Table</caption>
				<tr>
					<th>Username</th>
					<th>Password</th>
					<th>Email</th>
				</tr>
				<tr>
					<td>January</td>
					<td>$100</td>
					<td></td>
				</tr>
				<tr>
					<td>February</td>
					<td>$50</td>
				</tr>
			</table>
            <form class="form-default" action="/servlet/populate" method="post" enctype="application/x-www-form-urlencoded">
                    <button type="submit" class="button-large" id="submit" name="submit">Populate Database</button>
            </form>
        </div>
    </body>
</html>