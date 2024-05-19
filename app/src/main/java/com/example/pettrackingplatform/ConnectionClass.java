package com.example.pettrackingplatform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionClass {



    //模擬器要連localhost要用10.0.2.2
    //https://blog.csdn.net/eiuly/article/details/17081657
    protected static String ip = "10.0.2.2";
    protected static String port = "3306";
    protected static String db = "pet_tracking_platform";
    protected static String username = "root";
    protected static String password = "";

    public Connection CONN(){

        String url = "jdbc:mysql://"+ip+":"+port+"/"+db+"?useSSL=false";
        String username = "root";
        String password = "";


        try  {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }



    }
}