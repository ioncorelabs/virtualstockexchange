/*
 * ScripsUserEntity.java
 *
 * Created on October 24, 2008, 7:00 PM
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
 * Entity class ScripsUserEntity
 * 
 * @author Vaibhav
 */
@Entity
public class ScripsUserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String scripId;
    private String userId;
    private int sharesHeld;
    private int buyingPrice;
    
    /** Creates a new instance of ScripsUserEntity */
    public ScripsUserEntity() {
    }

    /**
     * Gets the id of this ScripsUserEntity.
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the id of this ScripsUserEntity to the specified value.
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this ScripsUserEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a ScripsUserEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScripsUserEntity)) {
            return false;
        }
        ScripsUserEntity other = (ScripsUserEntity)object;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ejb.ScripsUserEntity[id=" + getId() + "]";
    }

    public String getScripId() {
        return scripId;
    }

    public void setScripId(String scripId) {
        this.scripId = scripId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSharesHeld() {
        return sharesHeld;
    }

    public void setSharesHeld(int sharesHeld) {
        this.sharesHeld = sharesHeld;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }
    
}
