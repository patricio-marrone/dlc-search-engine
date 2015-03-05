<%-- 
    Document   : index
    Created on : 18/02/2015, 19:10:08
    Author     : GERBOOK
--%>

<%@page import="java.util.LinkedList"%>
<%@page import="ar.edu.utn.frc.dlc.searchengine.RankingEntry"%>
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

        <script src="js/jquery.min.js"></script>
        <script src="js/jquery.dataTables.min.js"></script>
        <script src="js/jquery.dataTables.bootstrap.js"></script>


        <!--<link rel="stylesheet" type="text/css" href="css/style.css" />-->
        <%!    private static final String LOCAL_PATH = "/search-engine/searchController";
        %>

    </head>
    <body>
        <div class="center">
            <h3>Welcome to Search Engine v1.0</h3>
        </div>

        <jsp:useBean id="searchText" class="String" scope="request" />

        <div id="input-search" class="form center">
            <form  class="registro" method="POST" action="<%=LOCAL_PATH%>" autocomplete="on" accept-charset="UTF-8">
                <h5>Enter your search</h5> 
                <p> 
                    <label for="text-find" class="text "></label>
                    <input type="text" name="text-find"  placeholder="Insert your search" class="input-xxlarge" 
                           value="<%=request.getAttribute("searchText")%>"/>
                    <INPUT type="submit" VALUE="Search" class="btn btn-sm btn-primary">
                </p>
            </form>
        </div>

        <jsp:useBean id="listDocuments" class="LinkedList<RankingEntry>" scope="request" />

        <%
            if (listDocuments.size() > 0) {
        %>

        <div class="table-responsive">
            <div class="col-xs-12">
                <h3 class="header smaller lighter blue">Tables Results</h3>
                <div class="table-header">
                    <b><%=listDocuments.size()%></b> results for "<%=request.getAttribute("searchText")%>"
                </div>
                <table id="results-table" class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>                                
                            <th class="center">Ranking</th>
                            <th class="center">Title</th>                                
                            <th class="center">Author</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (RankingEntry item : listDocuments) {
                        %>
                        <tr>                            
                            <td><%=item.getAcumulator()%></td>  
                            <td><a target="_blank" href="http://<%=item.getLinkFile()%>"><%=item.getTitle()%></a></td> 
                            <td><%=item.getAuthor()%></td> 
                        </tr>                            
                        <%      }
                        } else {
                        %>
                    <div class='col-lg-3'></div>
                    <div class='center col-lg-6'>
                        <div class="alert alert-danger">                       
                            <strong>
                                <i class="ace-icon fa fa-times"></i>
                                Oh snap!
                            </strong>
                            Change a few things up and try submitting again.
                            <br />
                        </div>
                    </div>
                    <%
                        }
                    %>                          
                    </tbody>
                </table>
            </div>            
        </div>   

        <script type="text/javascript">
            jQuery(function ($) {
                var oTable1 =
                        $('#results-table')
                        //.wrap("<div class='dataTables_borderWrap' />")   //if you are applying horizontal scrolling (sScrollX)
                        .dataTable({
                            "aaSorting": []
                        });
            });
        </script>
    </body>
</html> 