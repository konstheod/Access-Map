package com.AccessMap.Communicate.Forms;

import javax.xml.bind.annotation.*;

/**
 * Create History Form
 *
 */
@XmlRootElement
public class HistoryForm {
    private String userName;
    @XmlElement private final String[] historyArray;
    
    /**
     * Initializes a newly created HistoryForm object
     */
    public HistoryForm(){
        userName = "";
        historyArray = new String[5];
        for(int i=0;i<5;i++){
            historyArray[i] = "";
        }
    }
    
    /**
     * Sets the location of this object at a specific index
     * @param location location
     * @param index index
     */
    public void setHistory(String location, int  index){
        historyArray[index] = location;
    }
    
    /**
     * Returns the location of this object at a specific index
     * @param index index
     * @return location at specific index
     */
    public String getHistory(int index){
        return  historyArray[index];
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
}
