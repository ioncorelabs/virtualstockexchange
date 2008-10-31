/*
 * EditScripServlet.java
 *
 * Created on October 29, 2008, 9:20 PM
 */

package web;

import ejb.ScripsEntity;
import ejb.ScripsEntityFacadeLocal;
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
public class EditScripServlet extends HttpServlet {
    
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
        parameterMap.put("scripid",                 request.getParameter("scripid"));
        parameterMap.put("scripname",               request.getParameter("scripname"));
        parameterMap.put("totalshares",             request.getParameter("totalshares"));
        parameterMap.put("totalsharesavailable",    request.getParameter("totalsharesavailable"));
        parameterMap.put("marketcap",               request.getParameter("marketcap"));
        parameterMap.put("pricepershare",           request.getParameter("pricepershare"));
        
        ScripsEntityFacadeLocal scripsEntityFacade = (ScripsEntityFacadeLocal) lookupScripsEntityFacade();
            
        if (formSubmitted(parameterMap))
        {   
            ScripsEntity scrip = scripsEntityFacade.find(parameterMap.get("scripid"));
            
            scrip.setScripName(parameterMap.get("scripname"));
            scrip.setTotalShares(Integer.parseInt(parameterMap.get("totalshares")));
            scrip.setTotalSharesAvailable(Integer.parseInt(parameterMap.get("totalsharesavailable")));
            scrip.setMarketCap(Double.parseDouble(parameterMap.get("marketcap")));
            scrip.setPricePerShare(Double.parseDouble(parameterMap.get("pricepershare")));
            
            scripsEntityFacade.edit(scrip);
            response.sendRedirect("AdminServlet"); 
        }
        
        List scrips = scripsEntityFacade.findAll();
        printForm(request, response, scrips);
    }
    
    private boolean formSubmitted(HashMap<String, String> pm)
    {
        for (String value : pm.values())
            if (value == null)
                return false;
        
        return true;
    }
    
    private void printForm(final HttpServletRequest request, final HttpServletResponse response, List scrips) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet EditScrip</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet EditScrip at " + request.getContextPath () + "</h1>");
        out.println("Edit Scrip Form<br>");
        out.println("Users:<br>");
        out.println("<table width=750px border=1>");
        out.println("<tr><td>Scrip ID</td><td>Scrip Name</td><td>Total Shares</td><td>Total Shares Available</td>");
        out.println("<td>Market Cap</td><td>Price Per Share</td></tr>");
        
        for (Iterator it = scrips.iterator(); it.hasNext();)
        {
            ScripsEntity scrip = (ScripsEntity)it.next();
            out.println("<form>");
            out.println("<tr><td>" + scrip.getScripId() + "<input type='hidden' name='scripid' value='" + scrip.getScripId() + "'></td>");
            out.println("<td><input type='text' name='scripname' value='" + scrip.getScripName() + "'></td>");
            out.println("<td><input type='text' name='totalshares' value='" + scrip.getTotalShares()+ "'></td>");
            out.println("<td><input type='text' name='totalsharesavailable' value='" + scrip.getTotalSharesAvailable()+ "'></td>");
            out.println("<td><input type='text' name='marketcap' value='" + scrip.getMarketCap()+ "'></td>");
            out.println("<td><input type='text' name='pricepershare' value='" + scrip.getPricePerShare()+ "'></td>");
            out.println("<td><input type='submit' value='Edit'></td></tr>");
            out.println("</form>");        
        }
        
        out.println("</table><br>");
        out.println("<a href='/AdminServlet'>Back to Admin Page</a><br>");
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
    
    private ScripsEntityFacadeLocal lookupScripsEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsEntityFacadeLocal) c.lookup("NewsApp/ScripsEntityFacade/local");
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
