package com.AccessMap.Communicate;
import com.AccessMap.Communicate.Forms.SearchForm;
import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import com.AccessMap.Route.Directions;
import com.google.maps.model.LatLng;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//this method receive a request and returns a route for that request

/**
 * Create Search Route service
 * 
 */
@Path("search")
public class SearchRoute {
     private static Database db;     //Just an instance of the Database
    
    /**
     * Initializes a newly created SearchRoute object
     */
    public SearchRoute(){
        db = DatabaseCreator.getInstance();
    }
    
    // Is the Search Route service running test

    /**
     * Search Route service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Search Route service is running!!!";
    }
    
    /**
     * Get the json type of Search Route service
     * @return a SearchForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchForm getSearchForm() {
        SearchForm userForm = new SearchForm();
        userForm.setStartingPoint(0.0, 0.0);
        userForm.setDestination(0.0, 0.0);
        return userForm;
    }
    
    /**
     * Post the data the user filled
     * @param userSearch user data
     * @return a SearchForm object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchForm postSearchForm( SearchForm userSearch) {
        Directions searchDirection = new Directions();
        List<LatLng> choosen;
        int size;
        
        LatLng origin = new LatLng(userSearch.getStartingPoint()[0],userSearch.getStartingPoint()[1]);
        LatLng destination = new LatLng(userSearch.getDestination()[0],userSearch.getDestination()[1]);
        
        searchDirection.setOrigin(origin);
        searchDirection.setDestination(destination);
        searchDirection.getDirections();
        
        choosen = searchDirection.getChoosen();
        size = choosen.size();
        userSearch.initRoute(size);
        
        for(int i=0;i<size;i++){
            userSearch.insertPoint(choosen.get(i).lat, choosen.get(i).lng, i);
        }
       
        LatLng historyDest = new LatLng(userSearch.getDestination()[0],userSearch.getDestination()[1]);
        String history = searchDirection.getReverceGeo(historyDest);
        boolean checkHistory =  db.historyInsert(userSearch.getUserName() ,history);
        if(checkHistory==false){
            System.out.println("Failed to add destination to user history");
        }
        
        return userSearch;
    }
}
    
