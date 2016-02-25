package com.AccessMap.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Configures the database
 * 
 */
public class Database {
	
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/amdatabase";
    static final String USERNAME = "root";
    static final String PASSWORD = "1234";
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Statement stmt;
	
    /**
     * Initialize a newly created Database object
     */
    public Database() {
	dbConnection();
    }
	
    /**
     * Establishes the connection with SQL server
     */
    public void dbConnection() {
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
			stmt = conn.createStatement();
		} catch(SQLException se) {
			System.out.println("Connection failed.");
			se.printStackTrace();
			return;
		} catch(ClassNotFoundException ce) {
			System.out.println("Can't find JDBC driver.");
			ce.printStackTrace();
			return;
		}
		if (conn!= null) {
			System.out.println("Connected with db.");
		} else {
			System.out.println("Failed to make connection with db.(conn==NULL)");
		}
	}
	
    /**
     * Creates the tables of the database
     */
    public void createTables() {
		
		try {
			String sql = "CREATE TABLE users " + "(Username VARCHAR(255) not NULL, Password VARCHAR(255) not NULL, Email VARCHAR(255) not NULL UNIQUE, Name VARCHAR(255), Lastname VARCHAR(255), Address VARCHAR(255), Phone VARCHAR(255), EmergencyCall VARCHAR(255), PRIMARY KEY(Username)) ENGINE=InnoDB";
			stmt.executeUpdate(sql);
			System.out.println("Creation of users table complete!");
		} catch(SQLException se) {
			System.out.println("Table users already exists.");
		}
		
		try {
			String sql = "CREATE TABLE history " + "(Username VARCHAR(255) not NULL, Destination VARCHAR(255) not NULL, DestinationDate TIMESTAMP not NULL, FOREIGN KEY(Username) REFERENCES users(Username) ON DELETE CASCADE) ENGINE=InnoDB";
			stmt.executeUpdate(sql);
			System.out.println("Creation of history table complete!");
		} catch(SQLException se) {
			System.out.println("Table history already exists.");
		}
		
		try {
			String sql = "CREATE TABLE crossroads " + "(Lat1 DECIMAL(10,8) not NULL, Long1 DECIMAL(11,8) not NULL, Lat2 DECIMAL(10,8) not NULL, Long2 DECIMAL(11,8) not NULL, Evaluation DECIMAL(4,2) not NULL, Counter INTEGER, PRIMARY KEY(Lat1, Long1, Lat2, Long2)) ENGINE=InnoDB";
			stmt.executeUpdate(sql);
			System.out.println("Creation of crossroads table complete!");
		} catch(SQLException se) {
			System.out.println("Table crossroads already exists!");
		}
	}
	
    /**
     * Inserts the sign up data 
     * @param Username Username
     * @param Password Password
     * @param Email Email
     * @param Name Name
     * @param Lastname Lastname
     * @param Address Address
     * @param Phone Phone
     * @param EmergencyCall EmergencyCall
     * @return false if the user already exists else true
     */
    public boolean signUp(String Username, String Password, String Email, String Name, String Lastname, String Address, String Phone, String EmergencyCall) {
		// Returns true if SignUp was successful otherwise returns false
		try {
			pstmt = conn.prepareStatement("INSERT INTO users values(?,?,?,?,?,?,?,?)");
			pstmt.setString(1,Username);
			pstmt.setString(2,Password);
			pstmt.setString(3,Email);
			pstmt.setString(4,Name);
			pstmt.setString(5,Lastname);
			pstmt.setString(6,Address);
			pstmt.setString(7,Phone);
			pstmt.setString(8,EmergencyCall);
			pstmt.executeUpdate();
			return true;
		} catch(MySQLIntegrityConstraintViolationException se) {
			return false;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		}
	}
	
    /**
     * Authorization for users data
     * @param Username Username
     * @param Password Password
     * @return false if the authorization fail else true
     */
    public boolean userLogin(String Username, String Password) {
		// Returns true if username & password are correct , otherwise it returns false
		try {
			rs = stmt.executeQuery("SELECT * FROM users WHERE Username='" + Username + "'" + " AND Password='" + Password + "'");
			if(!rs.isBeforeFirst()) {
				return false;
			} else {
				return true;
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		}
	}
	
    /**
     * Change user's data
     * @param Username Username
     * @param Password Password
     * @param Name Name
     * @param Lastname Lastname
     * @param Address Address
     * @param Phone Phone
     * @param EmergencyCall EmergencyCall
     * @return false if something go wrong
     */
    public boolean userEdit(String Username, String Password, String Name, String Lastname, String Address, String Phone, String EmergencyCall) {
		
		try {
			pstmt = conn.prepareStatement("UPDATE users SET Password = ?, Name = ?, Lastname = ?, Address = ?, Phone = ?, EmergencyCall = ? WHERE Username = ?");
			pstmt.setString(1,Password);
			pstmt.setString(2,Name);
			pstmt.setString(3,Lastname);
			pstmt.setString(4,Address);
			pstmt.setString(5,Phone);
			pstmt.setString(6,EmergencyCall);
			pstmt.setString(7,Username);
			pstmt.executeUpdate();
			System.out.println("Edited " + Username + " information.");
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		}
	}
	
    /**
     * Deletes the data of the user
     * @param Username Username
     * @return false if something go wrong
     */
    public boolean userDelete(String Username) {
		// Deletes the registration in users table and also it's dependencies in history table
		try {
			pstmt = conn.prepareStatement("DELETE FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			pstmt.executeUpdate();
			System.out.println(Username + " deleted.");
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		}
	}
	
    /**
     * Returns history of the user
     * @param Username Username
     * @param Destination Destination
     * @return false if something go wrong
     */
    public boolean historyInsert(String Username, String Destination) {
		Date dt = new Date();
		Timestamp timestamp = new Timestamp(dt.getTime());
		try {
			pstmt = conn.prepareStatement("INSERT INTO history values(?,?,?)");
			pstmt.setString(1,Username);
			pstmt.setString(2,Destination);
			pstmt.setTimestamp(3,timestamp);
			pstmt.executeUpdate();
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		}
	}
	
    /**
     * Returns the history of the user
     * @param Username Username
     * @return History (max 5 elements)
     */
    public String[] getHistory(String Username) {
		// It returns the last 5 Destinations of the user with the corresponding Username
		String[] resultArray = new String[5];
		int counter = 0;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM history WHERE Username = ? ORDER BY DestinationDate DESC");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next() && counter < 5) {
				resultArray[counter] = rs.getString("Destination") + " " + rs.getTimestamp("DestinationDate").toString();
				counter++;
			} 
		} catch(SQLException se) {
			se.printStackTrace();
			resultArray[0] = "ERROR";
			return resultArray;
		}
		return resultArray;
	}
	
    /**
     * Inserts strait
     * @param Lat1 Start latitude
     * @param Long1 Start longitude
     * @param Lat2 End latitude
     * @param Long2 End longitude
     * @param Evaluation Evaluation of route
     * @return false if something go wrong
     */
    public boolean crossroadsInsert(double Lat1, double Long1, double Lat2, double Long2, double Evaluation) {
		// Stores the Lat/Long (Start - End) coordinates of the new crossroad with it's Evaluation
		try {
			pstmt = conn.prepareStatement("INSERT INTO crossroads values(?,?,?,?,?,?)");
			pstmt.setDouble(1,Lat1);
			pstmt.setDouble(2,Long1);
			pstmt.setDouble(3,Lat2);
			pstmt.setDouble(4,Long2);
			pstmt.setDouble(5,Evaluation);
			pstmt.setInt(6,1);     // This is to store into Counter the default value 1
			pstmt.executeUpdate();
			return true;
		} catch(MySQLIntegrityConstraintViolationException se) {
			System.out.println("Duplicate entry in crossroads table!");
			return false;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		}
	}
	
    /**
     * Returns the evaluation of the strait
     * @param Lat1 Start latitude
     * @param Long1 Start longitude
     * @param Lat2 End latitude
     * @param Long2 End longitude
     * @return the evaluation
     */
    public double getEvaluation(double Lat1, double Long1, double Lat2, double Long2) {
		// Returns the Evaluation of the crossroad , in case of error returns -1
		try {
			pstmt = conn.prepareStatement("SELECT Evaluation FROM crossroads WHERE (Lat1 = ? AND Long1 = ? AND Lat2 = ? AND Long2 = ?)");
			pstmt.setDouble(1,Lat1);
			pstmt.setDouble(2,Long1);
			pstmt.setDouble(3,Lat2);
			pstmt.setDouble(4,Long2);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getDouble("Evaluation");
			}
		} catch(SQLException se ) {
			return (-1);
		}
		return (-1);
	}
	
    /**
     * Returns the Counter of the crossroad , which shows how many times an evaluation has been stored
     * @param Lat1 Start latitude
     * @param Long1 Start longitude
     * @param Lat2 End latitude
     * @param Long2 End longitude
     * @return in case of error returns -1
     */
    public int getCounter(double Lat1, double Long1, double Lat2, double Long2) {
		// Returns the Counter of the crossroad , which shows how many times an evaluation has been stored , in case of error returns -1
		try {
			pstmt = conn.prepareStatement("SELECT Counter FROM crossroads WHERE (Lat1 = ? AND Long1 = ? AND Lat2 = ? AND Long2 = ?)");
			pstmt.setDouble(1,Lat1);
			pstmt.setDouble(2,Long1);
			pstmt.setDouble(3,Lat2);
			pstmt.setDouble(4,Long2);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("Counter");
			}
		} catch(SQLException se) {
			return (-1);
		}
		return (-1);
	}
	
    /**
     * Add new Evaluation and increase the counter of an already existed crossroad
     * @param Lat1 Start latitude
     * @param Long1 Start longitude
     * @param Lat2 End latitude
     * @param Long2 End longitude
     * @param Evaluation Evaluation of route
     * @param Counter number of straits
     * @return false if something go wrong
     */
    public boolean setEvaluation(double Lat1, double Long1, double Lat2, double Long2, double Evaluation, int Counter) {
		// Add new Evaluation and increase the counter of an already existed crossroad
		try {
			pstmt = conn.prepareStatement("UPDATE crossroads SET Evaluation = ?, Counter = ? WHERE (Lat1 = ? AND Long1 = ? AND Lat2 = ? AND Long2 = ?)");
			pstmt.setDouble(1,Evaluation);
			pstmt.setInt(2,Counter);
			pstmt.setDouble(3,Lat1);
			pstmt.setDouble(4,Long1);
			pstmt.setDouble(5,Lat2);
			pstmt.setDouble(6,Long2);
			pstmt.executeUpdate();
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		}
	}
	
    /**
     * Print crossroads
     */
    public void getCrossroads() {
		
		try {
			pstmt = conn.prepareStatement("SELECT * FROM crossroads");
			rs = pstmt.executeQuery();
			while(rs.next() ) {
				System.out.println(rs.getString("Lat1") + " " + rs.getString("Long1") + " " + rs.getString("Lat2") + " " + rs.getString("Long2") + " " + rs.getString("Evaluation"));
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return;
		}
	}
	
    /**
     * Return the password
     * @param Username Username
     * @return the password
     */
    public String getPassword(String Username) {
		
		try {
			pstmt = conn.prepareStatement("SELECT Password FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString("Password");
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return "";
		}
		return "";
	}
	
    /**
     * Returns the email of the user
     * @param Username Username
     * @return email of user
     */
    public String getEmail(String Username) {
		
		try {
			pstmt = conn.prepareStatement("SELECT Email FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString("Email");
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return "";
		}
		return "";
	}
	
    /**
     * Returns the firstName of the user
     * @param Username Username
     * @return firstName of user
     */
    public String getName(String Username) {
		
		try {
			pstmt = conn.prepareStatement("SELECT Name FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString("Name");
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return "";
		}
		return "";
	}
	
    /**
     * Returns the lastName of the user
     * @param Username Username
     * @return lastName of user
     */
    public String getLastname(String Username) {
		
		try {
			pstmt = conn.prepareStatement("SELECT Lastname FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString("Lastname");
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return "";
		}
		return "";
	}
	
    /**
     * Returns the address of the user
     * @param Username Username
     * @return address of user
     */
    public String getAddress(String Username) {
		
		try {
			pstmt = conn.prepareStatement("SELECT Address FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString("Address");
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return "";
		}
		return "";
	}
	
    /**
     * Returns the phone of the user
     * @param Username Username
     * @return phone of user
     */
    public String getPhone(String Username) {
		
		try {
			pstmt = conn.prepareStatement("SELECT Phone FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString("Phone");
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return "";
		}
		return "";
	}
	
    /**
     * Returns the phone of user's EmergencyCall
     * @param Username Username
     * @return phone of user's EmergencyCall
     */
    public String getEmergencyCall(String Username) {
		
		try {
			pstmt = conn.prepareStatement("SELECT EmergencyCall FROM users WHERE Username = ?");
			pstmt.setString(1,Username);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString("EmergencyCall");
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return "";
		}
		return "";
	}
}