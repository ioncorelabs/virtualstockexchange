/*
 * SellTransactionMessage.java
 *
 * Created on October 26, 2008, 7:45 PM
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
 * Entity class SellTransactionMessage
 * 
 * @author Vaibhav
 */
@MessageDriven(mappedName = "jms/SellTransactionMessage", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/mdb2")
    })
public class SellTransactionMessage implements MessageListener {

    @PersistenceContext
    private EntityManager em;
    
    @Resource
    private MessageDrivenContext mdc;
        
    /** Creates a new instance of SellTransactionMessage */
    public SellTransactionMessage() {
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
            see.setTotalAvailable(avail+num);
            seef.edit(see); 
        } else{//TODO: Raise exception, Scrip not found
            
        }
        
        ScripsUserEntityFacadeLocal suef = (ScripsUserEntityFacadeLocal)lookupUserEntityFacade();               
        
        al1 = suef.findScripForUser(userId, scripId);
        
        if(al1.isEmpty() != true) {
            //Updating table
            ScripsUserEntity sue = (ScripsUserEntity) al1.get(0);
            int held = sue.getSharesHeld();
            if(num>held) {
                //TODO: Raise exception, attempt to sell more shares than held
            }
            else if(held-num == 0) {
                suef.destroy(sue);
            }
            else {
                sue.setSharesHeld(held-num);            
                suef.edit(sue);            
            }            
        } else{//TODO:Scrip not held                       
        }
                
        em.flush();
        em.persist(object);
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
    
    private ScripsUserEntityFacadeLocal lookupUserEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsUserEntityFacadeLocal) c.lookup("NewsApp/ScripsUserEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }                    
}
