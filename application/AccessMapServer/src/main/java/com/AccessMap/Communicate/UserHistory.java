package com.AccessMap.Communicate;

import com.AccessMap.Communicate.Forms.HistoryForm;
import com.AccessMap.Database.Database;
import com.AccessMap.Database.DatabaseCreator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Create History service
 * 
 */
@Path("history")
public class UserHistory {
    private static Database db;
    
    /**
     * Initializes a newly created UserHistory object
     */
    public UserHistory(){
        db = DatabaseCreator.getInstance();
    }

    /**
     * History service running test
     * @return a test message(String)
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() {
        return "History service is running!!!";
    }
    
    /**
     * Get the json type of History service
     * @return a HistoryForm object
     */
    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public HistoryForm getHistoryForm() {
        HistoryForm user = new HistoryForm();
        return user;
    }
    
    /**
     * Post the history data that exists in the database, else a text message(String)
     * @param userParams user data
     * @return a HistoryForm object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HistoryForm postHistoryForm(HistoryForm userParams) {
        String[] history;
        history = db.getHistory(userParams.getUserName());
        
        
        if(history[0] == null){
            userParams.setHistory("You haven't searched anything yet.", 0);
        }
        else{
            userParams.setHistory(history[0], 0);
        }
        for(int i=1;i<5;i++){
            if("".equals(history[i])){
                 userParams.setHistory("", i);
            }
            else{
                 userParams.setHistory(history[i], i);
            }
        }
        return userParams;
    }
}
