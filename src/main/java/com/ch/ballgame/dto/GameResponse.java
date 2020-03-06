package com.ch.ballgame.dto;

public class GameResponse {

    private String responseMethod;

    private String result;

    private String error;

    public GameResponse() {

    }

    public GameResponse(String method, final String result) {
        this.responseMethod = method;
        this.result = result;
    }

    public GameResponse(final String responseMethod, final String error, final String result) {
        this.responseMethod = responseMethod;
        this.error = error;
        this.result = result;
    }

    public String getResponseMethod() {
        return this.responseMethod;
    }

    public void setResponseMethod(final String responseMethod) {
        this.responseMethod = responseMethod;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public void setError(final String error) {
        this.error = error;
    }
}
