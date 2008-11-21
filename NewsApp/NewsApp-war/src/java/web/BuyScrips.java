/*
 * BuyScrips.java
 *
 * Created on October 24, 2008, 7:08 PM
 */

package web;

import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacadeLocal;
import ejb.ScripsUserEntityFacadeLocal;
import ejb.TransactionHistoryEntity;
import ejb.VSEGenericException;
import java.io.*;
import java.util.Iterator;
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
 *
 * The servlet that generates the page for buying shares in a Scrip.
 * Shows the user all the Scrips available and needs the user to enter 
 * the number of shares to be bought.
 */
public class BuyScrips extends HttpServlet {
   
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        //Creating session object
        HttpSession appSession = request.getSession(true);
        
        //Checking if the session and logged in user are valid 
        if (isInvalidSession(appSession))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String scripId=request.getParameter("scripId");
        String num=request.getParameter("num");
        
        //Adding data to queue on page submit
        if ((scripId!=null) && (num!=null)) {
            
            Queue queue = null;
            QueueConnection connection = null;
            QueueSession session = null;
            MessageProducer messageProducer = null;
            try {
                
                InitialContext ctx = new InitialContext();
                
                //Doing a JNDI lookup on the Message-driven Bean JMS queue
                queue = (Queue) ctx.lookup("queue/mdb1");
                QueueConnectionFactory factory =
                        (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
                connection = factory.createQueueConnection();
                session = connection.createQueueSession(false,
                        QueueSession.AUTO_ACKNOWLEDGE);
                messageProducer = session.createProducer(queue);
                
                ObjectMessage message = session.createObjectMessage();
                
                //Creating a TransactionHistoryEntity object, that will be sent in the JMS message
                TransactionHistoryEntity e = new TransactionHistoryEntity();
                
                //Adding data to the object
                e.setScripId(scripId);
                e.setUserId(appSession.getAttribute("userid").toString());
                e.setTotalShares(Integer.parseInt(num));
                e.setTranType("Buy");
                e.setTranDate(System.currentTimeMillis());
                
                //Adding message to the queue
                message.setObject(e);
                messageProducer.send(message);
                messageProducer.close();
                connection.close();
                
                //Redirecting depending on the role of the user
                if(appSession.getAttribute("userrole").equals("t")) {
                    response.sendRedirect("TraderTradeSuccess");
                }                                
                else if(appSession.getAttribute("userrole").equals("i")) {
                    response.sendRedirect("InvestorTradeSuccess");
                }
                else {
                    response.sendRedirect("RoleEmptyFailure");
                }                                
            } catch (JMSException ex) {
                ex.printStackTrace();
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
            
        }
        
        
        PrintWriter out = response.getWriter();        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Virtual Stock Exchange: Buy Shares</title>");
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
        out.println("<span class=\"ttitle\" style=\"580px;\">Buy Shares</span><br>");
        out.println("<form>");
                
        //Doing a JNDI lookup for ScripsExchangeEntityFacade
        ScripsExchangeEntityFacadeLocal lookupExchangeEntityEntityFacade = (ScripsExchangeEntityFacadeLocal)lookupExchangeEntityFacade();
        List scrips = lookupExchangeEntityEntityFacade.findAll();
        
        //Showingall the Scrips in the system in a Select box
        out.println("<br> Scrip Name:");
        out.println("<select name='scripId'>");
        for (Object obj : scrips) {
            ScripsExchangeEntity elem = (ScripsExchangeEntity) obj;
            out.println("<option value =" +elem.getScripId()+">"+elem.getScripName() +" </option>");
        }
        out.println("</select><br><br>");
                        
        out.println("Number of shares: <input type='text' name='num'><br><br>");
        out.println("<input type='submit' value = 'Submit'>  ");
        out.println("</form>");
        out.println("<input type=\"button\" value=\"Cancel\" onClick=\"history.back();\"/>");
                
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
                ((String)session.getAttribute("userrole")).equals("a"); // only admin's CANNOT participate in exchange
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
