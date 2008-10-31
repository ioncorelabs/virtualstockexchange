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
        if (session.isNew() || session.getAttribute("userid") == null || session.getAttribute("userrole") == null || !((String)session.getAttribute("userrole")).equals("a"))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        System.out.println("At admin page as user '" + userid + "'");
        
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("scripid",             request.getParameter("scripid"));
        parameterMap.put("scripname",           request.getParameter("scripname"));
        parameterMap.put("totalshares",         request.getParameter("totalshares"));
        parameterMap.put("totalsharesavailable",request.getParameter("totalsharesavailable"));
        parameterMap.put("marketcap",           request.getParameter("marketcap"));
        parameterMap.put("pricepershare",       request.getParameter("pricepershare"));
        
        if (formSubmitted(parameterMap))
        {    
            int totalSharesInt = Integer.parseInt(parameterMap.get("totalshares"));
            int totalSharesAvailableInt = Integer.parseInt(parameterMap.get("totalsharesavailable"));
            double marketCapDbl = Double.parseDouble(parameterMap.get("marketcap"));
            double pricePerShareDbl = Double.parseDouble(parameterMap.get("pricepershare"));
            
            ScripsExchangeEntityFacadeLocal scripsEntityFacade = (ScripsExchangeEntityFacadeLocal) lookupScripsEntityFacade();
            ScripsExchangeEntity scripsEntity = new ScripsExchangeEntity(parameterMap.get("scripid"), 
                                                         parameterMap.get("scripname"),
                                                         totalSharesInt, totalSharesAvailableInt, marketCapDbl, pricePerShareDbl);
            
            scripsEntityFacade.create(scripsEntity);
            response.sendRedirect("AdminServlet"); 
        }
        
        printForm(request, response);
    }
    
    private boolean formSubmitted(HashMap<String, String> parameterMap)
    {
        for (String value : parameterMap.values())
            if (value == null)
                return false;
        
        return true;
    }
    
    private void printForm(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet AddScrip</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet AddScrip at " + request.getContextPath () + "</h1>");
        out.println("Add Scrip Form<br>");
        out.println("<form>");
        out.println("Scrip Id: <input type='text' name='scripid'><br/>");
        out.println("Scrip Name: <input type='text' name='scripname'><br/>");
        out.println("Total Shares: <input type='text' name='totalshares'><br/>");
        out.println("Total Shares Available: <input type='text' name='totalsharesavailable'><br/>");
        out.println("Market Cap: <input type='text' name='marketcap'><br/>");
        out.println("Price Per Share: <input type='text' name='pricepershare'><br/>");
        out.println("<input type='submit'><br/>");
        out.println("</form>");        
        out.println("</body>");
        out.println("</html>");
        
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
