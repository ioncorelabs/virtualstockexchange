/*
 * NewLoginTest.java
 * JUnit based test
 *
 * Created on November 30, 2008, 1:57 AM
 */

package web;

import java.net.HttpURLConnection;
import junit.framework.*;
import org.apache.cactus.*;


import ejb.LoginEntity;
import ejb.LoginEntityFacadeLocal;
import ejb.UsersEntity;
import ejb.UsersEntityFacadeLocal;
import java.io.*;
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
 */
public class NewLoginTest extends ServletTestCase {
    
    public NewLoginTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
    }

    protected void tearDown() throws Exception {
    }
    
    public void beginAdminLoginTest(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("userid", "admin");
        theRequest.addParameter("password", "pass");
    }

    /**
     * Test of processRequest method, of class web.NewLogin.
     */
    public void testAdminLoginTest() throws Exception {
        System.out.println("processRequest");
        
        NewLogin instance = new NewLogin();
        instance.doGet(request, response);
        
        assertNotNull("userid passed is null?", request.getParameter("userid"));
        assertNotNull("password passed is null?", request.getParameter("password"));
        
        assertNotNull("userid session not set?", session.getAttribute("userid"));
        assertNotNull("userrole session not set?", session.getAttribute("userrole"));
        
        // try printing request stuff here
        System.out.println("request URI: " + request.getRequestURI());
        System.out.println("request context path: " + request.getContextPath());
        System.out.println("request servlet path: " + request.getServletPath());
        System.out.println("request path info: " + request.getPathInfo());
    }
    
    public void endAdminLoginTest(WebResponse theResponse) throws Exception {
        System.out.println("the response text: '" + theResponse.getText() + "'");
        System.out.println("the redirector name: '" + theResponse.getWebRequest().getRedirectorName() + "'");
        ServletURL url = theResponse.getWebRequest().getURL();
        if (url != null)
        {
            System.out.println("the url contextx path: '" + theResponse.getWebRequest().getURL().getContextPath() + "'");
            System.out.println("the url path: '" + theResponse.getWebRequest().getURL().getPath() + "'");
            System.out.println("the url path info: '" + theResponse.getWebRequest().getURL().getPathInfo() + "'");
            System.out.println("the url servlet path: '" + theResponse.getWebRequest().getURL().getServletPath() + "'");
            System.out.println("the url query string: '" + theResponse.getWebRequest().getURL().getQueryString() + "'");
        }
        
        HttpURLConnection java_url_conn = theResponse.getConnection();
        if (java_url_conn != null)
        {
            System.out.println("java url conn request method: " + java_url_conn.getRequestMethod());
            java.net.URL java_url = java_url_conn.getURL();
            if (java_url != null)
            {
                System.out.println("java url path: " + java_url.getPath());
                System.out.println("java url query: " + java_url.getQuery());
            }
        }
        
    }
    
    
    public void beginTraderLoginTest(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("userid", "trader");
        theRequest.addParameter("password", "pass");
    }

    /**
     * Test of processRequest method, of class web.NewLogin.
     */
    public void testTraderLoginTest() throws Exception {
        System.out.println("processRequest");
        
        NewLogin instance = new NewLogin();
        instance.doGet(request, response);
        
        assertNotNull("userid passed is null?", request.getParameter("userid"));
        assertNotNull("password passed is null?", request.getParameter("password"));
        
        assertNotNull("userid session not set?", session.getAttribute("userid"));
        assertNotNull("userrole session not set?", session.getAttribute("userrole"));
        
        // try printing request stuff here
        System.out.println("=-=-=-=-=-=-=-=-=-=- TRADER =-=-=-=-=-=-=-=-=-=-=-");
        System.out.println("request URI: " + request.getRequestURI());
        System.out.println("request context path: " + request.getContextPath());
        System.out.println("request servlet path: " + request.getServletPath());
        System.out.println("request path info: " + request.getPathInfo());
    }
    
    public void endTraderLoginTest(WebResponse theResponse) throws Exception {
        System.out.println("the response text: '" + theResponse.getText() + "'");
        System.out.println("the redirector name: '" + theResponse.getWebRequest().getRedirectorName() + "'");
        ServletURL url = theResponse.getWebRequest().getURL();
        if (url != null)
        {
            System.out.println("the url contextx path: '" + theResponse.getWebRequest().getURL().getContextPath() + "'");
            System.out.println("the url path: '" + theResponse.getWebRequest().getURL().getPath() + "'");
            System.out.println("the url path info: '" + theResponse.getWebRequest().getURL().getPathInfo() + "'");
            System.out.println("the url servlet path: '" + theResponse.getWebRequest().getURL().getServletPath() + "'");
            System.out.println("the url query string: '" + theResponse.getWebRequest().getURL().getQueryString() + "'");
        }
        
        HttpURLConnection java_url_conn = theResponse.getConnection();
        if (java_url_conn != null)
        {
            System.out.println("java url conn request method: " + java_url_conn.getRequestMethod());
            java.net.URL java_url = java_url_conn.getURL();
            if (java_url != null)
            {
                System.out.println("java url path: " + java_url.getPath());
                System.out.println("java url query: " + java_url.getQuery());
            }
        }
    }
    
    public void beginInvestorLoginTest(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("userid", "investor");
        theRequest.addParameter("password", "pass");
    }

    /**
     * Test of processRequest method, of class web.NewLogin.
     */
    public void testInvestorLoginTest() throws Exception {
        System.out.println("processRequest");
        
        NewLogin instance = new NewLogin();
        instance.doGet(request, response);
        
        assertNotNull("userid passed is null?", request.getParameter("userid"));
        assertNotNull("password passed is null?", request.getParameter("password"));
        
        assertNotNull("userid session not set?", session.getAttribute("userid"));
        assertNotNull("userrole session not set?", session.getAttribute("userrole"));
        
        // try printing request stuff here
        System.out.println("=-=-=-=-=-=-=-=-=-=- INVESTOR =-=-=-=-=-=-=-=-=-=-=-");
        System.out.println("request URI: " + request.getRequestURI());
        System.out.println("request context path: " + request.getContextPath());
        System.out.println("request servlet path: " + request.getServletPath());
        System.out.println("request path info: " + request.getPathInfo());
    }
    
    public void endInvestorLoginTest(WebResponse theResponse) throws Exception {
        System.out.println("the response text: '" + theResponse.getText() + "'");
        System.out.println("the redirector name: '" + theResponse.getWebRequest().getRedirectorName() + "'");
        ServletURL url = theResponse.getWebRequest().getURL();
        if (url != null)
        {
            System.out.println("the url contextx path: '" + theResponse.getWebRequest().getURL().getContextPath() + "'");
            System.out.println("the url path: '" + theResponse.getWebRequest().getURL().getPath() + "'");
            System.out.println("the url path info: '" + theResponse.getWebRequest().getURL().getPathInfo() + "'");
            System.out.println("the url servlet path: '" + theResponse.getWebRequest().getURL().getServletPath() + "'");
            System.out.println("the url query string: '" + theResponse.getWebRequest().getURL().getQueryString() + "'");
        }
        
        HttpURLConnection java_url_conn = theResponse.getConnection();
        if (java_url_conn != null)
        {
            System.out.println("java url conn request method: " + java_url_conn.getRequestMethod());
            java.net.URL java_url = java_url_conn.getURL();
            if (java_url != null)
            {
                System.out.println("java url path: " + java_url.getPath());
                System.out.println("java url query: " + java_url.getQuery());
            }
        }
    }
    
    
    public void beginBadLoginTest(WebRequest theRequest) throws Exception {
        System.setProperty("cactus.contextURL", "http://localhost:8080/NewsApp-war");
        theRequest.addParameter("userid", "idontexist");
        theRequest.addParameter("password", "idontexist");
    }

    /**
     * Test of processRequest method, of class web.NewLogin.
     */
    public void testBadLoginTest() throws Exception {
        System.out.println("processRequest");
        
        NewLogin instance = new NewLogin();
        instance.doGet(request, response);
        
        assertNotNull("userid passed is null?", request.getParameter("userid"));
        assertNotNull("password passed is null?", request.getParameter("password"));
        
        assertNull("bad user/password was able to set userid?", session.getAttribute("userid"));
        assertNull("bad user/password was able to set role?", session.getAttribute("userrole"));
        
        // try printing request stuff here
        System.out.println("=-=-=-=-=-=-=-=-=-=- BAD LOGIN =-=-=-=-=-=-=-=-=-=-=-");
        System.out.println("request URI: " + request.getRequestURI());
        System.out.println("request context path: " + request.getContextPath());
        System.out.println("request servlet path: " + request.getServletPath());
        System.out.println("request path info: " + request.getPathInfo());
    }
    
    public void endBadLoginTest(WebResponse theResponse) throws Exception {
        System.out.println("the response text: '" + theResponse.getText() + "'");
        assertFalse("hmm... there isn't any response for the bad login? Thought there should be the error page.", theResponse.getText().equals(""));
        System.out.println("the redirector name: '" + theResponse.getWebRequest().getRedirectorName() + "'");
        ServletURL url = theResponse.getWebRequest().getURL();
        if (url != null)
        {
            System.out.println("the url contextx path: '" + theResponse.getWebRequest().getURL().getContextPath() + "'");
            System.out.println("the url path: '" + theResponse.getWebRequest().getURL().getPath() + "'");
            System.out.println("the url path info: '" + theResponse.getWebRequest().getURL().getPathInfo() + "'");
            System.out.println("the url servlet path: '" + theResponse.getWebRequest().getURL().getServletPath() + "'");
            System.out.println("the url query string: '" + theResponse.getWebRequest().getURL().getQueryString() + "'");
        }
        
        HttpURLConnection java_url_conn = theResponse.getConnection();
        if (java_url_conn != null)
        {
            System.out.println("java url conn request method: " + java_url_conn.getRequestMethod());
            java.net.URL java_url = java_url_conn.getURL();
            if (java_url != null)
            {
                System.out.println("java url path: " + java_url.getPath());
                System.out.println("java url query: " + java_url.getQuery());
            }
        }
    }

}
