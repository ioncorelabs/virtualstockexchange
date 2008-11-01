/*
 * BuyToCoverScrips.java
 *
 * Created on October 30, 2008, 6:47 PM
 */

package web;

import ejb.ScripsShortedEntity;
import ejb.ScripsShortedEntityFacadeLocal;
import ejb.TransactionHistoryEntity;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
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
public class BuyToCoverScrips extends HttpServlet {
    
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
        
        String num = request.getParameter("number");
        String userId = (String)appSession.getAttribute("userid");
                
        ScripsShortedEntityFacadeLocal scripsEntityFacade = (ScripsShortedEntityFacadeLocal) lookupScripsShortedEntityFacade();
        List scrips = scripsEntityFacade.findScrips(userId);
        int index =-1;
        
        
        if (num!=null) {
            
            
            String strButtonIndex =  request.getParameter("button");
            
            if (strButtonIndex!=null) {
                index =  Integer.parseInt(strButtonIndex);
            }//TODO: Raise exception, id index not found
            
            Vector vec = (Vector) request.getSession().getAttribute("Vector");                        
            ScripsShortedEntity elem  = (ScripsShortedEntity) vec.elementAt(index);                
         
            
            Queue queue = null;
            QueueConnection connection = null;
            QueueSession session = null;
            MessageProducer messageProducer = null;
            try {
                
                InitialContext ctx = new InitialContext();
                queue = (Queue) ctx.lookup("queue/mdb5");
                QueueConnectionFactory factory =
                        (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
                connection = factory.createQueueConnection();
                session = connection.createQueueSession(false,
                        QueueSession.AUTO_ACKNOWLEDGE);
                messageProducer = session.createProducer(queue);
                
                ObjectMessage message = session.createObjectMessage();
                // here we create a NewsEntity, that will be sent in JMS message
                TransactionHistoryEntity e = new TransactionHistoryEntity();
                
                e.setScripId(elem.getScripId());
                e.setUserId(userId);
                e.setTotalShares(Integer.parseInt(num));
                e.setTranType("BuyToCover");
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
        out.println("<title>Virtual Stock Exchance: Short Sell shares</title>");
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
        
        out.println("<span class=\"ttitle\" style=\"580px;\">Buy Shares Form</span><br>");
        out.println("<form>");
        
        out.println("<form  action=ListScrips onSubmit=initializeRadio() >");
        out.println("<table border=1 align=center >");
        out.println("<tr><td align =left>Name of the Scrip</td><td>Number of Shares Borrowed</td><td>Number of Shares Shorted</td><td>Number of Shares Returned</td></tr>");
        Vector vec = new Vector();
        
        int i =0;
        for (Iterator it = scrips.iterator(); it.hasNext();) {
            ScripsShortedEntity elem = (ScripsShortedEntity) it.next();
            out.println(" <tr><td align=left> <b>"+elem.getScripId()+"</td> <td align=left> "+elem.getSharesBorrowed()+
                    "</td><td align=left> "+elem.getSharesShorted()+"</td><td align=left> "+elem.getSharesReturned()+"</td></b><b/>");
            vec.add(elem);
            out.println("<td><input type=radio name=button value="+i+" ></td></tr>" );
            i++;
        }
        
        request.getSession().setAttribute("Vector",vec);
        out.println("<input type =hidden name = index >" );
        
        out.println("<tr><td colspan=4> Number of Shares to cover: <input type =text name=number id=num size =10  ></tr> ");
        out.println("<tr><td colspan=4><input type =submit value=Submit /></tr> ");
        out.println("</table>");
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
        return (session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("t")); // only traders can BuyToCover
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
    
    private ScripsShortedEntityFacadeLocal lookupScripsShortedEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsShortedEntityFacadeLocal) c.lookup("NewsApp/ScripsShortedEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }    
}
