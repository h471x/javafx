package app.controllers.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookFlightPageController {
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
