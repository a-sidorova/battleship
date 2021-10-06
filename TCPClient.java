import java.net.*;
import java.io.*;
import java.util.Scanner;


public class TCPClient {
    public static void main (String args[]) {
        Socket s = null;
        try {
            int serverPort = 7896;
            String serverIp = "localhost";
            s = new Socket(serverIp, serverPort);
            System.out.println("Successful connection to the server (port: '7896', ip-address: 'localhost')");

            DataInputStream in = new DataInputStream( s.getInputStream());
            DataOutputStream out = new DataOutputStream( s.getOutputStream());

            Scanner scanner = new Scanner(System.in);
           	Game.launch(in, out, scanner, false);
        } catch (UnknownHostException e) {
        	System.out.println("UnknownHostException:" + e.getMessage());
        } catch (EOFException e) {
        	System.out.println("EOFException:" + e.getMessage());
        } catch (IOException e) {
        	System.out.println("IOException:" + e.getMessage());
        } finally {
        	if (s != null)
        		try {
        			s.close();
        		} catch (IOException e) {
        			System.out.println("IOException [on close socket]: " + e.getMessage());}
        }
    }
}