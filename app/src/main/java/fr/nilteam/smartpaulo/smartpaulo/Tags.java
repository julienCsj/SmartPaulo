package fr.nilteam.smartpaulo.smartpaulo;

/**
 * Created by julien on 21/01/16.
 */
public enum Tags {
    ACCIDENT("Accident"),
    INSSALUBRITE("Inssalubrité"),
    VENDALISME("Vendalisme");

    private String traduction;

    Tags(String traduction) {
        this.traduction = traduction;
    }

    @Override public String toString(){
        return traduction;
    }
}
