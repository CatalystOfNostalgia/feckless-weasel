<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>Classroom</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <div class="column-beta">
            <br>
            <p id= "course">
              <script language="javascript" type="text/javascript">
                function createClassAndUni(className, university){}
                    var str1 = classname;
                    var result1 = str1.bold();
                    document.getElementByID("course").innerHTML = result1;
                    var str2 = university;
                    document.write(result2);
                }
            </p>
            </script>
        </br>
        <!--code for embedding pdf into html-->
         <a href="/pdfobject.jsp">PDF</a>
        <embed src="samplepdf.pdf" width="550" height="750"></embed>
            </div>
        </div>
    </body>>
</html>