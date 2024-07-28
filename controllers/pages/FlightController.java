package app.controllers.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FlightController {
    @FXML
    private TextField departureField;

    @FXML
    private TextField destinationField;

    @FXML
    private TextField airplaneField;

    @FXML
    private TextField departureTimeField;

    @FXML
    private Button addButton;

    @FXML
    private Label statusLabel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/airport";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private void initialize() {
        // Initialization logic (if any)
    }

    @FXML
    private void handleAddButtonAction() {
        // Retrieve input values
        String departure = departureField.getText();
        String destination = destinationField.getText();
        String airplane = airplaneField.getText();
        String departureTime = departureTimeField.getText();

        // Validate input (e.g., check if fields are empty)
        if (departure.isEmpty() || destination.isEmpty() || airplane.isEmpty() || departureTime.isEmpty()) {
            statusLabel.setText("Tous les champs doivent être remplis.");
            return;
        }

        // Insert flight details into the database and update status label
        String statusMessage = insertFlight(departure, destination, airplane, departureTime);
        statusLabel.setText(statusMessage);
    }

    private String insertFlight(String departure, String destination, String airplane, String departureTime) {
        String sql = "INSERT IGNORE INTO vol (origine, destination, avion, decolage) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the SQL query
            pstmt.setString(1, departure);
            pstmt.setString(2, destination);
            pstmt.setString(3, airplane);
            pstmt.setString(4, departureTime);

            // Execute the query
            int rowsAffected = pstmt.executeUpdate();

            // Check if insertion was successful
            if (rowsAffected > 0) {
                return "Vol ajouté avec succès!";
            } else {
                return "Échec de l'ajout du vol.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Échec de l'ajout du vol.";
        }
    }
}
