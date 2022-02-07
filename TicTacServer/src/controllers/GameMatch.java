/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.RecordMatch;
import models.UserModel;

/**
 *
 * @author Abdallah
 */
public class GameMatch {
    Socket s1, s2;
    int player1Id, player2Id;
    int playerNumber = 0, cntr = 0;
    int lastPlayerNumber = -1;

    PrintStream[] ps = new PrintStream[2];
    String[][] grid = new String[3][3];
    String[] XO = new String[]{"X", "O"};
    String[] users = new String[2];

    public GameMatch(String u1, String u2, Socket s1, Socket s2) {
        try {
            users[0] = u1;
            users[1] = u2;

            this.s1 = s1;
            this.s2 = s2;

            player1Id = UserModel.playerId(u1);
            player2Id = UserModel.playerId(u2);

            //initialize ps and assign X or O to each player
            ps[0] = new PrintStream(this.s1.getOutputStream());
            ps[0].println(XO[0]);

            ps[1] = new PrintStream(this.s2.getOutputStream());
            ps[1].println(XO[1]);


            Match savedMatch = RecordMatch.getRecordedMatch(player1Id, player2Id);

            if (savedMatch == null) {
                System.out.println("A new Match");
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        grid[i][j] = "-";
                    }
                }
            } else {
                System.out.println("A resumed Match");
                String currentGrid = resumeGame(savedMatch);

                ps[0].println("resume");
                ps[1].println("resume");

                ps[0].println(currentGrid);
                ps[1].println(currentGrid);

                //toggle the last turn 
                String currentTurn = "";
                if (savedMatch.playerTurn == 1) {
                    currentTurn = "X";
                } else {
                    currentTurn = "O";
                }
                ps[0].println(currentTurn);
                ps[1].println(currentTurn);

            }

        } catch (IOException ex) {
            Logger.getLogger(GameMatch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String resumeGame(Match savedMatch) {
        String savedGrid = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = savedMatch.grid[i][j];
                savedGrid += grid[i][j].charAt(0);
            }
        }
        RecordMatch.removeMatch(savedMatch.matchId);
        return savedGrid;
    }
}
