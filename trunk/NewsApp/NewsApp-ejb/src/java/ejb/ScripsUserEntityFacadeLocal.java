/*
 * ScripsUserEntityFacadeLocal.java
 *
 * Created on October 24, 2008, 7:03 PM
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
public interface ScripsUserEntityFacadeLocal {
    void create(ScripsUserEntity scripsUserEntity);

    void edit(ScripsUserEntity scripsUserEntity);

    void destroy(ScripsUserEntity scripsUserEntity);

    ScripsUserEntity find(Object pk);

    List findAll();
    
    List findScripForUser(String s1, String s2);
    
}
