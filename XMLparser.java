import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLparser {
    public static void main(String[] args) {

        List<Object[]> servlist = new ArrayList<>();
        // servlist.add(new Object[] { "tiny", 1 });
        // System.out.println("Server type " + servlist.get(0)[0]);

        try {
            File inputFile = new File("./ds-system.xml");

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
                    System.out.println("Server Type: " + tElement.getAttribute("type"));
                    System.out.println("Core Count: " + tElement.getAttribute("coreCount"));
                }
            }
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }

}
