package com.AccessMap.Communicate;

import com.AccessMap.Communicate.Forms.DeleteForm;
import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *Create delete service
 * 
 */
@Path("delete")
public class Delete {
    private DeleteForm user;
    private static Database db;
    
    /**
     * Initializes a newly created Delete object
     */
    public Delete(){
        user = new DeleteForm();
        db = DatabaseCreator.getInstance();
    }
    
    // Is the delete service running test

    /**
     * Delete service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Delete service is running!!!";
    }
    
    /**
     * Get the json type of delete service
     * @return a DeleteForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public DeleteForm getDeleteForm() {
        return user;
    }
    
    /**
     * Post Granted if there is identification, else post DENY
     * @param userParams user data
     * @return a DeleteForm object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DeleteForm postDeleteForm( DeleteForm userParams){
        
        if(db.userDelete(userParams.getUserName())){
            user.setUserName(userParams.getUserName());
            user.setPassword(userParams.getPassword());
            user.setPermission("GRANTED");
        }
        else{
            user.setUserName(userParams.getUserName());
            user.setPassword(userParams.getPassword());
            user.setPermission("DENY");
        }
        return user;
    }
}
