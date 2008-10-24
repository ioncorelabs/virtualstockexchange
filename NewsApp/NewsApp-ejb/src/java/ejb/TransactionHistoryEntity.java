/*
 * TransactionHistoryEntity.java
 *
 * Created on October 24, 2008, 6:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ejb;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class TransactionHistoryEntity
 * 
 * @author Vaibhav
 */
@Entity
public class TransactionHistoryEntity implements Serializable {

    @Id
    private
    String tranId;
    private String userId;
    private String scripId;
    private String tranType;
    private int totalShares;
    private float totalTranAmount;
    
      
    
    /** Creates a new instance of TransactionHistoryEntity */
    public TransactionHistoryEntity() {
    }


    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the userId fields in this object.
     * 
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getTranId() != null ? this.getTranId().hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this TransactionHistoryEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a TransactionHistoryEntity object that 
     * has the same userId field values as this object.
     * 
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userId fields are not set
        if (!(object instanceof TransactionHistoryEntity)) {
            return false;
        }
        TransactionHistoryEntity other = (TransactionHistoryEntity)object;
        if (this.getTranId() != other.getTranId() && (this.getTranId() == null || !this.getTranId().equals(other.getTranId()))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the userId fields.
     * 
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ejb.TransactionHistoryEntity[id=" + getTranId() + "]";
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScripId() {
        return scripId;
    }

    public void setScripId(String scripId) {
        this.scripId = scripId;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public int getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }

    public float getTotalTranAmount() {
        return totalTranAmount;
    }

    public void setTotalTranAmount(float totalTranAmount) {
        this.totalTranAmount = totalTranAmount;
    }
    
}
