import java.util.*;

public class Main {
    public static Attribute targetAttribute;
    public static String positiveLabel;
    public static long totalPositiveCases;
    public static String negativeLabel;
    public static long totalNegativeCases;
    public static long totalCases;
    public static Double targetAttributeEntropy;

    protected static List<Attribute> attributes = new ArrayList<>();

    public static Node id3(Examples examples, Attribute targetAttribute, List<Attribute> attributes) {
        Node root = new Node();

        if (examples.areAllPositive()) {
            root.setLabel(positiveLabel);
        } else
        if (examples.areAllNegative()) {
            root.setLabel(negativeLabel);

        } else
        if (attributes.isEmpty()) {
            root.setLabel(examples.getCountOfPositiveCases() > examples.getCountOfNegativeCases() ? positiveLabel : negativeLabel);
            return root;
        } else {
            List<Attribute> sortedAttributes = attributes.stream().sorted(Comparator.comparing(attribute -> attribute.getEntropy(examples))).toList();
            Attribute A = sortedAttributes.get(0);

            if(Double.isNaN(A.getEntropy(examples)))
                return root;

            root.setLabel(A.name);

            A.values.forEach(value -> {
                root.addChild(value, new Node());
                Examples conditionedExamples = examples.getExamplesByAttributeAndValue(A.name, value);
                if (conditionedExamples.areExamplesEmpty())
                    root.children.get(value).setLabel(conditionedExamples.getCountOfPositiveCases() > conditionedExamples.getCountOfNegativeCases() ? positiveLabel : negativeLabel);
                else {
                    attributes.remove(A);
                    root.children.put(value, id3(conditionedExamples, targetAttribute, new ArrayList<>(attributes.stream().toList())));
                    attributes.add(A);
                }
            });

        }
        return root;
    }

    public static void seeTree(Node root) {
        if (root == null) {
            System.out.println();
            return;
        }


        root.children.forEach((branch, node) -> {
            System.out.println("FATHER: " + root.label);
            System.out.println("CONNECT WITH: " + branch);
            System.out.println("CHILDREN: " + node.label);
            System.out.println("➖➖➖➖➖➖➖➖➖");
            seeTree(node);
        });
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        SSVProcessor ssvTrainProcessor = new SSVProcessor(args[0]);
        targetAttribute = new Attribute(ssvTrainProcessor.targetAttribute, ssvTrainProcessor.attributeToValue.get(ssvTrainProcessor.targetAttribute));
        positiveLabel = (String) targetAttribute.values.toArray()[0];
        negativeLabel = (String) targetAttribute.values.toArray()[1];

        ssvTrainProcessor.attributeToValue.forEach((attribute, values) -> {
            if (!Objects.equals(attribute, targetAttribute.name)) {
                Attribute newAttribute = new Attribute(attribute, values);
                attributes.add(newAttribute);
            }
        });
        Examples initialExamples = new Examples(ssvTrainProcessor.examples);

        totalNegativeCases = initialExamples.getCountOfNegativeCases();
        totalPositiveCases = initialExamples.getCountOfPositiveCases();
        totalCases = totalNegativeCases + totalPositiveCases;
        targetAttributeEntropy = Attribute.entropy(totalPositiveCases, totalNegativeCases);

        Node root = id3(initialExamples, targetAttribute, new ArrayList<>(attributes.stream().toList()));
        seeTree(root);

        SSVProcessor ssvTestProcessor = new SSVProcessor(args[1]);

        ssvTestProcessor.examples.forEach(map-> {
            System.out.println(seeResultFromTree(root, map));
        });


    }

    private static String seeResultFromTree(Node root, HashMap<String, String> map) {
        Node currentNode = root.clone();
        String branch;

        do {
            branch = map.get(currentNode.label);
            currentNode = currentNode.getChild(branch);
        }while (currentNode.getChild(branch)!= null);

        return currentNode.label;
    }
}