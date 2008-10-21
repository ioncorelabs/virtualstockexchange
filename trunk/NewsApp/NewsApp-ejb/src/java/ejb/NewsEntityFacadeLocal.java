/*
 * NewsEntityFacadeLocal.java
 *
 * Created on October 15, 2008, 10:16 PM
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
public interface NewsEntityFacadeLocal {
    void create(NewsEntity newsEntity);

    void edit(NewsEntity newsEntity);

    void destroy(NewsEntity newsEntity);

    NewsEntity find(Object pk);

    List findAll();
    
}
