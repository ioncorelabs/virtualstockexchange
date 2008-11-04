/*
 * EditUserServlet.java
 *
 * Created on October 29, 2008, 8:41 PM
 */

package web;

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
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(true);
        if (isInvalidSession(session))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        System.out.println("At admin page as user '" + userid + "'");
        
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("userid",              request.getParameter("userid"));
        parameterMap.put("username",            request.getParameter("username"));
        parameterMap.put("cashheld",            request.getParameter("cashheld"));        
        
        UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
            
        if (formSubmitted(parameterMap))
        {   
            UsersEntity user = usersEntityFacade.find(parameterMap.get("userid"));
            user.setUserName(parameterMap.get("username"));
            user.setCashHeld(Double.parseDouble(parameterMap.get("cashheld")));            
            
            usersEntityFacade.edit(user);
            response.sendRedirect("AdminServlet"); 
        }
        
        List users = usersEntityFacade.findAll();
        printForm(request, response, users);
    }
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("a");
    }
    
    private boolean formSubmitted(HashMap<String, String> pm)
    {
        for (String value : pm.values())
            if (value == null)
                return false;
        
        return true;
    }
    
    private void printForm(final HttpServletRequest request, final HttpServletResponse response, List users) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.getInstance().buildHtmlHeader("Edit User"));
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Edit User Form</span><br>");   
        out.println("Users:<br>");
        out.println("<table width=600px border=1>");
        out.println("<tr><td>User ID</td><td>User Name</td><td>Current Cash Held</td><td>Total Buying Power</td></tr>");
        
        _nf.setMaximumFractionDigits(2);
        _nf.setMinimumFractionDigits(2);
        _nf.setGroupingUsed(false);
        
        for (Iterator it = users.iterator(); it.hasNext();)
        {
            UsersEntity user = (UsersEntity)it.next();
            out.println("<form>");
            out.println("<tr><td>" + user.getUserId() + "<input type='hidden' name='userid' value='" + user.getUserId() + "'></td>");
            out.println("<td><input type='text' name='username' value='" + user.getUserName() + "'></td>");
            out.println("<td><input type='text' name='cashheld' value='" + _nf.format(user.getCashHeld())+ "'></td>");            
            out.println("<td><input type='submit' value='Edit'></td></tr>");
            out.println("</form>");        
        }
        
        out.println("</table><br>");
        out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
        
        out.println(HtmlBuilder.getInstance().buildHtmlFooter());        
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
