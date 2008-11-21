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
import web.utils.HtmlBuilder;

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
        response.setContentType("text/html;charset=UTF-8");
        
        String uid=request.getParameter("userid");
        String pwd=request.getParameter("password");
        
        if ((uid!=null) && (pwd!=null)) 
        {
            LoginEntityFacadeLocal loginEntityFacade = (LoginEntityFacadeLocal) lookupLoginEntityFacade();
            List news = loginEntityFacade.findAll(uid, pwd);          
                      
            Iterator it = news.iterator();
            if (!news.isEmpty()) 
            {   
                if(it.hasNext())
                {
                    HttpSession session = request.getSession(true);
                    if (session.isNew())
                        System.out.println("Creating new session for user '" + uid + "'");
                    else
                        System.out.println("session already exists. userid = '" + (String)session.getAttribute("userid") + "', role: '" + (String)session.getAttribute("userrole") + "'");
        
                    LoginEntity elem = (LoginEntity) it.next();
                    
                    session.setAttribute("userid", elem.getUserId());
                    if(elem.getUserRole() == 'a')
                    {   
                        session.setAttribute("userrole", "a");
                        response.sendRedirect("AdminServlet");   
                    }
                    if(elem.getUserRole() == 't')
                    {                     
                        session.setAttribute("userrole", "t");
                        response.sendRedirect("TraderHome");
                    }
                    if(elem.getUserRole() == 'i')
                    {                     
                        session.setAttribute("userrole", "i");
                        response.sendRedirect("InvestorServlet");
                    }
                }
            }
            
           /* for (Iterator it = news.iterator(); it.hasNext();)
            {
                LoginEntity elem = (LoginEntity) it.next();
                System.out.println("User: "+elem.getUserId()+"Pass: "+elem.getPassword()+"Role: "+elem.getUserRole());
            }
           */
            
           /* if (!news.isEmpty()) {
                response.sendRedirect("ListNews");
            } */
            
            
        }
        
        PrintWriter out = response.getWriter();
        //Common Styling Code
        out.println(HtmlBuilder.buildHtmlHeader("Login"));
        
        //Main Content
        out.println("<div id=\"head\" align=\"center\">");
        out.println("<span class=\"ttitle\" style=\"580px;\">COMS 4156: Advanced Software Engineering</span>");
        out.println("<br><br><br><br><br><br>");
        out.println("<span class=\"ttitle\">Sign In</span><br><br>");
        
        out.println("<form>");
        out.println("User Id:<font color=\"#FFFFFF\">______</font<input type='text' name='userid'><br/>");
        out.println("Password:<font color=\"#FFFFFF\">____</font<input type='password' name='password'><br/><br>");
        out.println("<input type='submit' value='Login'><br/>");
        out.println("</form>");
        out.println("</div>");
        
        //Common HTML Footer
        out.println(HtmlBuilder.buildHtmlFooter());
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
