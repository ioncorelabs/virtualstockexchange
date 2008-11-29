/*
 * UsersEntity.java
 *
 * Created on October 26, 2008, 5:31 PM
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
 * Entity class UsersEntity
 * 
 * @author Milind Nimesh
 */
@Entity
public class UsersEntity implements Serializable {

    @Id
    private String userId;
    private String userName;
    private double cashHeld;
    private double initialCashHeld;    
    private char active;
    
    /** Creates a new instance of UsersEntity */
    public UsersEntity() {
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getUserId() != null ? this.getUserId().hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this UsersEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a UsersEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersEntity)) {
            return false;
        }
        UsersEntity other = (UsersEntity)object;
        if (this.getUserId() != other.getUserId() && (this.getUserId() == null || !this.getUserId().equals(other.getUserId()))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ejb.UsersEntity[userid=" + getUserId() + "]";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getCashHeld() {
        return cashHeld;
    }

    public void setCashHeld(double cashHeld) {
        this.cashHeld = cashHeld;
    }

    public double getInitialCashHeld() {
        return initialCashHeld;
    }

    public void setInitialCashHeld(double initialCashHeld) {
        this.initialCashHeld = initialCashHeld;
    }

    public char getActive() {
        return active;
    }

    public void setActive(char active) {
        this.active = active;
    }
    
}
