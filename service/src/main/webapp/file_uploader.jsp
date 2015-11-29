<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>The file uploader for each class</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/dropzone.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/assets/css/feckless-dropzone.css" rel="stylesheet" type="text/css">
        <script src="${pageContext.request.contextPath}/assets/js/dropzone/dropzone.js"></script>
    </head>
    <body>
        <div class="container">
            <center><h1>Want to contribute and help others?</h1></center>
            <center><h2>Upload documents or make them right here on the site!</h2></center>
            <div class="col-md-4">
                <center><button type="button" class="btn btn-warning btn-lg fileinput-button" id="upload">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>Upload!</span>
                </button></center>       
            </div>
            <div class="col-md-4">
                <h1><center>OR</center></h1>
            </div>
            <div class="col-md-4">
                <center><button type="button" class="btn btn-warning btn-lg" onclick="location.href='/editor.jsp?cid=${course.getID()}'">
                    <i class="glyphicon glyphicon-edit"></i>
                    <span>Take notes!</span>
                </button></center>
            </div>
        </div>
        <form action="/servlet/file/upload" method="post" enctype="multipart/form-data">
        <div class="table table-striped files" id="previews">
            <div id="template" class="file-row">
                <!-- This is used as the file preview template -->
                <div class="col-md-3">
                    <span class="preview">
                        <img align="right" data-dz-thumbnail/>
                    </span>
                </div>
                <div class="col-md-6">
                    <div>
                        <input type="text" class="form-control" name="title" id="title" placeholder="Enter title here">
                    </div>    
                    <div>
                        <textarea class="form-control" rows="4" cols="200" id="description">Description</textarea>
                    </div>
                    <div>
                        <input type="hidden" id="classID" value="${param.classID}" />
                    </div>
                    <div class="btn-group" role="group" aria-label="Tag yo doc">
                        <button type="button" id="tag" class="btn btn-warning" value="notes">Notes</button>
                        <button type="button" id="tag" class="btn btn-warning" value="assignmentAnswers">Assignment Answers</button>
                        <button type="button" id="tag" class="btn btn-warning" value="assignment">Assignment</button>
                        <button type="button" id="tag" class="btn btn-warning" value="quiz">Quiz</button>
                        <button type="button" id="tag" class="btn btn-warning" value="quizAnswers">Quiz Answers</button>
                        <button type="button" id="tag" class="btn btn-warning" value="test">Test</button>
                        <button type="button" id="tag" class="btn btn-warning" value="testAnswers">Test Answers</button>
                    </div>
                </div>
                <div class="col-md-3">
                    <div>
                    <button type="submit" class="btn btn-warning btn-lg start" id="submit">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span>Submit</span>
                    </button>
                    </div>
                    <div>
                    <button data-dz-remove class="btn btn-danger cancel btn-lg">
                        <i class="glyphicon glyphicon-ban-circle"></i>
                        <span>Cancel</span>
                    </button>
                    </div>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/assets/js/dropzone/feckless_dropzone.js"></script>
        </form>
    </body>
</html>
