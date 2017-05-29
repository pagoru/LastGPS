package es.pagoru.lastgps;

/**
 * Created by Pablo on 26/05/17.
 */
public class LastGPS {

    public static void main(String[] args){

        GPS spainGPS = new GPS("gps-spain");

        String fromNode = "Logroño";

        Node n1 = spainGPS.getNode(fromNode);

        for(Node node : n1.getChildNodes()){
            Console.log(node);
        }

    }

}
