package hybrid.model.builder;

import hybrid.model.IDDefinedEntity;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Director {

    private CSVParser csvParser;
    private Iterator<CSVRecord> recordIterator;
    private CSVRecord currentRecord;
    private boolean isOpen = false;

    public Director() {
    }

    public static List<IDDefinedEntity> importFromCSV(String csvFilePath) {
        List<IDDefinedEntity> entities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                System.out.println("Read line: '" + line + "'");
                String[] fields = line.split(",");
                System.out.println("Split into " + fields.length + " fields: " + Arrays.toString(fields));
                if (fields.length >= 3) {
                    IDDefinedEntity entity = parseLineToEntity(fields);
                    if (entity != null) {
                        entities.add(entity);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    private static IDDefinedEntity parseLineToEntity(String[] fields) {
        try {
            int id = Integer.parseInt(fields[0].trim());
            System.out.println(id);
            String type = fields[1].trim();
            String name = fields[2].trim();
            System.out.println(name);
            Integer parentId = parseInteger(fields[3].trim());
            IDDefinedEntity.Builder builder;

            switch(type) {
                case "Contest":
                    builder = new ContestBuilder();
                    break;
                case "Site":
                    builder = new SiteBuilder();
                    break;
                default:
                    System.out.println("No type present");
                    return null;
            }

            return builder.addId(id)
                    .addName(name)
                    .addParentContestId(parentId)
                    .build();
        } catch (Exception e) {
            System.err.println("Error parsing line: " + Arrays.toString(fields));
            return null;
        }
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.equals("null") || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }


}
