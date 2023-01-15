/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package UAS_PBO_21410100039;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aditya Kesuma
 */
public class Koneksi {

    static boolean connectStatus = false;
    static Connection conn;



    public static Connection getConn() {
        if (conn == null) {
            try {
                String url = "jdbc:mysql://localhost/uaspbo"; // Url DB
                String user = "root"; // Username
                String pass = "";
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                conn = DriverManager.getConnection(url, user, pass);
//                Login.connectStatus.setText("Connected");
                connectStatus = true;
                System.out.println("Success");
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        }
        return conn;
    }

    public static void main(String[] args) {
        getConn();

    }

}
