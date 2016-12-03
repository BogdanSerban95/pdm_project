package project.passwordproject.classes;

import java.io.Serializable;

/**
 * Created by Serban on 02/12/2016.
 */

public class AccountDetails implements Serializable {
    private String userName;
    private String email;
    private String password;
    private String comments;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public AccountDetails(String userName, String email, String password, String comments) {
        this.userName = userName;
        this.email = email;
        this.password = password;

        this.comments = comments;
    }


}
