/*
 * UsersEntityFacade.java
 *
 * Created on October 26, 2008, 5:38 PM
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
 * @author Milind Nimesh
 */
@Stateless
public class UsersEntityFacade implements UsersEntityFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    /** Creates a new instance of UsersEntityFacade */
    public UsersEntityFacade() {
    }

    public void create(UsersEntity usersEntity) {
        em.persist(usersEntity);
    }

    public void edit(UsersEntity usersEntity) {
        em.merge(usersEntity);
    }
    
    public void delete(UsersEntity usersEntity) {
        em.remove(usersEntity);
    }

    public void destroy(UsersEntity usersEntity) {
        em.merge(usersEntity);
        em.remove(usersEntity);
    }

    public UsersEntity find(Object pk) {
        return (UsersEntity) em.find(UsersEntity.class, pk);
    }

    public List findAll() {
        return em.createQuery("select object(o) from UsersEntity as o").getResultList();
    }
    
    public List findUserById(String userId) {        
        return em.createQuery("select object(o) from UsersEntity as o where o.userId = ?1").setParameter(1, userId).getResultList();
    }    
}
