/*
 * ScripsExchangeEntityFacade.java
 *
 * Created on October 24, 2008, 7:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Vaibhav
 */
@Stateless
public class ScripsExchangeEntityFacade implements ScripsExchangeEntityFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    /** Creates a new instance of ScripsExchangeEntityFacade */
    public ScripsExchangeEntityFacade() {
    }

    public void create(ScripsExchangeEntity scripsExchangeEntity) {
        em.persist(scripsExchangeEntity);
    }

    public void edit(ScripsExchangeEntity scripsExchangeEntity) {
        em.merge(scripsExchangeEntity);
    }

    public void destroy(ScripsExchangeEntity scripsExchangeEntity) {
        em.merge(scripsExchangeEntity);
        em.remove(scripsExchangeEntity);
    }

    public ScripsExchangeEntity find(Object pk) {
        return (ScripsExchangeEntity) em.find(ScripsExchangeEntity.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from ScripsExchangeEntity as o").getResultList();
    }
    
    public List findScripById(String scripId) {        
        return em.createQuery("select object(o) from ScripsExchangeEntity as o where o.scripId = ?1").setParameter(1, scripId).getResultList();
    }
    
}
