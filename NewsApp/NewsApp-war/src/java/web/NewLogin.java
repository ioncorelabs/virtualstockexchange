/*
 * NewLogin.java
 *
 * Created on October 18, 2008, 5:27 PM
 */

package web;

import ejb.LoginEntity;
import ejb.LoginEntityFacadeLocal;
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
 * @author Vaibhav
 * @version
 */
public class NewLogin extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        
        response.setContentType("text/html;charset=UTF-8");
        
        String uid=request.getParameter("userid");
        String pwd=request.getParameter("password");
        
        session.setAttribute("userId", uid);        
        
        if ((uid!=null) && (pwd!=null)) {
            LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
            List news = loginEntityFacade.findAll(uid, pwd);
            
            if (!news.isEmpty()) {
                response.sendRedirect("SellScrips");
            }
        }
        
        PrintWriter out = response.getWriter();
        //TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet NewLogin</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet NewLogin at " + request.getContextPath() + "</h1>");
        out.println("<h2>Please enter your user id and password to login</h2>");
        out.println("</body>");
        out.println("</html>");
        
        out.println("<form>");
        out.println("User Id: <input type='text' name='userid'><br/>");
        out.println("Password: <input type='password' name='password'><br/>");
        out.println("<input type='submit'><br/>");
        out.println("</form>");
             
        
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
    
    private LoginEntityFacadeLocal lookupLoginEntityFacade() {
        try {
            Context c = new InitialContext();
            return (LoginEntityFacadeLocal) c.lookup("NewsApp/LoginEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    
}
