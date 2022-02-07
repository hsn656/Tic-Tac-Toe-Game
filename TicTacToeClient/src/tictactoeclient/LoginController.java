/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.User;

/**
 * FXML Controller class
 *
 * @author Hassan
 */
public class LoginController implements Initializable {
    
    static Socket socket;
    static BufferedReader fromServer;
    static PrintWriter toServer;
    
    static Vector<User> playerList=new Vector<>();
    static Stage primaryStage;
    String mssg;
    int flag = 0;
    static Vector<User> playersList = new Vector<>();
    User currentUser;
    boolean loggedIn = false;
    boolean isServerOn = false;
    PlayPageController playPageController;
    ClientListner listner;

    // <editor-fold defaultstate="collapsed" desc="fxml nodes">

    @FXML
    private Label label;
    @FXML
    private AnchorPane pane_login;
    @FXML
    private TextField userNameL;
    @FXML
    private PasswordField passwordL;
    @FXML
    private Button btnLogin;
    @FXML
    private AnchorPane pane_signUp;
    @FXML
    private TextField userName_SignUp;
    @FXML
    private PasswordField Password_signUp;
    @FXML
    private TextField email_SignUp;
    @FXML
    private Button submit;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnLoginForm;
    @FXML
    private Button connectBtn;
    
    // </editor-fold>


    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleLoginButton(ActionEvent event) {
    }

    @FXML
    private void submitHandler(ActionEvent event) {
          String user = userName_SignUp.getText();
          String Password = Password_signUp.getText();
                try {
                    if (flag == 1) {
                        User usr = new User();
                        toServer.println("login");
                        toServer.println(user);
                        toServer.println(Password);
                        System.out.println("waiting server authentication");
                        mssg = fromServer.readLine();
                        if (mssg.equals("loginDone")) {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlayPage.fxml")); 
                            Parent root = fxmlLoader.load();
                            playPageController = (PlayPageController) fxmlLoader.getController();
                            primaryStage= new Stage();
                            TicTacToeClient.stage.setScene(new Scene(root, 720, 700));
                            handleLoginOpeartionWithServer();
                            System.out.println("login success");
                            playPageController.SetCurrentUserInfo(currentUser.userName, currentUser.score);
                            listner = new ClientListner();
                            listner.start();
                            TicTacToeClient.stage.show();
                        } else if (mssg.equals("loginFailed")) {
                            //signActiontarget.setText("login Failed Please try again");
                            System.out.println("login Failed Please try again");
                        }
                    } else {
                       // signActiontarget.setText("Please Connect First then login");
                      System.out.println("no msg");
                    }
                } catch (IOException ex) {
                    // signActiontarget.setText("Please Connect First then login");
                    // Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
    private void handleLoginOpeartionWithServer() {
        try {
            loggedIn = true;
            currentUser = receiveUserInfo();
            SendDataToHomePage(currentUser);
            int playersNum = Integer.valueOf(fromServer.readLine());
            for (int i = 0; i < playersNum; i++) {
                playersList.add(receiveUserInfo());
            }
            SendAllInfoToGamePage(socket, toServer, fromServer, playersList);
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private User receiveUserInfo() {
        try {
           int userID = Integer.valueOf(fromServer.readLine());
            String userName = fromServer.readLine();
            String email = fromServer.readLine();
            String state = fromServer.readLine();
            int score = Integer.valueOf(fromServer.readLine());
            User user = new User(userID, userName, email, mssg, score, email, state);
             return user;
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
    
    private void SendDataToHomePage(User newUser) {
        playPageController.recieveData(newUser);
    }
    
    private void SendAllInfoToGamePage(Socket socket, PrintWriter toServer, BufferedReader fromServer, Vector<User> playerList) {
        playPageController.recieveSocket(socket, toServer, fromServer, playerList);
    }

    @FXML
    private void signUpHndelr(ActionEvent event) {
        
    }

    @FXML
    private void loginFormHndelr(ActionEvent event) {
        
    }

    @FXML
    private void connect(ActionEvent event) {
        
                String IP = "127.0.0.1";
                flag = 1;
                try {
                    socket = new Socket(IP, 20080);
                    toServer = new PrintWriter(socket.getOutputStream(), true);
                    fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                } catch (IOException ex) {
                    //signActiontarget.setText("Please Check Your Connection");
                    //Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
    public void recieveSocket(Socket socket, PrintWriter toServer, BufferedReader fromServer, Vector<User> playerList) {
        this.toServer = toServer;
        this.socket = socket;
        this.fromServer = fromServer;
        this.playerList = playerList;
        System.out.println(playerList.get(0).userName);
        System.out.println(playerList.get(1).userName);    
    }
    
     class ClientListner extends Thread {
        boolean isServerOn;
        public ClientListner() {
            System.out.println("Thread created");
            isServerOn = true;
        }

        public boolean isNumeric(String strNum) {
            if (strNum == null) {
                return false;
            }
            try {
                double d = Double.parseDouble(strNum);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
        }

        @Override
        public void run() {
            while (isServerOn) {
                try {
                    System.out.println("thread waiting for a message");
                    mssg = fromServer.readLine();
                    System.out.println(mssg);
                    //there will be alot of code here
                } catch (IOException ex) {
                    Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }  
}
