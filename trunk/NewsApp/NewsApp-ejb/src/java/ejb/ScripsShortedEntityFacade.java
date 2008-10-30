/*
 * ScripsShortedEntityFacade.java
 *
 * Created on October 30, 2008, 2:24 PM
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
public class ScripsShortedEntityFacade implements ScripsShortedEntityFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    /** Creates a new instance of ScripsShortedEntityFacade */
    public ScripsShortedEntityFacade() {
    }

    public void create(ScripsShortedEntity scripsShortedEntity) {
        em.persist(scripsShortedEntity);
    }

    public void edit(ScripsShortedEntity scripsShortedEntity) {
        em.merge(scripsShortedEntity);
    }

    public void destroy(ScripsShortedEntity scripsShortedEntity) {
        em.merge(scripsShortedEntity);
        em.remove(scripsShortedEntity);
    }

    public ScripsShortedEntity find(Object pk) {
        return (ScripsShortedEntity) em.find(ScripsShortedEntity.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from ScripsShortedEntity as o").getResultList();
    }
    
    public List findScripForUser(String userId, String scripId) {
        return em.createQuery("select object(o) from ScripsShortedEntity as o where o.scripId = ?1 and o.borrowerId = ?2").setParameter(1, scripId).setParameter(2, userId).getResultList();
    }
    
    public List findScrips(String userId) {
        return em.createQuery("select object(o) from ScripsShortedEntity as o where o.borrowerId=?1").setParameter(1,userId).getResultList();
    }
    
}
