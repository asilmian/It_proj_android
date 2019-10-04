package com.example.itemorganizer;


public class UserSingleton {
    private static UserSingleton SINGLETON = null;
    private static String userToken;

    private UserSingleton(){
        userToken = null;
    }

    public static UserSingleton getInstance(){
        if (SINGLETON == null){
            synchronized (UserSingleton.class){
                SINGLETON = new UserSingleton();
            }
        }
        return SINGLETON;
    }



    public void setUserToken(String token){
        userToken = token;
    }

    public String getUserToken(){
        return userToken;
    }
}
