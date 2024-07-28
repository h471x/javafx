package app.controllers.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PassengerController {
    @FXML
    private TextField passportNumField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField birthField;

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
        String passportNum = passportNumField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String birth = birthField.getText();

        // Validate input (e.g., check if fields are empty)
        if (passportNum.isEmpty() || nom.isEmpty() || prenom.isEmpty() || birth.isEmpty()) {
            statusLabel.setText("Tous les champs doivent être remplis.");
            return;
        }

        // Insert passenger details into the database
        String statusMessage = insertPassenger(passportNum, nom, prenom, birth);
        statusLabel.setText(statusMessage);
    }

    private String insertPassenger(String passportNum, String nom, String prenom, String birth) {
        String sql = "INSERT INTO passager (NumPssrt, PassNom, PassPrenom, Naissance) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            statement.setString(1, passportNum);
            statement.setString(2, nom);
            statement.setString(3, prenom);
            statement.setString(4, birth);

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
