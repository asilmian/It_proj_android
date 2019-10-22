package com.example.itemorganizer;


public class UserSingleton {
    private static UserSingleton SINGLETON = null;
    private static String userToken = null;
    private static String familyToken = null;
    private static String name = null;
    private static String email = null;

    public final static String IP = "http://167.71.243.144:5000/";

    private UserSingleton() {
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
    public void setFamilyToken(String t){ familyToken = t;}

    public String getUserToken() {
        return userToken;
    }
    public String getFamilyToken(){ return familyToken; }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getEmail(){return email; }
    public void setEmail(String newEmail){email = newEmail;}

}
