/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Abdallah
 */
public class Match {
    public int player1Id,player2Id, matchId,playerTurn;
    public String[][] grid={{"","",""},{"","",""},{"","",""}};
    public Match(String[][]grid,int player1Id,int player2Id,int matchId,int playerTurn){
        this.grid=grid;
        this.player1Id=player1Id;
        this.player2Id=player2Id;
        this.matchId=matchId;
        this.playerTurn=playerTurn;
     }
}
