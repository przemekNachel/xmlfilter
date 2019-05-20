package przemeknachel;

import java.util.HashMap;

public class TagTranslator {

    private HashMap<Integer, String> names = new HashMap<>();
    private HashMap<String, Integer> tags = new HashMap<>();

    public TagTranslator() {
        for(String[] line : new CsvReader().readData("tags.csv")) {
            String name = line[1].replaceAll("[^a-zA-Z0-9]","");
            Integer tag = new Integer(line[0]);
            names.put(tag, name);
            tags.put(name, tag);
        }
    }

    public String getName(int tag) {
        return names.get(tag);
    }

    public Integer getTag(String name) {
        return tags.get(name);
    }
}
