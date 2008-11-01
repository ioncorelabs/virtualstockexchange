/*
 * TransactionHistoryEntityFacade.java
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
public class TransactionHistoryEntityFacade implements TransactionHistoryEntityFacadeLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    /** Creates a new instance of TransactionHistoryEntityFacade */
    public TransactionHistoryEntityFacade() {
    }
    
    public void create(TransactionHistoryEntity transactionHistoryEntity) {
        em.persist(transactionHistoryEntity);
    }
    
    public void edit(TransactionHistoryEntity transactionHistoryEntity) {
        em.merge(transactionHistoryEntity);
    }
    
    public void destroy(TransactionHistoryEntity transactionHistoryEntity) {
        em.merge(transactionHistoryEntity);
        em.remove(transactionHistoryEntity);
    }
    
    public TransactionHistoryEntity find(Object pk) {
        return (TransactionHistoryEntity) em.find(TransactionHistoryEntity.class, pk);
    }
    
    public List findAll() {
        return em.createQuery("select object(o) from TransactionHistoryEntity as o").getResultList();
    }
    
    public List findAllTransactionsForUser(String userId) {
        return em.createQuery("select object(o) from TransactionHistoryEntity as o where o.userId = ?1").setParameter(1, userId).getResultList();
    }
    
    public List findTransactionsForUserAndScrip(String userId, String scripId) {
        return em.createQuery("select object(o) from TransactionHistoryEntity as o where o.userId = ?1 and o.scripId = ?2").setParameter(1, userId).setParameter(2, scripId).getResultList();
    }
    
    
}
