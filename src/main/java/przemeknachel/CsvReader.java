package przemeknachel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CsvReader {

    String[][] readData(String filePath) {
        String[] lines = read(filePath);
        String[][] csv = new String[lines.length][];
        int counter = 0;
        for (String line : lines) {
            String[] cells = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            csv[counter++] = cells;
        }
        return csv;
    }

    private String[] read(String filePath) {
        String lines = "";
        try {
            lines = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch(IOException e) {
            System.out.println("No file " + filePath);
        }
        return lines.split("\n");
    }
}