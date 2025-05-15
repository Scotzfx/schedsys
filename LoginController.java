package Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Hyperlink loginLink;

    // Your DB connection method
    private Connection getConnection() throws SQLException {
        return DataBaseConnection.getConnection();
    }

    @FXML
    private void registerUser(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both username and password.");
            alert.showAndWait();
            return;
        }

        if (password.length() < 6) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Weak Password");
            alert.setHeaderText(null);
            alert.setContentText("Password should be at least 6 characters.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // Remember: Hash passwords in production!

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("User registered successfully!");
                alert.showAndWait();

                usernameField.clear();
                passwordField.clear();
            } else {
                throw new SQLException("User registration failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while registering the user:\n" + e.getMessage());
            alert.showAndWait();
        }
        switchScene(event,"Login.fxml");
    }
    @FXML
    private void register(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Register.fxml"));
            Parent root = loader.load();

            // Get the source node and its scene/window
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // Set new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loginUser(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both username and password.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // In production, use hashed passwords

            var rs = ps.executeQuery();

            if (rs.next()) {
                // Successful login
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + username + "!");
                alert.showAndWait();

                switchScene(event,"Home.fxml");

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Error connecting to database: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    public void switchScene(ActionEvent event, String fxmlFile) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/" + fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        // Optionally show alert here
    }
}


}
