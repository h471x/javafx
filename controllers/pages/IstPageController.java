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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.property.SimpleObjectProperty;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
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

    // Column names and widths
    private static final String[] COLUMN_NAMES = {
        "Passeport", "Nom", "Prenom", "Date de naissance"
    };

    private static final double[] COLUMN_WIDTHS = {
        150, 150, 150, 200
    };

    private ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set the column resize policy
        istTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Initialize columns
        initializeColumns();

        // Load data when the page is displayed
        searchAndDisplayIst("");

        // Add listener to searchTextField to detect changes
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterData(newValue));
    }

    private void initializeColumns() {
        // Create and add columns only if not already created
        if (istTableView.getColumns().isEmpty()) {
            for (int i = 0; i < COLUMN_NAMES.length; i++) {
                final String columnName = COLUMN_NAMES[i]; // Effectively final

                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(getCellValue(cellData.getValue(), columnName)));

                // Set column width
                column.setPrefWidth(COLUMN_WIDTHS[i]);

                istTableView.getColumns().add(column);
            }
        }
    }

    private Object getCellValue(Map<String, Object> row, String columnName) {
        return row.get(columnName);
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
                       "FROM passager WHERE Ist=TRUE AND (NumPssrt LIKE ? OR PassNom LIKE ? OR PassPrenom LIKE ?) " +
                       "ORDER BY PassNom";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern); // For NumPssrt
            stmt.setString(2, searchPattern); // For PassNom
            stmt.setString(3, searchPattern); // For PassPrenom

            ResultSet rs = stmt.executeQuery();
            data.clear(); // Clear previous data

            // Update TableView with results
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= COLUMN_NAMES.length; i++) {
                    row.put(COLUMN_NAMES[i - 1], rs.getObject(i));
                }
                data.add(row);
                System.out.println("Retrieved record: " + row);  // Debug print
            }

            istTableView.setItems(data);
            System.out.println("TableView updated with " + data.size() + " records.");  // Debug print

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterData(String searchTerm) {
        // Filter data based on the search term
        ObservableList<Map<String, Object>> filteredData = FXCollections.observableArrayList();
        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        for (Map<String, Object> row : data) {
            boolean matches = row.values().stream()
                .anyMatch(value -> value != null && value.toString().toLowerCase().contains(lowerCaseSearchTerm));

            if (matches) {
                filteredData.add(row);
            }
        }

        // Update TableView with filtered data
        istTableView.setItems(filteredData);
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

    @FXML
    public void addIstExcel(ActionEvent event) {
        // Open a file chooser dialog to select the Excel file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {

                // Get the first sheet
                Sheet sheet = workbook.getSheetAt(0);
                if (sheet == null) {
                    System.err.println("The selected Excel file does not contain any sheets.");
                    return;
                }

                // Print header row
                Row headerRow = sheet.getRow(0);
                if (headerRow != null) {
                    System.out.print("Headers: ");
                    for (Cell cell : headerRow) {
                        System.out.print((cell != null ? cell.toString() : "Empty") + "\t");
                    }
                    System.out.println();
                } else {
                    System.err.println("The first row (header) is missing in the Excel file.");
                }

                // Iterate through rows and cells to print data
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        System.out.print("Row " + i + ": ");
                        for (Cell cell : row) {
                            if (cell != null) {
                                switch (cell.getCellType()) {
                                    case STRING:
                                        System.out.print(cell.getStringCellValue() + "\t");
                                        break;
                                    case NUMERIC:
                                        System.out.print(cell.getNumericCellValue() + "\t");
                                        break;
                                    case BOOLEAN:
                                        System.out.print(cell.getBooleanCellValue() + "\t");
                                        break;
                                    case FORMULA:
                                        System.out.print(cell.getCellFormula() + "\t");
                                        break;
                                    default:
                                        System.out.print("Unknown Type\t");
                                        break;
                                }
                            } else {
                                System.out.print("Empty\t");
                            }
                        }
                        System.out.println();
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading the Excel file: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
    }
}
