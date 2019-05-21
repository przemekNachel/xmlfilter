package przemeknachel;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.swing.text.html.HTML;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

public class XmlFilter {


    private final Document doc;
    private String stringToCheck;

    public XmlFilter(String filePath) {
        doc = getDocumentNode(filePath);
    }

    public boolean check(String string) {
        stringToCheck = string;

        Node node = doc.getChildNodes().item(0);
        if(node != null && node.getNodeName().equals("filter")) {
            return evaluate(node);
        }
        return false;
    }

    private boolean evaluate(Node node) {
        for(int i = 0; i < node.getChildNodes().getLength(); i++) {
            switch (node.getChildNodes().item(i).getNodeName()) {
                case "or":
                    return evaluateOr(node.getChildNodes().item(i));

                case "and":
                    return evaluateAnd(node.getChildNodes().item(i));

                case "not":
                    return evaluateNot(node.getChildNodes().item(i));

                case "check":
                    return evaluateCheck(node.getChildNodes().item(i));
            }
        }
        return false;
    }

    private boolean evaluateOr(Node node) {
        return true;
    }

    private boolean evaluateAnd(Node node) {
        return true;
    }

    private boolean evaluateNot(Node node) {
        return true;
    }

    private boolean evaluateCheck(Node node) {
        String type = node.getAttributes().getNamedItem("type").getTextContent();
        String field = node.getAttributes().getNamedItem("field").getTextContent();
        String value = node.getAttributes().getNamedItem("value").getTextContent();
        switch (type) {
            case "equals":
                return getValue(field).equals(value);
            case "in":
                break;
            case "matches":
                break;
        }
        return true;
    }

    private String getValue(String field) {
        String tag = String.valueOf(new TagTranslator().getTag(field));
        for(String s : stringToCheck.split("\\|")) {
            if(!s.equals("")) {
                String currentTag = s.split("=")[0];
                String value = s.split("=")[1];
                if(currentTag.equals(tag)) return value;
            }
        }
        return null;
    }

    private Document getDocumentNode(String filePath) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
