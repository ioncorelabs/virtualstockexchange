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
        if (isInvalidSession(session))
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
        
        boolean errored = false;
        
        if ((userid!=null) && (password!=null) && (username!=null) && (usertype!=null) && (cashheld!=null))
        {    
            char userRole = usertype.charAt(0);
            double cashHeldDbl = Double.parseDouble(cashheld);             
            
            LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
            UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
            
            if (loginEntityFacade.find(userid) != null)
            {
                errored = true;
            }
            else
            {
            
                LoginEntity loginEntity = new LoginEntity();
                UsersEntity usersEntity = new UsersEntity();

                usersEntity.setUserId(userid);
                loginEntity.setUserId(userid);
                loginEntity.setPassword(password);
                usersEntity.setUserName(username);
                loginEntity.setUserRole(userRole);
                usersEntity.setInitialCashHeld(cashHeldDbl);
                usersEntity.setCashHeld(cashHeldDbl);

                loginEntityFacade.create(loginEntity);
                usersEntityFacade.create(usersEntity);

                response.sendRedirect("AdminServlet");
                return;
            }
        }
        
        
        
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchance: Add User</title>");
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
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Add User Form</span><br>");
        if (errored)
            out.println("<font color=red><b>That user ID already exists, stupid head!</b></font><br>");
        out.println("<form>");
        out.println("User Id:<font color=\"#FFFFFF\">____</font> <input type='text' name='userid'><br/>");
        out.println("Password:<font color=\"#FFFFFF\">___</font> <input type='password' name='password'><br/>");
        out.println("User Name:<font color=\"#FFFFFF\">___</font><input type='text' name='username'><br/>");
        out.println("User Type:<font color=\"#FFFFFF\">___</font> <select name='usertype'>");
        out.println("<option value='admin'>Admin</option>");
        out.println("<option value='trader'>Trader</option>");
        out.println("<option value='investor'>Investor</option>");
        out.println("</select><br>");
        out.println("Cash Held:<font color=\"#FFFFFF\">__</font><input type='text' name='cashheld'><br/>");        
        out.println("<input type='submit' value='Add User'>   ");
         out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
        out.println("</form>"); 
        
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
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return (session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("a"));
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
