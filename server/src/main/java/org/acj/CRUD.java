package org.acj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class CRUD {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:server/data/database.db"; 

        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                System.out.println("Successfully connected to SQLite database at: " + url);

                // Create a new table if it doesn't already exist
                createTable(connection);

                // Create (Insert)
                insertUser(connection, "Alice", 30);
                insertUser(connection, "Bob", 25);

                // Read (Select)
                System.out.println("Current users in the database:");
                readUsers(connection);

                // Update
                updateUserAge(connection, "Alice", 31);

                // Read (Select) after update
                System.out.println("Users after update:");
                readUsers(connection);

                // Delete
                deleteUser(connection, "Bob");

                // Read (Select) after delete
                System.out.println("Users after deletion:");
                readUsers(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws Exception {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "age INTEGER"
                + ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            System.out.println("Table 'users' created successfully!");
        }
    }

    private static void insertUser(Connection connection, String name, int age) throws Exception {
        String insertQuery = "INSERT INTO users (name, age) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.executeUpdate();
            System.out.println("User '" + name + "' added successfully!");
        }
    }

    private static void readUsers(Connection connection) throws Exception {
        String selectQuery = "SELECT * FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
            }
        }
    }

    private static void updateUserAge(Connection connection, String name, int newAge) throws Exception {
        String updateQuery = "UPDATE users SET age = ? WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, newAge);
            preparedStatement.setString(2, name);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User '" + name + "' updated successfully!");
            } else {
                System.out.println("User '" + name + "' not found.");
            }
        }
    }

    private static void deleteUser(Connection connection, String name) throws Exception {
        String deleteQuery = "DELETE FROM users WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, name);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User '" + name + "' deleted successfully!");
            } else {
                System.out.println("User '" + name + "' not found.");
            }
        }
    }
}
