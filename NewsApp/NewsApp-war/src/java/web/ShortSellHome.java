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
        out.println("<title>Virtual Stock Exchance: Short Sell Home</title>");
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
        
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Short Sell</span><br>");
        
        out.println("<br><br><input type=button value=\"Borrow Scrips\" onclick=\"javascript:window.location='BorrowScrips'\"><br><br>");        
        out.println("<input type=button value=\"Short Sell\" onclick=\"javascript:window.location='ShortSellScrips'\"><br><br>");
        out.println("<input type=button value=\"Buy to Cover\" onclick=\"javascript:window.location='BuyToCoverScrips'\"><br><br>");
        
        out.println("<input type=\"button\" value=\"Back\" onClick=\"history.back();\"/>");
        
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
