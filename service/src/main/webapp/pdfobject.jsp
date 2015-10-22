<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>PDFObject example</title>
        <script type="text/javascript" src="pdfobject.js"></script>
        <script type="text/javascript">
            window.onload = function (){
                var myPDF = new PDFObject({ url: "sample.pdf" }).embed();
            };
        </script>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <p>
            It appears you don't have Adobe Reader or PDF support in this web
            browser. <a href="samplepdf.pdf">Click here to download the PDF</a>
        </p>
    </body>
</html>
