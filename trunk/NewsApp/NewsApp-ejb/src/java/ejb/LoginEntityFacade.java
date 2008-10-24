/*
 * LoginEntityFacade.java
 *
 * Created on October 18, 2008, 5:24 PM
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
public class LoginEntityFacade implements LoginEntityFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    /** Creates a new instance of LoginEntityFacade */
    public LoginEntityFacade() {
    }

    public void create(LoginEntity loginEntity) {
        em.persist(loginEntity);
    }

    public void edit(LoginEntity loginEntity) {
        em.merge(loginEntity);
    }

    public void destroy(LoginEntity loginEntity) {
        em.merge(loginEntity);
        em.remove(loginEntity);
    }

    public LoginEntity find(Object pk) {
        return (LoginEntity) em.find(LoginEntity.class, pk);
    }

    public List findAll(String userid, String pwd) {
        return em.createQuery("select object(o) from LoginEntity as o where o.userId = ?1 and o.password = ?2").setParameter(1, userid).setParameter(2, pwd).getResultList();
    }
    
}
