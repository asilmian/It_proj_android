package com.example.itemorganizer;

import java.util.HashMap;


//backend item used to create request to backend

public class BackendItem {

    private String url;
    private String body;
    private HashMap<String, String> headers;

    private Integer response_code;
    private String response;
    private String method;



    public BackendItem(String url, String method){
        this.url = url; this.method = method; this.body = null;
    }

    //getters and setters
    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        headers.putIfAbsent("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        headers.putIfAbsent("User-Agent", "Android App");
        this.headers = headers;
    }

    public Integer getResponse_code() {
        return response_code;
    }

    public void setResponse_code(Integer response_code) {
        this.response_code = response_code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMethod() {
        return method;
    }


}
