package com.AccessMap.Route;

import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import com.google.maps.model.LatLng;
import java.util.List;

/**
 * A test class to check the algorithms without the Android Application
 * 
 */
public class Maps {
    
    /**
     * Main method where we call the testIntersection() and test Directions()
     * methods to check the corresponding classes
     * @param args nothing useful
     */
    public static void main(String args[]){
        testIntersection();
        testDirections();
        
    }
    
    /**
     * Tests the Intersection class by insert point that the user passed
     * Display all the Straits that the user passed
     * Also saves the straits in the Database
     * 
     */
    public static void testIntersection(){
        Intersection test;
        int size; 
        test = new Intersection();
        test.setGrade(8);  //here we set evaluation for the route
        
        /*test.initReceivedRoute(39.361575, 22.954859);
        test.initReceivedRoute(39.361266, 22.954546);
        test.initReceivedRoute(39.360976, 22.954305);
        test.initReceivedRoute(39.360644, 22.953983);
        test.initReceivedRoute(39.360801, 22.953728);
        test.initReceivedRoute(39.361007, 22.953318);
        test.initReceivedRoute(39.361779, 22.951861);
        test.initReceivedRoute(39.361167, 22.953063);
        test.initReceivedRoute(39.361395, 22.952672);
        test.initReceivedRoute(39.361619, 22.952178);
        test.initReceivedRoute(39.361873, 22.951703);
        test.initReceivedRoute(39.362396, 22.951569);
        test.initReceivedRoute(39.362603, 22.952014);
        test.initReceivedRoute(39.362272, 22.952573);
        test.initReceivedRoute(39.361966, 22.953138);
        */
        
        test.initReceivedRoute(39.359884, 22.956409);
        test.initReceivedRoute(39.355675, 22.958820);
        
        test.findIntersection();
        test.saveIntersection();
        
        size = test.getSizeStrait();
        for(int i=0;i<size;i++){
            System.out.print("Strait " + i +": ");
            System.out.print("Start Lat " + test.getStraitStart(i).lat + " Lng " + test.getStraitStart(i).lng);
            System.out.println("\t End Lat " + test.getStraitEnd(i).lat + " Lng " + test.getStraitEnd(i).lng);
        }
    }
    
    /**
     * Tests the Direction class by insert Starting and Destination point
     * Display all the points the user must follow
     * 
     */
    public static void testDirections(){
        LatLng origin = new LatLng(39.359364, 22.955991);
        LatLng destination = new LatLng(39.356000, 22.956518);
        int size; 
        List<LatLng> choosen;
       
        Directions dir = new Directions();
        dir.setOrigin(origin);
        dir.setDestination(destination);
        dir.getDirections();
        
        Database db; 
        db = DatabaseCreator.getInstance();
        String history = dir.getReverceGeo(destination);
        boolean checkHistory =  db.historyInsert("konstheo" ,history);
        if(checkHistory==false){
            System.out.println("Failed to add destination to user history");
        }
        
        choosen = dir.getChoosen();
        size = choosen.size();
        
        System.out.println("Points the user must follow!");
        for(int i=0;i<size;i++){
            System.out.println("Point " + i + "\tLat " +  choosen.get(i).lat + "\tLng " +  choosen.get(i).lng);
        }
    }
}
