import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Client{

    public static void sendMsg(DataOutputStream dout, String msg){
        try{
            dout.write(msg.getBytes());
            dout.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public static String readMsg(DataInputStream din){
        String message = "";
        try{
            byte inBytes[] = new byte[din.available()];
            din.read(inBytes);
            for(int i = 0; i < inBytes.length; i ++){
                System.out.print((char)inBytes[i]);
                message += (char)inBytes[i];
            }
            System.out.println("");

        }catch(Exception e){
            e.printStackTrace();
        }
        return message;
    }

    public static void performHandshake(DataInputStream din, DataOutputStream dout){
        sendMsg(dout, "HELO");
        readMsg(din);
        sendMsg(dout, "AUTH sam");
        readMsg(din);
    }

    public static void main(String args[]) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        performHandshake(din, dout);
        sendMsg(dout, "REDY");
        readMsg(din);
        sendMsg(dout, "GETS All");
        readMsg(din);
        sendMsg(dout, "OK");
        readMsg(din);
        sendMsg(dout, "OK");
        readMsg(din);
        

        sendMsg(dout, "OK");
        sendMsg(dout, "QUIT");
        readMsg(din);

        din.close();
        dout.close();
        s.close();
    }

}

