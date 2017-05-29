package es.pagoru.lastgps;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Node {

    private GPS gps;

    private String name;
    private int cost = 0;

    private Map<String, Integer> childNodes = new HashMap<>();

    public Node(String name, Map<String, Integer> childNodes, GPS gps){
        this.name = name;
        this.childNodes = childNodes;
        this.gps = gps;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<Node> getChildNodes() {
        List<Node> nodeList = childNodes.entrySet().stream().filter(e -> e.getValue() > 0).map(e -> {
            Node node = gps.getNode(e.getKey());
            node.setCost(e.getValue() + getCost());
            return node;
        }).collect(Collectors.toList());
        nodeList.sort(Comparator.comparing(Node::getCost));
        return nodeList;
    }

    @Override
    public String toString() {
        return getName() + "(" + getCost() + ")";
    }
}
