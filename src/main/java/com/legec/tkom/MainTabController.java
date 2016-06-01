package com.legec.tkom;

import com.legec.tkom.core.SpamDetector;
import com.legec.tkom.core.model.EmailType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainTabController {
    private Stage primaryStage;
    @FXML
    private TextField filePathTF;
    @FXML
    private Label resultLabel;
    @FXML
    private ListView<String> messagesLV;

    private ObservableList<String> messagesModel = FXCollections.observableArrayList();

    private SpamDetector spamDetector;

    void init(Stage stage, SpamDetector spamDetector) {
        this.primaryStage = stage;
        this.spamDetector = spamDetector;
        messagesLV.setItems(messagesModel);
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
            if(spamDetector.process()){
                Pair<EmailType, List<String>> result = spamDetector.getResult();
                messagesModel.setAll(result.getValue());
                resultLabel.setText(result.getKey().name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
