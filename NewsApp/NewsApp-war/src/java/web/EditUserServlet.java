/*
 * EditUserServlet.java
 *
 * Created on October 29, 2008, 8:41 PM
 */

package web;

import ejb.LoginEntity;
import ejb.LoginEntityFacadeLocal;
import ejb.UsersEntity;
import ejb.UsersEntityFacadeLocal;
import java.io.*;
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

/**
 *
 * @author jmoral
 * @version
 */
public class EditUserServlet extends HttpServlet {
    
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
        
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("userid",              request.getParameter("userid"));
        parameterMap.put("username",            request.getParameter("username"));
        parameterMap.put("cashheld",            request.getParameter("cashheld"));        
        parameterMap.put("password",            request.getParameter("password"));        
        parameterMap.put("userrole",            request.getParameter("userrole"));        
        
        UsersEntityFacadeLocal usersEntityFacade = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
        LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
            
        if (formSubmitted(parameterMap))
        {   
            UsersEntity user = usersEntityFacade.find(parameterMap.get("userid"));
            LoginEntity login = loginEntityFacade.find(parameterMap.get("userid"));
            
            // TODO add field
            user.setUserName(parameterMap.get("username"));
            user.setCashHeld(Double.parseDouble(parameterMap.get("cashheld")));
            login.setPassword(parameterMap.get("password"));
            login.setUserRole(parameterMap.get("userrole").charAt(0));
            
            usersEntityFacade.edit(user);
            loginEntityFacade.edit(login);
            response.sendRedirect("AdminServlet"); 
        }
        
        List users = usersEntityFacade.findAll();
        printForm(loginEntityFacade, request, response, users);
    }
    
    private boolean formSubmitted(HashMap<String, String> pm)
    {
        for (String value : pm.values())
            if (value == null)
                return false;
        
        return true;
    }
    
    private void printForm(final LoginEntityFacadeLocal loginEntityFacade, final HttpServletRequest request, final HttpServletResponse response, List users) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchance: Edit User</title>");
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
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Edit User Form</span><br>");   
        out.println("Users:<br>");
        out.println("<table width=700px border=1>");
        out.println("<tr><td>User ID</td><td>User Name</td><td>Current Cash Held</td>");
        out.println("<td>Password</td><td>User Role</td></tr>");
        
        for (Iterator it = users.iterator(); it.hasNext();)
        {
            UsersEntity user = (UsersEntity)it.next();
            LoginEntity login = loginEntityFacade.find(user.getUserId());
            
            out.println("<form>");
            out.println("<tr><td>" + user.getUserId() + "<input type='hidden' name='userid' value='" + user.getUserId() + "'></td>");
            out.println("<td><input type='text' name='username' value='" + user.getUserName() + "'></td>");
            out.println("<td><input type='text' name='cashheld' value='" + user.getCashHeld()+ "'></td>");            
            out.println("<td><input type='password' name='password' value='" + login.getPassword()+ "'></td>");            
            
            out.println("<td>User Type: <select name='userrole'>");
            out.println("<option value='admin'" + ((login.getUserRole() == 'a') ? " selected" : "") + ">Admin</option>");
            out.println("<option value='trader'" + ((login.getUserRole() == 't') ? " selected" : "") + ">Trader</option>");
            out.println("<option value='investor'" + ((login.getUserRole() == 'i') ? " selected" : "") + ">Investor</option>");
            out.println("</select></td>");
            
            out.println("<td><input type='submit' value='Edit'></td></tr>");
            out.println("</form>");        
        }
        
        out.println("</table><br>");
        out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
        
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
    
    private LoginEntityFacadeLocal lookupLoginEntityFacade() {
        try {
            Context c = new InitialContext();
            return (LoginEntityFacadeLocal) c.lookup("NewsApp/LoginEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
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
