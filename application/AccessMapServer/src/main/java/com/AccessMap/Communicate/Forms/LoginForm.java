package com.AccessMap.Communicate.Forms;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Create Login Form
 * 
 */
@XmlRootElement
public class LoginForm {
    
    private String userName;
    private String password;
    private String permission;
    
    /**
     * Initializes a newly created LoginForm object
     */
    public LoginForm() {
        userName = "";
        password = "";
        permission = "";
    }
 
    /**
     * Initializes a newly created LoginForm object with specific userName and password
     * @param userName userName
     * @param password password
     */
    public LoginForm(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
    /**
     * Sets the userName of this object
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Returns the userName of this object
     * @return userName
     */
    public String getUserName() {
        return userName;
    }
   
    /**
     * Sets the password of this object
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Returns the password of this object
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the permission of this object
     * @param permission permission
     */
    public void setPermission(String permission){
        this.permission = permission;
    }
    
    /**
     * Returns the permission of this object
     * @return permission
     */
    public String getPermission(){
        return permission;
    }
}