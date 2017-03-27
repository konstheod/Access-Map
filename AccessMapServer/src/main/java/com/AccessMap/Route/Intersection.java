package com.AccessMap.Route;

import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;
import java.util.ArrayList;
import java.util.List;

public class Intersection {
    private GeoApiContext context;
    private List<LatLng> requestedRoute; //request from application
    private List<LatLng> straitStart; //starts of straits
    private List<LatLng> straitEnd;  //ends of straits
    private int grade;              // grade for the hole route
    private static Database db;     //Just an instance of the Database
   
    private LatLng start;       
    private SnappedPoint curr;
    
    public Intersection(GeoApiContext context) {
        this.context = context;
        requestedRoute = new ArrayList<>();
        straitStart = new ArrayList<>();
        straitEnd = new ArrayList<>();
        db = DatabaseCreator.getInstance();
    }

    public Intersection(){
        context = new GeoApiContext().setApiKey("AIzaSyCaESe10K54DA5nGKiI9g5Wo1FCQ1VdnZI");
        requestedRoute = new ArrayList<>();
        straitStart = new ArrayList<>();
        straitEnd = new ArrayList<>();
        db = DatabaseCreator.getInstance();
    }
    
    //insert LatLng to a receivedRoute List to manipulate it later
    public void initReceivedRoute(double latitude, double longitude){
        LatLng coordinates = new LatLng(latitude,longitude);
        requestedRoute.add(coordinates);
    }
    
    //method to set evaluation that user gave
    public void setGrade(int grade){
        this.grade = grade;
    }
    
    // method to calculate the new grade for a strait
    private boolean calGrade(LatLng start, LatLng end){
        double eval;
        double newGrade;
        int count;
        
        eval = db.getEvaluation(start.lat,start.lng,end.lat,end.lng);
        count = db.getCounter(start.lat,start.lng,end.lat,end.lng);
        newGrade = (eval*count+grade)/(count+1);
        if(db.setEvaluation(start.lat,start.lng,end.lat,end.lng,newGrade,(count+1))){
            return true;
        }
        return false;
    }

    //method to save the strait to database
    public void saveIntersection(){
        for(int i=0;i<straitStart.size();i++){
            if( !db.crossroadsInsert(straitStart.get(i).lat,straitStart.get(i).lng,
                    straitEnd.get(i).lat,straitEnd.get(i).lng,grade)){
                if(!calGrade(straitStart.get(i), straitEnd.get(i))){
                    System.out.println("Problem to insert intersection or to set new grade! " + straitStart.get(i)+ " " + straitEnd.get(i));
                }
            }
        }
    }

    //save temporary the start and the end of strait in two lists
    private void getIntersection(LatLng startPoint, LatLng endPoint){
        //the latitude of the startPoint must be smaller than the endPoint
        //With that we ensure that nothins is duplicated in the Database
        double digits = Math.pow(10,6);
        LatLng coorStart = new LatLng(Math.floor(startPoint.lat*digits)/digits,Math.floor(startPoint.lng*digits)/digits);
        LatLng coorEnd = new LatLng(Math.floor(endPoint.lat*digits)/digits,Math.floor(endPoint.lng*digits)/digits);
        if(startPoint.lat<endPoint.lat){
            straitStart.add(coorStart);
            straitEnd.add(coorEnd);
        }
        else{
            straitStart.add(coorEnd);
            straitEnd.add(coorStart);
        }
    }

    //check if the receivedPoint and sendPoint coordinates are the same, accuracy decimal aproximation
    private boolean checkCoordinates(SnappedPoint receivedPoint, LatLng sendPoint, int accuracy){
        double digits = Math.pow(10,accuracy);
        if((Math.floor(receivedPoint.location.lat*digits)/digits)==(Math.floor(sendPoint.lat*digits)/digits)){//check if latitude with first accuracy decimal is the same
            if((Math.floor(receivedPoint.location.lng*digits)/digits)==(Math.floor(sendPoint.lng*digits)/digits)){//check if longtitude with first accuracy decimal is the same
                return true;
            }
        }
        return false;
    }

