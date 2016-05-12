package com.legec.tkom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class SettingsTabController {
    private Stage primaryStage;
    @FXML
    private ListView suspiciousContLV;
    @FXML
    private ListView dangerousExtLV;


    void init(Stage stage){
        this.primaryStage = stage;
    }

    @FXML
    private void onAddSuspContent(ActionEvent actionEvent) {

    }

    @FXML
    private void onRemoveSuspContent(ActionEvent actionEvent) {

    }

    @FXML
    private void onAddDangerousExt(ActionEvent actionEvent) {
    }

    @FXML
    private void onRemoveDangerousExt(ActionEvent actionEvent) {
    }

    @FXML
    private void onAddServer(ActionEvent actionEvent) {

    }

    @FXML
    private void onRemoveServer(ActionEvent actionEvent) {

    }

    @FXML
    private void onAddSuspTitle(ActionEvent actionEvent) {

    }

    @FXML
    private void onRemoveSuspTitle(ActionEvent actionEvent) {

    }
}
