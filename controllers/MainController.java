package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
  @FXML
  private Label label;

  @FXML
  public void initialize() {
    label.setText("Hello, JavaFX!");
  }
}
