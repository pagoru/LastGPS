package es.pagoru.lastgps;

public class Console {

    public static void log(Object message){
        try{
            System.out.println("[GPS] -> " + message.toString());
        } catch (Exception e) {
            System.out.println("[GPS] -> null");
        }
    }

}
