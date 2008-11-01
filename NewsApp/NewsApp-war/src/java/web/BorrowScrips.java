/*
 * BorrowScrips.java
 *
 * Created on October 30, 2008, 2:32 PM
 */

package web;

import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacadeLocal;
import ejb.TransactionHistoryEntity;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
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
public class BorrowScrips extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession appSession = request.getSession(true);
        if (isInvalidSession(appSession))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String scripId=request.getParameter("scripId");
        String num=request.getParameter("num");
        
        if ((scripId!=null) && (num!=null)) {
            
            Queue queue = null;
            QueueConnection connection = null;
            QueueSession session = null;
            MessageProducer messageProducer = null;
            try {
                
                InitialContext ctx = new InitialContext();
                queue = (Queue) ctx.lookup("queue/mdb3");
                QueueConnectionFactory factory =
                        (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
                connection = factory.createQueueConnection();
                session = connection.createQueueSession(false,
                        QueueSession.AUTO_ACKNOWLEDGE);
                messageProducer = session.createProducer(queue);
                
                ObjectMessage message = session.createObjectMessage();
                // here we create a NewsEntity, that will be sent in JMS message
                TransactionHistoryEntity e = new TransactionHistoryEntity();
                
                e.setScripId(scripId);
                e.setUserId(appSession.getAttribute("userid").toString());
                e.setTotalShares(Integer.parseInt(num));
                e.setTranType("Borrow");
                e.setTranDate(System.currentTimeMillis());
                
                message.setObject(e);
                messageProducer.send(message);
                messageProducer.close();
                connection.close();
                //response.sendRedirect("ListNews");
                
            } catch (JMSException ex) {
                ex.printStackTrace();
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
            
        }
        
        
        PrintWriter out = response.getWriter();
        //TODO output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchance: Borrow Shares</title>");
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
  
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Borrow Shares Form</span><br>");
        out.println("<form>");
        
        ScripsExchangeEntityFacadeLocal lookupExchangeEntityEntityFacade = (ScripsExchangeEntityFacadeLocal)lookupExchangeEntityFacade();
        List scrips = lookupExchangeEntityEntityFacade.findAll();
        out.println("<select name='scripId'>");
        for (Object obj : scrips) {
            ScripsExchangeEntity elem = (ScripsExchangeEntity) obj;
            out.println("<option value =" +elem.getScripId()+">"+elem.getScripName() +" </option>");
        }
        out.println("</select><br><br>");
        
        out.println("Number of shares: <input type='text' name='num'><br><br>");
        out.println("<input type='submit' value='Submit'>  ");       
        out.println("</form>");
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
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return (session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("t")); // only traders allowed to borrow
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
    
    private ScripsExchangeEntityFacadeLocal lookupExchangeEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsExchangeEntityFacadeLocal) c.lookup("NewsApp/ScripsExchangeEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
}
