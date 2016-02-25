package com.AccessMap.Communicate;

import com.AccessMap.Communicate.Forms.EditForm;
import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Create Edit service
 * 
 */
@Path("edit")
public class Edit {
    private final EditForm user;
    private static Database db;
   
    /**
     * Initializes a newly created Edit object
     */
    public Edit() {
        user = new EditForm();
        db = DatabaseCreator.getInstance();
    }
    

    /**
     * Edit service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Edit service is running!!!";
    }
    
    /**
     * Get the json type of Edit service
     * @return a DeleteForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public EditForm getEditForm() {
        //apla stelnoume ena json morfhs EditForm
        return user;
    }
    
    /**
     * Post the data the user filled
     * @param userParams user data
     * @return an EditForm object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EditForm postEditForm( EditForm userParams){
        String userName = userParams.getUserName();
        
        user.setUserName(userName);
        user.setPassword(db.getPassword(userName));
        user.setEmail(db.getEmail(userName));
        user.setFirstName(db.getName(userName));
        user.setLastName(db.getLastname(userName));
        user.setAddress(db.getAddress(userName));
        user.setPhoneNumber(db.getPhone(userName));
        user.setEmergencyCall(db.getEmergencyCall(userName));
        
        return user;
    }
    
    /**
     * Post Granted if there is identification, else post DENY
     * @param userParams user data
     * @return an EditForm object
     */
    @POST
    @Path("confirm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EditForm confirmEditForm( EditForm userParams){
          
        if( db.userEdit(userParams.getUserName(),userParams.getPassword(),
                userParams.getFirstName(),userParams.getLastName(),
                userParams.getAddress(),userParams.getPhoneNumber(),
                userParams.getEmergencyCall())
        ){
            userParams.setPermission("GRANTED");
        }
        else{
            userParams.setPermission("DENY");
        }
        return userParams;
        
    }
}
