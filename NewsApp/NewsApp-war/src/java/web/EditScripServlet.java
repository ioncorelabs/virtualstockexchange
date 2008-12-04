/*
 * EditScripServlet.java
 *
 * Created on October 29, 2008, 9:20 PM
 */

package web;

import ejb.ScripsExchangeEntity;
import ejb.ScripsExchangeEntityFacadeLocal;
import java.io.*;
import java.text.NumberFormat;
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
import web.utils.HtmlBuilder;

/**
 *
 * @author jmoral
 * @version
 */
public class EditScripServlet extends HttpServlet {
    
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
        
        String userid = (String)session.getAttribute("userid");
        
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("scripid",                 request.getParameter("scripid"));
        parameterMap.put("scripname",               request.getParameter("scripname"));
//        parameterMap.put("totalshares",             request.getParameter("totalshares"));
//        parameterMap.put("totalsharesavailable",    request.getParameter("totalsharesavailable"));
//        parameterMap.put("marketcap",               request.getParameter("marketcap"));        
        
        ScripsExchangeEntityFacadeLocal scripsEntityFacade = (ScripsExchangeEntityFacadeLocal) lookupScripsEntityFacade();
        
        boolean erroredBlankFields = false;
//        boolean erroredNumType = false;
        boolean erroredScripNameMax = false;
        boolean erroredScripNameText = false;
            
        if (HtmlBuilder.isFormSubmitted(parameterMap))
        {   
            int intTotalShares = 0;
            int intTotalSharesAvail = 0;
            double dblTotalShares = 0;
            
            if (HtmlBuilder.hasBlankFields(parameterMap)) {
                erroredBlankFields = true;
            } else {
//                try{intTotalShares = Integer.parseInt(parameterMap.get("totalshares")); } 
//                catch(NumberFormatException e) { erroredNumType = true; }
//                
//                try{intTotalSharesAvail = Integer.parseInt(parameterMap.get("totalsharesavailable")); } 
//                catch(NumberFormatException e) { erroredNumType = true; }
//                
//                try{dblTotalShares = Double.parseDouble(parameterMap.get("marketcap")); } 
//                catch(NumberFormatException e) { erroredNumType = true; }
            }
//            if (!erroredNumType && (intTotalShares <= 0 || intTotalSharesAvail <= 0 || dblTotalShares <= 0.0))
//                erroredNumType = true;
            
            if (parameterMap.get("scripname").length() > 40)
                erroredScripNameMax = true;
            if (!HtmlBuilder.isValidScripName(parameterMap.get("scripname")))
                erroredScripNameText = true;
            
            if((!erroredBlankFields) && /*(!erroredNumType) &&*/ !erroredScripNameMax && !erroredScripNameText) 
            {
                ScripsExchangeEntity scrip = scripsEntityFacade.find(parameterMap.get("scripid"));

                scrip.setScripName(parameterMap.get("scripname"));
//                scrip.setTotalShares(Integer.parseInt(parameterMap.get("totalshares")));
//                scrip.setTotalAvailable(Integer.parseInt(parameterMap.get("totalsharesavailable")));
//                scrip.setMarketCap(Double.parseDouble(parameterMap.get("marketcap")));          

                scripsEntityFacade.edit(scrip);
                
                session.setAttribute("message", parameterMap.get("scripname")+" was successfully edited");
                response.sendRedirect("AdminSuccessServlet"); 
            }
        }
        
        List scrips = scripsEntityFacade.findAll();
        printForm(request, response, scrips, /*erroredNumType, */erroredBlankFields, erroredScripNameMax, erroredScripNameText);
    }
    
    private boolean isInvalidSession(final HttpSession session)
    {
        return  session.isNew() || 
                session.getAttribute("userid") == null || 
                session.getAttribute("userrole") == null || 
                !((String)session.getAttribute("userrole")).equals("a");
    }
    
    private void printForm(final HttpServletRequest request, final HttpServletResponse response, List scrips, 
//                            final boolean erroredNumType, 
                            final boolean erroredBlankFields,
                            final boolean erroredScripNameMax,
                            final boolean erroredScripNameText) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(HtmlBuilder.buildHtmlHeader("Edit Scrip"));
        
        out.println("<center><span class=\"ttitle\" style=\"580px;\"><br>Edit Scrip Form</span><br><br>");
        if (erroredBlankFields)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_BLANK);
//        if (erroredNumType)
//            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_NUMBER_GENERIC);
        if (erroredScripNameMax)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_SCRIPNAME_MAX);
        if (erroredScripNameText)
            HtmlBuilder.printErrorMessage(out, HtmlBuilder.ERRORS.INVALID_SCRIPNAME_TEXT);
        
        out.println("<br/><table width=430px border=1>");
        out.println("<tr><td>Scrip ID</td><td>Scrip Name</td>");
        //out.println("<td>Total Shares</td><td>Total Shares Available</td><td>Market Cap</td>");
        out.println("<td>&nbsp;</td></tr>");
        
        _nf.setMaximumFractionDigits(2);
        _nf.setMinimumFractionDigits(2);
        _nf.setGroupingUsed(false);
        
        for (Iterator it = scrips.iterator(); it.hasNext();)
        {
            ScripsExchangeEntity scrip = (ScripsExchangeEntity)it.next();
            out.println("<form method=post>");
            out.println("<tr><td>" + scrip.getScripId() + "<input type='hidden' name='scripid' value='" + scrip.getScripId() + "'></td>");
            out.println("<td><input type='text' name='scripname' value='" + scrip.getScripName() + "' maxlength=40></td>");
//            out.println("<td><input type='text' name='totalshares' value='" + scrip.getTotalShares()+ "' size=10 maxlength=9></td>");
//            out.println("<td><input type='text' name='totalsharesavailable' value='" + scrip.getTotalAvailable()+ "' size=10 maxlength=9></td>");
//            out.println("<td><input type='text' name='marketcap' value='" + _nf.format(scrip.getMarketCap())+ "' size=10 maxlength=9></td>");            
            out.println("<td><input type='submit' value='Edit'></td></tr>");
            out.println("</form></center>");
        }
        out.println("<tr><td align=center colspan=3><input type=\"button\" value=\"Cancel\" " +
                "onClick=\"window.location='AdminServlet'\"/></td></tr>");
        out.println("</table><br><br></center>");
        
        out.println(HtmlBuilder.buildHtmlFooter());
        out.close();
    }
    
    private ScripsExchangeEntityFacadeLocal lookupScripsEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsExchangeEntityFacadeLocal) c.lookup("NewsApp/ScripsExchangeEntityFacade/local");
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
