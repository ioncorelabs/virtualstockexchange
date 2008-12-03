/*
 * BuyToCoverScrips.java
 *
 * Created on October 30, 2008, 6:47 PM
 */

package web;

import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacadeLocal;
import ejb.ScripsShortedEntity;
import ejb.ScripsShortedEntityFacadeLocal;
import ejb.TransactionHistoryEntity;
import java.io.*;
import java.util.HashMap;
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
import web.utils.HtmlBuilder;

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
        if (isInvalidSession(appSession)) {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String num = request.getParameter("number");
        String userId = (String)appSession.getAttribute("userid");
        
        ScripsShortedEntityFacadeLocal scripsEntityFacade = (ScripsShortedEntityFacadeLocal) lookupScripsShortedEntityFacade();
        List scrips = scripsEntityFacade.findScrips(userId);
        int index =-1;
        int errorcode = 0;
        boolean erroredNumNull = false;
        boolean erroredNumType = false;
        int numInt = 0;
        
        if(num!=null) {
            if((num.equals(""))) {
                erroredNumNull = true;
            } else {
                try{numInt = Integer.parseInt(num);} catch(NumberFormatException e) {
                    erroredNumType = true;
                }
            }
        }
        
        if(!erroredNumType && (numInt<0)) {
            erroredNumType = true;
        }
        
        
        if (num!=null && (!erroredNumNull) && (!erroredNumType)) {
            
            String strButtonIndex =  request.getParameter("button");
            if (strButtonIndex==null) {
                errorcode = 2;
            } else {
                
                index =  Integer.parseInt(strButtonIndex);
                
                Vector vec = (Vector) request.getSession().getAttribute("Vector");
                ScripsShortedEntity elem  = (ScripsShortedEntity) vec.elementAt(index);
                
                List scrip = scripsEntityFacade.findScripForUser(userId, elem.getScripId());
                
                if((((ScripsShortedEntity)scrip.get(0)).getSharesBorrowed() - ((ScripsShortedEntity)scrip.get(0)).getSharesReturned()) < Integer.parseInt(num)) {
                    errorcode = 1;
                } else {
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
                        
                        appSession.setAttribute("message", num+" shares " +
                                "of "+elem.getScripId()+" were successfully covered");
                        //Redirecting depending on the role of the user
                        if(appSession.getAttribute("userrole").equals("t")) {
                            response.sendRedirect("TraderTradeSuccess");
                        } else if(appSession.getAttribute("userrole").equals("i")) {
                            response.sendRedirect("RoleEmptyFailure");
                        } else {
                            response.sendRedirect("RoleEmptyFailure");
                        }
                        
                    } catch (JMSException ex) {
                        ex.printStackTrace();
                    } catch (NamingException ex) {
                        ex.printStackTrace();
                    }
                }
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
        
        out.println("<p align=center><br><span class=\"ttitle\" style=\"580px;\">Buy Shares Form</span><br>");
        
        if (errorcode == 1) {
            out.println("<br><font color=red><b>You are attempting to cover more " +
                    "shares than you have borrowed, please try again." +
                    "</b></font><br><br>");
        }
        
        if (errorcode == 2) {
            out.println("<br><font color=red><b>Please select a scrip to sell. " +
                    "</b></font><br><br>");
        }
        
        if (erroredNumNull)
            out.println("<br><font color=red><b>Please enter the number of scrips to short sell</b></font><br><br>");
        if (erroredNumType)
            out.println("<br><font color=red><b>Please enter a valid value for number of scrips to short sell</b></font><br><br>");
        
        out.println("<form onSubmit=initializeRadio() method=post>");
        out.println("<br><table border=1 align=center>");
        out.println("<tr><td align =left>Name of the Scrip</td><td>Number of Shares Borrowed</td><td>Number of Shares Shorted</td><td>Number of Shares Returned</td><td>Status</td><td>&nbsp;</td></tr>");
        Vector vec = new Vector();
        ScripsExchangeEntityFacadeLocal lookupExchangeEntityEntityFacade = (ScripsExchangeEntityFacadeLocal)lookupExchangeEntityEntityFacade();
        
        int i =0;
        for (Iterator it = scrips.iterator(); it.hasNext();) {
            ScripsShortedEntity elem = (ScripsShortedEntity) it.next();
            out.println(" <tr><td align=left> <b>"+elem.getScripId()+"</td> <td align=left> "+elem.getSharesBorrowed()+
                    "</td><td align=left> "+elem.getSharesShorted()+"</td><td align=left> "+elem.getSharesReturned()+"</td></b><b/>");
            List scrip = lookupExchangeEntityEntityFacade.findScripById(elem.getScripId());
            if(((ScripsExchangeEntity)scrip.get(0)).getChange() == 1) {
                out.println("<td align=center><img src=/NewsApp-war/img/market_up.gif></td>");
            } else if(((ScripsExchangeEntity)scrip.get(0)).getChange() == 2) {
                out.println("<td align=center><img src=/NewsApp-war/img/market_down.gif></td>");
            } else {
                out.println("<td align=center>Unknown</td>");
            }
            
            vec.add(elem);
            out.println("<td><input type=radio name=button value="+i+" ></td></tr>" );
            i++;
        }
        
        request.getSession().setAttribute("Vector",vec);
        out.println("<input type =hidden name = index >" );
        
        out.println("<tr><td colspan=6> Number of Shares to cover: <input type =text name=number id=num size =10 maxlength=6></tr> ");
        out.println("<tr><td colspan=6 align=center><input type =submit value=Submit /> ");
        if(((String)appSession.getAttribute("userrole")).equals("t")) {
            out.println("<input type=\"button\" value=\"Cancel\" " +
                    "onClick=\"window.location='TraderHome'\"/></td></tr></table></p>");
        }
        
        if(((String)appSession.getAttribute("userrole")).equals("i")) {
            out.println("<input type=\"button\" value=\"Cancel\" " +
                    "onClick=\"window.location='InvestorServlet'\"/></td></tr></table></p>");
        }        
        out.println("</form><br>");
        
        
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
    
    private boolean isInvalidSession(final HttpSession session) {
        return  session.isNew() ||
                session.getAttribute("userid") == null ||
                session.getAttribute("userrole") == null ||
                !((String)session.getAttribute("userrole")).equals("t"); // only traders can shortsell
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
    
    private ScripsShortedEntityFacadeLocal lookupScripsShortedEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsShortedEntityFacadeLocal) c.lookup("NewsApp/ScripsShortedEntityFacade/local");
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
}

