import java.net.*;
import java.io.*;


public class TCPServer {
    public static void main (String args[]) {
        try {
            int serverPort = 7896; // the server port
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("Server started on port: '7896', ip-address: 'localhost'.");
            System.out.println("Waiting for the client to connect...");
            while(true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Client successfully connected!");

                Connection c = new Connection(clientSocket);
            }
        } catch(IOException e) {
            System.out.println("Listen socket: " + e.getMessage());
        }
    }
}