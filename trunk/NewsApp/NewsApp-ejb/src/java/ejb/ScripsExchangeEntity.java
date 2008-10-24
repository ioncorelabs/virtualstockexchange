/*
 * ScripsExchangeEntity.java
 *
 * Created on October 24, 2008, 5:11 PM
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
 * Entity class ScripsExchangeEntity
 * 
 * @author Vaibhav
 */
@Entity
public class ScripsExchangeEntity implements Serializable {

    @Id
    private String scripId;
    private String scripName;
    private int totalShares;
    private int totalAvailable;
    private float marketCap;
    private float pricePerShare;

    
    /** Creates a new instance of ScripsExchangeEntity */
    public ScripsExchangeEntity() {
    }


    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.scripId != null ? this.scripId.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this ScripsExchangeEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a ScripsExchangeEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScripsExchangeEntity)) {
            return false;
        }
        ScripsExchangeEntity other = (ScripsExchangeEntity)object;
        if (this.scripId != other.scripId && (this.scripId == null || !this.scripId.equals(other.scripId))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ejb.ScripsExchangeEntity[id=" + scripId + "]";
    }

    public String getScripId() {
        return scripId;
    }

    public void setScripId(String scripId) {
        this.scripId = scripId;
    }

    public String getScripName() {
        return scripName;
    }

    public void setScripName(String scripName) {
        this.scripName = scripName;
    }

    public int getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(int totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public float getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(float marketCap) {
        this.marketCap = marketCap;
    }

    public float getPricePerShare() {
        return pricePerShare;
    }

    public void setPricePerShare(float pricePerShare) {
        this.pricePerShare = pricePerShare;
    }
    
}
