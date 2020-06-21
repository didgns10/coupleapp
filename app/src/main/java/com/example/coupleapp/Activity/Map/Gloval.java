package com.example.coupleapp.Activity.Map;

public class Gloval {
    private static double latitude = 37.484103063360365;
    private static double longitude =126.97367727756499;
    private static String state=null;

    public static String getState() {
        return state;
    }

    public static void setState(String state) {
        Gloval.state = state;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        Gloval.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        Gloval.longitude = longitude;
    }
}
