package com.kituo.legalaid;

import com.kituo.legalaid.views.LoginView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the AI-Powered Legal Aid Chatbot frontend.
 * Screens are plain JavaFX views (no FXML) switched via {@link #navigate(Parent)}.
 */
public class MainApp extends Application {

    private static Stage primaryStage;
    private static Scene scene;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("Kituo Cha Sheria - AI Legal Aid Chatbot");

        Parent root = LoginView.create();
        scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm());

        stage.setScene(scene);
        stage.setMinWidth(1024);
        stage.setMinHeight(700);
        stage.show();
    }

    /** Swap the root node of the single shared Scene (simple view router). */
    public static void navigate(Parent root) {
        scene.setRoot(root);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
