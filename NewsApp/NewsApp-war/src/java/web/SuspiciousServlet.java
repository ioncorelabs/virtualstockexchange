/*
 * SuspiciousServlet.java
 *
 * Created on December 3, 2008, 8:27 PM
 */

package web;

import java.io.*;
import java.net.*;
import java.util.Random;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.html.dom.HTMLBuilder;
import web.utils.HtmlBuilder;

/**
 *
 * @author jmoral
 * @version
 */
public class SuspiciousServlet extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchance: Hmm...</title>");
        int rnd = (new Random()).nextInt(HtmlBuilder.DO_GET_ANNOYANCE_PAGES.length);
        out.println("<meta HTTP-EQUIV=\"REFRESH\" content=\"5; url=" + HtmlBuilder.DO_GET_ANNOYANCE_PAGES[rnd] + "\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<link href=\"greeny.css\" rel=\"stylesheet\" type=\"text/css\" />");
        out.println("</head>");
        out.println("<body>");
        out.println("<div id=\"tot\">");
        out.println("<div id=\"header\">");
        out.println("<img src=\"img/genericlogo.png\" align=\"left\" alt=\"company logo\"/> <span class=\"title\">Virtual Stock Exchange</span>");
        out.println("<div class=\"slogan\">Bulls & Bears</div>");
        out.println("<div id=\"corp\">");
        out.println("<div class=\"main-text\">");
        out.println("<center><span class=\"ttitle\" style=\"580px;\"><br>Hmm...</span><br><br>");
        out.println("<br/>");
        HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.SUSPICIOUS);
        out.println("</center>");
        
        out.println(HtmlBuilder.buildHtmlFooter());
        out.close();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
