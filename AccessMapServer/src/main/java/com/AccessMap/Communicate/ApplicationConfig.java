package com.AccessMap.Communicate;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * Creates the web service application
 * 
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    /**
     * Add services to web service
     * @return services
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.AccessMap.Communicate.Delete.class);
        resources.add(com.AccessMap.Communicate.Edit.class);
        resources.add(com.AccessMap.Communicate.EmergencyPhoneCall.class);
        resources.add(com.AccessMap.Communicate.Login.class);
        resources.add(com.AccessMap.Communicate.ReceiveRoute.class);
        resources.add(com.AccessMap.Communicate.SearchRoute.class);
        resources.add(com.AccessMap.Communicate.SignUp.class);
        resources.add(com.AccessMap.Communicate.UserHistory.class);
    }
}
