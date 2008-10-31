/*
 * TraderServlet.java
 *
 * Created on October 24, 2008, 7:56 PM
 */

package web;

import ejb.ScripsEntity;
import ejb.ScripsEntityFacadeLocal;
import ejb.ScripsShortedEntity;
import ejb.ScripsShortedEntityFacadeLocal;
import ejb.ScripsUserEntity;
import ejb.ScripsUserEntityFacadeLocal;
import ejb.TransactionHistoryEntity;
import ejb.TransactionHistoryEntityFacadeLocal;
import ejb.UsersEntity;
import ejb.UsersEntityFacadeLocal;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
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
 * @author Milind Nimesh
 * @version
 */
public class TraderServlet extends HttpServlet {
    
    private volatile double _portfolioTotal = 0.0;
    private volatile double _portfolioDifference = 0.0;
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(true);
        if (session.isNew() || (String)session.getAttribute("userid") == null || (String)session.getAttribute("userrole") == null || !((String)session.getAttribute("userrole")).equals("t"))
        {
            response.sendRedirect("NewLogin");
            return;
        }
        
        String userid = (String)session.getAttribute("userid");
        
        UsersEntityFacadeLocal usersEntityFacde = (UsersEntityFacadeLocal) lookupUsersEntityFacade();
        ScripsEntityFacadeLocal scripsEntityFacade = (ScripsEntityFacadeLocal) lookupScripsEntityFacade();
        ScripsUserEntityFacadeLocal scripsUserEntityFacade = (ScripsUserEntityFacadeLocal) lookupScripsUserEntityFacade();
        ScripsShortedEntityFacadeLocal scripsShortedEntityFacade = (ScripsShortedEntityFacadeLocal) lookupScripsShortedEntityFacade();
        TransactionHistoryEntityFacadeLocal transactionHistoryEntityFacade = 
                    (TransactionHistoryEntityFacadeLocal) lookupTransactionHistoryEntityFacade();
        
        // my user entity, which contains my initial cash held and current net worth (buying power)
        // FIXME: hardcoded for debugging. Should be getting username from HttpSession.
        UsersEntity self = usersEntityFacde.find(userid);
        
        // get list of all scrips listed in exchange
        List allscrips = scripsEntityFacade.findAll();
        
        // get list of all scrips that this user has borrowed
        // FIXME: hardcoded for debugging. Should be getting username from HttpSession.
        List borrowedscrips = scripsShortedEntityFacade.findScrips(userid);
        
        // get list of all scrips that this user owns
        // FIXME: hardcoded for debugging. Should be getting username from HttpSession.
        List userscrips = scripsUserEntityFacade.findAllScripsForUser(userid); 
        
        // get list of all transactions from this user.
        // FIXME: hardcoded for debugging. Should be getting username from HttpSession.
        List usertransactions = transactionHistoryEntityFacade.findAllTransactionsForUser(userid);
        
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Trader Homepage</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet Trader Homepage at " + request.getContextPath () + "</h1>");
        
        // FIXME: username hardcoded
        out.println("<b>Welcome back, Trader!<br/><br/>");
        printOwnedScripsTable(userid, scripsEntityFacade, transactionHistoryEntityFacade, out, userscrips);
        printBorrowedScripsTable(userid, scripsEntityFacade, transactionHistoryEntityFacade, out, borrowedscrips);
        printTransactionsTable(out, usertransactions);
        
