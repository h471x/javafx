package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
  private static Connection connection;

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/views/bookFlight.fxml"));
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Ajouter un passager");
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    // Close the database connection when the application is stopped
    closeDatabaseConnection();
  }

  public static void main(String[] args) {
    connection = initializeDatabase();
    if (connection != null) {
      createDatabaseSchema("config/database/database.sql");
      createDatabaseSchema("config/database/tables.sql");
      // createDatabaseSchema("config/database/seed.sql");
    } else {
      System.out.println("Database connection initialization failed.");
    }
    launch(args);
  }

  private static Connection initializeDatabase() {
    String url = "jdbc:mysql://localhost:3306/airport";
    String user = "root";
    String password = "";

    Connection connection = null;

    try {
      // Load the MySQL JDBC driver
      Class.forName("com.mysql.cj.jdbc.Driver");

      // Establish the connection
      connection = DriverManager.getConnection(url, user, password);
      System.out.println("Database connected successfully!");
    } catch (ClassNotFoundException e) {
      System.out.println("MySQL JDBC Driver not found.");
      e.printStackTrace();
    } catch (SQLException e) {
      System.out.println("Connection failed.");
      e.printStackTrace();
    }

    return connection;
  }

  private static void closeDatabaseConnection() {
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

  private static void createDatabaseSchema(String sqlScriptPath) {
    try (BufferedReader br = new BufferedReader(new FileReader(sqlScriptPath));
      Statement stmt = connection.createStatement()) {

      String line;
      StringBuilder sql = new StringBuilder();

      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("--") || line.startsWith("/*") || line.endsWith("*/") || line.isEmpty()) {
          // Skip comment lines and empty lines
          continue;
        }
        sql.append(line).append(" ");
        // If the line contains a semicolon, it's the end of an SQL statement
        if (line.endsWith(";")) {
          String command = sql.toString().trim();
          if (!command.isEmpty()) {
            try {
              // System.out.println("Executing: " + command);
              stmt.execute(command);
            } catch (SQLException e) {
              System.out.println("Error executing: " + command);
              System.out.println("Error message: " + e.getMessage());
              e.printStackTrace();
            }
            sql.setLength(0); // Reset the StringBuilder for the next statement
          }
        }
      }
      System.out.println("Database schema created from " + sqlScriptPath + " successfully.");
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }
}
