package es.pagoru.lastgps;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class GPS {

    private List<Node> positionList = new ArrayList<>();

    public Node getNode(String name) {
        return positionList.stream().filter(pl -> pl.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public GPS(String name){
        loadGPS(name);
    }

    private void loadGPS(String name){
        List<String> linePositions = Arrays.asList(getTextFromFile(name).split("\n"));

        linePositions.stream().forEach(lp -> {
            List<String> line = Arrays.asList(lp.split(":")[1].split(";"));
            Map<String, Integer> lineHash = line.stream().filter(e -> !e.equalsIgnoreCase(lp.split(":")[0])).collect(Collectors.toMap(
                    e -> e.split("\\(")[0],
                    e -> Integer.parseInt(e.split("\\(")[1].replaceAll("\\)", ""))
            ));

            positionList.add(new Node(lp.split(":")[0], lineHash, this));
        });

    }

    private String getTextFromFile(String fileName){

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
