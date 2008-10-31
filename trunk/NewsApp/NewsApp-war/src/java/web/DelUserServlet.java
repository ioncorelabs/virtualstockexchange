/*
 * DelUserServlet.java
 *
 * Created on October 29, 2008, 7:30 PM
 */

package web;

import ejb.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Milind Nimesh
 * @version
 */
public class DelUserServlet extends HttpServlet {
    
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
        
        String selfid = (String)session.getAttribute("userid");
        System.out.println("At admin page as user '" + selfid + "'");
        
        String userid = request.getParameter("userid");
        UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
        
        if (userid != null)
        {   System.out.println("zcheck1");
            usersEntityFacade.delete(usersEntityFacade.find(userid));
            System.out.println("zcheck2");
            response.sendRedirect("AdminServlet"); 
        }
        
        List users = usersEntityFacade.findAll();      
        PrintWriter out = response.getWriter();
       
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet DelUserServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet DelUserServlet at " + request.getContextPath () + "</h1>");
        out.println("Delete User Form");
        out.println("<form>");
        out.println("Scrip Id: <select name='userid'>");
        
                
        for (Iterator it = users.iterator(); it.hasNext();)
        {
            UsersEntity user = (UsersEntity)it.next();
            out.println("<option value='" + user.getUserId() + "'>" + user.getUserId() + "</option><br/>");
        }
        
        out.println("</select><br>");
        out.println("<input type='submit' value='Delete Scrip'><br/>");
        out.println("</form>");        
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
    
        private UsersEntityFacadeLocal lookupUsersEntityFacade() {
        try {
            Context c = new InitialContext();
            return (UsersEntityFacadeLocal) c.lookup("NewsApp/UsersEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
}
