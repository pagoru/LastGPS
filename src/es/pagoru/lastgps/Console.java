package es.pagoru.lastgps;

/**
 * Created by Pablo on 26/05/17.
 */
public class Console {

    public static void log(Object message){
        try{
            System.out.println("[LastGPS]: " + message.toString());
        } catch (Exception e) {
            System.out.println("[LastGPS]: null");
        }
    }

}