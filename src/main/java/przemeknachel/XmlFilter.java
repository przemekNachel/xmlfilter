package przemeknachel;


public class XmlFilter {


    public XmlFilter(String filePath) {
        String[] file = new CsvReader().read(filePath);
    }

    public boolean check(String fix) {
        return true;
    }
}
