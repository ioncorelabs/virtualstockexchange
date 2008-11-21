/*
 * DelScripServlet.java
 *
 * Created on October 26, 2008, 8:39 PM
 */

package web;

import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacadeLocal;
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
 * @author jmoral
 * @version
 */
public class DelScripServlet extends HttpServlet {
    
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
        
        String scripid = request.getParameter("scripid");
        ScripsExchangeEntityFacadeLocal scripsEntityFacade = (ScripsExchangeEntityFacadeLocal) lookupScripsEntityFacade();
            
        if (formSubmitted(scripid))
        {    
            scripsEntityFacade.destroy(scripsEntityFacade.find(scripid));
            response.sendRedirect("AdminServlet"); 
        }
        
        List scrips = scripsEntityFacade.findAll();
        printForm(request, response, scrips);
    }
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("a");
    }
    
    private boolean formSubmitted(String scripid)
    {
        return (scripid != null);
    }
    
    private void printForm(final HttpServletRequest request, final HttpServletResponse response, List scrips) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.buildHtmlHeader("Delete Scrip"));
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Delete Scrip Form</span><br>");   
        out.println("<form>");
        out.println("Scrip Id: <select name='scripid'>");
        
        for (Iterator it = scrips.iterator(); it.hasNext();)
        {
            ScripsExchangeEntity scrip = (ScripsExchangeEntity)it.next();
            out.println("<option value='" + scrip.getScripId() + "'>" + scrip.getScripId() + "</option><br/>");
        }
        
        out.println("</select><br>");
        out.println("<input type='submit' value='Delete Scrip'>    ");
        out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
        out.println("</form>");  
        
        out.println(HtmlBuilder.buildHtmlFooter());
        out.close();
    }
    
    private ScripsExchangeEntityFacadeLocal lookupScripsEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsExchangeEntityFacadeLocal) c.lookup("NewsApp/ScripsExchangeEntityFacade/local");
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
