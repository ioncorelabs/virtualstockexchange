/*
 * ShortSellHome.java
 *
 * Created on October 30, 2008, 1:37 PM
 */

package web;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;
import web.utils.HtmlBuilder;

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
        
        HttpSession session = request.getSession(true);
        if (isInvalidSession(session))
        {
            response.sendRedirect("NewLogin");
            return;
        }
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
        
        
        out.println("<p align=center><br><span class=\"ttitle\" style=\"580px;\">Short Sell Homepage</span><br><br>");
        
                
        out.println("<div class=\"main-content\">");
            out.println("Please refer to the menu on the left for short selling activities. " +
                    " As a part of Short Selling, if you anticipate an impending drop in price of a particular Scrip, you may borrow" +
                    " and sell shares of this scrip, hoping to return the borrowed shares when the price drops." +
                    " .Essentially, you are allowed to borrow a certain amount of shares of the scrip, depending on availability" +
                    " and sell them at a given price. Later, if the price of the scrip goes down, you may buy the shares " +
                    "at that lower price and return them to the original lender, hence making profit equivalent to the difference " +
                    "in original and new price.");
        out.println("</div>");
        
        
        out.println("<div id=\"menius\">");
        out.println("<div class=\"menu-title\">Menu</div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"BorrowScrips\">Borrow Scrips</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"ShortSellScrips\">Short Sell</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"BuyToCoverScrips\">Buy to Cover</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"TraderHome\">Cancel</a></div>");
        
        out.println("</div></p>");
        
        
        //out.println("<input type=\"button\" value=\"Back\" onClick=\"history.back();\"/>");
        
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
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("t"); // only traders can short sell
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
  if (request.getQueryString() != null)
            response.sendRedirect(HtmlBuilder.DO_GET_REDIRECT_PAGE);
        else
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

