/*
 * BorrowTransactionMessage.java
 *
 * Created on October 26, 2008, 1:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Entity class BorrowTransactionMessage
 * 
 * 
 * @author Vaibhav
 */
@MessageDriven(mappedName = "jms/BorrowTransactionMessage", activationConfig =  {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/mdb3")
})
public class BorrowTransactionMessage implements MessageListener {
    
    @PersistenceContext
    private EntityManager em;
    
    @Resource
    private MessageDrivenContext mdc;
    
    /**
     * Creates a new instance of BuyTransactionMessage
     */
    public BorrowTransactionMessage() {
    }
    
    public void onMessage(Message message) {
        ObjectMessage msg = null;
        try {
            if (message instanceof ObjectMessage) {
                msg = (ObjectMessage) message;
                TransactionHistoryEntity e = (TransactionHistoryEntity) msg.getObject();
                save(e);
            }
        } catch (JMSException e) {
            e.printStackTrace();
            mdc.setRollbackOnly();
        } catch (Throwable te) {
            te.printStackTrace();
        }
    }
    
    public void save(Object object) {
        List al;
        List al1;
        
        TransactionHistoryEntity the = (TransactionHistoryEntity) object;
        String scripId = the.getScripId();
        String userId = the.getUserId();
        int num = the.getTotalShares();
        
        ScripsExchangeEntityFacadeLocal seef = (ScripsExchangeEntityFacadeLocal)lookupExchangeEntityFacade();               
        
        al = seef.findScripById(scripId);
        
        if(al.isEmpty() != true) {
            ScripsExchangeEntity see = (ScripsExchangeEntity) al.get(0);
            int avail = see.getTotalAvailable();
            int lent = see.getTotalSharesLent();
            if(num>avail) {
                //TODO: Raise exception, requesting for more shares than available
            } else{
                see.setTotalAvailable(avail-num);
                see.setTotalSharesLent(lent+num);
                seef.edit(see);
                
                the.setPricePerShare(see.getPricePerShare());
            }
        } else{//TODO: Raise exception, Scrip not found
            
        }
        
        ScripsShortedEntityFacadeLocal ssef = (ScripsShortedEntityFacadeLocal)lookupShortedEntityFacade();               
        
        al1 = ssef.findScripForUser(userId, scripId);
        
        if(al1.isEmpty() != true) {
            //Updating table
            ScripsShortedEntity sse = (ScripsShortedEntity) al1.get(0);
            int held = sse.getSharesBorrowed();
            sse.setSharesBorrowed(held+num);   
            sse.setSharesShorted(0);
            sse.setSharesReturned(0);
            ssef.edit(sse);            
        } else{
            //Adding new entity
            ScripsShortedEntity newsse = new ScripsShortedEntity();
            newsse.setScripId(scripId);
            newsse.setBorrowerId(userId);
            newsse.setSharesBorrowed(num);
            ssef.create(newsse);                        
        }
                
        em.flush();
        em.persist(the);
    }
    
    
    private ScripsExchangeEntityFacadeLocal lookupExchangeEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsExchangeEntityFacadeLocal) c.lookup("NewsApp/ScripsExchangeEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    private ScripsShortedEntityFacadeLocal lookupShortedEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsShortedEntityFacadeLocal) c.lookup("NewsApp/ScripsShortedEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
}
