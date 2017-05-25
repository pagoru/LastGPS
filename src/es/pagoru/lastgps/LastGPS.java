package es.pagoru.lastgps;

import javax.rmi.CORBA.Util;
import java.util.*;
import java.util.stream.Collectors;

public class LastGPS {

    private static LinkedHashMap<String, Integer> gpsList = new LinkedHashMap<>();

    public static void main(String[] args){

        Positions.loadGPS("gps-spain");

        String from = "Lugo";
        String to = "Barcelona";

        gpsList.put(from, 0);

        Map<String, Integer> map = findGPS("", to);

//        Console.log(from + "(0)");
//        map.forEach((s, i) -> Console.log(s + "(" + i + ")"));


    }

    private static LinkedHashMap<String, Integer> findGPS(String from, String to){
        String nextPosition = gpsList.keySet().toArray()[0].toString();
        int numNextPosition = gpsList.get(nextPosition);

        Position currentP = Positions.getPosition(nextPosition);
        gpsList.remove(currentP.getName());

        Map<String, Integer> np = currentP.getNearPositions();
        np.keySet().stream().sorted(Comparator.comparing(np::get)).collect(Collectors.toMap(k -> k, np::get));

        for(String key : currentP.getNearPositions().keySet()){
            int num = currentP.getNearPositions().get(key);

            if(num > 0){
                int newNum = num + numNextPosition;

                if(key.equalsIgnoreCase(to)){
                    gpsList.put(key, newNum);
                    return gpsList;
                }

                if(gpsList.get(key) == null){
                    gpsList.put(key, newNum);
                    continue;
                }

                if(gpsList.get(key) > newNum){
                    gpsList.put(key, newNum);
                }

            }
        }

        gpsList.keySet().stream().forEachOrdered(k -> Console.log("[" + from + "->" + nextPosition + "]: " + k + "(" + gpsList.get(k) + ")"));

//        route.forEach((s, i) -> Console.log("[" + from + "->" + nextPosition + "]: " + s + "(" + i + ")"));


        return findGPS(currentP.getName(), to);
    }

}