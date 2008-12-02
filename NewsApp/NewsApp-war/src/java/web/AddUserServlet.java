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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.html.dom.HTMLBuilder;
import web.utils.*;

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
        
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("userid",              request.getParameter("userid"));
        parameterMap.put("password",            request.getParameter("password"));
        parameterMap.put("username",            request.getParameter("username"));
        parameterMap.put("usertype",            request.getParameter("usertype"));
        parameterMap.put("cashheld",            request.getParameter("cashheld"));
        
        boolean erroredBlankFields = false;
        boolean erroredNumType = false;
        boolean erroredUserNameText = false;
        boolean erroredUserNameMax = false;
        boolean erroredUserIDMin = false;
        boolean erroredUserIDMax = false;
        boolean erroredPasswordMin = false;
        boolean erroredPasswordMax = false;
        double dblCashHeld = 0;
        
        if(HtmlBuilder.isFormSubmitted(parameterMap)) {
            
            if(HtmlBuilder.hasBlankFields(parameterMap)) {
                erroredBlankFields = true;
            } else {
                try{dblCashHeld = Double.parseDouble(parameterMap.get("cashheld")); } 
                catch(NumberFormatException e) { erroredNumType = true; }
            }
            
            if (!erroredNumType && (dblCashHeld < 0.0))
                erroredNumType = true;
            if (parameterMap.get("username").length() > 40)
                erroredUserNameMax = true;
            
            if (parameterMap.get("userid").length() < 3)
                erroredUserIDMin = true;
            if (parameterMap.get("userid").length() > 16)
                erroredUserIDMax = true;
            
            if (parameterMap.get("password").length() < 3)
                erroredPasswordMin = true;
            if (parameterMap.get("password").length() > 16)
                erroredPasswordMax = true;
            
            
            if (HtmlBuilder.hasNumber(parameterMap.get("username")))
                erroredUserNameText = true;
            
        }
        
        boolean erroredUserExists = false;
        
        if (HtmlBuilder.isFormSubmitted(parameterMap) && 
                !erroredBlankFields && !erroredNumType && !erroredUserNameText && 
                !erroredUserNameMax && !erroredUserIDMin && !erroredUserIDMax && !erroredPasswordMin && !erroredPasswordMax) 
        {
            char userRole = parameterMap.get("usertype").charAt(0);
            double cashHeldDbl = Double.parseDouble(parameterMap.get("cashheld"));
            
            LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
            UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
            
            if (loginEntityFacade.find(parameterMap.get("userid")) != null) {
                erroredUserExists = true; // already exists
            } else {
                
                LoginEntity loginEntity = new LoginEntity();
                UsersEntity usersEntity = new UsersEntity();
                
                usersEntity.setUserId(parameterMap.get("userid"));
                loginEntity.setUserId(parameterMap.get("userid"));
                loginEntity.setPassword(parameterMap.get("password"));
                usersEntity.setUserName(parameterMap.get("username"));
                loginEntity.setUserRole(userRole);
                usersEntity.setInitialCashHeld(cashHeldDbl);
                usersEntity.setCashHeld(cashHeldDbl);
                usersEntity.setActive('y');

                loginEntityFacade.create(loginEntity);
                usersEntityFacade.create(usersEntity);
                
                session.setAttribute("message", parameterMap.get("username")+" was successfully added to the exchange");
                response.sendRedirect("AdminSuccessServlet");                
            }
        }
        
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.buildHtmlHeader("Add User"));
        
        out.println("<span class=\"ttitle\" style=\"580px;\"><center><br>Add User Form</span><br><br>");
        
        if (erroredUserExists)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.USER_EXISTS);
        if (erroredBlankFields)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_BLANK);
        if (erroredNumType)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_CASH);
        if (erroredUserNameText)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_USERNAME_TEXT);
        if (erroredUserNameMax)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_USERNAME_MAX);
        if (erroredUserIDMin)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_USERID_MIN);
        if (erroredUserIDMax)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_USERID_MAX);
        if (erroredPasswordMin)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_PASSWORD_MIN);
        if (erroredPasswordMax)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_PASSWORD_MAX);
        
        out.println("<br/><form method=post>");
        out.println("<table width=350px cellpadding=4px border=1>");
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
        out.println("<input type='submit' value='Submit'>   ");
        out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
        out.println("</form></center>");
        
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
