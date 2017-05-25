package es.pagoru.lastgps;

import java.util.HashMap;
import java.util.Map;

public class Position {

    private String name;
    private Map<String, Integer> nearPositions = new HashMap<>();

    public Position(String name, Map<String, Integer> nearPositions){
        this.name = name;
        this.nearPositions = nearPositions;
    }

    public String getName() {
        return name;
    }

    public int getPosition(String name){
        return getNearPositions().get(name);
    }

    public Map<String, Integer> getNearPositions() {
        return nearPositions;
    }
}
