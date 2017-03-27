package com.AccessMap.Communicate.Forms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Create Search Form
 *
 */
@XmlRootElement
public class SearchForm {
    private String userName;
    @XmlElement private Double[] startingPoint;
    @XmlElement private Double[] destination;
    @XmlElement private List<Double>[] route;

    /**
     * Initializes a newly created SearchForm object
     */
    public SearchForm() {
        userName = "";
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
     * Sets the size of the list of route
     * @param size size
     */
    public void initRoute(int size){
        route = new List[size];
    }
    
    /**
     * Sets latitude and longitude at a specific index of list route
     * @param latitude latitude
     * @param longitude longitude
     * @param index index
     */
    public void insertPoint(double latitude, double  longitude,int index){
        route[index] = new ArrayList<>();
        route[index].add(latitude);
        route[index].add(longitude);
    }
    
    /**
     * Returns the coordinates of a specific index
     * @param index index
     * @return coordinates of the point at that index
     */
    public List<Double> getPoint(int index){
        return route[index];
    }
    
    /**
     * Sets latitude and longitude  of the starting point
     * @param latitude latitude
     * @param longitude longitude
     */
    public void setStartingPoint(double latitude, double  longitude){
        startingPoint = new Double[2];
        startingPoint[0] = latitude;
        startingPoint[1] = longitude;
    }
    
    /**
     * Returns latitude and longitude  of the starting point
     * @return coordinates of starting point
     */
    public Double[] getStartingPoint(){
        return startingPoint;
    }
    
    /**
     * Sets latitude and longitude  of the destination point
     * @param latitude latitude
     * @param longitude longitude
     */
    public void setDestination(double latitude, double  longitude){
        destination = new Double[2];
        destination[0] = latitude;
        destination[1] = longitude;
    }
    
    /**
     * Returns latitude and longitude  of the destination point
     * @return coordinates of destination point
     */
    public Double[] getDestination(){
        return destination;
    }
}
