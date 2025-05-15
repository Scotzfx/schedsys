package Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/schedulesystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Logged-in user's name
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    // Sidebar buttons
    @FXML
    private Button addButton, dashboardButton, appointmentsButton, recordsButton,
                   documentsButton, settingsButton, logoutButton;

    // Main panel option boxes
    @FXML
    private VBox documentsBox, signatureBox, peopleBox, attachmentsBox;

    // Sidebar button handlers
    @FXML
    private void handleAddAction() {
        System.out.println("Add button clicked.");
    }

    @FXML
    private void handleDashboardAction() {
        System.out.println("Dashboard button clicked.");
    }

    @FXML
    private void handleAppointmentsAction() {
        System.out.println("Appointments button clicked.");
    }

    @FXML
    private void handleButtonAction() {
        System.out.println("Records button clicked.");
    }

    @FXML
    private void handleDocumentsAction() {
        System.out.println("Documents button clicked.");
    }

    @FXML
    private void handleSettingsAction(ActionEvent event) {
         
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Logout");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to logout?");

    ButtonType yesButton = new ButtonType("Yes");
    ButtonType cancelButton = new ButtonType("Cancel");

    alert.getButtonTypes().setAll(yesButton, cancelButton);

    alert.showAndWait().ifPresent(response -> {
        if (response == yesButton) {
            switchScene(event, "Login.fxml"); // Or any other logout logic
        }
    });
    }

    // Main panel box click handlers
    @FXML
    private void handleDocumentsBoxClick() {
        showChoiceDialog(
            "Document",
            "Choose a document type:",
            "Type:",
            List.of("Clearance Form", "Application Form", "Enrollment Form")
        );
    }

    @FXML
    private void handleSignatureBoxClick() {
        showChoiceDialog(
            "Signature",
            "Choose how to provide your signature:",
            "Option:",
            List.of("Draw Signature", "Upload Signature", "Use Saved Signature")
        );
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


    @FXML
    private void handlePeopleBoxClick() {
        showChoiceDialog(
            "People",
            "Choose an action:",
            "Action:",
            List.of("Add Person", "Edit Person", "Remove Person")
        );
    }

    @FXML
    private void handleAttachmentsBoxClick() {
        showChoiceDialog(
            "Attachment",
            "Choose an action:",
            "Action:",
            List.of("Upload File", "View Attachments", "Delete Attachment")
        );
    }

    // Helper to show a ChoiceDialog and store result
    private void showChoiceDialog(String category, String header, String contentText, List<String> choices) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Select " + category);
        dialog.setHeaderText(header);
        dialog.setContentText(contentText);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> {
    System.out.println(category + " selected: " + choice);
   
    saveSelectionToDatabase(category, choice);
});

    }

    // Store user selection in database
    private void saveSelectionToDatabase(String category, String selection) {
String sql = "INSERT INTO selections (username, category, choice) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, category);
            stmt.setString(3, selection);
            stmt.executeUpdate();

            System.out.println("Saved selection to database.");
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // Show user-friendly error dialog
    private void showDatabaseError(SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("Could not save your selection");
        alert.setContentText("An error occurred while saving to the database:\n" + e.getMessage());
        alert.showAndWait();
    }
    
    // AddController.java
@FXML
private void handleSubmit(ActionEvent event) throws IOException {
    String selectedDoc = documentTypeComboBox.getValue(); // Get selected item

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Dashboard.fxml"));
    Parent dashboardRoot = loader.load();

    HomeController controller = loader.getController();
    controller.setRecentRequest(selectedDoc);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(dashboardRoot));
    stage.show();
}

    private void setRecentRequest(String selectedDoc) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static class documentTypeComboBox {

        private static String getValue() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public documentTypeComboBox() {
        }
    }

}