        out.println("<h3>Summary:</h3><b>");
        out.println("Current Cash Held: $" + self.getCashHeld() + "<br/>");
        out.println("Net Income/Loss: $" + (_portfolioDifference + (self.getCashHeld() - self.getInitialCashHeld())) + "<br/>");
        out.println("Total Assets: $" + _portfolioTotal + "<br/>");
        out.println("Total Buying Power: $" + (self.getCashHeld() + _portfolioTotal) + "<br/></b>");
        
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }

    private void printOwnedScripsTable( final String userid,
                                        final ScripsEntityFacadeLocal scripsEntityFacade, 
                                        final TransactionHistoryEntityFacadeLocal transactionHistoryEntityFacade,
                                        final PrintWriter out, 
                                        final List userscrips) {
        
        out.println("All of My Scrips:<br/>");
        out.println("<table width=750px border=1>");
        out.println("<tr><td width=70px><b>Scrip ID</b></td><td><b>Current Value</b></td><td><b>Net income/loss</b></td><td><b>Shares Held</b></td><td><b>Total Shares</b></td>");
        out.println("<td><b>Total Shares Available</b></td><td><b>Market Cap</b></td><td><b>Price Per Share</b></td></tr>");
        
        _portfolioTotal = 0.0;
        _portfolioDifference = 0.0;
        
        for (Iterator it = userscrips.iterator(); it.hasNext();)
        {
            ScripsUserEntity scripuser = (ScripsUserEntity)it.next();
            ScripsEntity myscripsEntity = scripsEntityFacade.find(scripuser.getScripId());
            
            // FIXME: userid hardcoded for now.
            List transactionsForScrip = transactionHistoryEntityFacade.findTransactionsForUserAndScrip(userid, myscripsEntity.getScripId());
            double totalSpent = 0.0;
            for (Iterator transIter = transactionsForScrip.iterator(); transIter.hasNext();)
            {
                // Sum up all buy and sell transactions, ignoring 
                // everything else (borrow, buy-to-cover, et al)
                TransactionHistoryEntity trans = (TransactionHistoryEntity)transIter.next();
                if (trans.getTranType().equals("Buy"))
                    totalSpent += (double)trans.getPricePerShare() * (double)trans.getTotalShares();
                else if (trans.getTranType().equals("Sell"))
                    totalSpent -= (double)trans.getPricePerShare() * (double)trans.getTotalShares();
            }
            
            double currentValue = (double)scripuser.getSharesHeld() * (double)myscripsEntity.getPricePerShare();
            double changeValue = currentValue - totalSpent;
            
            _portfolioTotal += currentValue;
            _portfolioDifference += changeValue;
            
            if (changeValue < 0.0)
                out.println("<tr bgcolor='#E37676'>"); // red
            else
                out.println("<tr bgcolor='#67FD67'>"); // green
            
            out.println("<td>" + myscripsEntity.getScripId() + "</td>");
            out.println("<td>$" + currentValue + "</td>");
            out.println("<td>$" + changeValue + "</td>");
            out.println("<td>" + scripuser.getSharesHeld() + "</td>");
            out.println("<td>" + myscripsEntity.getTotalShares()+ "</td>");
            out.println("<td>" + myscripsEntity.getTotalSharesAvailable()+ "</td>");
            out.println("<td>" + myscripsEntity.getMarketCap()+ "</td>");
            out.println("<td>$" + myscripsEntity.getPricePerShare()+ "</td>");
            out.println("</tr>");
        }
        out.println("</table><br/>");
    }
    
    private void printBorrowedScripsTable(  final String userid,
                                            final ScripsEntityFacadeLocal scripsEntityFacade, 
                                            final TransactionHistoryEntityFacadeLocal transactionHistoryEntityFacade,
                                            final PrintWriter out, 
                                            final List borrowedscrips) {
        
        out.println("All of My Borrowed Scrips:<br/>");
        out.println("<table width=700px border=1>");
        out.println("<tr><td width=70px><b>Scrip ID</b></td><td><b>Current Value</b></td><td><b>Net income/loss</b></td>");
        out.println("<td><b>Shares Held</b></td><td><b>Price Per Share</b></td></tr>");
        
        for (Iterator it = borrowedscrips.iterator(); it.hasNext();)
        {
            ScripsShortedEntity scripshorted = (ScripsShortedEntity)it.next();
            ScripsEntity myscripsEntity = scripsEntityFacade.find(scripshorted.getScripId());
            
            // FIXME: userid hardcoded for now.
            List transactionsForScrip = transactionHistoryEntityFacade.findTransactionsForUserAndScrip(userid, myscripsEntity.getScripId());
            
            double totalBorrowingSpent = 0.0;
            for (Iterator transIter = transactionsForScrip.iterator(); transIter.hasNext();)
            {
                TransactionHistoryEntity trans = (TransactionHistoryEntity)transIter.next();
                if (trans.getTranType().equals("Borrow"))
                    totalBorrowingSpent += (double)trans.getPricePerShare() * (double)trans.getTotalShares();
                else if (trans.getTranType().equals("Return"))
                    totalBorrowingSpent -= (double)trans.getPricePerShare() * (double)trans.getTotalShares();
            }
            
            double currentValue = (double)scripshorted.getSharesBorrowed() * (double)myscripsEntity.getPricePerShare();
            double changeValue = currentValue - totalBorrowingSpent;
            
            // NOTE: want value to be lower than what I borrowed it for 
            //       so I can buy-to-cover and return for a net profit.
            if (changeValue > 0.0)
                out.println("<tr bgcolor='#E37676'>"); // red
            else
                out.println("<tr bgcolor='#67FD67'>"); // green
            
            out.println("<td>" + myscripsEntity.getScripId() + "</td>");
            out.println("<td>$" + currentValue + "</td>");
            out.println("<td>$" + changeValue + "</td>");
            out.println("<td>" + scripshorted.getSharesBorrowed()+ "</td>");
            out.println("<td>$" + myscripsEntity.getPricePerShare()+ "</td>");
            out.println("</tr>");
        }
        out.println("</table><br/>");
    }
    
    private void printTransactionsTable(final PrintWriter out, final List usertransactions) {
        
        
        out.println("All of My Transactions:<br/>");
        out.println("<table width=700px border=1>");
        out.println("<tr><td width=70px><b>Scrip ID</b></td><td><b>Transaction Type</b></td><td><b>Total Shares Bought/Sold</b></td>");
        out.println("<td><b>Price when Bought/Sold</b></td><td><b>Date of Transaction</b></td></tr>");
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        for (Iterator it = usertransactions.iterator(); it.hasNext();)
        {
            TransactionHistoryEntity transaction = (TransactionHistoryEntity)it.next();
            
            out.println("<tr>");
            out.println("<td>" + transaction.getScripId() + "</td>");
            out.println("<td>" + transaction.getTranType() + "</td>");
            out.println("<td>" + transaction.getTotalShares()+ "</td>");
            out.println("<td>$" + transaction.getPricePerShare()+ "</td>");
            out.println("<td width=250px>" + df.format(new Date(transaction.getTranDate())) + "</td>");
            out.println("</tr>");
        }
        out.println("</table><br/>");
    }
    
    private ScripsEntityFacadeLocal lookupScripsEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsEntityFacadeLocal) c.lookup("NewsApp/ScripsEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    private TransactionHistoryEntityFacadeLocal lookupTransactionHistoryEntityFacade() {
        try {
            Context c = new InitialContext();
            return (TransactionHistoryEntityFacadeLocal) c.lookup("NewsApp/TransactionHistoryEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    private ScripsShortedEntityFacadeLocal lookupScripsShortedEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsShortedEntityFacadeLocal) c.lookup("NewsApp/ScripsShortedEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
            
    private ScripsUserEntityFacadeLocal lookupScripsUserEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsUserEntityFacadeLocal) c.lookup("NewsApp/ScripsUserEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    private UsersEntityFacadeLocal lookupUsersEntityFacade() {
        try {
            Context c = new InitialContext();
            return (UsersEntityFacadeLocal) c.lookup("NewsApp/UsersEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
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
