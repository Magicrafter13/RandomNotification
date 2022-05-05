# Random Notification

## Members

Griffen Agnello, Cyrus Santiago, Daniel Belousov, Garett Loghry, Matthew Rease

## Description

This is the Android app and server source code for the CS455 networking group project. The app and server backend are built using Java. The app currently runs on API 30 R. When you load up the app, the app will try to connect over the network via TCP to the master server (currently the remote address is set as "matthewrease.net") on port 4555 by sending a "handshake" message to it. Once connected, you may press the "Notify!" button in the center of the screen to send a notification randomly to another user who is connected to the server. You may also change your name by typing in another name in the text field at the top. The client sends a "notification" message to the server, and then the server designates randomly who to send the notification to.

## Code Organization - Class Classifications
### /RandomNotification/app/src
- Main Activity:        The main method that loads up the app screen and notification channel and manager. <br/>
- NotificationListener: Used to send the notification based on a title and body of the message. <br/>
- Server:               Houses all the code used to connect to the master server. Creates a thread for handling the connection. <br/>
- serverConnect:        Builds the notification message as well as sends it based on the channel and listener. <br/>
- ServerData:           Contains LiveData. Handles messages to send and receive information from the server. <br/>

### /server/
- Server.java:                  Creates the master server and listens for incoming messages from clients. <br/>
- /server/ClientCollector.java: Manages all client connections. <br/>
- /server/Client.java:          Creates a client object to interact with the server <br/>

## How to Run

Open the project folder "Random Notification" in Android Studio. Click `Make`->`Make project` and then `Run`->`Run 'app'` after the app has finished building. You will need two devices to connect to the app in order to make this work properly. If there is only 1 user online, all notifications will be sent to themselves. The server will also need to be turned on in order to send and receive notifications. Hopefully it will be running already, however the server code in `/server/` has everything needed to start the server. The address host could be changed to something like `localhost`.

## Workload

- Griffen:  Wrote the whole README, contributed conceptually to the project idea, and researched and implemented how notifications are sent on Android.<br/>
- Matthew:  Master Server and client connection backend.<br/>
- Cyrus:    Worked on front end and changed a new color scheme and theme, and contributed conceptually.<br/>
- Garett:   Worked on front end and added a credits box on the main screen.<br/>
- Daniel:   Added the ability to change the name sent with the notifications. Also contributed significantly conceptually. Had the original idea for the project.
