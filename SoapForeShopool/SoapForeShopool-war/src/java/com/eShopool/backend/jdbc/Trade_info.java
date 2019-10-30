package com.eShopool.backend.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class consists of a set of basic oprations for database table trade_info,
 * including SELECT, UODATE, INSERT
 *
 * @author Group 10
 * @since 30/10/2019
 */
public class Trade_info extends Eshopool {

    /**
     * This method is to create a record of a tarde to database table trade_info
     *
     * @param String u_public_key, String c_img_id, String t_num
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static int create_trade(String u_public_key, String c_img_id, String t_num) throws SQLException {
        Commodity.update_commodity_num(c_img_id, t_num);
        double t_price = get_t_price(t_num, c_img_id);
        System.out.print(t_price);
        User.change_s_balance(c_img_id, t_price);
        User.change_b_balance(u_public_key, t_price);

        Connection con = getWrittingConnection();

        int b_ID = User.get_u_ID(u_public_key);
        int s_ID = Commodity.get_u_ID(c_img_id);
        int c_ID = Commodity.get_c_ID(c_img_id);

        String insert_sql = "INSERT INTO trade_info (b_ID,  s_ID, c_ID, t_num,t_price, t_date) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.prepareStatement(insert_sql);
        ps.setString(1, String.valueOf(b_ID));
        ps.setString(2, String.valueOf(s_ID));
        ps.setString(3, String.valueOf(c_ID));
        ps.setString(4, t_num);
        ps.setString(5, String.valueOf(t_price));
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ps.setString(6, dateString);

        int res = ps.executeUpdate();
        close(con);
        return res;
    }

    /**
     * This method is to get a total prizer in a record of trade_info
     *
     * @param String t_num, String c_img_id
     * @return double t_price
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static double get_t_price(String t_num, String c_img_id) throws SQLException {
        double c_price = Commodity.get_c_price(c_img_id);
        double t_price = Integer.parseInt(t_num) * c_price;
        return t_price;
    }

    /**
     * This method is to get a recorder of trade of a buyer
     *
     * @param String u_public_key
     * @return String record (s_ID, c_ID, t_num, t_price, t_date FROM
     * trade_info)
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_trade_info_b(String u_public_key) throws SQLException {
        int b_ID = User.get_u_ID(u_public_key);
        String b_ID_string = String.valueOf(b_ID);

        Connection con = getReadingConnection();
        String select_sql = "SELECT s_ID, c_ID, t_num, t_price, t_date FROM trade_info WHERE b_ID = " + b_ID_string + " ORDER BY t_date;";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        String result = "";
        if (resultSet.next()) {
            do {
                int s_ID = resultSet.getInt(1);
                int c_ID = resultSet.getInt(2);
                int t_num = resultSet.getInt(3);
                double t_price = resultSet.getDouble(4);
                String t_date = resultSet.getString(5);
                String s_phone = User.get_u_phone(s_ID);
                String c_name = Commodity.get_c_name(c_ID);
                String s_address = User.get_u_address(s_ID);
                result += "Date: " + t_date + "\n" + "Name: " + c_name + "\n" + "Saler phone: " + s_phone + "\n" + "Amount: " + String.valueOf(t_num) + "\n" + "Total Prics: " + String.valueOf(t_price) + "\n" + "Adress：" + s_address + ";";
            } while (resultSet.next());
        } else {
            return result = ";";
        }
        close(con);
        return result;
    }

    /**
     * This method is to get a recorder of trade of a saler
     *
     * @param String u_public_key
     * @return String record (s_ID, c_ID, t_num, t_price, t_date FROM
     * trade_info)
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static String get_trade_info_s(String u_public_key) throws SQLException {
        int s_ID = User.get_u_ID(u_public_key);
        String s_ID_string = String.valueOf(s_ID);

        Connection con = getReadingConnection();
        String select_sql = "SELECT b_ID, c_ID, t_num, t_price, t_date FROM trade_info WHERE s_ID = " + s_ID_string + " ORDER BY t_date;";
        System.out.println(select_sql);
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(select_sql);
        int rowCount = resultSet.getRow();
        System.out.println(rowCount);
        String result = "";

        if (resultSet.next()) {
            do {
                int b_ID = resultSet.getInt(1);
                int c_ID = resultSet.getInt(2);
                int t_num = resultSet.getInt(3);
                double t_price = resultSet.getDouble(4);
                String t_date = resultSet.getString(5);
                String b_phone = User.get_u_phone(b_ID);
                String c_name = Commodity.get_c_name(c_ID);
                String b_address = User.get_u_address(b_ID);
                result += "Date: " + t_date + "\n" + "Name: " + c_name + "\n" + "Buyer phone: " + b_phone + "\n" + "Amount: " + String.valueOf(t_num) + "\n" + "Total Price: " + String.valueOf(t_price) + "\n" + "Adress: " + b_address + ";";
            } while (resultSet.next());
        } else {
            result = ";";
        }
        close(con);
        return result;
    }

    /**
     * This method is to chesk weather the records of trade_info are unempty or
     * not
     *
     * @param String s_ID_string
     * @return boolean true or false
     * @throws SQLException on input error.
     * @see SQLException
     */
    public static boolean check_trade_info(String s_ID_string) throws SQLException {
        Connection con = getReadingConnection();
        String select_sql = "SELECT COUNT(*) FROM user WHERE s_ID = " + s_ID_string + " ORDER BY t_date;";
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
}
