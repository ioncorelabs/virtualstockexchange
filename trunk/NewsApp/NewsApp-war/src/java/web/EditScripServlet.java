/*
 * EditScripServlet.java
 *
 * Created on October 29, 2008, 9:20 PM
 */

package web;

import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacadeLocal;
import ejb.UsersEntityFacadeLocal;
import java.io.*;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
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
        parameterMap.put("scripid",                 request.getParameter("scripid"));
        parameterMap.put("scripname",               request.getParameter("scripname"));
        parameterMap.put("totalshares",             request.getParameter("totalshares"));
        parameterMap.put("totalsharesavailable",    request.getParameter("totalsharesavailable"));
        parameterMap.put("marketcap",               request.getParameter("marketcap"));
        parameterMap.put("pricepershare",           request.getParameter("pricepershare"));
        
        ScripsExchangeEntityFacadeLocal scripsEntityFacade = (ScripsExchangeEntityFacadeLocal) lookupScripsEntityFacade();
            
        if (formSubmitted(parameterMap))
        {   
            ScripsExchangeEntity scrip = scripsEntityFacade.find(parameterMap.get("scripid"));
            
            scrip.setScripName(parameterMap.get("scripname"));
            scrip.setTotalShares(Integer.parseInt(parameterMap.get("totalshares")));
            scrip.setTotalAvailable(Integer.parseInt(parameterMap.get("totalsharesavailable")));
            scrip.setMarketCap(Double.parseDouble(parameterMap.get("marketcap")));
            scrip.setPricePerShare(Double.parseDouble(parameterMap.get("pricepershare")));
            
            scripsEntityFacade.edit(scrip);
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
        out.println("<title>Virtual Stock Exchance: Edit Scrip</title>");
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
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Edit Scrip Form</span><br>");
        out.println("Users:<br>");
        out.println("<table width=750px border=1>");
        out.println("<tr><td>Scrip ID</td><td>Scrip Name</td><td>Total Shares</td><td>Total Shares Available</td>");
        out.println("<td>Market Cap</td><td>Price Per Share</td></tr>");
        
        _nf.setMaximumFractionDigits(2);
        _nf.setMinimumFractionDigits(2);
        _nf.setGroupingUsed(false);
        
        for (Iterator it = scrips.iterator(); it.hasNext();)
        {
            ScripsExchangeEntity scrip = (ScripsExchangeEntity)it.next();
            out.println("<form>");
            out.println("<tr><td>" + scrip.getScripId() + "<input type='hidden' name='scripid' value='" + scrip.getScripId() + "'></td>");
            out.println("<td><input type='text' name='scripname' value='" + scrip.getScripName() + "'></td>");
            out.println("<td><input type='text' name='totalshares' value='" + scrip.getTotalShares()+ "'></td>");
            out.println("<td><input type='text' name='totalsharesavailable' value='" + scrip.getTotalAvailable()+ "'></td>");
            out.println("<td><input type='text' name='marketcap' value='" + _nf.format(scrip.getMarketCap())+ "'></td>");
            out.println("<td><input type='text' name='pricepershare' value='" + _nf.format(scrip.getPricePerShare())+ "'></td>");
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
