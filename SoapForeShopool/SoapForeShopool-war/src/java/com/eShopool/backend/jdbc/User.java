package com.eShopool.backend.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class consists of a set of basic oprations for database table user,
 * including SELECT, UODATE, INSERT
 *
 * @author Group 10
 * @since 30/10/2019
 */
public class User extends Eshopool {

    /**
     * This method is to get u_public from database table user.
     *
     * @param String u_phone
     * @return String u_public
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_u_public(String u_phone) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_public_key FROM user WHERE u_phone = \"" + u_phone + "\";";
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
     * This method is to get u_public according to u_ID from database table user.
     *
     * @param String u_phone
     * @return String u_public
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_u_public_key_by_u_ID(int u_ID) throws SQLException {
        String u_ID_string = String.valueOf(u_ID);
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_public_key FROM user WHERE u_ID = \"" + u_ID_string + "\";";
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
     * This method is to get the private key from database table user.
     *
     * @param String u_public_key
     * @return String u_privte_key
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_private_key(String u_public_key) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_private_key FROM user WHERE u_public_key = \"" + u_public_key + "\";";
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
     * This method is to get phone from database table user.
     *
     * @param String u_public_key
     * @return String u_phone
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_phone(String u_public_key) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_phone FROM user WHERE u_public_key = \"" + u_public_key + "\";";
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
     * This method is to get u_ID from database table user.
     *
     * @param String u_public_key
     * @return int u_ID
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int get_u_ID(String u_public_key) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_ID FROM user WHERE u_public_key = \"" + u_public_key + "\";";
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
     * This method is to get u_password from database table user.
     *
     * @param String u_public_key
     * @return String u_password
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_password(String u_public_key) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_password FROM user WHERE u_public_key = \"" + u_public_key + "\";";
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
     * This method is to get user_basic_infor from database table user.
     *
     * @param String u_phone
     * @return String result includes u_address & u_language_preference
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_user_basic_infor(String u_phone) throws SQLException, ClassNotFoundException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_address, u_language_preference FROM user WHERE u_phone = \"" + u_phone + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString(1);
            result += ", " + resultSet.getString(2);
        }
        close(con);
        return result;
    }

    /**
     * This method is to get u_balance from database table user according u_ID.
     *
     * @param int u_ID
     * @return String u_balance
     * @throws SQLException on input error.
     * @see SQLException
     */
    private static double get_u_balance_by_u_ID(int u_ID) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_balance FROM user WHERE u_ID = \"" + u_ID + "\";";
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
     * This method is to get u_balance from database table user according to u_publi_key.
     *
     * @param String u_public_key
     * @return String u_balance
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static double get_u_balance_by_public_key(String u_public_key) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_balance FROM user WHERE u_public_key = \"" + u_public_key + "\";";
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
     * This method is to get u_address from database table user according to u_ID.
     *
     * @param int u_ID
     * @return String u_address
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_u_address(int u_ID) throws SQLException {
        String u_ID_string = String.valueOf(u_ID);

        Connection con = getReadingConnection();
        String select_sql = "SELECT u_address FROM user WHERE u_ID = \"" + u_ID_string + "\";";
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
     * This method is to get u_address from database table user according to u_publi_key.
     *
     * @param String u_public_key
     * @return String u_address
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_u_address_by_public_key(String u_public_key) throws SQLException {


        Connection con = getReadingConnection();
        String select_sql = "SELECT u_address FROM user WHERE u_public_key = \"" + u_public_key + "\";";
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
     * This method is to get u_phone from database table user according to u_publi_key.
     *
     * @param int u_ID
     * @return String u_phone
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_u_phone(int u_ID) throws SQLException {
        String u_ID_string = String.valueOf(u_ID);
        Connection con = getReadingConnection();
        String select_sql = "SELECT u_phone FROM user WHERE u_ID = \"" + u_ID_string + "\";";
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
     * This method is to get create an acount. INSERT information into database table user
     *
     * @param String u_phone, String u_public_key, String u_private_key
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static void create_account(String u_phone, String u_public_key, String u_private_key) throws SQLException {
        Connection con = getWrittingConnection();
        String insert_sql = "INSERT INTO user(u_phone, u_public_key, u_private_key, u_balance,  u_address, u_password) VALUES (?, ?, ?, 100, \" \", \"\");";
        PreparedStatement ps = con.prepareStatement(insert_sql);
        ps.setString(1, u_phone);
        ps.setString(2, u_public_key);
        ps.setString(3, u_private_key);
        int res = ps.executeUpdate();
        close(con);
    }

    /**
     * This method is to add deposit to an account. UPDATE the balance of an account
     *
     * @param String u_public_key, double u_deposit
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int update_u_balance(String u_public_key, double u_deposit) throws SQLException {
        double current_u_balance = User.get_u_balance_by_public_key(u_public_key);
        double new_u_balance = current_u_balance + u_deposit;
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE user SET u_balance = " + "\"" + String.valueOf(new_u_balance) + "\"" + " WHERE u_public_key" + " = " + "\"" + u_public_key + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
        return res;
    }

    /**
     * This method is to change address of an account. UPDATE the address of an account
     *
     * @param String u_public_key, String new_u_address
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int update_u_address(String u_public_key, String new_u_address) throws SQLException {
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE user SET u_address" + " = " + "\"" + new_u_address + "\"" + " WHERE u_public_key" + " = " + "\"" + u_public_key + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
        return res;
    }

    /**
     * This method is to change password of an account. UPDATE the password of an account
     *
     * @param String u_public_key, String new_u_password
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int update_u_password(String u_public_key, String new_u_password) throws SQLException {
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE user SET u_password" + " = " + "\"" + new_u_password + "\"" + " WHERE u_public_key" + " = " + "\"" + u_public_key + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
        return res;
    }


    /**
     * This method is to change the balance of a sale account. UPDATE the balance of a sale account
     *
     * @param String c_img_id, double t_price
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static void change_s_balance(String c_img_id, double t_price) throws SQLException {
        int s_ID = Commodity.get_u_ID(c_img_id);
        double current_u_balance = User.get_u_balance_by_u_ID(s_ID);
        double new_u_balance = current_u_balance + t_price;
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE user SET u_balance" + " = " + "\"" + String.valueOf(new_u_balance) + "\"" + " WHERE u_ID" + " = " + "\"" + s_ID + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
    }

    /**
     * This method is to change the balance of a buyer account. UPDATE the balance of a buyer account
     *
     * @param String u_public_key, double t_price
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static void change_b_balance(String u_public_key, double t_price) throws SQLException {
        double current_u_balance = User.get_u_balance_by_public_key(u_public_key);
        double new_u_balance = current_u_balance - t_price;
        Connection con = getWrittingConnection();
        String update_sql = "UPDATE user SET u_balance = " + "\"" + String.valueOf(new_u_balance) + "\"" + " WHERE u_public_key" + " = " + "\"" + u_public_key + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
    }

    /**
     * This method is to check the existence of public key
     *
     * @param String u_public_key
     * @return boolean: true or false
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static boolean check_u_public_key(String u_public_key) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT COUNT(*) FROM user WHERE u_public_key = \"" + u_public_key + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        int result = -1;
        while (resultSet.next()) {
            result = resultSet.getInt(1);
        }
        close(con);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method is to check the existence of phone
     *
     * @param String u_phone
     * @return boolean: true or false
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static boolean check_u_phone(String u_phone) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT COUNT(*) FROM user WHERE u_phone = \"" + u_phone + "\";";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        int result = -1;
        while (resultSet.next()) {
            result = resultSet.getInt(1);
        }
        close(con);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * This method is to get u_public from database table user.
     * 
     * @param String u_public_key
     * @return int flag
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int drop_account(String u_phone) throws SQLException {
        Connection con = getWrittingConnection();
        String new_phone = u_phone.substring(1);
        String update_sql = "UPDATE user SET u_phone = \"" + new_phone + "\",  u_password = \"\" WHERE u_phone" + " = " + "\"" + u_phone + "\"" + ";";
        System.out.println(update_sql);
        PreparedStatement ps = con.prepareStatement(update_sql);
        int res = ps.executeUpdate();
        close(con);
        return res;
    }

}