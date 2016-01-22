package fr.nilteam.smartpaulo.smartpaulo.model;

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

    @Column(name = "zone_latitude1")
    private Double zone_latitude1;
    @Column(name = "zone_longitude1")
    private Double zone_longitude1;
    @Column(name = "zone_latitude2")
    private Double zone_latitude2;
    @Column(name = "zone_longitude2")
    private Double zone_longitude2;

    public PointOfInterest() {
    }

    public PointOfInterest(Double latitude, Double longitude, String photo_url, String tag, String username) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo_url = photo_url;
        this.tag = tag;
        this.username = username;
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

    public String getTags() {
        return tag;
    }

    public String getUsername() {
        return username;
    }

    public Double getZone_latitude1() {
        return zone_latitude1;
    }

    public Double getZone_longitude1() {
        return zone_longitude1;
    }

    public Double getZone_latitude2() {
        return zone_latitude2;
    }

    public Double getZone_longitude2() {
        return zone_longitude2;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", photo_url='" + photo_url + '\'' +
                ", tag='" + tag + '\'' +
                ", username='" + username + '\'' +
                ", zone_latitude1=" + zone_latitude1 +
                ", zone_longitude1=" + zone_longitude1 +
                ", zone_latitude2=" + zone_latitude2 +
                ", zone_longitude2=" + zone_longitude2 +
                '}';
    }
}
