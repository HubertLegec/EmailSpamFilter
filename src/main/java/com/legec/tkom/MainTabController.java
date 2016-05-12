package com.legec.tkom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainTabController {
    private Stage primaryStage;
    @FXML
    private TextField filePathTF;
    @FXML
    private Label resultLabel;
    @FXML
    private ListView messagesLV;

    void init(Stage stage){
        this.primaryStage = stage;
    }

    @FXML
    private void onChooseFileClick(){

    }

    @FXML
    private void checkMessageOnClick(ActionEvent actionEvent) {

    }
}
