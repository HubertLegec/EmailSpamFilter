package com.legec.tkom;

import com.legec.tkom.core.configuration.Configuration;
import com.legec.tkom.core.configuration.GlobalConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class SettingsTabController {
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
    @FXML
    private CheckBox checkRoute;

    private ObservableList<String> contentModel = FXCollections.observableArrayList();
    private ObservableList<String> extensionsModel = FXCollections.observableArrayList();
    private ObservableList<String> serversModel = FXCollections.observableArrayList();
    private ObservableList<String> titleModel = FXCollections.observableArrayList();

    void init(){
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
    private void onCheckboxChange(){
        updateGlobalConfig();
    }

    @FXML
    private void onAddSuspContent(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Suspicious content", "Type content may be suspicious", "Value");
        if(val != null){
            contentModel.add(val);
            updateGlobalConfig();
        }
    }

    @FXML
    private void onRemoveSuspContent(ActionEvent actionEvent) {
        if(suspiciousContLV.getSelectionModel().getSelectedItems().size() == 1){
            contentModel.remove(suspiciousContLV.getSelectionModel().getSelectedIndex());
            updateGlobalConfig();
        }
    }

    @FXML
    private void onAddDangerousExt(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Dangerous extension", "Type attachment extension may be dangerous", "Extension");
        if(val != null){
            extensionsModel.add(val);
            updateGlobalConfig();
        }
    }

    @FXML
    private void onRemoveDangerousExt(ActionEvent actionEvent) {
        if(dangerousExtLV.getSelectionModel().getSelectedItems().size() == 1){
            extensionsModel.remove(dangerousExtLV.getSelectionModel().getSelectedIndex());
            updateGlobalConfig();
        }
    }

    @FXML
    private void onAddServer(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Suspicious server", "Type server address that may be dangerous", "Server address");
        if(val != null){
            serversModel.add(val);
            updateGlobalConfig();
        }
    }

    @FXML
    private void onRemoveServer(ActionEvent actionEvent) {
        if(serversLV.getSelectionModel().getSelectedItems().size() == 1){
            serversModel.remove(serversLV.getSelectionModel().getSelectedIndex());
            updateGlobalConfig();
        }
    }

    @FXML
    private void onAddSuspTitle(ActionEvent actionEvent) {
        String val = getInputTextFromDialog("Suspicious title", "Type word or phase which if present in title may be dangerous", "Value");
        if(val != null){
            titleModel.add(val);
            updateGlobalConfig();
        }
    }

    @FXML
    private void onRemoveSuspTitle(ActionEvent actionEvent) {
        if(suspiciousTitleLV.getSelectionModel().getSelectedItems().size() == 1){
            titleModel.remove(suspiciousTitleLV.getSelectionModel().getSelectedIndex());
            updateGlobalConfig();
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

    void loadConfiguration(Configuration configuration){
        contentModel.setAll(configuration.getSuspiciousWords());
        titleModel.setAll(configuration.getSuspiciousTitleWords());
        extensionsModel.setAll(configuration.getDangerousExtensions());
        serversModel.setAll(configuration.getDangerousServers());
        checkRoute.setSelected(configuration.isCheckRoute());
    }

    Configuration getConfig(){
        return new Configuration(extensionsModel, contentModel, titleModel, serversModel, checkRoute.isSelected());
    }

    private void updateGlobalConfig(){
        GlobalConfig.setConfiguration(getConfig());
    }
}
