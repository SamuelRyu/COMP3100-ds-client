import java.io.*;
import java.net.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class Client {

    public static void sendMsg(DataOutputStream dout, String msg) {
        try {
            dout.write(msg.getBytes());
            dout.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void readMsg(DataInputStream din) {
        try {
            byte inMsg[] = new byte[4096];
            din.read(inMsg);
            for (int i = 0; i < inMsg.length; i++) {
                System.out.print((char) inMsg[i]);
            }
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String XMLFileParser() {

        List<Object[]> servlist = new ArrayList<>();

        try {
            File inputFile = new File("ds-system.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputFile);

            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList servernodelist = doc.getElementsByTagName("server");

            for (int i = 0; i < servernodelist.getLength(); i++) {
                Node node = servernodelist.item(i);
                System.out.println("\nNode Name: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element tElement = (Element) node;
                    servlist.add(new Object[] { tElement.getAttribute("type"), tElement.getAttribute("coreCount") });
                    System.out.println("Server Type: " + tElement.getAttribute("type"));
                    System.out.println("Core Count: " + tElement.getAttribute("coreCount"));

                }
            }
            System.out.println("Largest Server" + servlist.get(7)[0]);

        }

        catch (Exception e) {
            System.out.println(e);
        }
        return servlist.get(7)[0].toString();
    }

    public static void performHandshake(DataInputStream din, DataOutputStream dout) {
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
	XMLFileParser();
        sendMsg(dout, "REDY");
        readMsg(din);
        sendMsg(dout, "OK");
        readMsg(din);

        din.close();
        dout.close();
        s.close();
    }

}