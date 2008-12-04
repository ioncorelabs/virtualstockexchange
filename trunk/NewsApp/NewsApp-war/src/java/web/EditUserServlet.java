/*
 * EditUserServlet.java
 *
 * Created on October 29, 2008, 8:41 PM
 */

package web;

import ejb.LoginEntity;
import ejb.LoginEntityFacade;
import ejb.LoginEntityFacadeLocal;
import ejb.UsersEntity;
import ejb.UsersEntityFacadeLocal;
import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
 * @author jmoral
 * @version
 */
public class EditUserServlet extends HttpServlet {
    
    private NumberFormat _nf = NumberFormat.getNumberInstance();
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(true);
        if (isInvalidSession(session)) {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("userid",              request.getParameter("userid"));
        parameterMap.put("username",            request.getParameter("username"));
        parameterMap.put("cashheld",            request.getParameter("cashheld"));
        parameterMap.put("password",            request.getParameter("password"));
        parameterMap.put("usertype",            request.getParameter("usertype"));
        
        UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
        LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
        
        boolean erroredBlankFields = false;
        boolean erroredNumType = false;
        boolean erroredUserNameMax = false;
        boolean erroredUserNameText = false;
        boolean erroredPasswordMin = false;
        boolean erroredPasswordMax = false;
        
        double numDbl = 0;
        
        if (HtmlBuilder.isFormSubmitted(parameterMap))
        {
            if (HtmlBuilder.hasBlankFields(parameterMap)) {
                erroredBlankFields = true;
            } else {
                try{numDbl = Double.parseDouble(parameterMap.get("cashheld")); } 
                catch(NumberFormatException e) { erroredNumType = true; }
            }
            if (!erroredNumType && (numDbl < 0.0))
                erroredNumType = true;
            
            if (parameterMap.get("username").length() > 40)
                erroredUserNameMax = true;
            if (parameterMap.get("password").length() < 3)
                erroredPasswordMin = true;
            if (parameterMap.get("password").length() > 16)
                erroredPasswordMax = true;
            
            if (!HtmlBuilder.isValidUserName(parameterMap.get("username")))
                erroredUserNameText = true;
            
            if((!erroredBlankFields) && (!erroredNumType) && (!erroredUserNameText) && 
                !erroredUserNameMax && !erroredPasswordMin && !erroredPasswordMax)
            {
                UsersEntity user = usersEntityFacade.find(parameterMap.get("userid"));
                LoginEntity login = loginEntityFacade.find(parameterMap.get("userid"));
                
                user.setUserName(parameterMap.get("username"));
                user.setCashHeld(Double.parseDouble(parameterMap.get("cashheld")));
                login.setPassword(parameterMap.get("password"));
                login.setUserRole(parameterMap.get("usertype").charAt(0));
                
                usersEntityFacade.edit(user);
                loginEntityFacade.edit(login);
                
                session.setAttribute("message", parameterMap.get("username")+" was successfully edited");
                response.sendRedirect("AdminSuccessServlet");
            }
        }
        
        List users = usersEntityFacade.findAllActive();
        
        printForm(request, response, users, loginEntityFacade,
                erroredBlankFields, erroredNumType, erroredUserNameMax, erroredUserNameText, erroredPasswordMin, erroredPasswordMax);
    }
    
    private boolean isInvalidSession(final HttpSession session) {
        return  session.isNew() ||
                session.getAttribute("userid") == null ||
                session.getAttribute("userrole") == null ||
                !((String)session.getAttribute("userrole")).equals("a");
    }
    
    private void printForm(final HttpServletRequest request, final HttpServletResponse response, 
                        List users, LoginEntityFacadeLocal loginEntityFacade,
                        final boolean erroredBlankFields, 
                        final boolean erroredNumType, 
                        final boolean erroredUserNameMax,
                        final boolean erroredUserNameText, 
                        final boolean erroredPasswordMin,
                        final boolean erroredPasswordMax) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.buildHtmlHeader("Edit User"));
        
        out.println("<center><span class=\"ttitle\" style=\"580px;\"><br/>Edit User Form</span><br><br>");
        if (erroredBlankFields)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_BLANK);
        if (erroredNumType)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_CASH);
        if (erroredUserNameMax)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_USERNAME_MAX);
        if (erroredUserNameText)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_USERNAME_TEXT);
        if (erroredPasswordMin)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_PASSWORD_MIN);
        if (erroredPasswordMax)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_PASSWORD_MAX);
        
        out.println("<br/><table width=600px border=1>");
        out.println("<tr><td>User ID</td><td>User Name</td><td>Password</td><td>User Role</td><td>Current Cash Held</td><td>&nbsp;</td></tr>");
        
        
        _nf.setMaximumFractionDigits(2);
        _nf.setMinimumFractionDigits(2);
        _nf.setGroupingUsed(false);
        
        for (Iterator it = users.iterator(); it.hasNext();) {
            UsersEntity user = (UsersEntity)it.next();
            LoginEntity login = loginEntityFacade.find(user.getUserId());
            
            out.println("<form method=post>");
            out.println("<tr><td>" + user.getUserId() + "<input type='hidden' name='userid' value='" + user.getUserId() + "'></td>");
            out.println("<td><input type='text' name='username' value='" + user.getUserName() + "' maxlength=16></td>");
            out.println("<td><input type='password' name='password' value='" + login.getPassword() + "' maxlength=16></td>");
            
            out.println("<td><select name='usertype'>");
            out.println("<option value='admin'" + ((login.getUserRole() == 'a') ? " selected" : "") + ">Admin</option>");
            out.println("<option value='trader'" + ((login.getUserRole() == 't') ? " selected" : "") + ">Trader</option>");
            out.println("<option value='investor'" + ((login.getUserRole() == 'i') ? " selected" : "") + ">Investor</option>");
            out.println("</select></td>");
            
            out.println("<td><input type='text' name='cashheld' value='" + _nf.format(user.getCashHeld())+ "' maxlength=9></td>");
            out.println("<td><input type='submit' value='Edit'></td></tr>");
            out.println("</form>");
        }
                
        out.println("<tr><td align=center colspan=6><input " +
                "type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/></center></td></tr>");
        
        out.println("</table><br><br>");
        
        out.println(HtmlBuilder.buildHtmlFooter());
        out.close();
    }
    
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
}
