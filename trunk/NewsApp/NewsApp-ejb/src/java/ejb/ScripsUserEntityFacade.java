/*
 * ScripsUserEntityFacade.java
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
public class ScripsUserEntityFacade implements ScripsUserEntityFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    /** Creates a new instance of ScripsUserEntityFacade */
    public ScripsUserEntityFacade() {
    }

    public void create(ScripsUserEntity scripsUserEntity) {
        em.persist(scripsUserEntity);
    }

    public void edit(ScripsUserEntity scripsUserEntity) {
        em.merge(scripsUserEntity);
    }

    public void destroy(ScripsUserEntity scripsUserEntity) {
        em.merge(scripsUserEntity);
        em.remove(scripsUserEntity);
    }

    public ScripsUserEntity find(Object pk) {
        return (ScripsUserEntity) em.find(ScripsUserEntity.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from ScripsUserEntity as o").getResultList();
    }
    
}
