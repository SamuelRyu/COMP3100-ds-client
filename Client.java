import java.io.*;
import java.net.*;

class Client{

    public static void main(String args[]) throws Exception {
        Socket s = new Socket("localhost", 30000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
    }

}