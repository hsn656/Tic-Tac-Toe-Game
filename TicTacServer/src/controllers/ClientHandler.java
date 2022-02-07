/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;
import models.UserModel;
import tictacserver.TicTacServer;

/**
 *
 * @author Hassan
 */
public class ClientHandler extends Thread {
    BufferedReader dis;
    PrintWriter ps;
    Socket s;
    User user;
    boolean serverOn;
    static Vector<User> playersList = new Vector<>();
    static Vector<String> onlinePlayersUNames = new Vector<>();
    static Vector<ClientHandler> onlinePlayers = new Vector<>();
    static Vector<GameMatch> gameMatches = new Vector<>();
  //  GameMatch gameMatch = null;
    int gameIndex;
  //static Vector<GameMatch> gameMatches = new Vector<>();
    int invitationIndex;
    static Vector<String> invitationStatus = new Vector<>();
    
       public ClientHandler(Socket s) {
        user = new User();
        try {
            this.s = s;
            dis = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
            ps = new PrintWriter(s.getOutputStream(), true);
            serverOn=true;
            System.out.println("new socket for player connected and new handler initated");
            start();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        @Override
    public void run() {
        String mssg="";
        int i = 0;
        while (serverOn) {
                System.out.println("waiting for new message");
            try {
                mssg = dis.readLine();

                if (mssg.equals("login")) {
                    System.out.println("trying to login");
                    signIn();
                } else if (mssg.equals("signup")) {
                    System.out.println("request signup");
                    signUP();
                } else if (mssg.equals("invite")) {
                    String invitedPlayerUserName = dis.readLine();
                    invitePlayer(invitedPlayerUserName);
                }
            }catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
//           
//                } else if (mssg.equals("logout")) {
//
//                    System.out.println("request logout");
//                    sendUpdatetoAllClients("logout");
//                    closePlayerConnection(this, "OFFLINE");
//
//                } else if (isNumeric(mssg) || mssg.contains("chat") || mssg.equals("pause")) {                    //send movement
//                    System.out.println("Play Movement");
//                    gameMatches.get(gameIndex).sendNewMove(mssg);
//                } else if (mssg.equals("accept") || mssg.equals("refused")) {
//                    //client accept or refuse invitation from another thread
//                    invitationStatus.set(invitationIndex, mssg);
//                } else {
//                    System.out.println("unknown operation");
//                    System.out.println(mssg);
//                }
//           
        }

    }
private void signIn() {
     String userName = "", password = "";
        try {
            userName = dis.readLine();
            password = dis.readLine();
            if (UserModel.validatePlayer(userName, password)) {

                UserModel.updatePlayerState(userName, "ONLINE");
                user = getUserInfo(userName);
                onlinePlayers.add(this);
                onlinePlayersUNames.add(userName);

                ps.println("loginDone");
                sendUserInfo(this.ps, user);
                //refresh players list and send the player list to the player
                refreshPlayersList();
                sendPlayersList();
                TicTacServer.updatePlayersTable();
                sendUpdatetoAllClients("login");
                System.out.println(userName+" has logged in");
            } else {
                ps.println("loginFailed");
                System.out.println(userName+" could not login");
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendUpdatetoAllClients(String updateState) {
        for (ClientHandler player : onlinePlayers) {
            if (player.user.userID != user.userID) {
                player.ps.println("updateStatus");
                player.ps.println(this.user.userName);
                player.ps.println(updateState);
                player.ps.println(playersList.size() - 1);
                for (int i = 0; i < playersList.size(); i++) {
                    if (playersList.get(i).userID != player.user.userID) {
                        sendUserInfo(player.ps, playersList.get(i));
                    }
                }
            }
        }
    }

    private User getUserInfo(String userName) {
        return UserModel.playerInfo(userName);
    }
    
    public void sendUserInfo(PrintWriter ps, User user) {
        ps.println(user.userID);
        ps.println(user.userName);
        ps.println(user.email);
        ps.println(user.state);
        ps.println(user.score);
    }

    public void refreshPlayersList() {
        playersList = UserModel.returnAllPlayers();
    }
    
    public void sendPlayersList() {
        ps.println(playersList.size() - 1);
        for (int i = 0; i < playersList.size(); i++) {
            if (playersList.get(i).userID != user.userID) {
                System.out.println("send player " + playersList.get(i).userName);
                sendUserInfo(this.ps, playersList.get(i));
            }
        }
    }

    public static void closeAllInternalSockets() {
        for (ClientHandler player : onlinePlayers) {
            player.ps.println("serveroff");
            player.serverOn=false;
            closePlayerConnection(player, "OFFLINE");
            try {
                player.s.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void closePlayerConnection(ClientHandler handler, String state) {
        try {
            System.out.println(handler.user.userName);
            UserModel.updatePlayerState(handler.user.userName, state);
            handler.dis.close();
            handler.ps.close();
            TicTacServer.updatePlayersTable();
            handler.stop();
            
            onlinePlayers.remove(handler);
            onlinePlayersUNames.remove(handler.user.userName);
            playersList.remove(handler);

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public void signUP() {
        try {
            user.userName = dis.readLine();
            user.password = dis.readLine();
            user.email = dis.readLine();
            user.score = 0;
            user.state = "OFFLINE";
            refreshPlayersList();
            //refresh data in server GUI
            //send updates to clients and server
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!(user.userName.isEmpty() && user.email.isEmpty() && user.password.isEmpty())) {
            if (UserModel.addPlayer(user)) {
                ps.println("signupDone");
                TicTacServer.updatePlayersTable();
            } else {
                ps.println("signupFailed");
            }
        } else {
            ps.println("signupFailed");
        }

    }

    public void invitePlayer(String userName) {
        ClientHandler invitedClient = onlinePlayers.get(onlinePlayersUNames.indexOf(userName));
        invitedClient.ps.println("invitation");
        invitedClient.ps.println(this.user.userName);
        invitedClient.ps.println(this.user.score);

        try {
            this.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        invitationStatus.add(new String("waiting"));
        this.invitationIndex = invitationStatus.size() - 1;
        invitedClient.invitationIndex = invitationStatus.size() - 1;
        while (invitationStatus.get(invitationIndex).equals("waiting"));
        String invitatonReply = invitationStatus.get(invitationIndex);
//        String invitatonReply = "accept";

        if (invitatonReply.equals("accept")) {
            //recive accept
            this.ps.println("invitationAccepted");
            try {
                this.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            gameMatches.add(new GameMatch(this.user.userName, userName, this.s, invitedClient.s));
            this.gameIndex = gameMatches.size() - 1;
            invitedClient.gameIndex = gameMatches.size() - 1;
        } else {
            this.ps.println("invitationRefused");
        }
    }    
}
