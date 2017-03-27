package com.AccessMap.Communicate.Forms;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Create Emergency Call Form
 * 
 */
@XmlRootElement
public class EmergencyCallForm {
    private String userName;
    private String emergencyNumber;
    
    /**
     * Initializes a newly created EmergencyCallForm object
     */
    public EmergencyCallForm(){
        userName = "";
        emergencyNumber = "";
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
     * Sets the emergencyNumber of this object
     * @param emergencyNumber emergencyNumber
     */
    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }
    
    /**
     * Returns the emergencyNumber of this object
     * @return emergencyNumber
     */
    public String getEmergencyNumber() {
        return emergencyNumber;
    }
}
