package com.AccessMap.Communicate.Forms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Create User Route Form
 * 
 */
@XmlRootElement
public class UserRouteForm {
    private String userName;
    @XmlElement private int evaluation;
    @XmlElement private List<Double> route;
    
    /**
     * Initializes a newly created UserRouteForm object
     */
    public UserRouteForm(){
        userName = "";
        evaluation = 0;
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
     * Create the list of the route points
     * @param size size
     */
    public void initRoute(int size){
        route = new ArrayList<>();
    }
    
    /**
     * Adds points that user passes
     * @param latitude latitude
     * @param longitude longitude
     */
    public void insertPoint(double latitude, double  longitude){
        route.add(latitude);
        route.add(longitude);
    }
    
    /**
     * Gets points that user had passed
     * @param index index
     * @return point
     */
    public Double[] getPoint(int index){
        Double[] point = new Double[2];
        point[0] = route.get(index*2);
        point[1] = route.get(index*2+1);
        return point;
    }
    
    /**
     * Sets the evaluation of the route
     * @param evaluation evaluation
     */
    public void setEvaluation(int evaluation){
        this.evaluation = evaluation;
    }
    
    /**
     * Returns the evaluation of the route
     * @return evaluation
     */
    public int getEvaluation(){
        return evaluation;
    }
    
    /**
     * Returns the number of points of the route
     * @return number of points
     */
    public int getSize(){
        return route.size()/2;
    }
}
