import java.io.*;
import java.net.*;
import java.util.Scanner;


class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public Connection (Socket _ClientSocket) {
        try {
            clientSocket = _ClientSocket;
            in = new DataInputStream( clientSocket.getInputStream());
            out = new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }
    public void run() { // an echo server
        try {
            Scanner scanner = new Scanner(System.in);
            Game.launch(in, out, scanner, true);
        } catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        } catch (IOException e) {System.out.println("readline:"+e.getMessage());
        }
    }
}