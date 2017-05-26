package es.pagoru.lastgps;

import javafx.geometry.Pos;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Pablo on 26/05/17.
 */
public class LastGPS {

    //TODO Falta sumar toooodas las distancias, no valido.
    public static void main(String[] args){

        Positions.loadGPS("gps-spain");
        /*String gps = findGPS("Barcelona", "Leon", "Soria")
                .stream().map(CurrentPosition::toString).collect(Collectors.joining("->"));*/

        //Problemon
        Console.log(findGPS("Leon", "Soria"));

    }
    //TODO Al volver a calcular la ruta, en segunda vuelta, empieza desde el principio... :(
    private static List<CurrentPosition> findGPS(String fromPlace, String toPlace, String... butIWantToGoOtherPlacesFirst){
        if(butIWantToGoOtherPlacesFirst.length != 0){

            List<CurrentPosition> places = new ArrayList<>();
            String lastPlace = fromPlace;

            for(String firstPlaceToGo : butIWantToGoOtherPlacesFirst){
                places.add(find(new CurrentPosition(lastPlace, 0), lastPlace, firstPlaceToGo));
                lastPlace = firstPlaceToGo;
            }
            Console.log(toPlace);
            places.add(find(new CurrentPosition(lastPlace, 0), lastPlace, toPlace));
            return places;
        }

        if(Positions.getPosition(fromPlace) == null || Positions.getPosition(toPlace) == null) return null;
        List<CurrentPosition> al = new ArrayList<>();
        al.add(find(new CurrentPosition(fromPlace, 0), fromPlace, toPlace));
        return al;
    }

    private static CurrentPosition find(CurrentPosition currentPosition, String fromPosition, String toPosition){
        Position position = Positions.getPosition(currentPosition.getName());
        if(currentPosition.getName().equalsIgnoreCase(toPosition)) return currentPosition;
        for(Map.Entry<String, Integer> entry : position.getNearPositions().entrySet()){
            if(entry.getValue() < 1) continue;
            if(entry.getKey().equalsIgnoreCase(fromPosition)) continue;
            if(currentPosition.getParents().containsKey(entry.getKey())) continue;
            CurrentPosition nextPos = new CurrentPosition(entry.getKey(), entry.getValue());
            nextPos.addParent(currentPosition);
            return find(nextPos, fromPosition, toPosition);
        }
        return null;
    }

}

class CurrentPosition {
    private String name;
    private int cost;
    private TreeMap<String, Integer> parents;

    CurrentPosition(String name, int cost){
        this.name = name;
        this.cost = cost;

        parents = new TreeMap<>();
    }

    void addParent(CurrentPosition parentPosition){
        parents.putAll(parentPosition.parents);
        parents.put(parentPosition.name, parentPosition.cost);
        cost += parentPosition.cost;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public TreeMap<String, Integer> getParents() {
        return parents;
    }

    @Override
    public String toString() {
        return parents.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue))
                .map(e -> e.getKey() + "(" + e.getValue() + ")")
                .collect(Collectors.joining("->")) + ( parents.size() != 0 ? "->" : "") + name + "(" + cost + ")";
    }
}
