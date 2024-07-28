package app.models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard {
  public void show(Stage primaryStage) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/views/dashboard.fxml"));
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Dashboard");
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
