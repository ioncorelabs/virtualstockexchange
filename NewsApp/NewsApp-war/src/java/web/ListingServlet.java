/*
 * ListNews.java
 *
 * Created on October 18, 2008, 5:47 PM
 */

package web;

import ejb.NewsEntity;
import ejb.NewsEntityFacadeLocal;
import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacade;
import ejb.ScripsExchangeEntityFacadeLocal;
import ejb.ScripsUserEntity;
import ejb.ScripsUserEntityFacade;
import ejb.ScripsUserEntityFacadeLocal;
import java.io.*;
import java.text.NumberFormat;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Administrator
 * @version
 */
public class ListingServlet extends HttpServlet {
    
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
        
        PrintWriter out = response.getWriter();
        ScripsExchangeEntityFacadeLocal lookupExchangeEntityEntityFacade = (ScripsExchangeEntityFacadeLocal)lookupExchangeEntityEntityFacade();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchance: Listing</title>");
        out.println("</head>");
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
        
        if (request.getParameter("scripSelected")!=null ){
            int change=0;
            String ScripId = request.getParameter("scripSelected");
            System.out.println( "method called " + ScripId);
            
            
            List  changes = lookupExchangeEntityEntityFacade.findAll();
            ScripsExchangeEntity ex = null;
            for (int j=0;j<changes.size();j++ ){
                ex =  (ScripsExchangeEntity) changes.get(j);
                if (ex!=null ){
                    
                    System.out.println("the id is "+ ex.getScripId());
                    if (ex.getScripId().equals(ScripId)){
                        change =  ex.getChange();
                        break ;
                    }
                }
            }
            
            _nf.setMaximumFractionDigits(2);
            _nf.setMinimumFractionDigits(2);
            
            out.println("<table border=1 align=center >");
            out.println(" <tr ><td align=left> "+"ScripId " +"</td> <td align=left> "+""+"ScripName"+"</td><td align=left> Status" +"</td><td align=left> Share Price" );
            if (change ==1 ){
                out.println(" <tr ><td align=left> "+ex.getScripId()+"</td> <td align=left> "+""+ex.getScripName()+"</td><td align=center><img src=/NewsApp-war/img/market_up.gif></b><b/></td>"+"<td>"+ _nf.format(ex.getPricePerShare())+"</td>" );
            }else if (change ==2 ) {
                out.println(" <tr ><td align=left> "+ex.getScripId()+"</td> <td align=left> "+""+ex.getScripName()+"</td><td align=center><img src=/NewsApp-war/img/market_down.gif></b><b/></td>" +"<td>"+_nf.format(ex.getPricePerShare())+"</td>" );
            }else {
                out.println(" <tr><td align=left> "+ex.getScripId()+"</td> <td align=left> "+""+ex.getScripName()+"</td><td align=left>Unchanged</td>"+"<td>"+_nf.format(ex.getPricePerShare())+"</td></tr>");
            }
            out.println("</table >");
            out.println("<br><input type=\"button\" value=\"Back\" onClick=\"history.back();\"/>");
            
        }else{
            
            //ScripsExchangeEntityFacadeLocal lookupExchangeEntityEntityFacade = (ScripsExchangeEntityFacadeLocal)lookupExchangeEntityEntityFacade();
            List scrips1 = lookupExchangeEntityEntityFacade.findAll();
            
            ScripsUserEntityFacadeLocal scripsEntityFacade = (ScripsUserEntityFacadeLocal) lookupScripsUserEntityFacade();
            
            
            out.println("<form action=ListingServlet >");
            out.println("<table border=1 align=center >");
            
            List  changes = lookupExchangeEntityEntityFacade.findAll();
            
            System.out.println("Size IS  " + changes.size() );
            
            out.println( "<tr><td align=center><select id=scripSelected name=scripSelected>");
            for (Iterator it = changes.iterator(); it.hasNext();){
                ScripsExchangeEntity elem = (ScripsExchangeEntity) it.next();
                out.println( "<option  name=scripSelected value"+ elem.getScripId() +">"+elem.getScripId());
            }
            out.println("</select></td></tr>");
            out.println("<tr  ><td colspan=2> Submit <input type =submit value=Submit /></tr> ");
            out.println("</table ");
            out.println("</form>");
            out.println("<br><input type=\"button\" value=\"Back\" onClick=\"history.back();\"/>");
            
            
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
        
    }
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                ((String)session.getAttribute("userrole")).equals("a"); // only admins CANNOT do this.
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
    
    private ScripsUserEntityFacadeLocal lookupScripsUserEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsUserEntityFacadeLocal) c.lookup("NewsApp/ScripsUserEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    private ScripsExchangeEntityFacadeLocal lookupExchangeEntityEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsExchangeEntityFacadeLocal) c.lookup("NewsApp/ScripsExchangeEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    private ScripsUserEntityFacadeLocal lookupScripUserEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsUserEntityFacadeLocal) c.lookup("NewsApp/ScripsUserEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
}
