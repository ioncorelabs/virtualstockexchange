/*
 * TransactionHistoryEntityFacadeLocal.java
 *
 * Created on October 24, 2008, 7:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Vaibhav
 */
@Local
public interface TransactionHistoryEntityFacadeLocal {
    void create(TransactionHistoryEntity transactionHistoryEntity);

    void edit(TransactionHistoryEntity transactionHistoryEntity);

    void destroy(TransactionHistoryEntity transactionHistoryEntity);

    TransactionHistoryEntity find(Object pk);

    List findAll();
    
}
