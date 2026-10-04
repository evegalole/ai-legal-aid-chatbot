package com.kituo.legalaid.components;

import com.kituo.legalaid.MainApp;
import com.kituo.legalaid.views.CasesView;
import com.kituo.legalaid.views.ChatbotView;
import com.kituo.legalaid.views.DashboardView;
import com.kituo.legalaid.views.LoginView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Left-hand navigation shared by every logged-in screen (Dashboard, AI Chatbot,
 * My Cases & Appointments). Pass the name of the screen that should be shown
 * as active, e.g. {@code Sidebar.build("Dashboard")}.
 */
public final class Sidebar {

    private Sidebar() {
    }

    public static VBox build(String activeScreen) {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        Text title = new Text("KITUO CHA SHERIA\n");
        title.getStyleClass().add("sidebar-title");
        Text subtitle = new Text("AI Legal Aid");
        subtitle.getStyleClass().add("sidebar-subtitle");
        TextFlow heading = new TextFlow(title, subtitle);
        heading.getStyleClass().add("sidebar-title");

        Button dashboard = navLink("Dashboard", activeScreen, () -> MainApp.navigate(DashboardView.create()));
        Button chatbot = navLink("AI Chatbot", activeScreen, () -> MainApp.navigate(ChatbotView.create()));
        Button cases = navLink("My Cases", activeScreen, () -> MainApp.navigate(CasesView.create()));
        Button appointments = navLink("Appointments", activeScreen, () -> MainApp.navigate(CasesView.create()));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logout = navLink("Log Out", "__none__", () -> MainApp.navigate(LoginView.create()));

        sidebar.getChildren().addAll(heading, dashboard, chatbot, cases, appointments, spacer, logout);
        sidebar.setAlignment(Pos.TOP_LEFT);
        return sidebar;
    }

    private static Button navLink(String label, String activeScreen, Runnable onClick) {
        Button button = new Button(label);
        button.getStyleClass().add(label.equals(activeScreen) ? "nav-link-active" : "nav-link");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> onClick.run());
        return button;
    }
}
