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
        if (session.isNew() || session.getAttribute("userid") == null || session.getAttribute("userrole") == null || !((String)session.getAttribute("userrole")).equals("a"))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        System.out.println("At admin page as user '" + userid + "'");
        
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchance: Adminstrator Homepage</title>");
        out.println("</head>");
        out.println("<body>");
        //Common Styling Code
        out.println("<link href=\"greeny.css\" rel=\"stylesheet\" type=\"text/css\" />");
        out.println("</head>");
        out.println("<body>");
        out.println("<div id=\"tot\">");
        out.println("<div id=\"header\">");
        out.println("<img src=\"img/genericlogo.png\" align=\"left\" alt=\"company logo\"/> <span class=\"title\">Virtual Stock Exchange</span>");
        out.println("<div class=\"slogan\">Bulls & Bears</div>");       
        out.println("<div id=\"corp\">");
        out.println("<div class=\"main-text\">");
        //Common Ends
        out.println("<div id=\"head\" align=\"center\"");
        out.println("<span class=\"ttitle\" style=\"580px;\">Administrator</span><br>");
        out.println("<span class=\"ttitle\"><b>Welcome back, " + userid + "!</b></span><br><br><br>");
        out.println("</div>");
        out.println("<br><br><br><br><br>");
        
        out.println("<div id=\"menius\">");
        out.println("<div class=\"menu-title\">Menu</div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" vspace=\"5\" align=\"left\" /><a href=\"AddUserServlet\">Add User</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" vspace=\"5\" align=\"left\" /><a href=\"DelUserServlet\">Delete User</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" vspace=\"5\" align=\"left\" /><a href=\"AddScripServlet\">Add Scrip</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" vspace=\"5\" align=\"left\" /><a href=\"DelScripServlet\">Delete Scrip</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" vspace=\"5\" align=\"left\" /><a href=\"EditUserServlet\">Edit User</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" vspace=\"5\" align=\"left\" /><a href=\"EditScripServlet\">Edit Scrip</a></div>"); 
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" vspace=\"5\" align=\"left\" /><a href=\"LogoutServlet\">Logout</a></div>");
        out.println("</div>");
      
        //Common Starts
        out.println("</div></div>");
        out.println("<div class=\"clear\"></div>");        
        out.println("<div class=\"footer\"><span style=\"margin-left:400px;\">The Bulls & Bears Team</span></div>");
        out.println("</div>");
        //Common Ends
        
        out.println("</body>");
        out.println("</html>");
        
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
