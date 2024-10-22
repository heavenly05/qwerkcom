package org.acj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");


            // Path to the local SQLite database file inside the data directory
            String url = "jdbc:sqlite:server/data/database.db"; 
            connection = DriverManager.getConnection(url);
            
            if (connection != null) {
                System.out.println("Successfully connected to SQLite database at: " + url);

                // Create a new table (if it doesn't already exist)
                String createTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT NOT NULL, "
                        + "age INTEGER"
                        + ");";
                Statement statement = connection.createStatement();
                statement.execute(createTableQuery);
                System.out.println("Table 'users' created successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
