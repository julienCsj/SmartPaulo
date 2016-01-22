package fr.nilteam.smartpaulo.smartpaulo.model;

/**
 * Created by julien on 21/01/16.
 */
public enum Tags {
    ACCIDENT("Accident"),
    RECYCLAGE("Recyclage"),
    INSALUBRITE("Insalubrité"),
    VANDALISME("Vandalisme");

    private String traduction;

    Tags(String traduction) {
        this.traduction = traduction;
    }

    @Override public String toString(){
        return traduction;
    }
}
