/*
 * ListNews.java
 *
 * Created on October 15, 2008, 10:17 PM
 */

package web;

import ejb.NewsEntity;
import ejb.NewsEntityFacadeLocal;
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

/**
 *
 * @author Vaibhav
 * @version
 */
public class ListNews extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet ListNews</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet ListNews at " + request.getContextPath() + "</h1>");
        NewsEntityFacadeLocal newsEntityFacade = (NewsEntityFacadeLocal) lookupNewsEntityFacade();
        List news = newsEntityFacade.findAll();
        for (Iterator it = news.iterator(); it.hasNext();) {
            NewsEntity elem = (NewsEntity) it.next();
            out.println(" <b>"+elem.getTitle()+" </b><br />");
            out.println(elem.getBody()+"<br /> ");
        }
        out.println("<a href='PostMessage'>Add new message</a>");
        
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
    
    private NewsEntityFacadeLocal lookupNewsEntityFacade() {
        try {
            Context c = new InitialContext();
            return (NewsEntityFacadeLocal) c.lookup("NewsApp/NewsEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
}
