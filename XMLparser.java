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
                    servlist.add(new Object[] { tElement.getAttribute("type"), tElement.getAttribute("coreCount") });
                }
            }
        }

        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(servlist.get(servlist.size() - 1)[0].toString());
    }

}
