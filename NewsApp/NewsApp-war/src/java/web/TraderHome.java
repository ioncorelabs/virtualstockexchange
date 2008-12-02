/*
 * TraderHome.java
 *
 * Created on October 30, 2008, 12:49 PM
 */

package web;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;
import web.utils.HtmlBuilder;

/**
 *
 * @author Vaibhav
 * @version
 */
public class TraderHome extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        if (isInvalidSession(session))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();        
        //print boilerplate HTML header
        out.println(HtmlBuilder.buildHtmlHeader("Trader Homepage"));
        
        //main HTML content
        out.println("<div id=\"head\" align=\"center\"");
        out.println("<span class=\"ttitle\" style=\"580px;\"><br>Trader Homepage</span><br><br>");       
        out.println("</div>");
        out.println("<br><br><br>");
        
        out.println("<div class=\"main-content\">");
        out.println("Please refer to the menu on the left for the activities available to you.");
        out.println("</div>");
                
        out.println("<div id=\"menius\">");
        out.println("<div class=\"menu-title\">Menu</div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"BuyScrips\">Buy</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"SellScrips\">Sell</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"ShortSellHome\">Short Sell</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"TraderServlet\">Your Portfolio</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"ListingServlet\">Scrip Lookup</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"LogoutServlet\">Logout</a></div>");
        out.println("</div>");
                
        //print boilerplate HTML footer
        out.println(HtmlBuilder.buildHtmlFooter());
        out.close();
    }
    
    /**
     * Helper function for checking if the user is authorized to be at this page.
     * Checks that a valid session exists and for the appropriate role.
     * @param session
     * @return true if valid, false if invalid session or not authorized
     */
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("t");
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
      if (request.getQueryString() != null)
            response.sendRedirect(HtmlBuilder.DO_GET_REDIRECT_PAGE);
        else
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

