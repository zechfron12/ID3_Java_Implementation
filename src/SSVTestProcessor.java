import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SSVTestProcessor {
    HashMap<String, Integer> attributeToNumberOfValues;
    List<HashMap<String, String>> testExamples;
    String targetAttribute;
    List<String> attributes;
    HashMap<String, HashSet<String>> attributeToValue;

    public SSVTestProcessor(String filePath) {
        testExamples = new ArrayList<>();
        attributes = new ArrayList<>();
        attributeToValue = new HashMap<>();
        attributeToNumberOfValues = new HashMap<>();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);

            processFirstLine(myReader.nextLine());
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if (line.isEmpty()) continue;

                String[] splitLine = line.split(" ");
                if (Objects.equals(splitLine[0], "%")) {
                    for (int i = 1; i < splitLine.length; i += 2) {
                        String attribute = splitLine[i];
                        int numberOfValues = Integer.parseInt(splitLine[i + 1]);
                        attributes.add(attribute);
                        attributeToNumberOfValues.put(attribute, numberOfValues);
                        attributeToValue.put(attribute, new HashSet<>());
                    }
                    targetAttribute = splitLine[splitLine.length - 2];
                } else {
                    HashMap<String, String> instance = new HashMap<>();
                    for (int i = 0; i < splitLine.length; i++) {
                        attributeToValue.get(attributes.get(i)).add(splitLine[i]);
                        instance.put(attributes.get(i), splitLine[i]);
                    }
                    testExamples.add(instance);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void processFirstLine(String line) {
        String[] splitLine = line.split(" ");
        if (Objects.equals(splitLine[0], "%")) {
            for (int i = 1; i < splitLine.length; i += 2) {
                String attribute = splitLine[i];
                int numberOfValues = Integer.parseInt(splitLine[i + 1]);

                attributes.add(attribute);
                attributeToNumberOfValues.put(attribute, numberOfValues);
                attributeToValue.put(attribute, new HashSet<>());
            }
            targetAttribute = splitLine[splitLine.length - 2];
        } else {
            HashMap<String, String> instance = new HashMap<>();
            for (int i = 0; i < splitLine.length; i++) {
                attributes.add("Attribute" + i);
                attributeToValue.put(attributes.get(i), new HashSet<>());
                attributeToValue.get(attributes.get(i)).add(splitLine[i]);
                targetAttribute = attributes.get(i);
                instance.put(attributes.get(i), splitLine[i]);
            }
            testExamples.add(instance);
        }
    }
}
