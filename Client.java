import java.io.*;
import java.net.*;


class Client{

    public static void sendMsg(DataOutputStream dout, String msg){
        try{
            dout.write(msg.getBytes());
            dout.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public static void readMsg(DataInputStream din){
        try{
            din.readLine();

            byte inMsg[] = new byte[1024];
            din.read(inMsg);
            for(int i = 0; i < inMsg.length; i ++){
                System.out.print((char)inMsg[i]);
            }
            System.out.println("");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void performHandshake(DataInputStream din, DataOutputStream dout){
        sendMsg(dout, "HELO");
        readMsg(din);
        sendMsg(dout, "AUTH username");
        readMsg(din);
    }



    public static void main(String args[]) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        performHandshake(din, dout);

        sendMsg(dout, "REDY");
        readMsg(din);
        sendMsg(dout, "QUIT");
        readMsg(din);

        din.close();
        dout.close();
        s.close();
    }

}