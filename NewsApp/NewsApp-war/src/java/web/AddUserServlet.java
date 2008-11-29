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
import org.apache.html.dom.HTMLBuilder;
import web.utils.HtmlBuilder;

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
        if (isInvalidSession(session)) {
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
        boolean erroredNumNull = false;
        boolean erroredNumType = false;
        boolean erroredUserName = false;
        int numInt = 0;
        
        if((userid != null) && (password != null) && (username != null) && (usertype != null) && (cashheld != null) ) {
            
            if((userid.equals("")) || (password.equals("")) || (username.equals("")) || (cashheld.equals(""))){
                erroredNumNull = true;
            }else{
                try{numInt = Integer.parseInt(cashheld);} catch(NumberFormatException e) {
                    erroredNumType = true;
                }
            }
            if(!erroredNumType && (numInt<0)) {
                erroredNumType = true;
            }
            
            if(HtmlBuilder.hasNumber(username)) {
                erroredUserName = true;
            }
        }
        
        
        
        
        if ((userid!=null) && (password!=null) && (username!=null) && (usertype!=null) && (cashheld!=null) && (!erroredNumNull) && (!erroredNumType) && (!erroredUserName)) {
            char userRole = usertype.charAt(0);
            double cashHeldDbl = Double.parseDouble(cashheld);
            
            LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
            UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
            
            if (loginEntityFacade.find(userid) != null) {
                errored = true;
            } else {
                
                LoginEntity loginEntity = new LoginEntity();
                UsersEntity usersEntity = new UsersEntity();
                
                usersEntity.setUserId(userid);
                loginEntity.setUserId(userid);
                loginEntity.setPassword(password);
                usersEntity.setUserName(username);
                loginEntity.setUserRole(userRole);
                usersEntity.setInitialCashHeld(cashHeldDbl);
                usersEntity.setCashHeld(cashHeldDbl);
                usersEntity.setActive('y');

                loginEntityFacade.create(loginEntity);
                usersEntityFacade.create(usersEntity);
                
                response.sendRedirect("AdminServlet");
                return;
            }
        }
        
        
        
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.buildHtmlHeader("Add User"));
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Add User Form</span><br>");
        if (errored)
            out.println("<font color=red><b>That user ID already exists, please try again.</b></font><br>");
        
        if (erroredNumNull)
            out.println("<br><font color=red><b>All fields are required</b></font><br><br>");
        if (erroredNumType)
            out.println("<br><font color=red><b>Please enter a valid value for cash held</b></font><br><br>");
        if (erroredUserName)
            out.println("<br><font color=red><b>User Name can only contain alphabets </b></font><br><br>");
        
        out.println("<form>");
        
        out.println("<table width=350px cellpadding=4px>");
        out.println("<tr><td width=150px>User Id:</td><td><input type='text' name='userid'></td></tr>");
        out.println("<tr><td>Password:</td><td><input type='password' name='password'></td></tr>");
        out.println("<tr><td>User Name:</td><td><input type='text' name='username'></td></tr>");
        out.println("<tr><td>User Type:</td><td><select name='usertype'>");
        out.println("<option value='admin'>Admin</option>");
        out.println("<option value='trader'>Trader</option>");
        out.println("<option value='investor'>Investor</option>");
        out.println("</select></td></tr>");
        out.println("<tr><td>Cash Held:</td><td><input type='text' name='cashheld'></td></tr>");
        out.println("</table>");
        out.println("<input type='submit' value='Add User'>   ");
        out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
        out.println("</form>");
        
        out.println(HtmlBuilder.buildHtmlFooter());
        out.close();
    }
    
    private boolean isInvalidSession(final HttpSession session) {
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
    
    /**
     * Perform JNDI lookup on UsersEntity for handle on its facade.
     * @return Local facade of the UsersEntity
     */
    private UsersEntityFacadeLocal lookupUsersEntityFacade() {
        try {
            Context c = new InitialContext();
            return (UsersEntityFacadeLocal) c.lookup("NewsApp/UsersEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    /**
     * Perform JNDI lookup on LoginEntity for handle on its facade.
     * @return Local facade of the LoginEntity
     */
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
