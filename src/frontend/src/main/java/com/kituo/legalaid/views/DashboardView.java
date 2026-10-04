package com.kituo.legalaid.views;

import com.kituo.legalaid.components.Sidebar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public final class DashboardView {

    private DashboardView() {
    }

    public static Parent create() {
        BorderPane root = new BorderPane();
        root.setLeft(Sidebar.build("Dashboard"));

        BorderPane main = new BorderPane();
        main.setTop(topBar("Dashboard"));

        VBox content = new VBox(24);
        content.setPadding(new Insets(32));

        HBox statRow = new HBox(20,
                statCard("Active Conversations", "3", false),
                statCard("Pending Escalations", "1", true),
                statCard("Upcoming Appointments", "2", false));
        HBox.setHgrow(statRow, Priority.ALWAYS);
        for (var node : statRow.getChildren()) {
            HBox.setHgrow(node, Priority.ALWAYS);
        }

        VBox activityCard = new VBox(10);
        activityCard.getStyleClass().add("card");
        Label heading = new Label("Recent Activity");
        heading.getStyleClass().add("card-heading");
        activityCard.getChildren().addAll(
                heading,
                activityRow("\"Can my landlord evict me without notice?\"", "badge-resolved", "Resolved"),
                activityRow("\"My employer hasn't paid my final salary\"", "badge-escalated", "Escalated"),
                activityRow("\"How do I register a land title transfer?\"", "badge-resolved", "Resolved"));

        content.getChildren().addAll(statRow, activityCard);
        main.setCenter(content);

        root.setCenter(main);
        return root;
    }

    private static VBox statCard(String label, String value, boolean accent) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card");
        Label l = new Label(label);
        l.getStyleClass().add("card-label");
        Label v = new Label(value);
        v.getStyleClass().add(accent ? "card-value-accent" : "card-value");
        card.getChildren().addAll(l, v);
        return card;
    }

    private static HBox activityRow(String text, String badgeStyle, String badgeText) {
        Label question = new Label(text);
        Label badge = new Label(badgeText);
        badge.getStyleClass().addAll("badge", badgeStyle);
        HBox row = new HBox(question, spacer(), badge);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setStyle("-fx-border-color: transparent transparent #EEEEEE transparent; -fx-border-width: 0 0 1 0;");
        return row;
    }

    private static javafx.scene.layout.Region spacer() {
        javafx.scene.layout.Region r = new javafx.scene.layout.Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    static HBox topBar(String title) {
        Label pageTitle = new Label(title);
        pageTitle.getStyleClass().add("page-title");
        Label user = new Label("Eve W.");
        user.getStyleClass().add("user-label");
        HBox bar = new HBox(pageTitle, spacer(), user);
        bar.getStyleClass().add("topbar");
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }
}
