#!/bin/bash

# Set the JavaFX and MySQL Connector library paths
JAVAFX_PATH="libs/javafx/linux"
DBCONNECTOR_PATH="libs/dbconnector/mysql-connector.jar"
MODULES="javafx.controls,javafx.fxml"

# Initialize an empty variable to collect all .java files
javac_command=""

# Collect and compile all .java files found recursively
find . -name "*.java" | while read -r file; do
	echo "Compiling $(basename "$file")..."
	javac --module-path "$JAVAFX_PATH" --add-modules "$MODULES" -cp "$DBCONNECTOR_PATH" -d . "$file"
	if [ $? -ne 0 ]; then
		echo "Compilation failed for $(basename "$file")."
		exit 1
	fi
done

# Run the Main class
echo "Running..."
java --module-path "$JAVAFX_PATH" --add-modules "$MODULES" -cp ".:$DBCONNECTOR_PATH" app.Main
