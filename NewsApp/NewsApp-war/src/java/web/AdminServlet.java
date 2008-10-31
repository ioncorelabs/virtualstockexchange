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
        out.println("<title>Adminstrator</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<center>");
        out.println("<b>Administrator Screen</b><br/>");
        out.println("<b>Welcome back, " + userid + "!</b><br/>");
        out.println("<a href='AddUserServlet'>Add User</a><br/>");
        out.println("<a href='DelUserServlet'>Delete User</a><br/>");
        out.println("<a href='AddScripServlet'>Add Scrip</a><br/>");
        out.println("<a href='DelScripServlet'>Delete Scrip</a><br/>");
        out.println("<a href='EditUserServlet'>Edit User</a><br/>");
        out.println("<a href='EditScripServlet'>Edit Scrip</a><br/>");
        out.println("</center>");                          
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
