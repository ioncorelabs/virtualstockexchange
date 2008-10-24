/*
 * PostMessage.java
 *
 * Created on October 15, 2008, 10:19 PM
 */

package web;

import ejb.NewsEntity;
import java.io.*;
import javax.jms.Queue;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Vaibhav
 * @version
 */
public class PostMessage extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String title=request.getParameter("title");
        String body=request.getParameter("body");
        
        if ((title!=null) && (body!=null)) {
            
            Queue queue = null;
            QueueConnection connection = null;
            QueueSession session = null;
            MessageProducer messageProducer = null;
            try {
                
                InitialContext ctx = new InitialContext();
                queue = (Queue) ctx.lookup("queue/mdb");
                QueueConnectionFactory factory =
                        (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
                connection = factory.createQueueConnection();
                session = connection.createQueueSession(false,
                        QueueSession.AUTO_ACKNOWLEDGE);
                messageProducer = session.createProducer(queue);
                
                ObjectMessage message = session.createObjectMessage();
                // here we create a NewsEntity, that will be sent in JMS message
                NewsEntity e = new NewsEntity();
                e.setTitle(title);
                e.setBody(body);
                
                message.setObject(e);
                messageProducer.send(message);
                messageProducer.close();
                connection.close();
                response.sendRedirect("ListNews");
                
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
        out.println("<title>Servlet NewServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
        out.println("<form>");
        out.println("Title: <input type='text' name='title'><br/>");
        out.println("Message: <textarea name='body'></textarea><br/>");
        out.println("<input type='submit'><br/>");
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
}
