/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frc.dlc.searchengine;

import ar.edu.utn.frc.dlc.searchengine.sqlite.DAL;
import java.io.IOException;
import java.util.LinkedList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

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

        DAL dal = new MongoDAL();
        dal.open();
        DictionaryReader dictionaryReader = new DictionaryReader();
        dictionaryReader.setDal(dal);

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String query = request.getParameter("text-find");

        // Parser -> Terms
        String[] terms = query.split("\\s+");
        for (int i = 0; i < terms.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            terms[i] = terms[i].replaceAll("[^\\w]", "");
            terms[i] = terms[i].toLowerCase();
        }

        RankingBuilder rb = new RankingBuilder(dictionaryReader, terms);
        LinkedList<RankingEntry> rankingList = rb.getRankingEntries();       

        request.setAttribute("listDocuments", rankingList);
        request.setAttribute("searchText", query);
        //Transferimos el control al JSP
        RequestDispatcher dispatch = request.getRequestDispatcher("results.jsp");
        
        response.setCharacterEncoding("UTF-8");
        
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
    }

}
