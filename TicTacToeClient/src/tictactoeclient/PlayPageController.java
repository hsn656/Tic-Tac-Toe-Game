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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableView<?> dataTable;
    @FXML
    private TableColumn<?, ?> nameColumn;
    @FXML
    private TableColumn<?, ?> scoreColumn;
    @FXML
    private TableColumn<?, ?> emailColumn;
    @FXML
    private TableColumn<?, ?> stateColumn;
    @FXML
    private Button inviteButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void sendInvitation(ActionEvent event) {
    }
    
    public void recieveSocket(Socket socket, PrintWriter toServer, BufferedReader fromServer, Vector<User> playerList) {
        this.toServer = toServer;
        this.socket = socket;
        this.fromServer = fromServer;
        this.playerList = playerList;
//        
//        System.out.println(playerList.get(0).userName);
//        System.out.println(playerList.get(1).userName);    
    }
    
    public String recieveData(User myData) {
        System.out.println(myData.userName);
        user = myData.userName;
        score = myData.score;

        return user;
    }
      
    public void SetCurrentUserInfo(String name, Integer score) {
//        myName.setText(name);
//        myScore.setText(String.valueOf(score));
//        tableView.getItems().clear();
//        ObservableList<User> data = tableView.getItems();
//        
//        for (int i = 0; i < playerList.size(); i++) {
//            System.out.println(playerList.get(i).userName + " " + playerList.get(i).state);
//            data.add(new User(playerList.get(i).userID, playerList.get(i).userName, playerList.get(i).email, playerList.get(i).state, playerList.get(i).score));
//        }
//        
    }
    
    
}
