package app.controllers.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class BookFlightPageController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Map<String, Object>> resultsTableView;

    private static final String[] COLUMN_NAMES = {
        "Numero du vol", "Provenance", "Destination", "Decolage", "Nombre de passager"
    };

    private static final double[] COLUMN_WIDTHS = {
        150, 150, 150, 200, 150
    };

    @FXML
    private void initialize() {
        // Set the column resize policy
        resultsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Initialize columns
        initializeColumns();

        // Load data when the page is displayed
        searchAndDisplayData("");

        // Add listener to searchTextField to detect changes
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchAndDisplayData(newValue));
    }

    private void initializeColumns() {
        // Create and add columns only if not already created
        if (resultsTableView.getColumns().isEmpty()) {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                final String columnName = COLUMN_NAMES[i]; // Effectively final

                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(getCellValue(cellData.getValue(), columnName)));

                // Set column width
                column.setPrefWidth(COLUMN_WIDTHS[i]);

                resultsTableView.getColumns().add(column);
            }
        }
    }

    private Object getCellValue(Map<String, Object> row, String columnName) {
        return row.get(columnName);
    }

    @FXML
    private void search() {
        String searchTerm = searchTextField.getText();
        searchAndDisplayData(searchTerm);
    }

    private void searchAndDisplayData(String searchTerm) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/airport";
        String user = "root";
        String password = "";

        // SQL query with conditions for each column
        String query = "SELECT Pr.NumVol AS 'Numero du vol', V.Origine AS Provenance, V.Destination AS Destination, " +
                      "DATE_FORMAT(V.Decolage, '%Y-%m-%d %H:%i:%s') AS Decolage, COUNT(P.PassNom) AS 'Nombre de passager' " +
                      "FROM prendre AS Pr " +
                      "INNER JOIN vol AS V ON Pr.NumVol = V.NumVol AND V.Decolage >= NOW() " +
                      "INNER JOIN passager AS P ON Pr.NumPssrt = P.NumPssrt " +
                      "WHERE Pr.NumVol LIKE ? OR V.Origine LIKE ? OR V.Destination LIKE ? " +
                      "GROUP BY V.NumVol";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern); // For Pr.NumVol
            stmt.setString(2, searchPattern); // For V.Origine
            stmt.setString(3, searchPattern); // For V.Destination

            ResultSet rs = stmt.executeQuery();
            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

            // Update TableView with results
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= COLUMN_NAMES.length; i++) {
                    row.put(COLUMN_NAMES[i - 1], rs.getObject(i));
                }
                data.add(row);
                System.out.println("Retrieved record: " + row);  // Debug print
            }

            resultsTableView.setItems(data);
            System.out.println("TableView updated with " + data.size() + " records.");  // Debug print

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bookFlight(ActionEvent event) {
        try {
            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/views/windows/bookFlight.fxml"));

            // Create a new Stage (window)
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Réserver un vol");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