    //check if the receivedPoint and sendPoint coordinates are the same, accuracy decimal aproximation
    private boolean checkCoordinates(LatLng receivedPoint, LatLng sendPoint, int accuracy){
        double digits = Math.pow(10,accuracy);
        if((Math.floor(receivedPoint.lat*digits)/digits)==(Math.floor(sendPoint.lat*digits)/digits)){//check if latitude with first accuracy decimal is the same
            if((Math.floor(receivedPoint.lng*digits)/digits)==(Math.floor(sendPoint.lng*digits)/digits)){//check if longtitude with first accuracy decimal is the same
                return true;
            }
        }
        return false;
    }
    
    
    /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! na ftiaksoume ayto me thn antitheth poreia*/
    public void findIntersection(){
        int index;//iterator for requestRoute
        int currIndex;
        int iterPoints;
        int requestSize;//size of receoved list
        LatLng[]  requestedArray;
        SnappedPoint[] receivedPoints = null;
        
        iterPoints = 0;
        index = 0;
        requestSize = requestedRoute.size();
        
        while(iterPoints<requestSize){
            currIndex = 0;
            
            if((requestSize-iterPoints)>= 90){
                iterPoints = iterPoints + 90;
            }
            else{
                iterPoints = requestSize;
            }

            //insert N points to requestedArray
            requestedArray = new LatLng[requestedRoute.subList(index, iterPoints).size()];
            requestedArray = requestedRoute.subList(index,iterPoints).toArray(requestedArray);
            index = iterPoints - 2;
            //try to get snap to road points
            try {
                receivedPoints = RoadsApi.snapToRoads(context, true, requestedArray).await();
            } catch (Exception ex) {
                System.out.println("Problem with snapToRoads " + ex);
            }
            
            for(SnappedPoint point: receivedPoints){
                if(point.originalIndex != -1 && currIndex != point.originalIndex) {
                    //shmainei oti se ayto to shmeio arxizei na phgainei antitheta se kapoion dromo
                    //pragma mh apodekto giati theoroyme oti kineitai san oxhma kai oxi os pezos
                    break;
                }
                else {//ean original index =-1 or =index
                    if(curr != null) {//den einai to proto stoixeio poy elegxetai ston pinaka
                        //ean point==curr kai ena apo ta dyo index ==-1 tote einai intersection
                        if(checkCoordinates(point, curr.location, 6)&&(curr.originalIndex==-1 || point.originalIndex==-1 )){
                            //einai intersection
                            checkPoint();
                        }
                        else {//proxwra den einai intersection
                            curr = point;
                        }
                    }
                    else {//einai to proto ston pinaka
                        curr = point;
                    }
                    if(point.originalIndex != -1) {//ean index>0 tote shmainei pos elegxe requested point
                        currIndex++;
                    }
                }
            }

        }
    }
    
    //check if the strait is already in our lists
    private boolean checkExistence(LatLng startPoint, LatLng endPoint){
        //the latitude of the startPoint must be smaller than the endPoint, because that is the way
        //that we temporary saved on the lists
        if(startPoint.lat>endPoint.lat){
            LatLng temp;
            temp = startPoint;
            startPoint = endPoint;
            endPoint = temp;
        }
        //we check if the strait is already on the lists
        for(int i=0; i<straitStart.size(); i++){
            if(checkCoordinates(startPoint,straitStart.get(i), 6)&&checkCoordinates(endPoint, straitEnd.get(i), 6)){
                return true;
            }
        }
        return false;
    }
    
    //do the necessarily things to get the intersection and move on
    private void checkPoint(){
        if(start != null) {//this means we find strait, so we add it on list
            if(!checkCoordinates(curr, start, 6)&& !checkExistence(start, curr.location)){
                getIntersection(start, curr.location);
            }
        }
        //we get the start of the next strait
        start = curr.location;
        curr = null;
    }
    
    public LatLng getStraitStart(int i){
        return straitStart.get(i);
    }
    
    public LatLng getStraitEnd(int i){
        return straitEnd.get(i);
    }
    
    public int getSizeStrait(){
        return straitStart.size();
    }
}
