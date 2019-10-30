package com.eShopool.backend.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class consists of a set of basic oprations for database table commodity,
 * including SELECT, UODATE, INSERT
 *
 * @author Group 10
 * @since 30/10/2019
 */
public class Commodity extends Eshopool {
    
    /**
     * This method is to create a record of a commodity.
     * 
     * @throws SQLException 
     */
    public static int create_commodity(String u_public_key, String c_name, String c_price, String c_num, String category_ID, String c_desc, String c_img_id) throws SQLException {
        Connection con = getWrittingConnection();
        int u_ID = User.get_u_ID(u_public_key);
        if (category_ID.equals("Necessities"))
            category_ID = "1";
        else if (category_ID.equals("Electronics"))
            category_ID = "2";
        else if (category_ID.equals("Cosmetic"))
            category_ID = "3";
        else
            category_ID = "0";

        String insert_sql = "INSERT INTO commodity (c_name,  c_price, c_num, category_ID,  u_ID, c_desc, c_img_id, c_lock) VALUES (?, ?, ?, ?, ?, ?, ?, \"0\");";
        PreparedStatement ps = con.prepareStatement(insert_sql);
        ps.setString(1, c_name);
        ps.setString(2, c_price);
        ps.setString(3, c_num);
        ps.setString(4, category_ID);
        ps.setString(5, String.valueOf(u_ID));
        ps.setString(6, c_desc);
        ps.setString(7, c_img_id);

        int res = ps.executeUpdate();
        close(con);
        return res;
    }

    /**
     * This method is to get a record of a commodity from database table commodity.
     * 
     * @return String record (c_name, c_price, c_num, category_ID, u_ID, c_desc FROM commodity)
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_commodity_infor(String c_img_id) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT c_name, c_price, c_num, category_ID, u_ID, c_desc FROM commodity WHERE c_img_id = \"" + c_img_id + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString(1);
            result += "," + resultSet.getString(2);
            result += "," + resultSet.getString(3);
            result += "," + resultSet.getString(4);
            result += "," + resultSet.getString(5);
            result += "," + resultSet.getString(6) + ",";
        }
        close(con);
        return result;
    }

    /**
     * This method is to get commodity ID from database table commodity.
     * 
     * @return int c_ID
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int get_c_ID(String c_img_id) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT c_ID FROM commodity WHERE c_img_id = \"" + c_img_id + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        int result = -1;
        while (resultSet.next()) {
            result = resultSet.getInt(1);
        }
        close(con);
        return result;
    }

    /**
     * This method is to get a saler ID from database table commodity.
     * 
     * @return int u_ID
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int get_u_ID(String c_img_id) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_ID FROM commodity WHERE c_img_id = \"" + c_img_id + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        int result = -1;
        while (resultSet.next()) {
            result = resultSet.getInt(1);
        }
        close(con);
        return result;

    }

    /**
     * This method is to get commodity price from database table commodity.
     * 
     * @return String price
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static double get_c_price(String c_img_id) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT c_price FROM commodity WHERE c_img_id = \"" + c_img_id + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        double result = -1;
        while (resultSet.next()) {
            result = resultSet.getDouble(1);
        }
        close(con);
        return result;
    }

    /**
     * This method is to get commodity number from database table commodity.
     * 
     * @return String price
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int get_c_num(String c_img_id) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT c_num FROM commodity WHERE c_img_id = \"" + c_img_id + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        int result = -1;
        while (resultSet.next()) {
            result = resultSet.getInt(1);
        }
        close(con);
        return result;
    }

    /**
     * This method is to get saler ID from database table commodity.
     * 
     * @return String u_public_key
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_s_public_key(String c_img_id) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_ID FROM commodity WHERE c_img_id = \"" + c_img_id + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        int u_ID = -1;
        while (resultSet.next()) {
            u_ID = resultSet.getInt(1);
        }
        close(con);
        String u_public_key = User.get_u_public_key_by_u_ID(u_ID);
        return u_public_key;
    }

    /**
     * This method is to get commodity name from database table commodity.
     * 
     * @return String c_name
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_c_name(int c_ID) throws SQLException {
        String c_ID_string = String.valueOf(c_ID);
        Connection con = getReadingConnection();
        String select_sql = "SELECT c_name FROM commodity WHERE c_ID = \"" + c_ID_string + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString(1);
        }
        close(con);
        return result;
    }

    /**
     * This method is to update a commodity number from database table commodity.
     * 
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static void update_commodity_num(String c_img_id, String t_num) throws SQLException {
        int current_c_num = Commodity.get_c_num(c_img_id);
        int new_c_num = current_c_num - Integer.parseInt(t_num);
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE commodity SET c_num" + " = " + "\"" + String.valueOf(new_c_num) + "\"" + " WHERE c_img_id" + " = " + "\"" + c_img_id + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
    }

    /**
     * This method is to check the status of the lock
     * 
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String check_lock(String c_img_id) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT c_lock FROM commodity WHERE c_img_id = \"" + c_img_id + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString(1);
        }
        close(con);
        return result;
    }

    /**
     * This method is to add the lock to a commodity
     * 
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static void add_lock(String c_img_id) throws SQLException {
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE commodity SET c_lock = 1 " + " WHERE c_img_id" + " = " + "\"" + c_img_id + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
    }

    /**
     * This method is to release the lock of a commodity
     * 
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static void release_lock(String c_img_id) throws SQLException {
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE commodity SET c_lock = 0 " + " WHERE c_img_id" + " = " + "\"" + c_img_id + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
    }
}
