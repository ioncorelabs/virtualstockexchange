/*
 * ListScrips.java
 *
 * Created on October 24, 2008, 7:08 PM
 */

package web;

import ejb.ScripsUserEntity;
import ejb.ScripsUserEntityFacadeLocal;
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
public class SellScrips extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession appSession = request.getSession(true);
        
        String num = request.getParameter("number");
        String userId = (String)appSession.getAttribute("userId");
                
        ScripsUserEntityFacadeLocal scripsEntityFacade = (ScripsUserEntityFacadeLocal) lookupScripsUserEntityFacade();
        List scrips = scripsEntityFacade.findScrips(userId);
        int index =-1;
        
        
        if (num!=null) {
            
            
            String strButtonIndex =  request.getParameter("button");
            
            if (strButtonIndex!=null) {
                index =  Integer.parseInt(strButtonIndex);
            }//TODO: Raise exception, id index not found
            
            Vector vec = (Vector) request.getSession().getAttribute("Vector");                        
            ScripsUserEntity elem  = (ScripsUserEntity) vec.elementAt(index);                
         
            
            Queue queue = null;
            QueueConnection connection = null;
            QueueSession session = null;
            MessageProducer messageProducer = null;
            try {
                
                InitialContext ctx = new InitialContext();
                queue = (Queue) ctx.lookup("queue/mdb2");
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
                e.setTranType("Sell");
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
        out.println("<title>Sell shares</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
        out.println("<form>");
        
        out.println("<form  action=ListScrips onSubmit=initializeRadio() >");
        out.println("<table border=1 align=center >");
        out.println("<tr><td align =left>Name of the Scrip</td><td>Number of Shares</td>  ");
        Vector vec = new Vector();
        
        int i =0;
        for (Iterator it = scrips.iterator(); it.hasNext();) {
            ScripsUserEntity elem = (ScripsUserEntity) it.next();
            out.println(" <tr><td align=left> <b>"+elem.getScripId()+"</td> <td align=left> "+elem.getSharesHeld()+"</td></b><b/>");
            vec.add(elem);
            out.println("<td><input type=radio name=button value="+i+" ></td></tr>" );
            i++;
        }
        
        request.getSession().setAttribute("Vector",vec);
        out.println("<input type =hidden name = index >" );
        
        out.println("<tr><td colspan=2> Number of Shares to Sell <input type =text name=number id=num size =10  ></tr> ");
        out.println("<tr><td colspan=2> Submit <input type =submit value=submit /></tr> ");
        out.println("</table ");
        out.println("</form>");
        
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
    
    private ScripsUserEntityFacadeLocal lookupScripsUserEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsUserEntityFacadeLocal) c.lookup("NewsApp/ScripsUserEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
}
