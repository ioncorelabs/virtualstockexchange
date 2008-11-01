/*
 * UsersEntityFacadeLocal.java
 *
 * Created on October 26, 2008, 5:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Milind Nimesh
 */
@Local
public interface UsersEntityFacadeLocal {
    void create(UsersEntity usersEntity);

    void edit(UsersEntity usersEntity);

    void destroy(UsersEntity usersEntity);
    
    void delete(UsersEntity usersEntity);

    UsersEntity find(Object pk);

    List findAll();
    
    List findUserById(String userId);
    
}
