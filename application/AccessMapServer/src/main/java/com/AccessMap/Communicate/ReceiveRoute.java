package com.AccessMap.Communicate;

import com.AccessMap.Communicate.Forms.UserRouteForm;
import com.AccessMap.Route.Intersection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Create Receive service
 * 
 */
@Path("receive")
public class ReceiveRoute {
    
    /**
     * Receive service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Receive Route service is running!!!";
    }
    
    /**
     * Get the json type of Receive service
     * @return a UserRouteForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public UserRouteForm getUserRouteForm() {
        //apla stelnoume ena json morfhs UserRouteForm
        UserRouteForm form = new UserRouteForm();
        form.initRoute(2);
        form.insertPoint(0.0, 0.0);
        form.insertPoint(0.0, 0.0);
        return form;
    }
    
    /**
     * Post the data the user filled
     * @param userRoute route data
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void postUserRouteForm( UserRouteForm userRoute) {
        Intersection route = new Intersection();
        int size = userRoute.getSize();
        double lat;
        double lng;
        
        for(int i = 0; i<size; i++){
            lat = userRoute.getPoint(i)[0];
            lng = userRoute.getPoint(i)[1];
            route.initReceivedRoute(lat,lng);
        }
        route.findIntersection();
        route.setGrade(userRoute.getEvaluation());
        
        if(route.getSizeStrait()>0){
            route.saveIntersection();
        }
    }
}
