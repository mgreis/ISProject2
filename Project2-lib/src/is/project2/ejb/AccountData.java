/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

/**
 * Account data.
 *
 * @author Flávio J. Saraiva
 */
public class AccountData {

    private Long id;
    private String email;
    private char[] password;

    public AccountData(Long id) {
        this.id = id;
    }

    public AccountData(Long id, String email, char[] password) {
        this(id);
        this.email = email;
        this.password = password;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(char[] password) {
        this.password = password;
    }
}
