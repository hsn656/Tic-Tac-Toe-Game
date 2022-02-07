/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Hassan
 */
public class User {
    public int userID;
    public String userName;
    public String email;
    public String state;
    public String password;
    public String pic;
    public int score;
    
    public User(){
    
    }
    
    
     public User(int userId,String userName,String email,String password,int score ,String pic,String state){
        this.userID=userId;
        this.userName=userName;
        this.email=email;
        this.state=state;
        this.password=password;
        this.score=score;
        this.pic=pic;
    }

    public User(int userID, String userName, String email, String state, int score) {
        this.userID=userID;
        this.userName=userName;
        this.email=email;
        this.state=state;
        this.score=score;
    }

    public User(String userName, String email,int score, String state ) {
        this.userName = userName;
        this.email= email;
        this.score = score;
        this.state = state;
    }

    public String getUserName(){
        return userName;
    }
    public String getEmail(){
        return email;
    }
    public String getState(){
        return state;
    }
    public int getScore(){
        return score;
    }

}
