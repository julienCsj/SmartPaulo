package fr.nilteam.smartpaulo.smartpaulo.model;

import android.graphics.Point;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 21/01/16.
 */
@Table(name = "points")
public class PointOfInterest extends Model {

    public static Map<String, PointOfInterest> mapMarkerPoi = new HashMap<String, PointOfInterest>();

    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "photo_url")
    private String photo_url;
    @Column(name = "tag")
    private String tag;
    @Column(name = "username")
    private String username;

    @Column(name = "x1")
    private Double x1;
    @Column(name = "y1")
    private Double y1;
    @Column(name = "x2")
    private Double x2;
    @Column(name = "y2")
    private Double y2;

    public PointOfInterest() {
    }

    public PointOfInterest(Double latitude, Double longitude, String photo_url, String tag, String username, Double x1, Double y1, Double x2, Double y2) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo_url = photo_url;
        this.tag = tag;
        this.username = username;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public Double getX1() {
        return x1;
    }

    public Double getY1() {
        return y1;
    }

    public Double getX2() {
        return x2;
    }

    public Double getY2() {
        return y2;
    }

    public String getTags() {

        return tag;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public String toString() {
        return "PointOfInterest{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", photo_url='" + photo_url + '\'' +
                ", tag='" + tag + '\'' +
                ", username='" + username + '\'' +
                ", x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }
}
