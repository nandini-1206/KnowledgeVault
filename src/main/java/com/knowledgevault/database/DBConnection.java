
package com.knowledgevault.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/KnowledgeVault";

    private static final String USER =
            System.getenv("KV_DB_USER");

    private static final String PASSWORD =
            System.getenv("KV_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }
}
