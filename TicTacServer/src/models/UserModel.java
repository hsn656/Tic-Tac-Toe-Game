/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hassan
 */
public class UserModel {
     static final String DB_URL = "jdbc:mysql://localhost:3306/user_base";
   static final String DB_DRV = "com.mysql.cj.jdbc.Driver";
   static final String DB_USER = "abdallah";
   static final String DB_PASSWD = "root";

  
    public static Connection connect() throws SQLException {
       return (Connection) DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
   }
    
    
    public static boolean validatePlayer(String username, String password) {
       try {
           Connection connection = connect();
           Statement statement = (Statement) connection.createStatement();
           ResultSet resultSet=statement.executeQuery("SELECT * FROM users order by Points DESC");
           boolean playerNameFound=false,correctPassword=false;
           while(resultSet.next()){
               System.out.println(resultSet.getString("User_Name"));
               if(resultSet.getString("User_Name").equals(username)){
                   playerNameFound=true;
                   if(resultSet.getString("Password").equals(password))
                   {
                       correctPassword=true;
                   }
                   break;
               }
             
           } 
           resultSet.close();
           connection.close();
           return correctPassword & playerNameFound;
       } catch (SQLException ex) {
           Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
       }
       return false;
   }
    
    
     public static boolean addPlayer(User user) {
       try {
           Connection connection = connect();
           
           PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO users ( User_Name, Email,Password,Points,User_Pic,State) VALUES (?,?,?,?,?,?)");
           
           preparedStatement.setString(1, user.userName);
           preparedStatement.setString(2, user.email);
           preparedStatement.setString(3, user.password);
           preparedStatement.setInt(4, user.score);
           preparedStatement.setString(5,"");//hna lsa 7owar l soora
           preparedStatement.setString(6,user.state);
           
           int res=preparedStatement.executeUpdate(); 
           preparedStatement.close();
           connection.close();
           return res>0;
       } catch (SQLException ex) {
           Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
       }
       return false;
   }
   
  
    public static boolean updatePlayerState(String username,String state) {
       try {
           Connection connection = connect();
           PreparedStatement preparedStatement=connection.prepareStatement("UPDATE users SET State =? where User_name=? ");
           System.out.println(username);
           System.out.println(state);
           preparedStatement.setString(1, state);
           preparedStatement.setString(2, username);
           int res=preparedStatement.executeUpdate();  
           preparedStatement.close();
           connection.close();
           return res>0;
           
       } catch (SQLException ex) {
           Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
       }
      return false;
   }

    public static User playerInfo(String username) {
       try {
           Connection connection = connect();
           Statement statement = (Statement) connection.createStatement();
           ResultSet resultSet=statement.executeQuery("SELECT * FROM users");
           
           
           while(resultSet.next()){
               
               if(resultSet.getString("User_Name").equals(username)){
                   User user=new User(resultSet.getInt("User_ID"),resultSet.getString("User_Name"),resultSet.getString("Email"),resultSet.getString("Password"),resultSet.getInt("Points"),resultSet.getString("User_Pic"), resultSet.getString("State"));
                   resultSet.close();
                   connection.close();
                   return user;
               }
           }
          
       } catch (SQLException ex) {
           Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
       }
        return null;
   }
   
    public static Vector<User> returnAllPlayers() {
       try {
           Vector<User>tmp=new Vector<User>();
           
           Connection connection = connect();
           Statement statement = (Statement) connection.createStatement();
           ResultSet resultSet=statement.executeQuery("SELECT * FROM users");
           while(resultSet.next()){
               
               User currentPlayer=new User(resultSet.getInt("User_ID"),resultSet.getString("User_Name"),resultSet.getString("Email"),resultSet.getString("Password"),resultSet.getInt("Points"),resultSet.getString("User_Pic"), resultSet.getString("State"));
               tmp.add(currentPlayer);
           }  
           resultSet.close();
           connection.close();
           return tmp;
       } catch (SQLException ex) {
           Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;
  }
   
    
}




