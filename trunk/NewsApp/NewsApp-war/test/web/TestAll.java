/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jmoral
 */
public class TestAll extends TestCase {
    
    public TestAll(String testName) {
        super(testName);
    }            

    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        
        suite.addTest(new TestSuite(web.NewLoginTest.class));
        suite.addTest(new TestSuite(web.AddScripServletTest.class));
        return suite;
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

}
