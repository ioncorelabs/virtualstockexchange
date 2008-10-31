/*
 * ShortSellHome.java
 *
 * Created on October 30, 2008, 1:37 PM
 */

package web;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Vaibhav
 * @version
 */
public class ShortSellHome extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet ShortSellHome</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet ShortSellHome at " + request.getContextPath () + "</h1>");
        
        out.println("<br><br><input type=button value=\"Borrow Scrips\" onclick=\"javascript:window.location='BorrowScrips'\"><br><br>");        
        out.println("<input type=button value=\"Short Sell\" onclick=\"javascript:window.location='ShortSellScrips'\"><br><br>");
        out.println("<input type=button value=\"Buy to Cover\" onclick=\"javascript:window.location='BuyToCoverScrips'\"><br><br>");
                
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
