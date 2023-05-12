package JDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

import client.ChatClient;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class mysqlConnection {
	public static Connection conn;
//	private static String url = "jdbc:mysql://127.0.0.1/QuestionBank?serverTimezone=IST";
//	private static String user = "root";
//	private static String password = "765765";
	
	public static Connection connect(String url, String user, String password) {
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	 System.out.println("Driver definition failed");
        	 }
        
        try 
        {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("SQL connection succeed");
            
            // 172.20.10.3
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
		return conn;
	}
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException{
        if(conn != null && !conn.isClosed())
            return conn;
        //connect(url, user, password);
        return conn;
    }
}
