package com.bugtracker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/game_issue_tracker?serverTimezone=UTC";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String user = System.getenv("GAME_TRACKER_DB_USER");
        String password = System.getenv("GAME_TRACKER_DB_PASSWORD");

        if (user == null || user.isBlank() || password == null) {
            throw new SQLException(
                    "Set GAME_TRACKER_DB_USER and GAME_TRACKER_DB_PASSWORD before connecting.");
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver is not available to this web app.", e);
        }

        return DriverManager.getConnection(URL, user, password);
    }
}