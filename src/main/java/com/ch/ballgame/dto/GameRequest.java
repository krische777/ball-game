package com.ch.ballgame.dto;

import com.google.gson.JsonObject;

public class GameRequest {

    private String method;

    private JsonObject params;
    
    public GameRequest(final String method, final JsonObject params) {
        this.method = method;
        this.params = params;
    }

    public GameRequest() {

    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public JsonObject getParams() {
        return this.params;
    }

    public void setParams(final JsonObject params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "JsonRequest{" +
                "method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
