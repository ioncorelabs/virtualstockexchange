/*
 * AdminServlet.java
 *
 * Created on October 24, 2008, 7:35 PM
 */

package web;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import web.utils.HtmlBuilder;

/**
 *
 * @author Milind Nimesh
 * @version
 */
public class AdminServlet extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(true);
        if (isInvalidSession(session))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        System.out.println("At admin page as user '" + userid + "'");
        
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.buildHtmlHeader("Administrator Homepage"));
        out.println("<div id=\"head\" align=\"center\"");
        out.println("<br><span class=\"ttitle\" style=\"580px;\">Administrator Homepage</span><br>");        
        out.println("</div>");
        
        out.println("<div class=\"main-content\">");
        out.println("<br><br>The administrative menu on the left gives you full control over the Virtual Stock Exchange. Choose from the menu options to perform operations like Add, Edit, Enable on both Users and Scrips. ");
        out.println("</div><br>");
        
        out.println("<div id=\"menius\">");
        out.println("<div class=\"menu-title\">Menu</div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"AddUserServlet\">Add User</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"EditUserServlet\">Edit User</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"DelUserServlet\">Deactivate User</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"ReUserServlet\">Reactivate User</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"AddScripServlet\">Add Scrip</a></div>");                
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"EditScripServlet\">Edit Scrip</a></div>"); 
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"LogoutServlet\">Logout</a></div>");
        out.println("</div>");
      
        out.println(HtmlBuilder.buildHtmlFooter());
        out.close();
    }
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("a");
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
