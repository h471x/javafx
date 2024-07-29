package app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class DashboardController {

  @FXML
  private Pane contentPane;

  @FXML
  private void initialize() {
    showReservationPage();
  }

  @FXML
  private void showReservationPage() {
    loadPage("pageReservations.fxml");
  }

  @FXML
  private void showVolsPage() {
    loadPage("pageVols.fxml");
  }

  @FXML
  private void showPassagersPage() {
    loadPage("pagePassagers.fxml");
  }

  @FXML
  private void showAdminsPage() {
    loadPage("pageAdmins.fxml");
  }

  private void loadPage(String fxmlFile) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/pages/" + fxmlFile));
      Parent newPage = loader.load();
      contentPane.getChildren().clear();
      contentPane.getChildren().add(newPage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
