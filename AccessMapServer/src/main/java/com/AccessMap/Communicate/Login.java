package com.AccessMap.Communicate;

import com.AccessMap.Communicate.Forms.LoginForm;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;

import com.AccessMap.Database.DatabaseCreator;
import com.AccessMap.Database.Database;

/**
 * Create Login service
 * 
 */
@Path("login")
public class Login {
    
    private final  LoginForm user;
    private static Database db;
    
    /**
     * Initializes a newly created Login object
     */
    public Login() {
        user = new LoginForm();
        db = DatabaseCreator.getInstance();
    }

    
    // Is the login service running test

    /**
     * Login service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "Login service is running!!!";
    }
    
    /**
     * Get the json type of Login service
     * @return a LoginForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public LoginForm getLoginForm() {
        return user;
    }
    
    /**
     * Post Granted if there is identification, else post DENY
     * @param userParams user data
     * @return a LoginForm object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoginForm postLoginForm( LoginForm userParams) {
        user.setUserName(userParams.getUserName());
        user.setPassword(userParams.getPassword());
        
        //call database methods
        boolean login;
        login = db.userLogin(user.getUserName(),user.getPassword());
        if(login){
            user.setPermission("GRANTED");
        }
        else{
            user.setPermission("DENY");
        }
        return user;
    }
}
