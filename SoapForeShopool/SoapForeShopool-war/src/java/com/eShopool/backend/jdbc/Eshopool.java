package com.eShopool.backend.jdbc;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The class consists of a set of basic database paremeters
 * such as database username, database password, database name,
 * as well as the ip address od reading & writting server.
 * 
 * @author Group 10
 * @since 30/10/2019
 */
public class Eshopool {

    static final String DATABASE_USERNAME = "admin";
    static final String DATABASE_PASSWORD = "eshopool";
    static final String dbName = "eshopool";
    static final String[] writeServer = {"192.168.0.171", "192.168.0.50"};
    static final String[] readServer = {"192.168.0.171", "192.168.0.50"};

    static LoadBalance lb = new LoadBalance(writeServer, readServer);

    /**
     * This method is to get the reading connection of the database
     * 
     * @return readingCon.
     * @exception SQLException On input error.
     * @see SQLException
     */
    public static Connection getReadingConnection() {
        int index = 5;
        while (true) {
            try {
                index = lb.readIP();
                String reading_ip = lb.getRIP(index);
                System.out.println(reading_ip);
                DriverManager.setLoginTimeout(3);
                Connection readingCon = DriverManager.getConnection("jdbc:mysql://" + reading_ip + ":3306/" + dbName, DATABASE_USERNAME, DATABASE_PASSWORD);
                readingCon.setAutoCommit(false);
                lb.sEffectWeightUp(index);

                return readingCon;
            } catch (SQLException ex) {
                Logger.getLogger(Eshopool.class.getName()).log(Level.SEVERE, null, ex);
                lb.sEffectWeightDown(index);
                System.out.println("reading connection failure");
                
            }
        }
    }

    /**
     * This method is to get the writting connection of the database
     * 
     * @return writtingCon.
     * @exception SQLException On input error.
     * @see SQLException
     */
    public static Connection getWrittingConnection() {
        int index = 5;
        while (true) {
            try {
                index = lb.writeIP();
                String writting_ip = lb.getWIP(index);
                System.out.println(writting_ip);
                DriverManager.setLoginTimeout(3);
                Connection writtingCon = DriverManager.getConnection("jdbc:mysql://" + writting_ip + ":3306/" + dbName, DATABASE_USERNAME, DATABASE_PASSWORD);
                writtingCon.setAutoCommit(false);
                
                lb.mEffectWeightUp(index);
                return writtingCon;
            } catch (SQLException ex) {
                lb.mEffectWeightDown(index);
                Logger.getLogger(Eshopool.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("writting connection failure");
            }
        }
    }

    /**
     * This method is to close the database connection
     * 
     * @param con
     * @exception SQLException On input error.
     * @see SQLException
     */
    public static void close(Connection con) {
        try {
            con.commit();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Eshopool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
