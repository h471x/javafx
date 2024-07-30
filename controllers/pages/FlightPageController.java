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

public class FlightPageController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Map<String, Object>> flightsTableView;

    private static final String[] COLUMN_NAMES = {
        "Numero", "Provenance", "Destination", "Avion", "Decolage"
    };

    private static final double[] COLUMN_WIDTHS = {
        150, 150, 150, 150, 200
    };

    @FXML
    private void initialize() {
        // Set the column resize policy
        flightsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Initialize columns
        initializeColumns();

        // Load data when the page is displayed
        searchAndDisplayFlights("");

        // Add listener to searchTextField to detect changes
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchAndDisplayFlights(newValue));
    }

    private void initializeColumns() {
        // Create and add columns only if not already created
        if (flightsTableView.getColumns().isEmpty()) {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                final String columnName = COLUMN_NAMES[i];

                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(columnName)));

                // Set column width
                column.setPrefWidth(COLUMN_WIDTHS[i]);

                flightsTableView.getColumns().add(column);
            }
        }
    }

    @FXML
    private void search() {
        String searchTerm = searchTextField.getText();
        searchAndDisplayFlights(searchTerm);
    }

    
    private void searchAndDisplayFlights(String searchTerm) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/airport";
        String user = "root";
        String password = "";

        // SQL query with conditions for each column
        String query = "SELECT NumVol AS Numero, Origine AS Provenance, Destination AS Destination, " +
                      "Avion AS Avion, DATE_FORMAT(Decolage, '%Y-%m-%d %H:%i:%s') AS Decolage " +
                      "FROM vol WHERE Decolage >= NOW() " +
                      "AND (NumVol LIKE ? OR Origine LIKE ? OR Destination LIKE ? OR Avion LIKE ?) " +
                      "ORDER BY Decolage ASC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern); // For NumVol
            stmt.setString(2, searchPattern); // For Origine
            stmt.setString(3, searchPattern); // For Destination
            stmt.setString(4, searchPattern); // For Avion

            ResultSet rs = stmt.executeQuery();
            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (String columnName : COLUMN_NAMES) {
                    row.put(columnName, rs.getObject(columnName));
                }
                data.add(row);
                System.out.println("Retrieved record: " + row);  // Debug print
            }

            // Update TableView with results
            flightsTableView.setItems(data);
            System.out.println("TableView updated with " + data.size() + " records.");  // Debug print

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addVols(ActionEvent event) {
        try {
            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/views/windows/addFlight.fxml"));

            // Create a new Stage (window)
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Ajouter un vol");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
