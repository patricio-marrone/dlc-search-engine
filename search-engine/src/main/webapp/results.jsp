<%-- 
    Document   : index
    Created on : 18/02/2015, 19:10:08
    Author     : GERBOOK
--%>

<!--CODIFICATION TYPE-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>
<%@page import="ar.edu.utn.frc.dlc.searchengine.Document"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Engine</title>   

        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="css/ace-fonts.css" />
        <link rel="stylesheet" type="text/css" href="css/ace.min.css" />
        <link rel="stylesheet" type="text/css" href="css/default.css" />
        <!--<script src="js/ace-extra.min.js"></script>-->



        <!--<link rel="stylesheet" type="text/css" href="css/style.css" />-->
        <%!    private static final String LOCAL_PATH = "/search-engine/searchController";
        %>

    </head>
    <body>
        <div class="center">
            <h3>Wellcome to Search Engine v1.0</h3>
        </div>

        <jsp:useBean id="searchText" class="String" scope="request" />



        <div id="input-search" class="form center">
            <form  class="registro" method="POST" action="<%=LOCAL_PATH%>" autocomplete="on">
                <h5>Enter your search</h5> 
                <p> 
                    <label for="text-find" class="text "></label>
                    <input type="text" name="text-find"  placeholder="Insert your search" value="<%=request.getAttribute("searchText")%>"                                           
                           />
                    <INPUT TYPE="BUTTON" VALUE="Search" class="btn btn-xs btn-primary fa fa-user">
                </p>
            </form>
        </div>

        <jsp:useBean id="listDocuments" class="ArrayList<Document>" scope="request" />

        <%
            if (listDocuments.size() > 0) {
        %>

        <h3 class="header smaller lighter blue">Search results</h3>
        <div class="table-header">
            Results for - (( Colocar busqueda ))-
        </div>

        <div class="table-responsive"> 

            <div class="dataTables_borderWrap"> 
                <div>
                    <table id="results-table" class="table table-striped table-bordered table-hover no-margin-bottom no-border-top">
                        <thead>
                            <tr>                                
                                <th>Document</th>
                                <th class="center">Score</th>                                
                                <th class="center">Go</th>
                            </tr>
                        </thead>

                        <tbody>
                            <%
                                for (Document item : listDocuments) {
                            %>
                            <tr> 
                                <td><a href="#"><%=item.getPath()%></a></td>
                                <td>NO DATA</td>                                
                                <td>NO SCORE</td>                              
                            </tr>                            
                            <%      }
                                }
                            %>  
                        </tbody>
                    </table>

                </div>
                <div class="modal-footer no-margin-top">
                    <ul class="pagination pull-right no-margin">
                        <li class="prev disabled">
                            <a href="#">
                                <i class="ace-icon fa fa-angle-double-left"></i>
                            </a>
                        </li>

                        <li class="active">
                            <a href="#">1</a>
                        </li>

                        <li>
                            <a href="#">2</a>
                        </li>

                        <li>
                            <a href="#">3</a>
                        </li>

                        <li class="next">
                            <a href="#">
                                <i class="ace-icon fa fa-angle-double-right"></i>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            jQuery(function ($) {
                var oTable1 =
                        $('#results-table')
                        //.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
                        .dataTable({
                            bAutoWidth: false,
                            "aoColumns": [
                                {"bSortable": false},
                                null, null
                            ]


                                    //,
                                    //"sScrollY": "200px",
                                    //"bPaginate": false,

                                    //"sScrollX": "100%",
                                    //"sScrollXInner": "120%",
                                    //"bScrollCollapse": true,
                                    //Note: if you are applying horizontal scrolling (sScrollX) on a ".table-bordered"
                                    //you may want to wrap the table inside a "div.dataTables_borderWrap" element

                                    //"iDisplayLength": 50
                        });
            })

        </script>
    </body>
</html> 