package com.github.steadiestllama.xfm2gui.instancehandling;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
Adapted from here:
https://www.rgagnon.com/javadetails/java-0288.html
 */

public class SingleInstanceServer extends Thread{
    // you may need to customize this for your machine
    public static final int PORT = 271;

    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    @Override
    public void run() {
        try {
            // Create the server socket
            serverSocket = new ServerSocket(PORT, 1);
            while (true) {
                // Wait for a connection
                clientSocket = serverSocket.accept();
                // System.out.println("*** Got a connection! ");
                clientSocket.close();
            }
        }
        catch (IOException ioe) {
            System.out.println("Error in JustOneServer: " + ioe);
        }
    }
}
