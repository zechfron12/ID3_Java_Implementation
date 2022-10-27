import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Attribute {
    static final double LOG2 = Math.log(2.0);

    static double xlogx(double x) {
        return x == 0 ? 0 : x * Math.log(x) / LOG2;
    }

    static double entropy(double positive, double negative){
        return -xlogx(positive/(positive + negative)) - xlogx(negative/(positive + negative));
    }

    String name;
    Set<String> values;

    public Attribute(String name, Set<String> values) {
        this.name = name;
        this.values = values;
    }

    public double getEntropy(Examples examples) {
        AtomicReference<Double> entropy = new AtomicReference<>((double) 0);

        values.forEach(value -> {
            Examples conditionalExample = examples.getExamplesByAttributeAndValue(name, value);
            double positive = conditionalExample.getCountOfPositiveCases();
            double negative = conditionalExample.getCountOfNegativeCases();
            entropy.updateAndGet(v -> (v + (positive + negative)/Main.totalCases * entropy(positive, negative)));
        });
        return entropy.get();
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
