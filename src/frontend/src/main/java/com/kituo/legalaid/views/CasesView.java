package com.kituo.legalaid.views;

import com.kituo.legalaid.components.Sidebar;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public final class CasesView {

    private CasesView() {
    }

    public static Parent create() {
        BorderPane root = new BorderPane();
        root.setLeft(Sidebar.build("My Cases"));

        BorderPane main = new BorderPane();
        main.setTop(DashboardView.topBar("My Cases & Appointments"));

        VBox content = new VBox(24);
        content.setPadding(new Insets(32));

        content.getChildren().addAll(
                sectionCard("Escalated Cases", casesTable()),
                sectionCard("Upcoming Appointments", appointmentsTable()));

        main.setCenter(content);
        root.setCenter(main);
        return root;
    }

    private static VBox sectionCard(String heading, TableView<?> table) {
        Label h = new Label(heading);
        h.getStyleClass().add("card-heading");
        VBox card = new VBox(14, h, table);
        card.getStyleClass().add("card");
        return card;
    }

    /** Row model for the escalated-cases table (mirrors LEGAL_QUERY + ESCALATION). */
    public static class CaseRow {
        private final String question;
        private final String category;
        private final String status;
        private final String date;

        public CaseRow(String question, String category, String status, String date) {
            this.question = question;
            this.category = category;
            this.status = status;
            this.date = date;
        }

        public String getQuestion() { return question; }
        public String getCategory() { return category; }
        public String getStatus() { return status; }
        public String getDate() { return date; }
    }

    /** Row model for the appointments table (mirrors PROFESSIONAL_RESPONSE scheduling). */
    public static class AppointmentRow {
        private final String professional;
        private final String topic;
        private final String dateTime;
        private final String status;

        public AppointmentRow(String professional, String topic, String dateTime, String status) {
            this.professional = professional;
            this.topic = topic;
            this.dateTime = dateTime;
            this.status = status;
        }

        public String getProfessional() { return professional; }
        public String getTopic() { return topic; }
        public String getDateTime() { return dateTime; }
        public String getStatus() { return status; }
    }

    private static TableView<CaseRow> casesTable() {
        TableView<CaseRow> table = new TableView<>();
        table.getStyleClass().add("data-table");
        table.setItems(FXCollections.observableArrayList(
                new CaseRow("Employer hasn't paid final salary", "Employment", "Assigned", "1 Oct 2026"),
                new CaseRow("Boundary dispute with neighbour", "Land", "Pending", "28 Sep 2026")));

        TableColumn<CaseRow, String> question = new TableColumn<>("Question");
        question.setCellValueFactory(new PropertyValueFactory<>("question"));
        question.setPrefWidth(380);

        TableColumn<CaseRow, String> category = new TableColumn<>("Category");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<CaseRow, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<CaseRow, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(question, category, status, date);
        return table;
    }

    private static TableView<AppointmentRow> appointmentsTable() {
        TableView<AppointmentRow> table = new TableView<>();
        table.getStyleClass().add("data-table");
        table.setItems(FXCollections.observableArrayList(
                new AppointmentRow("Adv. Wanjiru M.", "Employment dispute", "6 Oct 2026, 10:00", "Confirmed"),
                new AppointmentRow("Adv. Otieno K.", "Land boundary dispute", "9 Oct 2026, 14:00", "Awaiting confirmation")));

        TableColumn<AppointmentRow, String> professional = new TableColumn<>("Legal Professional");
        professional.setCellValueFactory(new PropertyValueFactory<>("professional"));
        professional.setPrefWidth(200);

        TableColumn<AppointmentRow, String> topic = new TableColumn<>("Topic");
        topic.setCellValueFactory(new PropertyValueFactory<>("topic"));
        topic.setPrefWidth(220);

        TableColumn<AppointmentRow, String> dateTime = new TableColumn<>("Date & Time");
        dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        TableColumn<AppointmentRow, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(professional, topic, dateTime, status);
        return table;
    }
}
