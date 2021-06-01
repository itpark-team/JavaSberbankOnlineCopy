package com.company.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static AnchorPane root;
    public static ArrayList<Pane> pages = new ArrayList<>();

    public static final int AUTH_PAGE = 0;
    public static final int WORK_PAGE = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("main.fxml"));

        pages.add(FXMLLoader.load(getClass().getResource("views/AuthPage.fxml")));
        pages.add(FXMLLoader.load(getClass().getResource("views/WorkPage.fxml")));

        GoToPage(AUTH_PAGE);

        primaryStage.setTitle("Сбербанк онлайн (копия)");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void GoToPage(int page){
        root.getChildren().clear();
        root.getChildren().add(pages.get(page));
    }
}
