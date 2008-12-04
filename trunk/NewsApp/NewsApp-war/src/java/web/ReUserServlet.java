/*
 * ReUserServlet.java
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
import web.utils.HtmlBuilder;

/**
 *
 * @author Milind Nimesh
 * @version
 */
public class ReUserServlet extends HttpServlet {
    
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
        
        boolean erroredSelect = false;
        
        String selfid = (String)session.getAttribute("userid");
        System.out.println("At admin page as user '" + selfid + "'");
        
        String userid = request.getParameter("userid");
        UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
        
        if(userid != null && userid.equals("--SELECT--"))
            erroredSelect = true;
        
        LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
        List userlist = loginEntityFacade.findById(userid);
        
        if (userid != null && !erroredSelect) {
            UsersEntity user = usersEntityFacade.find(userid);
            user.setActive('y');
            usersEntityFacade.edit(user);
            
            LoginEntity newUser = (LoginEntity)userlist.get(0);
            if(newUser.getUserRole() == 'j')
                newUser.setUserRole('i');
            if(newUser.getUserRole() == 'u')
                newUser.setUserRole('t');
            loginEntityFacade.edit(newUser);
            
            response.sendRedirect("AdminServlet");
        }
        
        List users = usersEntityFacade.findAllInActive();
        printForm(users, response, erroredSelect);
    }
    
    private void printForm(final List users, final HttpServletResponse response, final boolean erroredSelect) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.buildHtmlHeader("Reactivate User"));
        
        if(users.iterator().hasNext()){
            out.println("<span class=\"ttitle\" style=\"580px;\"><center><br>Reactivate User Form</span><br><br>");
            if(erroredSelect)
                HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_REDELUSER_SELECT);
            
            out.println("<form method=post>");
            out.println("<font size=4>User Id:</font> <select name='userid'>");
            out.println("<option value =\"--SELECT--\")>--SELECT--</option>");
            
            for (Iterator it = users.iterator(); it.hasNext();) {
                UsersEntity user = (UsersEntity)it.next();
                out.println("<option value='" + user.getUserId() + "'>" + user.getUserId() + "</option><br/>");
            }
            
            out.println("</select><br><br>");
            out.println("<input type='submit' value='Reactivate'>   ");
            out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
            out.println("</form><center>");
        }  else{
            out.println("<span class=\"ttitle\" style=\"580px;\"><center><br>Currently no user is deactivated.</span><br><br>");
            out.println("<input type=\"button\" value=\"Cancel\" " +
                    "onClick=\"window.location='AdminServlet'\"/>");
        }
        
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
     * Perform JNDI lookup for LoginEntity
     * @return Local facade of LoginEntity bean.
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

