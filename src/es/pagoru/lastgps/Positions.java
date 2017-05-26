package es.pagoru.lastgps;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Positions {

    private static List<Position> positionList = new ArrayList<>();

    public static Position getPosition(String name) {
        return positionList.stream().filter(pl -> pl.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void loadGPS(String name){
        List<String> linePositions = Arrays.asList(getTextFromFile(name).split("\n"));

        linePositions.stream().forEach(lp -> {
            List<String> line = Arrays.asList(lp.split(":")[1].split(";"));
            Map<String, Integer> lineHash = line.stream().filter(e -> !e.equalsIgnoreCase(lp.split(":")[0])).collect(Collectors.toMap(
                    e -> e.split("\\(")[0],
                    e -> Integer.parseInt(e.split("\\(")[1].replaceAll("\\)", ""))
            ));

            positionList.add(new Position(lp.split(":")[0], lineHash));
        });

    }

    private static String getTextFromFile(String fileName){

        String text = "";
        try {
            FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "/src/assets/gps/" + fileName + ".txt");
            Scanner in = new Scanner(file);

            StringBuilder sb = new StringBuilder();
            while(in.hasNext()) sb.append(in.next() + "\n");
            in.close();

            text = sb.toString();

            file.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return text;
    }

}
