package app.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
  private static final String URL = "jdbc:mysql://localhost:3306/airport";
  private static final String USER = "root";
  private static final String PASSWORD = "";

  // Load the MySQL JDBC driver
  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("MySQL JDBC Driver not found.");
      e.printStackTrace();
    }
  }

  // Method to establish a database connection
  public static Connection getConnection() {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("Database connected successfully!");
    } catch (SQLException e) {
      System.out.println("Connection failed.");
      e.printStackTrace();
    }
    return connection;
  }

  // Method to close a database connection
  public static void closeConnection(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
        System.out.println("Database connection closed.");
      } catch (SQLException e) {
        System.out.println("Failed to close the connection.");
        e.printStackTrace();
      }
    }
  }
}
