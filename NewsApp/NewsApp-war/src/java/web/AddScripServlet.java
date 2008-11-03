/*
 * AddScripServlet.java
 *
 * Created on October 26, 2008, 7:55 PM
 */

package web;

import ejb.LoginEntity;
import ejb.LoginEntityFacadeLocal;
import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacadeLocal;
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
import web.utils.HtmlBuilder;

/**
 *
 * @author jmoral
 * @version
 */
public class AddScripServlet extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(true);
        if (isInvalidSession(session))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        System.out.println("At admin page as user '" + userid + "'");
        
        PrintWriter out = response.getWriter();
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("scripid",             request.getParameter("scripid"));
        parameterMap.put("scripname",           request.getParameter("scripname"));
        parameterMap.put("totalshares",         request.getParameter("totalshares"));
        parameterMap.put("pricepershare",       request.getParameter("pricepershare"));
        
        boolean errored = false;
        
        if (formSubmitted(parameterMap))
        {    
            int totalSharesInt              = Integer.parseInt(parameterMap.get("totalshares"));
            double pricePerShareDbl         = Double.parseDouble(parameterMap.get("pricepershare"));
            double marketCapDbl             = (double)totalSharesInt * (double)pricePerShareDbl;
            
            ScripsExchangeEntityFacadeLocal scripsEntityFacade = (ScripsExchangeEntityFacadeLocal) lookupScripsEntityFacade();
            if (scripsEntityFacade.find(parameterMap.get("scripid")) != null)
            {
                errored = true;
            }
            else
            {
                ScripsExchangeEntity scripsEntity =  new ScripsExchangeEntity(parameterMap.get("scripid"), parameterMap.get("scripname"), 
                                                        totalSharesInt, totalSharesInt, marketCapDbl, pricePerShareDbl);

                scripsEntityFacade.create(scripsEntity);
                response.sendRedirect("AdminServlet");
                return;
            }
        }
        
        printForm(out, request, response, errored);
    }
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("a");
    }
    
    private boolean formSubmitted(HashMap<String, String> parameterMap)
    {
        for (String value : parameterMap.values())
            if (value == null)
                return false;
        
        return true;
    }
    
    private void printForm(PrintWriter out, final HttpServletRequest request, final HttpServletResponse response, final boolean errored) throws IOException {
        out.println(HtmlBuilder.getInstance().buildHtmlHeader("Add Scrip"));
        out.println("<span class=\"ttitle\" style=\"580px;\">Add Scrip Form</span><br>");
        if (errored)
            out.println("<font color=red><b>That scrip ID already exists, stupid head!</b></font><br>");
        out.println("<form>");
        
        out.println("<table width=350px cellpadding=4px>");
        out.println("<tr><td width=150px>Scrip Id:</td><td><input type='text' name='scripid'></td></tr>");
        out.println("<tr><td>Scrip Name:</td><td><input type='text' name='scripname'></td></tr>");
        out.println("<tr><td>Total Shares:</td><td><input type='text' name='totalshares'></td></tr>");
        out.println("<tr><td>Price Per Share:</td><td><input type='text' name='pricepershare'></td></tr>");
        out.println("</table>");
        
        out.println("<input type='submit' value='Add Scrip'>   ");
        out.println("<input type=\"button\" value=\"Cancel\" onClick=\"window.location='AdminServlet'\"/>");
        out.println("</form>"); 
        
        out.println(HtmlBuilder.getInstance().buildHtmlFooter());
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
