/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeclient;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.User;

/**
 * FXML Controller class
 *
 * @author Hassan
 */
public class PlayPageController implements Initializable {
    
    static Socket socket;
    static BufferedReader fromServer;
    static PrintWriter toServer;
    static Vector<User> playerList;
    static Stage primaryStage;
    String mssg;
    int flag = 0;
    static Vector<User> playersList = new Vector<>();
    User currentUser;
    static String user;
    static Integer score;
    
    
    
    @FXML
    private Label displayUerName;
    @FXML
    private Label displayUserScore;
    @FXML
    private TableView<User> dataTable;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Integer> scoreColumn;
    @FXML
    private TableColumn<User, String> stateColumn;
    @FXML
    private Button inviteButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("score"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<User, String>("state"));
    }    

    @FXML
    private void sendInvitation(ActionEvent event) {
    }
    
    static public void recieveSocket(Socket _socket, PrintWriter _toServer, BufferedReader _fromServer, Vector<User> _playerList) {
        toServer = _toServer;
        socket = _socket;
        fromServer = _fromServer;
        playerList = _playerList;
        
//        System.out.println(playerList.get(0).userName);
//        System.out.println(playerList.get(1).userName);    
    }
    
    static public String recieveData(User myData) {
        user = myData.userName;
        score = myData.score;

        return user;
    }
      
    public void SetCurrentUserInfo(String name, Integer score) {
        displayUerName.setText(user);
        displayUserScore.setText(score.toString());
        dataTable.setEditable(true);
        dataTable.getItems().clear();
        ObservableList<User> data = FXCollections.observableArrayList();
//        (ObservableList<User>) dataTable.getItems();
        for (int i = 0; i < playerList.size(); i++) {
            System.out.println(playerList.get(i).userName + " " + playerList.get(i).state);
            data.add(new User(playerList.get(i).userName, playerList.get(i).email, playerList.get(i).score, playerList.get(i).state));
        }   
        dataTable.setItems(data);
    }
}
