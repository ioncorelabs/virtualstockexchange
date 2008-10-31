/*
 * AddUserServlet.java
 *
 * Created on October 24, 2008, 8:48 PM
 */

package web;

import ejb.LoginEntity;
import ejb.LoginEntityFacadeLocal;
import ejb.UsersEntity;
import ejb.UsersEntityFacadeLocal;
import java.io.*;
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
public class AddUserServlet extends HttpServlet {
    
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
        
        String userid=request.getParameter("userid");
        String password=request.getParameter("password");
        String username=request.getParameter("username");
        String usertype=request.getParameter("usertype");
        String cashheld=request.getParameter("cashheld");
        String totalbp=request.getParameter("totalbp");
        

        
        if ((userid!=null) && (password!=null) && (username!=null) && (usertype!=null) && (cashheld!=null) && (totalbp!=null))
        {    char userRole = usertype.charAt(0);
             double cashHeldDbl = Double.parseDouble(cashheld);
             double totalBPDbl = Double.parseDouble(totalbp);
            
            LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
            LoginEntity loginEntity = new LoginEntity();
            
            UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
            UsersEntity usersEntity = new UsersEntity();
            
            usersEntity.setUserId(userid);
            loginEntity.setUserId(userid);
            loginEntity.setPassword(password);
            usersEntity.setUserName(username);
            loginEntity.setUserRole(userRole);
            usersEntity.setInitialCashHeld(cashHeldDbl);
            usersEntity.setCashHeld(cashHeldDbl);
            usersEntity.setTotalBuyPower(totalBPDbl);
            
            loginEntityFacade.create(loginEntity);
            usersEntityFacade.create(usersEntity);
            
            response.sendRedirect("AdminServlet"); 
        }
        
        
        
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet AddUser</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet AddUser at " + request.getContextPath () + "</h1>");
        out.println("Add User Form<br>");
        out.println("<form>");
        out.println("User Id: <input type='text' name='userid'><br/>");
        out.println("Password: <input type='password' name='password'><br/>");
        out.println("User Name: <input type='text' name='username'><br/>");
        out.println("User Type: <select name='usertype'>");
        out.println("<option value='admin'>Admin</option>");
        out.println("<option value='trader'>Trader</option>");
        out.println("<option value='investor'>Investor</option>");
        out.println("</select><br>");
        out.println("Cash Held: <input type='text' name='cashheld'><br/>");
        out.println("Total Buying Power: <input type='text' name='totalbp'><br/>");
        out.println("<input type='submit'><br/>");
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
