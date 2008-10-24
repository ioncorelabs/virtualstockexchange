/*
 * ScripsExchangeEntityFacadeLocal.java
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
public interface ScripsExchangeEntityFacadeLocal {
    void create(ScripsExchangeEntity scripsExchangeEntity);

    void edit(ScripsExchangeEntity scripsExchangeEntity);

    void destroy(ScripsExchangeEntity scripsExchangeEntity);

    ScripsExchangeEntity find(Object pk);

    List findAll();
    
}
