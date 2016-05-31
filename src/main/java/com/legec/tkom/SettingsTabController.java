package com.legec.tkom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class SettingsTabController {
    private Stage primaryStage;
    @FXML
    private ListView<String> suspiciousContLV;
    @FXML
    private ListView<String> dangerousExtLV;
    @FXML
    private ListView<String> serversLV;
    @FXML
    private ListView<String> suspiciousTitleLV;

    @FXML
    private Button removeServerBT;
    @FXML
    private Button removeTitleBT;
    @FXML
    private Button removeExtensionBT;
    @FXML
    private Button removeContentBT;

    private ObservableList<String> contentModel = FXCollections.observableArrayList();
    private ObservableList<String> extensionsModel = FXCollections.observableArrayList();
    private ObservableList<String> serversModel = FXCollections.observableArrayList();
    private ObservableList<String> titleModel = FXCollections.observableArrayList();


    void init(Stage stage){
        this.primaryStage = stage;
        suspiciousContLV.setItems(contentModel);
        dangerousExtLV.setItems(extensionsModel);
        serversLV.setItems(serversModel);
        suspiciousTitleLV.setItems(titleModel);

        removeContentBT.disableProperty().bind(suspiciousContLV.getSelectionModel().selectedItemProperty().isNull());
        removeExtensionBT.disableProperty().bind(dangerousExtLV.getSelectionModel().selectedItemProperty().isNull());
        removeTitleBT.disableProperty().bind(suspiciousTitleLV.getSelectionModel().selectedItemProperty().isNull());
        removeServerBT.disableProperty().bind(serversLV.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void onAddSuspContent(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Suspicious content", "Type content may be suspicious", "Value");
        if(val != null){
            contentModel.add(val);
        }
    }

    @FXML
    private void onRemoveSuspContent(ActionEvent actionEvent) {
        if(suspiciousContLV.getSelectionModel().getSelectedItems().size() == 1){
            contentModel.remove(suspiciousContLV.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void onAddDangerousExt(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Dangerous extension", "Type attachment extension may be dangerous", "Extension");
        if(val != null){
            extensionsModel.add(val);
        }
    }

    @FXML
    private void onRemoveDangerousExt(ActionEvent actionEvent) {
        if(dangerousExtLV.getSelectionModel().getSelectedItems().size() == 1){
            extensionsModel.remove(dangerousExtLV.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void onAddServer(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Suspicious server", "Type server IP may be dangerous", "IP address");
        if(val != null){
            serversModel.add(val);
        }
    }

    @FXML
    private void onRemoveServer(ActionEvent actionEvent) {
        if(serversLV.getSelectionModel().getSelectedItems().size() == 1){
            serversModel.remove(serversLV.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void onAddSuspTitle(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Suspicious title", "Type word or phase which if present in title may be dangerous", "Value");
        if(val != null){
            titleModel.add(val);
        }
    }

    @FXML
    private void onRemoveSuspTitle(ActionEvent actionEvent) {
        if(suspiciousTitleLV.getSelectionModel().getSelectedItems().size() == 1){
            titleModel.remove(suspiciousTitleLV.getSelectionModel().getSelectedIndex());
        }
    }

    private String getInputTextFromDialog(String title, String header, String content){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(content);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
