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
    private static int index;
    private TagTranslator tagTranslator = new TagTranslator();

    public static void main(String[] args) {
        index = 1;
        System.out.println(String.valueOf(index) + ". " + new XmlFilter("filter.xml").check("8=FIX.4.4|9=395|35=8|49=SENDER|56=TARGET|52=20160101-10:16:11.877|122=20160101-10:16:11|34=15425|115=BAPP|17=12345678-1|527=321765860354|150=F|31=16.00|32=166|30=DSMD|29=1|54=1|10006=1|60=20160101-10:16:11.000|382=1|375=AERT|75=20160101|775=0|119=2686.00|55=N/A|14=0|6=0|528=A|22=100|48=0003012345|10=228|"));
        System.out.println(String.valueOf(index) + ". " + new XmlFilter("filter.xml").check("8=FIX.4.4|9=391|35=8|49=SENDER|56=TARGET|52=20160101-10:50:59.929|122=20160101-10:50:59|34=15697|115=BAPP|17=98765421-1|527=000127251481|150=F|31=250.00|32=400|30=XOTC|29=5|54=1|10006=1|60=20160101-10:50:58.000|382=1|375=12345|75=20160101|775=0|119=100000.00|55=N/A|14=0|6=0|528=R|22=100|48=0002912345|10=153|"));
        System.out.println(String.valueOf(index) + ". " + new XmlFilter("filter.xml").check("8=FIX.4.4|9=388|35=8|49=SENDER|56=TARGET|52=20160101-13:31:17.181|122=20160101-13:31:17|34=29430|115=BAPP|17=54789787-1|527=000021454874|150=F|31=250.00|32=92|30=XYZX|29=5|54=1|10006=1|60=20160101-13:31:14.000|382=1|375=12345|75=20160101|775=0|119=23000.00|55=N/A|14=0|6=0|528=R|22=100|48=0002912345|10=036|"));
        System.out.println(String.valueOf(index) + ". " + new XmlFilter("filter.xml").check("8=FIX.4.4|9=395|35=8|49=SENDER|56=TARGET|52=20160101-13:17:27.556|122=20160101-13:17:27|34=29374|115=BAPP|17=216546544-1|527=37218187454213|150=F|31=42.00|32=110|30=DSMD|29=1|54=1|10006=1|60=20160101-13:17:27.000|382=1|375=AERT|75=20160101|775=0|119=4650.00|55=N/A|14=0|6=0|528=A|22=100|48=0003012345|10=052|"));
        System.out.println(String.valueOf(index) + ". " + new XmlFilter("filter.xml").check("8=FIX.4.4|9=378|35=8|49=SENDER|56=TARGET|52=20160101-10:31:13.872|122=20160101-10:31:13|34=18926|115=ASDF|17=32165478-1|527=RTE321654654|150=F|31=0.85|32=3|30=XXXX|29=4|54=2|10006=1|60=20160101-10:31:12.000|375=10098|75=20160101|775=0|119=2.55|55=N/A|14=0|6=0|528=A|22=100|48=123456789|10=166|"));
        System.out.println(String.valueOf(index) + ". " + new XmlFilter("filter.xml").check("8=FIX.4.4|9=348|35=8|49=SENDER|56=TARGET|52=20160101-10:31:14.035|122=20160101-10:31:14|34=18927|115=HW-ASDF1|17=456123485-1|527=TYU321655454|150=F|31=0.85|32=3|30=XXYY|29=4|54=1|10006=1|60=20160101-10:31:12.000|382=1|375=1238|75=20160101|775=E|55=N/A|14=0|6=0|528=Y|22=100|48=12456789|10=140|"));
    }

    public XmlFilter(String filePath) {
        doc = getDocumentNode(filePath).getChildNodes().item(0);
    }

    public boolean check(String string) {
        stringToCheck = string;
        System.out.println(index++ + ". " + stringToCheck);

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
        String currentValue = getValue(field);
        boolean result = false;
        switch (type) {
            case "equals":
                result = getValue(field).equals(value);
            case "in":
                result = Arrays.asList(value.split(",")).contains(getValue(field));
            case "matches":
                result = getValue(field).matches(value);
        }
        System.out.println("type: " + type + "    field: " + field + "/" + tagTranslator.getTag(field) + "    value: " + value + "    currentValue: " + currentValue + " " + result);
        return result;
    }

    private String getValue(String field) {
        String tag = String.valueOf(tagTranslator.getTag(field));
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
