package com.ngocrong;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/nr_kame";
        String username = "root";
        String password = ""; // Điền password nếu có

        try {
            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connection successful!");
            connection.close();
        } catch (SQLException e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
