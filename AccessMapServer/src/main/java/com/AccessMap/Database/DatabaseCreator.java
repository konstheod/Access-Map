package com.AccessMap.Database;

/**
 * Create the database
 * 
 */
public class DatabaseCreator {
    
    private static DatabaseCreator instance = null;
    private static Database db;
    
    /**
     * Initialize the database
     * @return database
     */
    public static Database getInstance(){
        if(instance==null){
            instance = new DatabaseCreator();
            db = new Database();
            db.createTables();
        }
        return db;
    }
}
