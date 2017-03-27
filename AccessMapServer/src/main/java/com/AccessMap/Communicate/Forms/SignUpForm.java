package com.AccessMap.Communicate.Forms;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Create Sign Up Form
 * 
 */
@XmlRootElement
public class SignUpForm {
    
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String emergencyCall;
    
    private String permission;
    
    /**
     * Initializes a newly created SignUpForm object
     */
    public SignUpForm() {
        userName = "";
        password = "";
        email = "";
        firstName = "";
        lastName = "";
        address = "";
        phoneNumber = "";
        emergencyCall = "";
        permission = "";
    }
    
    /**
     * Initializes a newly created SignUpForm object with specific userName, password and email
     * @param userName userName
     * @param password password
     * @param email email
     */
    public SignUpForm(String userName, String password, String email){
        this.userName = userName;
        this.password = password;
        this.email = email;
        firstName = "";
        lastName = "";
        address = "";
        phoneNumber = "";
        emergencyCall = "";
        permission = "";
    }
    
     /**
     * Initializes a newly created SignUpForm object with specific userName, password, email, firstName and lastName
     * @param userName userName
     * @param password password
     * @param email email
     * @param firstName firstName
     * @param lastName lastName
     */
    public SignUpForm(String userName, String password, String email, String firstName, String lastName){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        address = "";
        phoneNumber = "";
        emergencyCall = "";
        permission = "";
    }
    
    /**
     * Initializes a newly created SignUpForm object with specific userName, password, email, firstName, lastName, address, phoneNumber and emergencyCall
     * @param userName userName
     * @param password password
     * @param email email
     * @param firstName firstName
     * @param lastName lastName
     * @param address address
     * @param phoneNumber phoneNumber
     * @param emergencyCall emergencyCall
     */
    public SignUpForm(String userName, String password, String email, String firstName, String lastName,
            String address, String phoneNumber, String emergencyCall){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emergencyCall = emergencyCall;
        permission = "";
    }
    
    /**
     * Sets the userName of this object
     * @param userName userName
     */
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    /**
     * Returns the userName of this object
     * @return userName
     */
    public String getUserName(){
        return userName;
    }   
        
    /**
     * Sets the password of this object
     * @param password password
     */
    public void setPassword(String password){
        this.password = password;
    }
    
    /**
     * Returns the password of this object
     * @return password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Sets the email of this object
     * @param email email
     */
    public void setEmail(String email){
        this.email = email;
    }
    
    /**
     * Returns the email of this object
     * @return email
     */
    public String getEmail(){
        return email;
    }
    
    /**
     * Sets the firstName of this object
     * @param firstName firstName
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    /**
     * Returns the firstName of this object
     * @return firstName
     */
    public String getFirstName(){
        return firstName;
    }
    
    /**
     * Sets the lastName of this object
     * @param lastName lastName
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    /**
     * Returns the lastName of this object
     * @return lastName
     */
    public String getLastName(){
        return lastName;
    }
    
    /**
     * Sets the address of this object
     * @param address address
     */
    public void setAddress(String address){
        this.address = address;
    }
    
    /**
     * Returns the address of this object
     * @return address
     */
    public String getAddress(){
        return address;
    }
    
    /**
     * Sets the phoneNumber of this object
     * @param phoneNumber phoneNumber
     */
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Returns the phoneNumber of this object
     * @return phoneNumber
     */
    public String getPhoneNumber(){
        return  phoneNumber;
    }
    
    /**
     * Sets the emergencyCall of this object
     * @param emergencyCall emergencyCall
     */
    public void setEmergencyCall(String emergencyCall){
        this.emergencyCall = emergencyCall;
    }
    
    /**
     * Returns the emergencyCall of this object
     * @return emergencyCall
     */
    public String getEmergencyCall(){
        return  emergencyCall;
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
