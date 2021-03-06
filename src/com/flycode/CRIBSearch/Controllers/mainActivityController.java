package com.flycode.CRIBSearch.Controllers;

import com.flycode.CRIBSearch.Dialogs.*;
import com.flycode.CRIBSearch.PadSql.PadSqlUtil;
import com.flycode.CRIBSearch.SearchEngine.IndexDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
public class mainActivityController {
    @FXML private ComboBox<String> comboBox;
    @FXML private TableView tableView;
    @FXML private PasswordField password_field;
    @FXML private TextField login_field;
    @FXML private Label info_label;
    @FXML private Tab tab_sheets;
    @FXML private Button loadButton;
    @FXML private Button deleteButton;

    private ResultSet resultSet;
    private ObservableList<String> tableNames = FXCollections.observableArrayList();
    private ObservableList data = FXCollections.observableArrayList();
    private String SelectedTable;
    private PadSqlUtil padsql;
    private ResourceBundle dialogResources = ResourceBundle.getBundle("com.flycode.CRIBSearch.resources.dialog", Locale.getDefault());
    private PadDialog padDialog = new PadDialog();
    private Stage stage;

    public void initialize(PadSqlUtil p,Stage primaryStage) {
        this.padsql = p;
        this.stage = primaryStage;
    }

    public void onClickLoginButton() throws Exception{
        padsql.setUsername(login_field.getText()).setPass(password_field.getText()).ConnectDB();
        if (padsql.CONNECTION_STATUS) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/TenantView.fxml"));
            Parent root = fxmlLoader.load();
            TenantViewController controller = fxmlLoader.getController();
            controller.initialize(padsql);
            callTenantView(root);
            myDialog.showInfo(dialogResources.getString("LoginSuccessTitle"),dialogResources.getString("LoginSuccessMessage"));
            IndexDataBase(padsql);
        } else {
            myDialog.showError(dialogResources.getString("LoginErrorTitle"),dialogResources.getString("LoginErrorMessage"));
        }
    }

    private void IndexDataBase(PadSqlUtil p){
        Thread indexThread = new Thread(() -> {
            IndexDB iDB = new IndexDB(p);
            iDB.IndexWholeDB();
        });
        indexThread.run();
        //indexThread.sleep();
    }

    private void callTenantView(Parent root){
        stage.setTitle("Tenant View");
        stage.setScene(new Scene(root,880,580));
        stage.show();
    }

    private void fillComboBox() {
        //TODO: add automatic filling of table names form database itself
        tableNames.addAll("client_test","tenant","owner","building");
        comboBox.setItems(tableNames);
    }

    public void onClickLoadButton() {
        data.clear();
        tableView.getColumns().clear();
        /*spv.getColumns().clear();*/
        try {
            buildData(SelectedTable);
            /*buildSpreadSheet(SelectedTable);*/
        } catch (SQLException e) {
            myDialog.showThrowable("Error","at onClickLoadButton()",e);
        }
    }

    public void onClickAddButton() {
        try{
            if (comboBox.getValue().equals("tenant")){
                padDialog.tenantDialog("Add a new tenant",null,padsql,1);
            }else if (comboBox.getValue().equals("owner")) {
                padDialog.ownerDialog("Add a new Owner",null,padsql,1);
            }else if(comboBox.getValue().equals("building")){
                padDialog.buildingDialog("Add a new Building",null,padsql,1);
            }else{
                myDialog.showWarning("Warning!!!","No such table");
            }
        }catch (Exception e){
            myDialog.showThrowable("Error","at onClickAddButton()",e);
        }
        onClickLoadButton();
    }

    public void onClickDeleteButton() {
        Object string = data.get(tableView.getSelectionModel().getSelectedIndex());
        String id = (String) ((ObservableList) string).get(0);
        try{
            if (comboBox.getValue().equals("tenant")){
                padsql.DeleteTenant(id);
            }else if(comboBox.getValue().equals("owner")){
                padsql.DeleteOwner(id);
            }else if(comboBox.getValue().equals("building")){
                padsql.DeleteBuilding(id);
            }
        }catch (Exception e){
            myDialog.showThrowable("Error","at onClickDeleteButton()",e);
        }
        onClickLoadButton();
    }

    public void onMouseClickOnTable() {
        /*int index = tableView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Object string = data.get(index);
            textField1.setText(String.valueOf(((ObservableList) string).get(1)));
            textField2.setText(String.valueOf(((ObservableList) string).get(2)));
            textField3.setText(String.valueOf(((ObservableList) string).get(3)));
            textField4.setText(String.valueOf(((ObservableList) string).get(4)));
            textField5.setText(String.valueOf(((ObservableList) string).get(5)));
            textField6.setText(String.valueOf(((ObservableList) string).get(6)));
        } else {
            textField1.setText("");
            textField2.setText("");
            textField3.setText("");
            textField4.setText("");
            textField5.setText("");
            textField6.setText("");
        }*/
    }

    public void onClickUpdateButton() {
        try {
            ObservableList list = (ObservableList) data.get(tableView.getSelectionModel().getSelectedIndex());
            if (comboBox.getValue().equals("tenant")) {
                padDialog.tenantDialog("Update Tenant",list,padsql,2);
            } else if (comboBox.getValue().equals("owner")) {
                padDialog.ownerDialog("Update Owner",list,padsql,2);
            } else if (comboBox.getValue().equals("building")) {
                padDialog.buildingDialog("Update Building",list,padsql,2);
            }else{
                myDialog.showWarning("WARNING","NO SUCH TABLE");
            }
        }catch (Exception e){
            myDialog.showThrowable("Error","at onClickUpdateButton()",e);
        }
        onClickLoadButton();
    }

    public void onSelectComboBoxTableName() {
        SelectedTable = comboBox.getValue();
        loadButton.setDisable(false);
        deleteButton.setDisable(false);
        onClickLoadButton();
    }

    private void buildData(String table) throws SQLException {
        try{
        resultSet = padsql.selectTable(table); //SELECT ALL THE COLUMNS
        } catch (Exception e){
            myDialog.showThrowable("Error","Error in loading table.",e);
        }


         //TABLE COLUMN ADDED DYNAMICALLY
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableView.getColumns().addAll(col);

        }


         //Data added to ObservableList
        while (resultSet.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                row.add(resultSet.getString(i));
            }
            data.add(row);

        }

        //FINALLY ADDED TO TableView
        tableView.setItems(data);
    }
}
