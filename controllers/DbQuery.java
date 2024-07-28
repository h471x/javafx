package app.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbQuery {

    private Connection connection;

    public DbQuery(Connection connection) {
        this.connection = connection;
    }

    public void executeSqlFile(String filePath) {
        StringBuilder sql = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Statement stmt = connection.createStatement()) {

            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }

            // Split the SQL script into individual commands
            String[] sqlCommands = sql.toString().split(";");

            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command.trim());
                }
            }

            System.out.println("SQL file executed successfully.");

        } catch (IOException | SQLException e) {
            System.out.println("Failed to execute SQL file.");
            e.printStackTrace();
        }
    }
}
