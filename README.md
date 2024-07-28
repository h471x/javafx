### Compile

```
javac --module-path libs\javafx --add-modules javafx.controls,javafx.fxml -d . *.java```

### Run

```
cmd.exe /c "java --module-path libs\javafx\javafx-base-win.jar;libs\javafx\javafx-controls-win.jar;libs\javafx\javafx-fxml-win.jar;libs\javafx\javafx-graphics-win.jar;libs\formsfx\formsfx-core.jar --add-modules javafx.controls,javafx.fxml,javafx.graphics Main"
```

Or For Short

```
cmd.exe /c "java --module-path libs\javafx --add-modules javafx.controls Main.java"
```
