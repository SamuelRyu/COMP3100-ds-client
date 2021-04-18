import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class Client {

    static List<Object[]> servlist = new ArrayList<>();

    public static void sendMsg(DataOutputStream dout, String msg) {
        try {
            byte[] message = msg.getBytes();
            message = Arrays.copyOf(message, message.length + 1);
            message[message.length - 1] = 10;
            dout.write(message);
            System.out.println("SENT " + msg);
            dout.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readMsg(DataInputStream din) {
        String message = "";
        try {
            byte inBytes[] = new byte[din.available() + 64];
            din.read(inBytes);
            for (int i = 0; i < inBytes.length; i++) {
                message += (char) inBytes[i];
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        System.out.println("RCVD " + message);
        return message;
    }

    // Reads XML file by traversing the file tree hierarchically
    public static void retrieveXML() {
        try {
            File inputFile = new File("./ds-system.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputFile);

            doc.getDocumentElement().normalize();

            NodeList servernodelist = doc.getElementsByTagName("server");

            for (int i = 0; i < servernodelist.getLength(); i++) {
                Node node = servernodelist.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element tElement = (Element) node;
                    servlist.add(new Object[] { tElement.getAttribute("type"), tElement.getAttribute("coreCount"), tElement.getAttribute("limit") });
                }
            }
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }

    // Pull largest server name
    public static String XMLFileParser() {
        return servlist.get(servlist.size() - 1)[0].toString();
    }

    // Pull limit of largest server
    public static String XMLLimit() {
        return servlist.get(servlist.size() - 1)[2].toString();
    }

    public static void performHandshake(DataInputStream din, DataOutputStream dout) {
        sendMsg(dout, "HELO");
        readMsg(din);
        sendMsg(dout, "AUTH " + System.getProperty("user.name"));
        readMsg(din);
    }

    public static void main(String args[]) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        // Pull ds-system.xml, grab type, coreCount and limit
        retrieveXML();

        // Start handshake
        performHandshake(din, dout);

        // Ready to start receiving jobs
        sendMsg(dout, "REDY");
        int count = 0;
        String response = readMsg(din);
        while (!response.contains("NONE")) {
            if (response.contains("JOBN")) {
                sendMsg(dout, "SCHD " + response.split("\\s+")[2] + " " + XMLFileParser() + " " + count);
                response = readMsg(din);

                // Check for errors, if error found, send to the next server
                if (response.contains("ERR")) {
                    count++;
                    // Reset count to send to first server, otherwise will reach out of bounds and try to send to servers that dont exist
                    if (count == Integer.parseInt(XMLLimit())) {
                        count = 0;
                    }
                    sendMsg(dout, "SCHD " + response.split("\\s+")[2] + XMLFileParser() + " " + count);
                    response = readMsg(din);
                }

            }
            // Ready for next job
            sendMsg(dout, "REDY");
            response = readMsg(din);
        }

        // Quit
        sendMsg(dout, "QUIT");
        readMsg(din);

        din.close();
        dout.close();
        s.close();
    }

}