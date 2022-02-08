/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeclient;

import java.awt.Color;
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
import java.util.regex.Pattern;
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
//    static Vector<User> playerList=new Vector<>();
//    static Stage primaryStage;
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
    @FXML
    private TextField connect_field;
    
    // </editor-fold>


    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

@FXML 
    private void connect_handelr(ActionEvent event) {
//                String IP = "127.0.0.1";
                flag = 1;
                try {
                    socket = new Socket(connect_field.getText(), 20080);
                    toServer = new PrintWriter(socket.getOutputStream(), true);
                    fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                } catch (IOException ex) {
                    //signActiontarget.setText("Please Check Your Connection");
                    //Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
                }
}
    @FXML 
    private void submitHandler(ActionEvent event) {

                String name = userName_SignUp.getText();
                String Pass = Password_signUp.getText();
                String Email = email_SignUp.getText();
                System.out.println(name);
                System.out.println(Pass);
                System.out.println(Email);
                String error = "";
                String regexName = "\\p{Upper}(\\p{Lower}+\\s?)";
//                String patternName = "(" + regexName + "){2,3}";
                System.out.println("The name is: " + name);
//                System.out.println("Is the above name valid? " + name.matches(patternName));
//                boolean nameResult = name.matches(patternName);
                String regexEmail = "^(.+)@(.+)$";
                Pattern patternEmail = Pattern.compile(regexEmail);
                System.out.println("Is the above Email valid? " + Email.matches(String.valueOf(patternEmail)));
                boolean EmailResult = Email.matches(String.valueOf(patternEmail));
                String regexPass = "((?=.*\\d)(?=.*[@#$%!]).{5,40})";
                Pattern patternPass = Pattern.compile(regexPass);
                System.out.println("Is the above Pass valid? " + Pass.matches(String.valueOf(patternPass)));
                boolean passResult = Pass.matches(String.valueOf(patternPass));
//
//                if (name.equals("") || Pass.equals("") || Email.equals("")) {
//                    error = "Please Fill All Inputs\n";
//                    TicTacToeClient.signActiontarget.setText(error);
//                }
//                if (nameResult == false) {
//
//                    error += "Please Enter Valid Name\n";
//                    TicTacToeClient.signActiontarget.setText(error);
//                }
//                if (EmailResult == false) {
//                    error += "Please Enter Valid Email\n";
//                    TicTacToeClient.signActiontarget.setText(error);
//                }
//                if (passResult == false) {
//                    error += "Please Enter Valid Password\n";
//                    TicTacToeClient.signActiontarget.setText(error);
//                } else {
                    TicTacToeClient.signActiontarget.setText("");
                    toServer.println("signup");
                    toServer.println(name);
                    toServer.println(Pass);
                    toServer.println(Email);

                    try {
                        mssg = fromServer.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (mssg.equals("signupDone")) {

                                System.out.println("Sign up Done");
//                                TicTacToeClient.signActiontarget.setFill(Color.YELLOW);
                                TicTacToeClient.signActiontarget.setText("Signed up successfully, Please Login");

                            } else if (mssg.equals("signupFailed")) {

                                System.out.println("Sign up Failed Please try again");
//                                TicTacToeClient.signActiontarget.setFill(Color.YELLOW);
                                TicTacToeClient.signActiontarget.setText("Sign up Failed Please try again");

                            }

                        }
                    });

//                }

    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
          String user = userNameL.getText();
          String Password = passwordL.getText();
                try {
                    if (flag == 1) {
                        User usr = new User();
                        toServer.println("login");
                        toServer.println(user);
                        toServer.println(Password);
                        System.out.println("waiting server authentication");
                        mssg = fromServer.readLine();
                        if (mssg.equals("loginDone")) {
                            System.out.println("hello from login");
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlayPage.fxml"));
                            System.out.println("hello from after login");
                            handleLoginOpeartionWithServer();
                            Parent root = fxmlLoader.load();
                            playPageController = (PlayPageController) fxmlLoader.getController();
//                          primaryStage= new Stage();
                            TicTacToeClient.stage.setScene(new Scene(root, 720, 700));
                            System.out.println("login success");
                            playPageController.SetCurrentUserInfo(currentUser.userName, currentUser.score);
                            listner = new ClientListner();
                            listner.start();
                            TicTacToeClient.stage.setTitle("Home page");
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
            // the second E-mail actually handles the picture (not yet implemented)
            User user = new User(userID, userName, email, "***", score, email, state);
             return user;
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
    
    private void SendDataToHomePage(User newUser) {
        PlayPageController.recieveData(newUser);
    }
    
    private void SendAllInfoToGamePage(Socket socket, PrintWriter toServer, BufferedReader fromServer, Vector<User> playerList) {
        PlayPageController.recieveSocket(socket, toServer, fromServer, playerList);
    }

    @FXML
    private void signUpHndelr(ActionEvent event) {
             pane_signUp.setVisible(true);
             pane_login.setVisible(false);

    }

    @FXML
    private void loginFormHndelr(ActionEvent event) {
          pane_login.setVisible(true);
            pane_signUp.setVisible(false);
    }

//    @FXML
//    private void connect(ActionEvent event) {
//        
//                String IP = "127.0.0.1";
//                flag = 1;
//                try {
//                    socket = new Socket(IP, 20080);
//                    toServer = new PrintWriter(socket.getOutputStream(), true);
//                    fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                    // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                } catch (IOException ex) {
//                    //signActiontarget.setText("Please Check Your Connection");
//                    //Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//    }
    
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
