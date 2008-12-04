/*
 * LoginEntityFacadeLocal.java
 *
 * Created on October 18, 2008, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Vaibhav
 */
@Local
public interface LoginEntityFacadeLocal {
    void create(LoginEntity loginEntity);

    void edit(LoginEntity loginEntity);

    void destroy(LoginEntity loginEntity);

    LoginEntity find(Object pk);

    List findAll(String userid, String pwd);
    
    List findById(String userid);    
}
