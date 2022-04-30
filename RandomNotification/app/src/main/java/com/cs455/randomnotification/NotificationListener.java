package com.cs455.randomnotification;

public interface NotificationListener {
    /**
     * Called when a notification has been received.
     */
    void onNotify(String title, String body);
}
