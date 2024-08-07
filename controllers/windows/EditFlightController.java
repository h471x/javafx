package app.controllers.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EditFlightController {
    @FXML
    private ComboBox<String> flightNumComboBox;

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
        // Populate ComboBox with flight numbers
        populateFlightNumComboBox();
    }

    private void populateFlightNumComboBox() {
        String sql = "SELECT NumVol FROM vol WHERE Decolage >= NOW()";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            List<String> flightNumbers = new ArrayList<>();
            while (resultSet.next()) {
                flightNumbers.add(resultSet.getString("NumVol"));
            }

            flightNumComboBox.getItems().addAll(flightNumbers);

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Erreur lors du chargement des numéros de vol.");
        }
    }

    @FXML
    private void handleAddButtonAction() {
        // Retrieve input values
        String flightNum = flightNumComboBox.getValue();
        String nom = nomField.getText();
        String prenom = prenomField.getText();

        // Validate input (e.g., check if fields are empty)
        if (flightNum == null || nom.isEmpty() || prenom.isEmpty()) {
            statusLabel.setText("Tous les champs doivent être remplis.");
            return;
        }

        // Check if passenger exists and insert if valid
        String statusMessage = checkAndInsertPassenger(flightNum, nom, prenom);
        statusLabel.setText(statusMessage);
    }

    private String checkAndInsertPassenger(String flightNum, String nom, String prenom) {
        String checkPassengerSql = "SELECT NumPssrt FROM passager WHERE PassNom = ? AND PassPrenom = ?";
        String insertPassengerSql = "INSERT INTO prendre (NumPssrt, NumVol) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // Check if the passenger exists
            try (PreparedStatement checkStatement = connection.prepareStatement(checkPassengerSql)) {
                checkStatement.setString(1, nom);
                checkStatement.setString(2, prenom);

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        return "Le passager n'existe pas dans la base de données.";
                    }
                }
            }

            // Get the passenger ID
            int numPssrt;
            try (PreparedStatement checkStatement = connection.prepareStatement(checkPassengerSql)) {
                checkStatement.setString(1, nom);
                checkStatement.setString(2, prenom);

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        numPssrt = resultSet.getInt("NumPssrt");
                    } else {
                        return "Erreur lors de la récupération de l'identifiant du passager.";
                    }
                }
            }

            // Insert the passenger into the flight
            try (PreparedStatement insertStatement = connection.prepareStatement(insertPassengerSql)) {
                insertStatement.setInt(1, numPssrt);
                insertStatement.setString(2, flightNum);

                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    return "Passager ajouté avec succès!";
                } else {
                    return "Échec de l'ajout du passager.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Échec de l'ajout du passager.";
        }
    }
}
