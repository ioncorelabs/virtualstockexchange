/*
 * VSEGenericException.java
 *
 * Created on October 31, 2008, 4:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

/**
 *
 * @author Vaibhav
 */
public class VSEGenericException extends RuntimeException{
    
    /** Creates a new instance of VSEGenericException */
    public VSEGenericException() {
    }
    
    public VSEGenericException(String msg){
      super(msg);
    }

    public VSEGenericException(String msg, Throwable t){
      super(msg,t);
    } 
    
}
