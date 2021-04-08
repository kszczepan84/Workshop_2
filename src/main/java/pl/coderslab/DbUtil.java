package pl.coderslab;

import java.sql.*;

public class DbUtil {
    public static final  String DB_NAME = "workshop2";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME+"?useSSL=false&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}


