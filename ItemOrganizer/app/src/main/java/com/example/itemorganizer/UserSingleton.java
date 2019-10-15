package com.example.itemorganizer;


public class UserSingleton {
    private static UserSingleton SINGLETON = null;
    private static String userToken;
    private static String currToken;
    public final static String IP = "http://167.71.243.144:5000/";

    private UserSingleton() {
        userToken = null;
    }

    public static UserSingleton getInstance() {
        if (SINGLETON == null) {
            synchronized (UserSingleton.class) {
                SINGLETON = new UserSingleton();
            }
        }
        return SINGLETON;
    }


    public void setUserToken(String token) {
        userToken = token;
    }
    public void setCurrToken(String t){ currToken = t;}

    public String getUserToken() {
        return userToken;
    }
    public String getCurrToken(){ return currToken; }

}
