package com.AccessMap.Communicate;

import com.AccessMap.Communicate.Forms.SignUpForm;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;

import com.AccessMap.Database.DatabaseCreator;
import com.AccessMap.Database.Database;

/**
 * Create Sign Up service
 *
 */
@Path("signup")
public class SignUp {
    private final SignUpForm user;
    private static Database db;
    
    /**
     * Initializes a newly created SignUp object
     */
    public SignUp() {
        user = new SignUpForm();
        db = DatabaseCreator.getInstance();
    }
    

    /**
     * Sign Up service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "SignUp service is running!!!";
    }
    
    /**
     * Get the json type of Sign Up service
     * @return a SignUpForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public SignUpForm getSignUpForm() {
        return user;
    }
    
    /**
     * Post Granted if there is identification, else post DENY
     * @param userParams user data
     * @return a SignUpForm object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SignUpForm postSignUpForm( SignUpForm userParams) {
        user.setUserName(userParams.getUserName());
        user.setPassword(userParams.getPassword());
        user.setEmail(userParams.getEmail());
        user.setFirstName(userParams.getFirstName());
        user.setLastName(userParams.getLastName());
        user.setAddress(userParams.getAddress());
        user.setPhoneNumber(userParams.getPhoneNumber());
        user.setEmergencyCall(userParams.getEmergencyCall());
        
        //call database methods to signup
        boolean signUp;
        signUp = db.signUp(user.getUserName(),user.getPassword(),user.getEmail(),user.getFirstName(),
                user.getLastName(),user.getAddress(),user.getPhoneNumber(),user.getEmergencyCall());
        if (signUp){
            user.setPermission("GRANTED");
        }
        else{
            user.setPermission("DENY");
        }
        return user;                
    }   
}
