/*
 * HtmlBuilder.java
 *
 * Created on November 2, 2008, 7:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package web.utils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jmoral
 */


public class HtmlBuilder {
    
    /** cannot instantiate */
    private HtmlBuilder() {}
    
    public static final String DO_GET_REDIRECT_PAGE = "http://in.youtube.com/watch?v=Yu_moia-oVI";
    
    public enum ERRORS {
        USER_EXISTS,
        SCRIP_EXISTS,
        INVALID_BLANK,
        INVALID_CASH,
        INVALID_USERNAME_TEXT,
        INVALID_USERNAME_MAX,
        INVALID_SCRIPNAME_TEXT,
        INVALID_SCRIPNAME_MAX,
        INVALID_SCRIPID_MIN,
        INVALID_SCRIPID_MAX,
        INVALID_USERID_MIN,
        INVALID_USERID_MAX,
        INVALID_ID_TEXT,
        INVALID_PASSWORD_MIN,
        INVALID_PASSWORD_MAX,
        INVALID_TOTAL_SHARES,
        INVALID_MARKET_CAP,
        INVALID_NUMBER_GENERIC,
        INVALID_LOGIN,
        ACCOUNT_DEACTIVATED,
        INVALID_USERTYPE_SELECT,
        INVALID_REDELUSER_SELECT
    }
    
    public static String buildHtmlHeader(final String pageTitle) {
        StringBuffer header = new StringBuffer();
        header.append("<html>");
        header.append("<head>");
        header.append("<title>Virtual Stock Exchance: " + pageTitle + "</title>");
        header.append("</head>");
        header.append("<body>");
        //Common Styling Code
        header.append("<link href=\"greeny.css\" rel=\"stylesheet\" type=\"text/css\" />");
        header.append("</head>");
        header.append("<body>");
        header.append("<div id=\"tot\">");
        header.append("<div id=\"header\">");
        header.append("<img src=\"img/genericlogo.png\" align=\"left\" alt=\"company logo\"/> <span class=\"title\">Virtual Stock Exchange</span>");
        header.append("<div class=\"slogan\">Bulls & Bears</div>");
        header.append("<div id=\"corp\">");
        header.append("<div class=\"main-text\">");
        return header.toString();
    }
    
    public static String buildHtmlFooter() {
        StringBuffer footer = new StringBuffer();
        footer.append("</div></div>");
        footer.append("<div class=\"clear\"></div>");
        footer.append("<div class=\"footer\"><span style=\"margin-left:400px;\">The Bulls & Bears Team</span></div>");
        footer.append("</div>");
        //Common Ends
        footer.append("</body>");
        footer.append("</html>");
        return footer.toString();
    }
    
    public static boolean hasNumber(String s) {
        for (int j = 0; j < s.length(); j++) {
            if (Character.isDigit(s.charAt(j))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Takes a hashtable of HTML Form input name/value pairs and checks if any
     * are null (i.e., that the form hasn't been submitted.)x
     * @param params hashtable of HTML Form input name/value pairs.
     * @return true if form submitted, false otherwise.
     */
    public static boolean isFormSubmitted(final HashMap<String, String> params) {
        for (String value : params.values())
            if (value == null)
                return false;
        
        return true;
    }
    
    /**
     * Takes a hashtable of HTML Form input name/value pairs and checks if any
     * any are blank. Assumes already checked that none are null using isFormSubmitted
     * @param params hashtable of HTML Form input name/value pairs.
     * @return true if form submitted, false otherwise.
     */
    public static boolean hasBlankFields(final HashMap<String, String> params) {
        for (String value : params.values())
            if (value.equals(""))
                return true;
        
        return false;
    }
    
    public static void printErrorMessage(final PrintWriter out, final HtmlBuilder.ERRORS error) {
        switch (error) {
            case ACCOUNT_DEACTIVATED:
                out.println("<font color=red><b>Your account has been deactivated. Please contact the administrator or try a different login." +
                        "</b></font><br/>");
                break;
                
            case INVALID_LOGIN:
                out.println("<font color=red><b>Invalid Username or Password. Please try again.</b></font><br/>");
                break;
                
            case INVALID_BLANK:
                out.println("<font color=red><b>All fields are required</b></font><br/>");
                break;
                
            case INVALID_CASH:
                out.println("<font color=red><b>Please enter a valid value for cash held</b></font><br/>");
                break;
                
            case INVALID_USERNAME_TEXT:
                out.println("<font color=red><b>User Name can only contain alphabets</b></font><br/>");
                break;
                
            case INVALID_USERNAME_MAX:
                out.println("<font color=red><b>User Name must be shorter than 40 characters</b></font><br/>");
                break;
                
            case INVALID_TOTAL_SHARES:
                out.println("<font color=red><b>Please enter a valid value for total shares</b></font><br/>");
                break;
                
            case INVALID_MARKET_CAP:
                out.println("<font color=red><b>Please enter a valid value for market cap</b></font><br/>");
                break;
                
            case USER_EXISTS:
                out.println("<font color=red><b>That user ID already exists, please try again with another user id.</b></font><br/>");
                break;
                
            case SCRIP_EXISTS:
                out.println("<font color=red><b>That scrip ID already exists, please try again with another scrip id!</b></font><br/>");
                break;
                
            case INVALID_USERID_MIN:
                out.println("<font color=red><b>User IDs must be at least 3 characters long</b></font><br/>");
                break;
                
            case INVALID_USERID_MAX:
                out.println("<font color=red><b>User IDs must be shorter than 16 characters</b></font><br/>");
                break;
                
            case INVALID_ID_TEXT:
                out.println("<font color=red><b>User/Scrip IDs must only contain characters or numbers</b></font><br/>");
                break;
                
            case INVALID_SCRIPNAME_MAX:
                out.println("<font color=red><b>Scrip names must be shorter than 40 characters</b></font><br/>");
                break;
                
            case INVALID_SCRIPID_MIN:
                out.println("<font color=red><b>Scrip IDs must be at least 3 characters long</b></font><br/>");
                break;
                
            case INVALID_SCRIPID_MAX:
                out.println("<font color=red><b>Scrip IDs must be shorter than 16 characters</b></font><br/>");
                break;
                
            case INVALID_PASSWORD_MIN:
                out.println("<font color=red><b>Passwords must be at least 3 characters long</b></font><br/>");
                break;
                
            case INVALID_PASSWORD_MAX:
                out.println("<font color=red><b>Passwords must be shorter than 16 characters</b></font><br/>");
                break;
                
            case INVALID_NUMBER_GENERIC:
                out.println("<font color=red><b>Invalid number values. Please make sure values are sane</b></font><br/>");
                break;
                
            case INVALID_SCRIPNAME_TEXT:
                out.println("<font color=red><b>Scrip name can only contain alphabets</b></font><br/>");
                break;
                
            case INVALID_USERTYPE_SELECT:
                out.println("<font color=red><b>Please select a valid user type</b></font><br/>");
                break;
                
            case INVALID_REDELUSER_SELECT:
                out.println("<font color=red><b>Please select a valid user</b></font><br/>");
                break;
        }
    }
    
    public static boolean isValidUserName(String name) {
        Pattern p = Pattern.compile("[^A-Za-z ]");
        Matcher m = p.matcher(name);
        return !m.find();
    }
    
    public static boolean isValidScripName(String name) {
        Pattern p = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher m = p.matcher(name);
        return !m.find();
    }
    
    public static boolean isValidID(String name) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(name);
        return !m.find();
    }
}
