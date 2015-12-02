<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="com.fecklessweasel.service.objectmodel.*, java.sql.Connection, com.fecklessweasel.service.datatier.*,  com.fecklessweasel.service.*, java.util.*" %>
<nav class="navbar navbar-fixed-top navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a href="/index.jsp" class="pull-left">
                <img style="padding-top: 15%; max-width:50px; margin-top: -7px" src="${pageContext.request.contextPath}/assets/img/logo.png">
            </a>
        </div>
         <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
         <link rel ="stylesheet" href="/assets/dropdown.scss">
         <script src="//code.jquery.com/jquery-1.10.2.js"></script>
         <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <div id="navbar" class="navbar-collapse collapse">
        <form class="form-inline" id="uidform" name="uidform" style="visibility:hidden" action="/servlet/DepartmentSearch" method="post" enctype="application/x-www-form-urlencoded">
        <input type="hidden" class="form-control" id="univId" name="univId">
        <input type="submit" style="position: absolute; left: -9999px" value="Submit">
        </form>
        <form class="form-inline" id="didform" name="didform" style="visibility:hidden" action="/servlet/CourseSearch" method="post" enctype="application/x-www-form-urlencoded">
            <input type="hidden" class="form-control" id="deptId" name="deptId">
            <input type="submit" style="position: absolute; left: -9999px" value="Submit">
        </form>
        <form class="navbar-form navbar-nav" id="courseform" action="servlet/search" method="post" enctype="application/x-www-form-urlencoded">
            <div class="form-group">
                <input type="text" class="form-control" id="college" name="college" placeholder="University">
                    <% List<University> universities =
                            SQLSource.interact(new SQLInteractionInterface<List<University>>() {
                               @Override
                               public List<University> run(Connection connection) throws ServiceException {
                                   return University.lookUpAll(connection);
                                   }
                               });
                    List<String> uniName = new ArrayList<String>();
                    for(int i = 0; i < universities.size(); i++){
                        uniName.add(universities.get(i).getLongName());
                    }
                    %>
                    <p hidden id="uniSize"><%=uniName.size()%></p>
                    <%
                    for(int i=0; i<uniName.size(); i++){
                    %>
                    <div class= "university" style="display: none;">
                       <%=uniName.get(i)%>
                    </div>
                    <div class = "univID" style="display: none;">
                       <%=universities.get(i).getID()%>
                    </div>
                    <%}%>
                <script>
                    var nodeList = document.getElementsByClassName("university");
                    var IdNodeList = document.getElementsByClassName("univID");
                    var uniList = new Array();
                    var uIdList = new Array ();
                    for (var i = 0; i < nodeList.length; i++) {
                            var uni = String(nodeList.item(i).innerText)
                            uni = uni.trim()
                            uniList.push(uni);
                            var uid = String(IdNodeList.item(i).innerText);
                            uid = uid.trim();
                            uIdList.push(uid);
                       }
                    $(function() {
                        $("#college").autocomplete({
                        source: function( request, response ) {
                             var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
                             response( $.grep( uniList, function( item ){
                                 return matcher.test( item );
                             }) );
                            },
                        change: function(event, ui) {
                          var index =0;
                          var college = document.getElementById("college").value;
                             while(index<uniList.length){
                                 if(college == uniList[index]){
                                     var uid = uIdList[index];
                                     break;
                                 }
                                 index++
                             }
                          $('#univId').val(uid);
                            }
                        });
                    });
                </script>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" id="department" name="department" placeholder="Department Acronym">'>
                  <script>
                      var deptList = new Array();
                      var deptIdList = new Array();
                      var deptClick = document.getElementById("department");
                      document.addEventListener('DOMContentLoaded', function () {
                          deptClick.addEventListener('focus', function() {
                              var $form = $("#uidform");
                              deptList = new Array();
                              deptIdList = new Array;
                              $.post($form.attr("action"), $form.serialize(), function(responseJson) {
                                $.each(responseJson, function(key, value) {
                                    deptList.push(value);
                                    deptIdList.push(key);
                                });
                              });
                          });
                      });
                      $(function() {
                          $("#department").autocomplete({
                              source: function( request, response ) {
                                  var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
                                  response( $.grep( deptList, function( item ){
                                      return matcher.test( item );
                                  }) );
                              },
                              change: function(event, ui) {
                                  var indexD =0;
                                  var department = document.getElementById("department").value;
                                  while(indexD<deptList.length){
                                      if(department == deptList[indexD]){
                                          var did = deptIdList[indexD];
                                          break;
                                      }
                                      indexD++
                                  }
                                  $('#deptId').val(did);
                              }
                          });
                      });
                </script>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" id="class" name="class" placeholder="Course Number">
                <script>
                var courseList = new Array();
                var courseIdList = new Array();
                var courseClick = document.getElementById("class");
                document.addEventListener('DOMContentLoaded', function () {
                    courseClick.addEventListener('focus', function() {
                        courseList = new Array();
                        courseLidList = new Array();
                        var $form = $("#didform");
                            $.post($form.attr("action"), $form.serialize(), function(responseJson) {
                                $.each(responseJson, function(key, value) {
                                    courseList.push(value);
                                    courseIdList.push(key);
                                });
                            });
                         });
                });
                $(function() {
                    $("#class").autocomplete({
                        source: function( request, response ) {
                            var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
                            response( $.grep( courseList, function( item ){
                                return matcher.test( item );
                            }) );
                        },
                        change: function(event, ui) {
                            var indexC =0;
                            var course = document.getElementById("class").value;
                            while(indexC<courseList.length){
                                if(course == courseList[indexC]){
                                    var cid = courseIdList[indexC];
                                    break;
                                }
                                indexC++
                            }
                            $('#courseId').val(cid);
                        }
                    });
                });
                </script>
            </div>
            <input type="hidden" class="form-control" id="courseId" name="courseId">
            <button type="submit" class="btn btn-warning" id="search-submit" name="search-submit">Go</button>
        </form>
        <ul class="nav navbar-nav navbar-right">
            <%-- Instantiate the session if there is one. You can use this in pages that include this. --%>
            <%
                final UserSession authSession = UserSessionUtil.resumeSession(request);
             %>
            <%-- Show signup and login or logout button --%>
            <%= authSession != null ?
                "<li><a href='/account/index.jsp'>Profile</a></li>"+
                "<li><form class='navbar-form navbar-nav' action='/servlet/user_session' method='post'>" +
                "<input type='hidden' name='action' value='delete'>"+
                "<a href='javascript:;' onclick='parentNode.submit();\'><span class='glyphicon glyphicon-log-out' style='color:#f0ad4e; font-size:2em;''></a>" +
                "</form></li>":
                "<li><a href='/account/create.jsp'>Sign up</a></li>" +
                "<li><a href='/account/login.jsp'>Login</a></li>"
                %>
        </ul>
        </div>
    </div>
</nav>
