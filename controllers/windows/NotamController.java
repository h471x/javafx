package app.controllers.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotamController {
    @FXML
    private TextField flightNumField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

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
        String flightNum = flightNumField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();

        // Validate input (e.g., check if fields are empty)
        if (flightNum.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
            statusLabel.setText("Tous les champs doivent être remplis.");
            return;
        }

        // Insert passenger details into the database
        String statusMessage = insertPassenger(flightNum, nom, prenom);
        statusLabel.setText(statusMessage);
    }

    private String insertPassenger(String flightNum, String nom, String prenom) {
        String sql = "INSERT INTO prendre (NumPssrt, NumVol) VALUES (" +
                     "(SELECT NumPssrt FROM passager WHERE PassNom = ? AND PassPrenom = ?), " +
                     "(SELECT NumVol FROM vol WHERE NumVol = ?))";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, flightNum);

            // Execute the query
            int rowsInserted = statement.executeUpdate();

            // Check if insertion was successful
            if (rowsInserted > 0) {
                return "Passager ajouté avec succès!";
            } else {
                return "Échec de l'ajout du passager.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Échec de l'ajout du passager.";
        }
    }
}
