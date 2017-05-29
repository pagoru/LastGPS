package es.pagoru.lastgps;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Pablo on 26/05/17.
 */
public class LastGPS {


    public static void main(String[] args){

        Positions.loadGPS("gps-spain");
        /*String gps = findGPS("Barcelona", "Leon", "Soria")
                .stream().map(CurrentPosition::toString).collect(Collectors.joining("->"));*/

        //Problemon
        //Mostra la 'ruta' de Leon fins a Soria
        Console.log(findGPS("Leon", "Soria"));
        //Mostra la 'ruta' de Leon fins a Soria pasant per Barcelona
        Console.log(findGPS("Leon", "Soria", "Barcelona"));

    }

    private static List<CurrentPosition> findGPS(String fromPlace, String toPlace, String... butIWantToGoOtherPlacesFirst){
        //Nomes s'activa si el gps ha de pasar per alguna ciutat extra
        if(butIWantToGoOtherPlacesFirst.length != 0){

            List<CurrentPosition> places = new ArrayList<>();
            String lastPlace = fromPlace;

            //Pasa per totes les ciutat que ha de visitar abans de la desti
            for(String firstPlaceToGo : butIWantToGoOtherPlacesFirst){
                places.add(find(new CurrentPosition(lastPlace, 0), lastPlace, firstPlaceToGo));
                //Asigna la última posició com a la primera per tal de que al donar la volta sigui la primera
                lastPlace = firstPlaceToGo;
            }
            Console.log(toPlace);
            places.add(find(new CurrentPosition(lastPlace, 0), lastPlace, toPlace));
            return places;
        }

        //Comprova que les posicions siguin valides, ja que pot ser que el nom esitgui mal introduit
        if(Positions.getPosition(fromPlace) == null || Positions.getPosition(toPlace) == null) return null;
        List<CurrentPosition> al = new ArrayList<>();
        al.add(find(new CurrentPosition(fromPlace, 0), fromPlace, toPlace));
        return al;
    }

    private static CurrentPosition find(CurrentPosition currentPosition, String fromPosition, String toPosition){
        //Busca entre les posicions la posició actual mitjançant el nom introduit
        Position position = Positions.getPosition(currentPosition.getName());
        //Si la posició actual equival a la posició final, retorna la posició actual
        if(currentPosition.getName().equalsIgnoreCase(toPosition)) return currentPosition;
        //Recorre totes les posicions de les que depen aquesta posició
        for(Map.Entry<String, Integer> entry : position.getNearPositions().entrySet()){
            //Va a la seguent posició si el valor de la entrada no es major a 1 (-1 i 0)
            if(entry.getValue() < 1) continue;
            //Va a la seguent posició si el valor de la entrada es igual a la posició final
            if(entry.getKey().equalsIgnoreCase(fromPosition)) continue;
            //Va a la seguent posició si els pares d'aquesta posició contenten la entrada actual
            if(currentPosition.getParents().containsKey(entry.getKey())) continue;
            //Retorna la seguent posició que es necesita analitza
            CurrentPosition nextPos = new CurrentPosition(entry.getKey(), entry.getValue());
            //Se le afageix el actual pare a la seguent posició creada
            nextPos.addParent(currentPosition);
            //Retorna la busca d'aquesta posició
            return find(nextPos, fromPosition, toPosition);
        }
        //Retorna null en cas de no trobar ningun cami per evitar la recursivitat
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
