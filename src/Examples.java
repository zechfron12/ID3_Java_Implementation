import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Examples {
    List<HashMap<String, String>> examples;

    public Examples(List<HashMap<String, String>> examples) {
        this.examples = examples;
    }

    public Examples getExamplesByAttributeAndValue(String attribute, String value) {
        List<HashMap<String, String>> newExamples = examples.stream().filter(map -> Objects.equals(map.get(attribute), value)).toList();
        return new Examples(newExamples);
    }

    public long getCountOfPositiveCases(){
        return examples.stream().filter(map -> Objects.equals(map.get(Main.targetAttribute.name), Main.positiveLabel)).count();
    }

    public boolean areAllPositive() {
        AtomicBoolean ok = new AtomicBoolean(true);
        examples.forEach((map -> {
            if(Objects.equals(map.get(Main.targetAttribute.name), Main.negativeLabel)) {
                ok.set(false);
            }
        }));

        return ok.get();
    }

    public boolean areAllNegative() {
        AtomicBoolean ok = new AtomicBoolean(true);
        examples.forEach((map -> {
            if(Objects.equals(map.get(Main.targetAttribute.name), Main.positiveLabel)) {
                ok.set(false);
            }
        }));

        return ok.get();
    }

    public long getCountOfNegativeCases(){
        return examples.stream().filter(map -> Objects.equals(map.get(Main.targetAttribute.name), Main.negativeLabel)).count();
    }

    public boolean areExamplesEmpty(){
        return examples.isEmpty();
    }

    public Examples clone() throws CloneNotSupportedException {
        Examples clone = (Examples) super.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "Examples{" +
                "examples=" + examples +
                '}';
    }
}
