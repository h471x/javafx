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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PassengerPageController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Map<String, Object>> passengersTableView;

    // Column names and widths
    private static final String[] COLUMN_NAMES = {
        "Passeport", "Nom", "Prenom", "Date de naissance"
    };

    private static final double[] COLUMN_WIDTHS = {
        150, 150, 150, 200
    };

    @FXML
    private void initialize() {
        // Set the column resize policy
        passengersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Initialize columns
        initializeColumns();

        // Load data when the page is displayed
        searchAndDisplayPassengers("");

        // Add listener to searchTextField to detect changes
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchAndDisplayPassengers(newValue));
    }

    private void initializeColumns() {
        // Create and add columns only if not already created
        if (passengersTableView.getColumns().isEmpty()) {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                final String columnName = COLUMN_NAMES[i]; // Effectively final

                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(getCellValue(cellData.getValue(), columnName)));

                // Set column width
                column.setPrefWidth(COLUMN_WIDTHS[i]);

                passengersTableView.getColumns().add(column);
            }
        }
    }

    private Object getCellValue(Map<String, Object> row, String columnName) {
        return row.get(columnName);
    }

    @FXML
    private void search() {
        String searchTerm = searchTextField.getText();
        searchAndDisplayPassengers(searchTerm);
    }

    private void searchAndDisplayPassengers(String searchTerm) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/airport";
        String user = "root";
        String password = "";

        // Query to fetch data based on the search term
        String query = "SELECT NumPssrt AS Passeport, PassNom AS Nom, PassPrenom AS Prenom, Naissance AS `Date de naissance` " +
                       "FROM passager WHERE Notam=FALSE AND Ist=FALSE " +
                       "AND (NumPssrt LIKE ? OR PassNom LIKE ? OR PassPrenom LIKE ?) " +
                       "ORDER BY PassNom";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern); // For NumPssrt
            stmt.setString(2, searchPattern); // For PassNom
            stmt.setString(3, searchPattern); // For PassPrenom

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

            passengersTableView.setItems(data);
            System.out.println("TableView updated with " + data.size() + " records.");  // Debug print

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addPassenger(ActionEvent event) {
        try {
            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/views/windows/addPassenger.fxml"));

            // Create a new Stage (window)
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Ajouter un passager");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
