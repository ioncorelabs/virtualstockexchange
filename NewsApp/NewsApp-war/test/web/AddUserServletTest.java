/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;

/**
 *
 * @author jmoral
 */
public class AddUserServletTest extends ServletTestCase {
    
    public AddUserServletTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void beginBlankId(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.setAutomaticSession(false);
        theRequest.addParameter("userid", "", WebRequest.POST_METHOD);
    }

    public void testBlankId() throws Exception {
        System.out.println("BlankId");
//        System.out.println("1 session isNew: " + session.isNew());
        session = request.getSession(true);
        System.out.println("2 session isNew: " + session.isNew());
        session.setAttribute("userid", "admin");
        session.setAttribute("userrole", "a");
        
        AddUserServlet instance = new AddUserServlet();
        instance.processRequest(request, response);
        System.out.println("3 session isNew: " + session.isNew());
        
        
        // check we stayed
    }
    
    public void endBlankId(WebResponse theResponse) throws Exception {
        System.out.println("the response: '" + theResponse.getText() + "'");
        assertTrue("hmmm...", theResponse.getText().contains("All fields are required"));
    }

    
//    public void beginBlankName(WebRequest theRequest) throws Exception {
//        theRequest.addParameter("username", "", WebRequest.GET_METHOD);
//    }
//
//    public void testBlankName() throws Exception {
//        System.out.println("BlankName");
//        AddUserServlet instance = new AddUserServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddUserServlet", request.getServletPath());
//    }
//    
//    
//    public void beginBlankCashHeld(WebRequest theRequest) throws Exception {
//        theRequest.addParameter("cashheld", "", WebRequest.GET_METHOD);
//    }
//
//    public void testBlankCashHeld() throws Exception {
//        System.out.println("BlankCashHeld");
//        AddUserServlet instance = new AddUserServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddUserServlet", request.getServletPath());
//    }
//    
//    
//    public void beginShortID(WebRequest theRequest) throws Exception {
//        theRequest.addParameter("userid", "a", WebRequest.GET_METHOD);
//    }
//
//    public void testShortID() throws Exception {
//        System.out.println("ShortID");
//        AddUserServlet instance = new AddUserServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddUserServlet", request.getServletPath());
//    }
//    
//    
//    public void beginLongID(WebRequest theRequest) throws Exception {
//        theRequest.addParameter("userid", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" + 
//                                          "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", WebRequest.GET_METHOD);
//    }
//
//    public void testLongID() throws Exception {
//        System.out.println("LongID");
//        AddUserServlet instance = new AddUserServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddUserServlet", request.getServletPath());
//    }
//    
//    
//    public void beginTextForCashHeld(WebRequest theRequest) throws Exception {
//        theRequest.addParameter("cashheld", "abcdefg", WebRequest.GET_METHOD);
//    }
//
//    public void testTextForCashHeld() throws Exception {
//        System.out.println("TextForCashHeld");
//        AddUserServlet instance = new AddUserServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddUserServlet", request.getServletPath());
//    }
//    
//    
//    public void beginNegativeCashHeld(WebRequest theRequest) throws Exception {
//        theRequest.addParameter("cashheld", "-1", WebRequest.GET_METHOD);
//    }
//
//    public void testNegativeCashHeld() throws Exception {
//        System.out.println("NegativeCashHeld");
//        AddUserServlet instance = new AddUserServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddUserServlet", request.getServletPath());
//    }
//    
//    
//    public void beginMaxCashHeld(WebRequest theRequest) throws Exception {
//        theRequest.addParameter("cashheld", "50001", WebRequest.GET_METHOD);
//    }
//
//    public void testMaxCashHeld() throws Exception {
//        System.out.println("MaxCashHeld");
//        AddUserServlet instance = new AddUserServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddUserServlet", request.getServletPath());
//    }
}
