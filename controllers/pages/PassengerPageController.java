package app.controllers.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PassengerPageController {
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
