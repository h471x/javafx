package app.controllers.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class IstPageController {
  public void addIst(ActionEvent event) {
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

