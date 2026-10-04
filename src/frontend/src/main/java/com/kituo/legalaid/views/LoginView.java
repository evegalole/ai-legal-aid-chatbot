package com.kituo.legalaid.views;

import com.kituo.legalaid.MainApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Login screen. Also offers "continue as guest" so an unregistered user can
 * ask a quick question without creating an account (see use case diagram).
 */
public final class LoginView {

    private LoginView() {
    }

    public static Parent create() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #1D2124;");

        VBox card = new VBox(20);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(400);
        card.setMaxHeight(VBox.USE_PREF_SIZE);

        Text brand = new Text("KITUO CHA SHERIA");
        brand.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1D2124;");
        Text tagline = new Text("AI Legal Aid Chatbot");
        tagline.setStyle("-fx-font-size: 13px; -fx-fill: #C5401D;");
        VBox brandBox = new VBox(2, brand, tagline);
        brandBox.setAlignment(Pos.CENTER);

        Label loginTab = new Label("Log In");
        loginTab.getStyleClass().add("tab-active");
        Hyperlink registerTab = new Hyperlink("Register");
        registerTab.getStyleClass().add("tab-inactive");
        // TODO: build a real RegisterView once the backend has a /register endpoint.
        registerTab.setOnAction(e -> {
            javafx.scene.control.Alert notice = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION,
                    "Registration isn't built yet \u2014 for now, log in or continue as a guest.");
            notice.setHeaderText(null);
            notice.showAndWait();
        });
        HBox tabs = new HBox(24, loginTab, registerTab);
        tabs.setStyle("-fx-border-color: transparent transparent #EEEEEE transparent; -fx-border-width: 0 0 1 0;");

        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("field-label");
        TextField emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.getStyleClass().add("login-field");

        Label pwLabel = new Label("Password");
        pwLabel.getStyleClass().add("field-label");
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("********");
        pwField.getStyleClass().add("login-field");

        Button loginButton = new Button("Log In");
        loginButton.getStyleClass().add("btn-primary");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        // TODO: wire to backend authentication; navigates straight to the
        // dashboard for now so the flow can be demoed end to end.
        loginButton.setOnAction(e -> MainApp.navigate(DashboardView.create()));

        Hyperlink guestLink = new Hyperlink("continue as guest");
        guestLink.getStyleClass().add("link");
        guestLink.setOnAction(e -> MainApp.navigate(ChatbotView.create()));
        TextFlow guestLine = new TextFlow(new Text("or "), guestLink, new Text(" for a quick question"));
        guestLine.setStyle("-fx-text-alignment: center;");
        guestLine.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox form = new VBox(16, emailLabel, emailField, pwLabel, pwField, loginButton);

        card.getChildren().addAll(brandBox, tabs, form, guestLine);
        card.setAlignment(Pos.CENTER);
        StackPane.setMargin(card, new Insets(40));

        root.getChildren().add(card);
        return root;
    }
}