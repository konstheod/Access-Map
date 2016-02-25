package com.AccessMap.Communicate;

import com.AccessMap.Communicate.Forms.EmergencyCallForm;
import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Create Emergency Phone Call service
 * 
 */
@Path("emergency")
public class EmergencyPhoneCall {
    private static Database db;
    
    /**
     * Initializes a newly created EmergencyPhoneCall object
     */
    public EmergencyPhoneCall(){
        db = DatabaseCreator.getInstance();
    }
    
    // Is the Emergency Phone Call service running test

    /**
     * Emergency Phone Call service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Emergency Phone Call service is running!!!";
    }
    
    /**
     * Get the json type of emergency phone call service
     * @return an EmergencyCallForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public EmergencyCallForm getLoginForm() {
        //apla stelnoume ena json morfhs EmergencyCallForm
        return new EmergencyCallForm();
    }
    
    /**
     * Post the data the user filled
     * @param userParams user data
     * @return an EmergencyCallForm object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EmergencyCallForm postEmergencyCallForm( EmergencyCallForm userParams) {
        //call database methods
        String phone;
        phone = db.getEmergencyCall(userParams.getUserName());
        if("".equals(phone)){
            userParams.setEmergencyNumber("112");
        }
        else{
            userParams.setEmergencyNumber(phone);
        }
        return userParams;
    }
}
