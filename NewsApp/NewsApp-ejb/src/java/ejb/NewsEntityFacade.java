/*
 * NewsEntityFacade.java
 *
 * Created on October 15, 2008, 10:16 PM
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
public class NewsEntityFacade implements NewsEntityFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    /** Creates a new instance of NewsEntityFacade */
    public NewsEntityFacade() {
    }

    public void create(NewsEntity newsEntity) {
        em.persist(newsEntity);
    }

    public void edit(NewsEntity newsEntity) {
        em.merge(newsEntity);
    }

    public void destroy(NewsEntity newsEntity) {
        em.merge(newsEntity);
        em.remove(newsEntity);
    }

    public NewsEntity find(Object pk) {
        return (NewsEntity) em.find(NewsEntity.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from NewsEntity as o").getResultList();
    }
    
}
