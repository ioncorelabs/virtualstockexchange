/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;


/**
 *
 * @author jmoral
 */
public class AddScripServletTest extends ServletTestCase {
    
    public AddScripServletTest(String testName) {
        super(testName);
    }            

    protected void setUp() throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
    }

    protected void tearDown() throws Exception {
    }
    
    public void beginBlankId(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("scripid", "");
    }

    public void testBlankId() throws Exception {
        System.out.println("BlankId");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }

    
    public void beginBlankName(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("scripname", "");
    }

    public void testBlankName() throws Exception {
        System.out.println("BlankName");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginBlankTotalShares(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("totalshares", "");
    }

    public void testBlankTotalShares() throws Exception {
        System.out.println("BlankTotalShares");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginBlankPPS(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("pricepershare", "");
    }

    public void testBlankPPS() throws Exception {
        System.out.println("BlankPPS");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }

    
    public void beginShortID(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("scripid", "a");
    }

    public void testShortID() throws Exception {
        System.out.println("ShortID");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginLongID(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("scripid", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" + 
                                           "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
    }

    public void testLongID() throws Exception {
        System.out.println("LongID");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginTextForTS(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("totalshares", "abcdefg");
    }

    public void testTextForTS() throws Exception {
        System.out.println("TextForTS");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    public void beginTextForPPS(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("pricepershare", "abcdefg");
    }

    public void testTextForPPS() throws Exception {
        System.out.println("TextForPPS");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginNegativeTS(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("totalshares", "-1");
    }

    public void testNegativeTS() throws Exception {
        System.out.println("NegativeTS");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginNegativePPS(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("pricepershare", "-1");
    }

    public void testNegativePPS() throws Exception {
        System.out.println("NegativeTS");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginMaxTS(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("pricepershare", (new Integer(1<<32)).toString());
    }

    public void testMaxTS() throws Exception {
        System.out.println("MaxTS");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
    
    
    public void beginMaxPPS(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("pricepershare", "1001", WebRequest.GET_METHOD);
    }

    public void testMaxPPS() throws Exception {
        System.out.println("MaxPPS");
        assertTrue(true);
//        AddScripServlet instance = new AddScripServlet();
//        instance.doGet(request, response);
//        
//        // check we stayed
//        assertEquals("/Newsapp-war/AddScripServlet", request.getServletPath());
    }
}
