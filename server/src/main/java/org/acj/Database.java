package org.acj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
  private static final String url = "jdbc:sqlite:server/data/database.db"; 

  private static Connection connection;

  public static void CreateConnection() {
    try {
      Connection conn = null;
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection(url);

      if (conn != null) {
        Database.connection = conn;
        createTable();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void closeConnection() {
    try {
      if (Database.connection != null) {
        Database.connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void testCrudFunctionality() {
    try {
      insertUser("Alice", 30);
      insertUser("Bob", 25);

      System.out.println("Current users in the database:");
      readUsers();

      updateUserAge("Alice", 31);

      System.out.println("Users after update:");
      readUsers();

      deleteUser("Bob");

      System.out.println("Users after deletion:");
      readUsers();
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  private static void createTable() throws Exception {
    if (Database.connection != null) {
      String create_table_query = "CREATE TABLE IF NOT EXISTS users ("
              + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
              + "name TEXT NOT NULL, "
              + "age INTEGER"
              + ");";
      try (Statement statement = Database.connection.createStatement()) {
        statement.execute(create_table_query);
        System.out.println("Table 'users' created successfully!");
      }
    } else {
      System.out.println("Connection has not been created.");
    }
  }

  private static void insertUser(String name, int age) throws Exception {
    if (Database.connection != null) {
      String insert_query = "INSERT INTO users (name, age) VALUES (?, ?)";
      try (PreparedStatement prepared_statement = Database.connection.prepareStatement(insert_query)) {
        prepared_statement.setString(1, name);
        prepared_statement.setInt(2, age);
        prepared_statement.executeUpdate();
        System.out.println("User '" + name + "' added successfully!");
      }
    } else {
      System.out.println("Connection has not been created.");
    }
  }

  private static void readUsers() throws Exception {
    if (Database.connection != null) {
      String select_query = "SELECT * FROM users";
      try (Statement statement = Database.connection.createStatement();
          ResultSet result_set = statement.executeQuery(select_query)) {
        while (result_set.next()) {
          int id = result_set.getInt("id");
          String name = result_set.getString("name");
          int age = result_set.getInt("age");
          System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
        }
      }
    } else {
      System.out.println("Connection has not been created.");
    }
  }

  private static void updateUserAge(String name, int new_age) throws Exception {
    if (Database.connection != null) {
      String update_query = "UPDATE users SET age = ? WHERE name = ?";
      try (PreparedStatement prepared_statement = Database.connection.prepareStatement(update_query)) {
          prepared_statement.setInt(1, new_age);
          prepared_statement.setString(2, name);
          int rows_updated = prepared_statement.executeUpdate();
          if (rows_updated > 0) {
              System.out.println("User '" + name + "' updated successfully!");
          } else {
              System.out.println("User '" + name + "' not found.");
          }
      }
    } else {
      System.out.println("Connection has not been created.");
    }
  }

  private static void deleteUser(String name) throws Exception {
    if (Database.connection != null) {
      String delete_query = "DELETE FROM users WHERE name = ?";
      try (PreparedStatement prepared_statement = Database.connection.prepareStatement(delete_query)) {
          prepared_statement.setString(1, name);
          int rows_deleted = prepared_statement.executeUpdate();
          if (rows_deleted > 0) {
              System.out.println("User '" + name + "' deleted successfully!");
          } else {
              System.out.println("User '" + name + "' not found.");
          }
      }
    } else {
      System.out.println("Connection has not been created.");
    }
  }
}
