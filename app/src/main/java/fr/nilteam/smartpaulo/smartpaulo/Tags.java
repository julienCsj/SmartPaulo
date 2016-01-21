package fr.nilteam.smartpaulo.smartpaulo;

/**
 * Created by julien on 21/01/16.
 */
public enum Tags {
    ACCIDENT("Accident"),
    RECYCLAGE("Recyclage"),
    INSALUBRITE("Insalubrité"),
    VENDALISME("Vendalisme");

    private String traduction;

    Tags(String traduction) {
        this.traduction = traduction;
    }

    @Override public String toString(){
        return traduction;
    }
}
