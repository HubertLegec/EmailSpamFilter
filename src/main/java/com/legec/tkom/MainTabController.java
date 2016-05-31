package com.legec.tkom;

import com.legec.tkom.core.SpamDetector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainTabController {
    private Stage primaryStage;
    @FXML
    private TextField filePathTF;
    @FXML
    private Label resultLabel;
    @FXML
    private ListView messagesLV;

    private SpamDetector spamDetector;

    void init(Stage stage, SpamDetector spamDetector) {
        this.primaryStage = stage;
        this.spamDetector = spamDetector;
    }

    @FXML
    private void onChooseFileClick() {
        FileChooser fileChooser = new FileChooser();
        File chosenFile = fileChooser.showOpenDialog(primaryStage.getScene().getWindow());
        if (chosenFile != null) {
            filePathTF.setText(chosenFile.getAbsolutePath());
        }
    }

    @FXML
    private void checkMessageOnClick(ActionEvent actionEvent) {
        try {
            spamDetector.init(filePathTF.getText());
            spamDetector.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
