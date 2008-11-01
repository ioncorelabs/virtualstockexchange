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
    private double marketCap;
    private double pricePerShare;
    private int totalSharesLent;
    private int change;
    
    
    /** Creates a new instance of ScripsExchangeEntity */
    public ScripsExchangeEntity() {
    }
    
    
    public ScripsExchangeEntity(String scripId,
            String scripName,
            int totalShares,
            int totalSharesAvailable,
            double marketCap,
            double pricePerShare) {
        this.scripId = scripId;
        this.scripName = scripName;
        this.totalShares = totalShares;
        this.totalAvailable = totalSharesAvailable;
        this.marketCap = marketCap;
        this.pricePerShare = pricePerShare;
    }
    
    /**
     * Returns a hash code value for the object.  This implementation computes
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getScripId() != null ? this.getScripId().hashCode() : 0);
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
        if (this.getScripId() != other.getScripId() && (this.getScripId() == null || !this.getScripId().equals(other.getScripId()))) return false;
        return true;
    }
    
    /**
     * Returns a string representation of the object.  This implementation constructs
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ejb.ScripsExchangeEntity[id=" + getScripId() + "]";
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
    
    public double getMarketCap() {
        return marketCap;
    }
    
    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }
    
    public double getPricePerShare() {
        return pricePerShare;
    }
    
    public void setPricePerShare(double pricePerShare) {
        this.pricePerShare = pricePerShare;
    }
    
    public int getTotalSharesLent() {
        return totalSharesLent;
    }
    
    public void setTotalSharesLent(int totalSharesLent) {
        this.totalSharesLent = totalSharesLent;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }
    
}
