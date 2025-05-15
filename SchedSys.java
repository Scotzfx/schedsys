package schedsys;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SchedSys extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent contentRoot = null;
            URL fxmlUrl = getClass().getResource("/FXML/Login.fxml");

            System.out.println("Classpath FXML location: " + fxmlUrl);

            if (fxmlUrl != null) {
                contentRoot = FXMLLoader.load(fxmlUrl);
                System.out.println("Loaded FXML from classpath.");
            } else {
                File fxmlFile = new File("src/schedsys/Register.fxml");
                if (!fxmlFile.exists()) {
                    throw new IOException("Register.fxml not found in classpath or filesystem.\n"
                            + "Tried:\n - /schedsys/Register.fxml (classpath)\n - src/schedsys/Register.fxml (file)");
                }
                System.out.println("Loaded FXML from filesystem: " + fxmlFile.getAbsolutePath());
                contentRoot = FXMLLoader.load(fxmlFile.toURI().toURL());
            }

            // Add custom top bar with X button
            Button closeButton = new Button("X");
            closeButton.setOnAction(e -> Platform.exit());
            closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 16;");

            HBox topBar = new HBox(closeButton);
            topBar.setAlignment(Pos.TOP_RIGHT);
            topBar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5 10 0 0;");

            BorderPane decoratedRoot = new BorderPane();
            decoratedRoot.setTop(topBar);
            decoratedRoot.setCenter(contentRoot);

            Scene scene = new Scene(decoratedRoot, 800, 600); // default size

            // Optional: Load CSS if available
            URL cssUrl = getClass().getResource("/schedsys/Home.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("Loaded CSS from: " + cssUrl);
            } else {
                File cssFile = new File("src/schedsys/Home.css");
                if (cssFile.exists()) {
                    scene.getStylesheets().add(cssFile.toURI().toString());
                    System.out.println("Loaded CSS from filesystem: " + cssFile.getAbsolutePath());
                } else {
                    System.out.println("No CSS file found.");
                }
            }

            /*primaryStage.initStyle(StageStyle.UNDECORATED); // removes title bar
            primaryStage.setResizable(false);                // disables resizing
            primaryStage.setFullScreen(false);  */             // disables fullscreen
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sched-U School Management System");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Application failed to start", e.getMessage());
        }
    }

    private void showError(String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Startup Error");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        System.out.println("Starting application...");
        launch(args);
    }
}
