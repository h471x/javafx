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
            Parent root = FXMLLoader.load(getClass().getResource("/views/add_passenger.fxml"));
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
        createDatabaseSchema();
        launch(args);
    }

    private static Connection initializeDatabase() {
        String url = "jdbc:mysql://localhost:3306/";
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

    private static void createDatabaseSchema() {
        String sqlScriptPath = "config/database/database.sql";

        try (BufferedReader br = new BufferedReader(new FileReader(sqlScriptPath));
             Statement stmt = connection.createStatement()) {

            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }

            String[] sqlCommands = sql.toString().split(";");

            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command.trim());
                }
            }

            System.out.println("Database schema created successfully.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
