package fr.nilteam.smartpaulo.smartpaulo.model;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 21/01/16.
 */
public class PointOfInterest {
    private long latitude;
    private long longitude;
    private String photo_url;
    private List<Tags> tags;
    private String username;
    private long created_at;

    private long zone_latitude1;
    private long zone_longitude1;
    private long zone_latitude2;
    private long zone_longitude2;

    public static Map<String, PointOfInterest> mapMarkerPoi = new HashMap<String, PointOfInterest>();

    public PointOfInterest(long latitude, long longitude, String photo_url, List<Tags> tags, String username, long created_at, long zone_latitude1, long zone_longitude1, long zone_latitude2, long zone_longitude2) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo_url = photo_url;
        this.tags = tags;
        this.username = username;
        this.created_at = created_at;
        this.zone_latitude1 = zone_latitude1;
        this.zone_longitude1 = zone_longitude1;
        this.zone_latitude2 = zone_latitude2;
        this.zone_longitude2 = zone_longitude2;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public String getUsername() {
        return username;
    }

    public long getCreated_at() {
        return created_at;
    }

    public long getZone_latitude1() {
        return zone_latitude1;
    }

    public long getZone_longitude1() {
        return zone_longitude1;
    }

    public long getZone_latitude2() {
        return zone_latitude2;
    }

    public long getZone_longitude2() {
        return zone_longitude2;
    }
}
