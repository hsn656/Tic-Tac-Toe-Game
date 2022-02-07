/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import controllers.Match;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdallah
 */
public class RecordMatch {
    static final String DB_URL = "jdbc:mysql://localhost:3306/user_base";
    static final String DB_DRV = "com.mysql.jdbc.Driver";
    static final String DB_USER = "abdallah";
    static final String DB_PASSWD = "root";

    
    public static Connection connect() throws SQLException {
       return (Connection) DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
   }

    public static boolean removeMatch(int matchId){
       try {
           Connection connection = connect();
           PreparedStatement preparedStatement=connection.prepareStatement("DELETE FROM recorded_match where Match_ID =?");
           preparedStatement.setInt(1, matchId);
            
           int res=preparedStatement.executeUpdate();
           
           connection.close();
           preparedStatement.close();
           return res>0;
       } catch (SQLException ex) {
           Logger.getLogger(RecordMatch.class.getName()).log(Level.SEVERE, null, ex);
       }
           return false;
    }

    public static Match getRecordedMatch(int playerId1,int playerId2) {
       try {
           Connection connection = connect();
           Statement statement =connection.createStatement();
           ResultSet resultSet=statement.executeQuery("SELECT * FROM recorded_match");
           String[][] grid= new String[3][3];
           while(resultSet.next()){
               if(resultSet.getInt("User1_ID")==playerId1 && resultSet.getInt("User2_ID")==playerId2  ){
                   
                   for(int i=0;i<3;i++){
                       for(int j=0;j<3;j++){
                           String colName="Cell";
                           int x= i+(3*j);
                           x++;
                           colName+=String.valueOf(x);
                           grid[i][j]=resultSet.getString(colName);
                       }
                   }
                   Match m= new Match(grid,resultSet.getInt("User1_ID"),resultSet.getInt("User2_ID"),resultSet.getInt("Match_ID"),resultSet.getInt("player_turn"));
                   resultSet.close();
                   return m;
               }
           }
       } catch (SQLException ex) {
           Logger.getLogger(RecordMatch.class.getName()).log(Level.SEVERE, null, ex);
       }
        return null;
    }
}
