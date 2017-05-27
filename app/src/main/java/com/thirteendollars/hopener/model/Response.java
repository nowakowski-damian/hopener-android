package com.thirteendollars.hopener.model;

/**
 * Created by Damian Nowakowski on 03/05/2017.
 * mail: thirteendollars.com@gmail.com
 */

public class Response {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response(String message) {
        this.message = message;
    }
}
