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

public class BookFlightPageController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Map<String, Object>> resultsTableView;

    @FXML
    private void initialize() {
        // Set the column resize policy to remove the extra empty column
        resultsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load data when the page is displayed
        searchAndDisplayData("");
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

        // Adjust the query based on whether the searchTerm is empty
        String query = "SELECT Pr.NumVol AS 'Numero du vol', V.Origine AS Provenance, V.Destination AS Destination, " +
                       "V.Decolage AS Decolage, COUNT(P.PassNom) AS 'Nombre de passager' " +
                       "FROM prendre AS Pr " +
                       "INNER JOIN vol AS V ON Pr.NumVol=V.NumVol AND V.Decolage >=NOW()" +
                       "INNER JOIN passager AS P ON Pr.NumPssrt=P.NumPssrt " +
                       "GROUP BY V.NumVol";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

            // Clear any existing columns
            resultsTableView.getColumns().clear();

            // Get metadata to dynamically create columns
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                final String columnName = metaData.getColumnLabel(i);  // Use getColumnLabel to get the alias name
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(cellData -> {
                    Map<String, Object> row = cellData.getValue();
                    return new SimpleObjectProperty<>(row.get(columnName));
                });
                resultsTableView.getColumns().add(column);
            }

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnLabel(i), rs.getObject(i));
                }
                data.add(row);
                System.out.println("Retrieved record: " + row);  // Debug print
            }

            // Update TableView with results
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
