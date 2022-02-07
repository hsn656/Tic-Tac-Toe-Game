/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictacserver;

import controllers.ClientHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.User;
import models.UserModel;

/**
 *
 * @author Hassan
 */
public class TicTacServer extends Application {
    
    public static TableView table = new TableView();
    private static Vector<User> vAllUsers;
    private static ObservableList<Player> data = FXCollections.observableArrayList(getALLUsersFromDB());

    
     public static Vector<Player> getALLUsersFromDB() {
        vAllUsers = UserModel.returnAllPlayers();
        Vector<Player> allPlayer = new Vector<Player>();
        for (int index = 0; index < vAllUsers.size(); index++) {
            allPlayer.add(new Player(vAllUsers.get(index)));
        }
        return allPlayer;
    }
    
    public static void updatePlayersTable() {
        data = FXCollections.observableArrayList(getALLUsersFromDB());
        table.setItems(data);
    }
    
    
    
    @Override
     public void start(Stage primaryStage) {
        final Label label = new Label("UserName of Players");
        label.setFont(new Font("Arial", 20));
        table.setEditable(true);
        TableColumn userIdCol = new TableColumn("User ID");
        userIdCol.setMinWidth(25);
        userIdCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("userID"));
        TableColumn userNameCol = new TableColumn("User Name");
        userNameCol.setMinWidth(25);
        userNameCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("userName"));
        TableColumn passCol = new TableColumn("Password");
        passCol.setMinWidth(20);
        passCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("password"));
        TableColumn emailCol = new TableColumn("email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("email"));
        TableColumn scoreCol = new TableColumn("Score");
        scoreCol.setMinWidth(50);
        scoreCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("score"));
        TableColumn statusCol = new TableColumn("Status");
        scoreCol.setMinWidth(20);
        statusCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("state"));
        table.setItems(data);
        table.getColumns().addAll(userIdCol, userNameCol, passCol, emailCol, scoreCol, statusCol);
        // SwitchButtonC switchBtn = new SwitchButtonC();
        ToggleButton onBtn = new ToggleButton("ON");
        ToggleButton offBtn = new ToggleButton("OFF");
        ToggleGroup toggleGroup = new ToggleGroup();
        onBtn.setToggleGroup(toggleGroup);
        offBtn.setToggleGroup(toggleGroup);
        onBtn.setSelected(true);
        offBtn.setStyle("-fx-padding: 8 15 15 15;\n"
                + "    -fx-background-insets: 0,0 0 5 0,0 0 6 0,0 0 7 0;\n"
                + "    -fx-background-radius: 8;\n"
                + "    -fx-background-color:\n"
                + "            linear-gradient(from 0% 93% to 0% 100%,#a34313 0%,#903b12 100%),\n"
                + "             #9d4024,\n"
                + "             #d86e3a,\n"
                + "            radial-gradient(center 50% 50%, radius 100%,#d96e3a,#c54e2c);\n"
                + "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75),4,0,0,1);\n"
                + "    -fx-font-weight: bold;\n"
                + "    -fx-font-size: 1.1em;\n"
                + "    -fx-text-fill: white;");
        onBtn.setStyle("-fx-padding: 8 15 15 15;\n"
                + "    -fx-background-insets: 0,0 0 5 0,0 0 6 0,0 0 7 0;\n"
                + "    -fx-background-radius: 8;\n"
                + "    -fx-background-color:\n"
                + "            linear-gradient(from 0% 93% to 0% 100%,#a34313 0%,#903b12 100%),\n"
                + "             #9d4024,\n"
                + "             #d86e3a,\n"
                + "            radial-gradient(center 50% 50%, radius 100%,#d96e3a,#c54e2c);\n"
                + "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75),4,0,0,1);\n"
                + "    -fx-font-weight: bold;\n"
                + "    -fx-font-size: 1.1em;\n"
                + "    -fx-text-fill: white;");
        
         HBox leftBox = new HBox(onBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        HBox rightBox = new HBox(offBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        HBox bottom = new HBox(leftBox, rightBox);
        bottom.setPadding(new Insets(10));
        bottom.setStyle("fx-background-color:black");
        BorderPane root = new BorderPane(table);
        root.setBottom(bottom);
//        root.setStyle("-fx-background-image: url('https://games.lol/wp-content/uploads/2018/11/x-o-tic-tac-toe-pc-download.png')");
        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add("views/serverCascadeStyleSheet.css");
        scene.getStylesheets().add("views/styleSheet.css");
        primaryStage.setWidth(600);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Game Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        Thread socketServerThread = new Thread(new GameServerXO());
        socketServerThread.setDaemon(true); //terminate the thread when program end
        socketServerThread.start();
        offBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                socketServerThread.stop();
//                ClientHandler.closeAllInternalSockets();
                System.out.println("close Server");
            }
        });
        onBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                socketServerThread.resume();
                System.out.println("resume thread server");
            }
        });
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private class GameServerXO extends Thread {

        ServerSocket serverSocket;
        static final int SocketServerPORT = 20080;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                System.out.println("start accepting clients");
                while (true) {
                    System.out.println("waiting");
                    Socket s = serverSocket.accept();
                    System.out.println("new player connected");
                    new ClientHandler(s);
                }
            } catch (IOException ex) {

            }
        }

        public GameServerXO() {

        }

        public void closeServer() {
            try {
                serverSocket.close();
                System.out.println("server is closed");
                ClientHandler.closeAllInternalSockets();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public static class Player {

        private final SimpleStringProperty userID;
        private final SimpleStringProperty userName;
        private final SimpleStringProperty password;
        private final SimpleStringProperty email;
        private final SimpleStringProperty state;
        private final SimpleStringProperty score;

        private Player(User userobj) {
            this.userID = new SimpleStringProperty(Integer.toString(userobj.userID));
            this.userName = new SimpleStringProperty(userobj.userName);
            this.state = new SimpleStringProperty(userobj.state);
            this.password = new SimpleStringProperty(userobj.password);
            this.email = new SimpleStringProperty(userobj.email);
            this.score = new SimpleStringProperty(Integer.toString(userobj.score));
        }

        private Player(String userID, String userName, String password, String email, String score, String state) {
            this.userID = new SimpleStringProperty(userID);
            this.userName = new SimpleStringProperty(userName);
            this.state = new SimpleStringProperty(state);
            this.password = new SimpleStringProperty(password);
            this.email = new SimpleStringProperty(email);
            this.score = new SimpleStringProperty(score);
        }

        public void setUserID(String userId) {
            userID.set(userId);
        }

        public String getUserID() {
            return userID.get();
        }

        public void setScore(String scores) {
            score.set(scores);
        }

        public String getScore() {
            return password.get();
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String emails) {
            email.set(emails);
        }

        public String getUserName() {
            return userName.get();
        }

        public void setUserName(String uName) {
            userName.set(uName);
        }

        public String getState() {
            return state.get();
        }

        public void setState(String states) {
            state.set(states);
        }

        public void setPassword(String passwords) {
            password.set(passwords);
        }

        public String getPassword() {
            return password.get();
        }

    }

    
        
}
