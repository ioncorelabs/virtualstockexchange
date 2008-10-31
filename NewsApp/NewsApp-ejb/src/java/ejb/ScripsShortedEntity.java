/*
 * ScripsShortedEntity.java
 *
 * Created on October 30, 2008, 2:21 PM
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
 * Entity class ScripsShortedEntity
 * 
 * @author Vaibhav
 */
@Entity
public class ScripsShortedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String scripId;
    private String borrowerId;
    private int sharesBorrowed;
    private int sharesShorted;
    private int sharesReturned;
    
    
    
    /** Creates a new instance of ScripsShortedEntity */
    public ScripsShortedEntity() {
    }

    /**
     * Gets the id of this ScripsShortedEntity.
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the id of this ScripsShortedEntity to the specified value.
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
     * Determines whether another object is equal to this ScripsShortedEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a ScripsShortedEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScripsShortedEntity)) {
            return false;
        }
        ScripsShortedEntity other = (ScripsShortedEntity)object;
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
        return "ejb.ScripsShortedEntity[id=" + getId() + "]";
    }

    public String getScripId() {
        return scripId;
    }

    public void setScripId(String scripId) {
        this.scripId = scripId;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public int getSharesBorrowed() {
        return sharesBorrowed;
    }

    public void setSharesBorrowed(int sharesBorrowed) {
        this.sharesBorrowed = sharesBorrowed;
    }

    public int getSharesShorted() {
        return sharesShorted;
    }

    public void setSharesShorted(int sharesShorted) {
        this.sharesShorted = sharesShorted;
    }

    public int getSharesReturned() {
        return sharesReturned;
    }

    public void setSharesReturned(int sharesReturned) {
        this.sharesReturned = sharesReturned;
    }
    
}
