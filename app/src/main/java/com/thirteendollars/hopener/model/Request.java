package com.thirteendollars.hopener.model;

/**
 * Created by Damian Nowakowski on 03/05/2017.
 * mail: thirteendollars.com@gmail.com
 */

//        "uuid" : "*",
//        "device" : "garage/fence",
//        "activity" : "open/close/stop"

public class Request {

    public static final String DEVICE_GARAGE = "garage";
    public static final String DEVICE_FENCE = "fence";
    public static final String ACTIVITY_OPEN = "open";
    public static final String ACTIVITY_CLOSE = "close";
    public static final String ACTIVITY_STOP= "stop";

    private String uuid;
    private String device;
    private String activity;

    public Request(String uuid, String device, String activity) {
        this.uuid = uuid;
        this.device = device;
        this.activity = activity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
