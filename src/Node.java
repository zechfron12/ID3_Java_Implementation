import java.util.HashMap;
public class Node {
    String label;
    HashMap<String, Node> children;

    public Node() {
        children = new HashMap<>();
    }

    public Node(String label) {
        this.label = label;
        children = new HashMap<>();
    }

    public void setLabel(String label){
        this.label = label;
    }

    public Node clone() {
        Node newNode = new Node(this.label);
        newNode.children = this.children;
        return newNode;
    }
    public void addChild(String value, Node node) {
        children.put(value, node);
    }
    public Node getChild(String value){ return  children.get(value);}
}
