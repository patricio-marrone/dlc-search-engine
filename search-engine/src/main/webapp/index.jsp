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

        <!--<link rel="stylesheet" type="text/css" href="css/style.css" />-->
        <%!    private static final String LOCAL_PATH = "/search-engine/searchController";
        %>

    </head>
    <body>
        <div class="center">
            <h3>Wellcome to Search Engine v1.0</h3>
        </div>
        <div id="input-search" class="form center">
            <form  class="registro" method="POST" action="<%=LOCAL_PATH%>" autocomplete="on">
                <h5>Enter your search</h5> 
                <p> 
                    <label for="text-find" class="text "></label>
                    <input type="text" name="text-find" class="input-xxlarge" placeholder="Insert your search" required/>
                    <INPUT type="submit" VALUE="Search" class="btn btn-sm btn-primary">
                </p>
            </form>
        </div>
    </body>
</html> 