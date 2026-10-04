package com.kituo.legalaid.views;

import com.kituo.legalaid.components.Sidebar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public final class ChatbotView {

    private ChatbotView() {
    }

    public static Parent create() {
        BorderPane root = new BorderPane();
        root.setLeft(Sidebar.build("AI Chatbot"));

        BorderPane main = new BorderPane();
        main.setTop(DashboardView.topBar("AI Chatbot"));

        VBox messages = new VBox(16);
        messages.setPadding(new Insets(32));

        messages.getChildren().add(aiBubble(
                "Hello! Ask me about tenant rights, employment disputes or land "
                        + "ownership, or continue as a guest.", false));
        messages.getChildren().add(userBubble(
                "My landlord wants to evict me with only 2 days notice. Is that legal?"));
        messages.getChildren().add(aiBubble(
                "Under Kenyan tenancy law, landlords generally must give reasonable "
                        + "written notice before eviction \u2014 2 days is unlikely to meet "
                        + "that standard.", true));

        ScrollPane scroll = new ScrollPane(messages);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        TextField input = new TextField();
        input.setPromptText("Type your legal question...");
        input.getStyleClass().add("chat-input");
        HBox.setHgrow(input, Priority.ALWAYS);

        Button send = new Button("Send");
        send.getStyleClass().add("btn-primary");

        HBox inputRow = new HBox(12, input, send);
        inputRow.setPadding(new Insets(20, 32, 20, 32));
        inputRow.setStyle("-fx-background-color: white; -fx-border-color: #E3E1DE transparent transparent transparent; -fx-border-width: 1 0 0 0;");
        inputRow.setAlignment(Pos.CENTER);

        main.setCenter(scroll);
        main.setBottom(inputRow);

        root.setCenter(main);
        return root;
    }

    private static VBox aiBubble(String text, boolean withFeedback) {
        Text body = new Text(text);
        body.getStyleClass().add("bubble-ai-text");
        TextFlow flow = new TextFlow(body);
        flow.getStyleClass().add("bubble-ai");
        flow.setMaxWidth(600);

        VBox box = new VBox(4, flow);
        box.setAlignment(Pos.CENTER_LEFT);

        if (withFeedback) {
            Label disclaimer = new Label("General information only \u2014 not professional legal advice.");
            disclaimer.getStyleClass().add("disclaimer");

            Button helpful = new Button("Helpful");
            helpful.getStyleClass().add("btn-secondary");
            Button notHelpful = new Button("Not helpful");
            notHelpful.getStyleClass().add("btn-secondary");
            HBox feedback = new HBox(10, helpful, notHelpful);

            box.getChildren().addAll(disclaimer, feedback);
        }
        return box;
    }

    private static HBox userBubble(String text) {
        TextFlow flow = new TextFlow(new Text(text));
        flow.getStyleClass().add("bubble-user");
        flow.setMaxWidth(600);
        for (javafx.scene.Node n : flow.getChildren()) {
            if (n instanceof Text t) {
                t.getStyleClass().add("bubble-user-text");
            }
        }
        HBox row = new HBox(flow);
        row.setAlignment(Pos.CENTER_RIGHT);
        return row;
    }
}
