package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML
    private VBox contentArea;

    // Class to hold document info
    static class DocumentRequest {
        String documentType;
        Date dateRequested;

        DocumentRequest(String documentType, Date dateRequested) {
            this.documentType = documentType;
            this.dateRequested = dateRequested;
        }
    }

    @FXML
    void handleDashboardAction(ActionEvent event) {
        contentArea.getChildren().clear();

        Label headingLabel = new Label("Recent Document Request");
        headingLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
        contentArea.getChildren().add(headingLabel);

        List<DocumentRequest> requests = getDocumentRequests();

        for (DocumentRequest doc : requests) {
            Label label = new Label("• " + doc.documentType + " - Submitted on " + doc.dateRequested.toString());
            contentArea.getChildren().add(label);
        }

        if (requests.isEmpty()) {
            contentArea.getChildren().add(new Label("No document requests found."));
        }
    }

    private List<DocumentRequest> getDocumentRequests() {
        List<DocumentRequest> list = new ArrayList<>();
        String query = "SELECT document_type, date_requested FROM document_requests ORDER BY date_requested DESC LIMIT 5";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("document_type");
                Date date = rs.getDate("date_requested");
                list.add(new DocumentRequest(type, date));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
