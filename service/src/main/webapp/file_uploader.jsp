<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>The file uploader for each class</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/assets/css/dropzone.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/assets/css/feckless-dropzone.css" rel="stylesheet" type="text/css">
        <script src="${pageContext.request.contextPath}/assets/js/dropzone/dropzone.js"></script>
    </head>
    <body>
        <div class="table table-striped column-center files" id="previews">
            <div id="template" class="file-row column-center">
                <!-- This is used as the file preview template -->
                <div class="preview-image">
                    <img data-dz-thumbnail />
                </div>
                <div class="text-fields">
                    <div>
                        <input type="text" class="title" placeholder="Enter title here">
                    </div>    
                    <div>
                        <textarea rows="4" cols="200" placeholder="Enter a description here">
                        </textarea>
                    </div>
                </div>
                <div class="button-fields">
                    <button class="btn btn-primary button-extra-large start">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span>Submit</span>
                    </button>
                    <button data-dz-remove class="btn btn-warning cancel button-extra-large">
                        <i class="glyphicon glyphicon-ban-circle"></i>
                        <span>Cancel</span>
                    </button>
                </div>
            </div>
        </div>  
        <script src="${pageContext.request.contextPath}/assets/js/dropzone/feckless_dropzone.js"></script>
    </body>
</html>
