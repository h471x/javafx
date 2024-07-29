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

public class IstPageController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Map<String, Object>> istTableView;

    @FXML
    private void initialize() {
        // Set the column resize policy to remove the extra empty column
        istTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load data when the page is displayed
        searchAndDisplayIst("");
    }

    @FXML
    private void search() {
        String searchTerm = searchTextField.getText();
        searchAndDisplayIst(searchTerm);
    }

    private void searchAndDisplayIst(String searchTerm) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/airport";
        String user = "root";
        String password = "";

        // Query to fetch data based on the search term
        String query = "SELECT NumPssrt AS Passeport, PassNom AS Nom, PassPrenom AS Prenom, Naissance AS `Date de naissance` " +
                       "FROM passager WHERE Ist=TRUE ORDER BY PassNom";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

            // Clear any existing columns
            istTableView.getColumns().clear();

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
                istTableView.getColumns().add(column);
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
            istTableView.setItems(data);
            System.out.println("TableView updated with " + data.size() + " records.");  // Debug print

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addIst(ActionEvent event) {
        try {
            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/views/windows/addIst.fxml"));

            // Create a new Stage (window)
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Ajouter un IST");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
