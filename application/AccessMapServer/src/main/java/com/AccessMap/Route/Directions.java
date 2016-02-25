package com.AccessMap.Route;

import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds directions from specific points
 * 
 */
public class Directions {
    private LatLng origin;
    private LatLng destination;
    private final GeoApiContext context;
    private List<LatLng>[] routes; //pinakas me tis diadromes poy epistrefei to directions api
    private static Database db;     //Just an instance of the Database
    private List<LatLng> choosen;
    
    /**
     * Initializes a newly created Directions object
     */
    public Directions(){
        context = new GeoApiContext().setApiKey("AIzaSyDdOA-NqeM4_5E_Pe3c19Jh1AxxEAqYTRY");
        db = DatabaseCreator.getInstance();
        choosen = new ArrayList<>();
    }
    
    private boolean checkCoordinates(LatLng receivedPoint, LatLng sendPoint, int accuracy){
        double digits = Math.pow(10,accuracy);
        if((Math.floor(receivedPoint.lat*digits)/digits)==(Math.floor(sendPoint.lat*digits)/digits)){//check if latitude with first accuracy decimal is the same
            if((Math.floor(receivedPoint.lng*digits)/digits)==(Math.floor(sendPoint.lng*digits)/digits)){//check if longtitude with first accuracy decimal is the same
                return true;
            }
        }
        return false;
    }
    
    /**
     * Converts coordinates to readable address
     * @param point the conversion point
     * @return readable address
     */
    public String getReverceGeo(LatLng point){
        GeocodingResult[] revResults;
        try{
            revResults =  GeocodingApi.reverseGeocode(context, point).await();
        }
        catch(Exception e){
            System.out.println("Problem with Reverse Geocode");
            return null;
        }
            return revResults[0].formattedAddress;
    }
    
    /**
     * Finds the more accessible route
     * @return false if there is a problem, else true
     */
    public boolean getDirections() {
        DirectionsRoute[] directResults;
        String strOrigin = getReverceGeo(origin);
        String strDestination = getReverceGeo(destination);
        List<LatLng> pol;
        double eval;
        double prevEval = 0;
        double evalCounter;
        int choosenRoute = -1;
        int counter;
        int size;
        
        try{
            directResults = DirectionsApi.getDirections(context, strOrigin, strDestination).alternatives(true).await();
        }
        catch(Exception e){
            System.err.println("Problem to Directions Api " + e);
            return false;
        }
        
        routes = new List[directResults.length];
        
        for(int i=0; i<directResults.length; i++){
            routes[i] = new ArrayList<>();
            counter = -1;
            for (DirectionsLeg leg : directResults[i].legs) {
                for (DirectionsStep step : leg.steps) {
                    pol = step.polyline.decodePath();
                    for(int l=0; l<pol.size(); l++){
                        if(l!=0){
                            routes[i].add(pol.get(l));
                            counter++;
                        }
                        else if(counter!=-1){
                            if(!checkCoordinates(pol.get(0), routes[i].get(counter), 6)){
                                routes[i].add(pol.get(l));
                                counter++;
                            }
                        }
                        else{
                            routes[i].add(pol.get(l));
                            counter++;
                        }
                    }
                    pol.clear();
                }
            }
        }
        
        for(int i=0;i<routes.length;i++){
            evalCounter = 0;
            Intersection inter = new Intersection();
            for(int j=0;j<routes[i].size();j++){
                inter.initReceivedRoute(routes[i].get(j).lat,routes[i].get(j).lng);
            }
            inter.findIntersection();
            size = inter.getSizeStrait();
            
            for(int j=0;j<size;j++){
                eval =  db.getEvaluation(inter.getStraitStart(j).lat,inter.getStraitStart(j).lng,
                        inter.getStraitEnd(j).lat,inter.getStraitEnd(j).lng);
                if(eval!=-1){
                   evalCounter = evalCounter + eval;
                }
                else{//se peripotsh poy den yparxei toy bazoyme san bathmologia to 5
                    evalCounter = evalCounter + 5; 
                }
            }
            
            evalCounter = evalCounter/inter.getSizeStrait();
            if(prevEval<evalCounter){
                prevEval = evalCounter;
                choosenRoute = i;
            }
        }
        setChoosen(routes[choosenRoute]);
        
        return true;
    }
    
    private void setChoosen(List<LatLng> choosen){
        this.choosen = choosen;
    }
    
    /**
     * Returns choosen route
     * @return choosen route
     */
    public List<LatLng> getChoosen(){
        return choosen;
    }
    
    /**
     * Set the starting point of the route (origin)
     * @param origin the starting point of the route
     */
    public void setOrigin(LatLng origin){
        this.origin = origin;
    }
    
    /**
     * Set the destination the user want
     * @param destination destination the user want
     */
    public void setDestination(LatLng destination){
        this.destination = destination;
    }
    
    /**
     * Return the starting point of the route (origin)
     * @return the starting point of the route (origin)
     */
    public LatLng getOrigin(){
        return origin;
    }
    
    /**
     * Return the destination the user want
     * @return the destination the user want
     */
    public LatLng getDestination(){
        return destination;
    }
}
