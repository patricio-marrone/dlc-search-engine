/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frc.dlc.searchengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author GERBOOK
 */
@WebServlet(name = "searchController", urlPatterns = {"/searchController"})
public class searchController extends HttpServlet {

    private static final String LOCAL_PATH = "/search-engine/searchController";
    //Agregamos un atributo de tipo ListaComentarios que nos va a servir de Persistencia Volátil

    /**
     * Método de inicialización del Servlet, este método se ejecuta cuando el
     * servlet es cargado por el servidor es posible realizar aquí todas las
     * inicializaciónes que hagan falta.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        // Inicializamos la lista de comentarios, recordemos que este método se ejecuta una sola vez
        // para el servlet.
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet searchController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet searchController at ASDASD" + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchText = request.getParameter("text-find");

        //call search metod
        Document d1 = new Document();
        d1.setPath("documento1//documento1.txt" + searchText);
        Document d2 = new Document();
        d2.setPath("documento2//documento2.txt");
        Document d3 = new Document();
        d3.setPath("documento3//documento3.txt");
        Document d4 = new Document();
        d4.setPath("documento4//documento4.txt");
        Document d5 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d6 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d7 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d8 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d9 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d01 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d12 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d14 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d15 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d13 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d20 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d21 = new Document();
        d5.setPath("documento5//documento5.txt");
        Document d25 = new Document();
        d5.setPath("documento5//documento5.txt");

        ArrayList<Document> listDoc;
        listDoc = new ArrayList<>();
        listDoc.add(d5);
        listDoc.add(d4);
        listDoc.add(d1);
        listDoc.add(d2);
        listDoc.add(d2);        
        listDoc.add(d3);        
        listDoc.add(d3);        
        listDoc.add(d3);        
        listDoc.add(d3);        
        listDoc.add(d3);        
        listDoc.add(d3);        
        listDoc.add(d3);        
        listDoc.add(d3);        
        listDoc.add(d3);        

        request.setAttribute("listDocuments", listDoc);
        request.setAttribute("searchText", searchText);
        //Transferimos el control al JSP
        RequestDispatcher dispatch = request.getRequestDispatcher("results.jsp");
        dispatch.forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
