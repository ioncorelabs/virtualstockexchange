/*
 * HtmlBuilder.java
 *
 * Created on November 2, 2008, 7:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package web.utils;

/**
 *
 * @author jmoral
 */
public class HtmlBuilder {
    
    /** cannot instantiate */
    protected HtmlBuilder() {}
    
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
        for (int j = 0;j < s.length();j++) {
            if (Character.isDigit(s.charAt(j))) {
                return true;
            }
        }
        return false;
    }
    
    
}
