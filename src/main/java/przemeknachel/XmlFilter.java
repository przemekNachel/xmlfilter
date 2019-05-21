package przemeknachel;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class XmlFilter {


    private final Node doc;
    private String stringToCheck;

    public XmlFilter(String filePath) {
        doc = getDocumentNode(filePath).getChildNodes().item(0);
    }

    public boolean check(String string) {
        stringToCheck = string;
        if(doc != null && doc.getNodeName().equals("filter")) {
            return evaluate(doc.getChildNodes().item(1));
        }
        System.out.println("no filter tag");
        return false;
    }


    private boolean evaluate(Node node) {
        return evaluateSingleNode(node);
    }

    private boolean evaluateSingleNode(Node node) {
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case "or":
                return evaluateOr(node);

            case "and":
                return evaluateAnd(node);

            case "not":
                return evaluateNot(node);

            case "check":
                return evaluateCheck(node);
        }
        return false;
    }

    private boolean evaluateOr(Node node) {
        for(int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if(isCorrectNode(child) && evaluate(child)) {
                return true;
            }
        }
        return false;
    }

    private boolean evaluateAnd(Node node) {
        for(int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if(isCorrectNode(child) && !evaluate(child)) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateNot(Node node) {
        for(int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if(isCorrectNode(child)) {
                boolean df = !evaluate(child);
                return df;
            }
        }
        return true;
    }

    private boolean isCorrectNode(Node node) {
        String name = node.getNodeName();
        String[] correctNames = new String[]{"and", "or", "not", "check"};
        if (Arrays.asList(correctNames).contains(name)) return true;
        return false;
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
