/*
 * BuyTransactionMessage.java
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
 * Entity class BuyTransactionMessage
 *
 *
 * @author Vaibhav
 *
 * This is the Message-driven Bean that feeds off the queue dedicated to buy requests
 * and executes the buy transaction.
 */
//Creating 
@MessageDriven(mappedName = "jms/BuyTransactionMessage", activationConfig =  {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/mdb1")
})
public class BuyTransactionMessage implements MessageListener {
    
    @PersistenceContext
    private EntityManager em;
    
    @Resource
    private MessageDrivenContext mdc;
    
    /**
     * Creates a new instance of BuyTransactionMessage
     */
    public BuyTransactionMessage() {
    }
    
    
    public void onMessage(Message message) {
        ObjectMessage msg = null;
        try {
            if (message instanceof ObjectMessage) {
                
                //Grabbing message from queue
                msg = (ObjectMessage) message;
                
                //Creating TransactionHistoryEntity object from message
                TransactionHistoryEntity e = (TransactionHistoryEntity) msg.getObject();                
                save(e);
            }
        } catch (JMSException e) {
            e.printStackTrace();
            mdc.setRollbackOnly();
        } 
    }
    
    
    public void save(Object object) {
        List al;
        List al1;
        List al2;
        
        TransactionHistoryEntity the = (TransactionHistoryEntity) object;
        String scripId = the.getScripId();
        String userId = the.getUserId();
        int num = the.getTotalShares();        
        
        //Doing a JNDI lookup on ScripsExchangeEntityFacade
        ScripsExchangeEntityFacadeLocal seef = (ScripsExchangeEntityFacadeLocal)lookupExchangeEntityFacade();
        
        //Finding Scrip details by Scrip Id
        al = seef.findScripById(scripId);
        
        //Updating total available shares and price per share (according to 
        //market model) in ScripsExchangeEntity
        if(al.isEmpty() != true) {
            ScripsExchangeEntity see = (ScripsExchangeEntity) al.get(0);
            int avail = see.getTotalAvailable();
            if(num>avail) {
                //TODO: trying to buy more than available                
            } else{
                see.setTotalAvailable(avail-num);
                seef.edit(see);
                
                the.setPricePerShare(see.getPricePerShare());                
                
                //Share price value reset, using marketcap/totalshares
                double newprice = (see.getMarketCap())/(avail - num + see.getTotalSharesLent());
                see.setPricePerShare(newprice);                   
                see.setChange(1);
            }
        } else{//TODO: Raise exception, Scrip not found
            
        }
        
        
        //Doing a JNDI lookup on ScripsUserEntityFacade
        ScripsUserEntityFacadeLocal suef = (ScripsUserEntityFacadeLocal)lookupUserEntityFacade();
        
        al1 = suef.findScripForUser(userId, scripId);
        
        //Updating Scrip entry to ScripsUserEntity or adding one if it doesnot 
        //exist
        if(al1.isEmpty() != true) {
            //Updating table
            ScripsUserEntity sue = (ScripsUserEntity) al1.get(0);
            int held = sue.getSharesHeld();
            sue.setSharesHeld(held+num);            
            suef.edit(sue);
        } else{
            //Adding new entity
            ScripsUserEntity newsue = new ScripsUserEntity();
            newsue.setScripId(scripId);
            newsue.setUserId(userId);
            newsue.setSharesHeld(num);
            suef.create(newsue);
        }
        
        //Doing a JNDI lookup on UsersEntityFacade
        UsersEntityFacadeLocal uef = (UsersEntityFacadeLocal)lookupUsersFacade();
        
        al2 = uef.findUserById(userId);
        
        //Updating user's cash held
        if(al2.isEmpty() != true) {
            //Updating table
            UsersEntity ue = (UsersEntity) al2.get(0);
            double balance = ue.getCashHeld();
            ue.setCashHeld(balance - (the.getPricePerShare() * num));
            uef.edit(ue);
        } else{
            //TODO: Raise exception, user doesnot exist.
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
    
    private ScripsUserEntityFacadeLocal lookupUserEntityFacade() {
        try {
            Context c = new InitialContext();
            return (ScripsUserEntityFacadeLocal) c.lookup("NewsApp/ScripsUserEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
    
    private UsersEntityFacadeLocal lookupUsersFacade() {
        try {
            Context c = new InitialContext();
            return (UsersEntityFacadeLocal) c.lookup("NewsApp/UsersEntityFacade/local");
        } catch(NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }
}
