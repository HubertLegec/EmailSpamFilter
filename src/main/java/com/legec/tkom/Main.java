package com.legec.tkom;

import com.legec.tkom.core.SpamDetector;
import com.legec.tkom.core.configuration.GlobalConfig;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    @FXML
    private MainTabController mainTabController;
    @FXML
    private SettingsTabController settingsTabController;

    private SpamDetector spamDetector = new SpamDetector();

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Email Spam Filter");
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("MainWindow.fxml"));
            loader.setController(this);
            BorderPane rootLayout = loader.load();

            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            mainTabController.init(primaryStage, spamDetector);
            settingsTabController.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private void onLoadConfiguration(){
        try {
            GlobalConfig.loadConfiguration();
            settingsTabController.loadConfiguration(GlobalConfig.getConfiguration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveConfiguration(){
        GlobalConfig.setConfiguration(settingsTabController.getConfig());
        try {
            GlobalConfig.saveConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


