/*
 * InvestorServlet.java
 *
 * Created on October 24, 2008, 7:57 PM
 */

package web;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Milind Nimesh
 * @version
 */
public class InvestorServlet extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(true);
        if (session.isNew() || session.getAttribute("userid") == null || session.getAttribute("userrole") == null || !((String)session.getAttribute("userrole")).equals("i"))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        System.out.println("At investor page as user '" + userid + "'");
        
        PrintWriter out = response.getWriter();
       
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Investor Homepage</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<b>Investor Menu. Welcome back, " + userid + "!</b><br>");
        out.println("<br><br><input type=button value=Buy onclick=\"javascript:window.location='BuyScrips'\"><br><br>");
        out.println("<input type=button value=Sell onclick=\"javascript:window.location='SellScrips'\"><br><br>");        
        out.println("<input type=button value=\"Your Portfolio\" onclick=\"javascript:window.location='InvestorPortfolio'\"><br><br>");
        out.println("<input type=button value=\"Scrip Lookup\" onclick=\"javascript:window.location='ListingServlet'\"><br><br>");
        
        out.println("</body>");
        out.println("</html>");
        
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
}
