/*
 * TraderHome.java
 *
 * Created on October 30, 2008, 12:49 PM
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
public class TraderHome extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        if (isInvalidSession(session))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        session.setAttribute("userid", session.getAttribute("userid"));
        
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchance: Trader Homepage</title>");
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
        
        out.println("<div id=\"head\" align=\"center\"");
        out.println("<span class=\"ttitle\" style=\"580px;\">Trader Homepage</span><br>");
        
        out.println("</div>");
        out.println("<br><br><br><br><br>");
                
        out.println("<div id=\"menius\">");
        out.println("<div class=\"menu-title\">Menu</div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"BuyScrips\">Buy</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"SellScrips\">Sell</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"ShortSellHome\">Short Sell</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"TraderServlet\">Your Portfolio</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"ListingServlet\">Scrip Lookup</a></div>");
        out.println("<div class=\"menu-item\"><img src=\"img/arrow.gif\" hspace=\"10\" align=\"left\" /><a href=\"LogoutServlet\">Logout</a></div>");
        out.println("</div>");
                
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
                !((String)session.getAttribute("userrole")).equals("t");
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
