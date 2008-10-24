/*
 * LoginEntity.java
 *
 * Created on October 18, 2008, 5:22 PM
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
 * Entity class LoginEntity
 * 
 * @author Vaibhav
 */
@Entity
public class LoginEntity implements Serializable {

    @Id
    private String userId;
    private String password;
    private char userRole;
    
    /** Creates a new instance of LoginEntity */
    public LoginEntity() {
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
     * Determines whether another object is equal to this LoginEntity.  The result is 
     * <code>true</code> if and only if the argument is not null and is a LoginEntity object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoginEntity)) {
            return false;
        }
        LoginEntity other = (LoginEntity)object;
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
        return "ejb.LoginEntity[id=" + getUserId() + "]";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String UserId) {
        this.userId = UserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public char getUserRole() {
        return userRole;
    }

    public void setUserRole(char UserRole) {
        this.userRole = UserRole;
    }
    
}
