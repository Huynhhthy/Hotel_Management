package com.example.cusapphotel.model;

public class Loginstatus {
    private static Loginstatus instance;
    private boolean loggedIn = false;
    private  String userID = null;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private Loginstatus(){}

    public static synchronized Loginstatus getInstance(){
        if(instance == null){
            instance = new Loginstatus();
        }
        return instance;
    }

    public void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }
}
