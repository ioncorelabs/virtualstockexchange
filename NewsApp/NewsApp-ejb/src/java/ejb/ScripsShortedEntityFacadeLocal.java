/*
 * ScripsShortedEntityFacadeLocal.java
 *
 * Created on October 30, 2008, 2:24 PM
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
public interface ScripsShortedEntityFacadeLocal {
    void create(ScripsShortedEntity scripsShortedEntity);

    void edit(ScripsShortedEntity scripsShortedEntity);

    void destroy(ScripsShortedEntity scripsShortedEntity);

    ScripsShortedEntity find(Object pk);

    List findAll();
    
    List findScripForUser(String userId, String scripId);
    
    List findScrips(String userId);
    
}
