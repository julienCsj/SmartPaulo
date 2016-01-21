package fr.nilteam.smartpaulo.smartpaulo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 21/01/16.
 * APIService is a thread-safe singleton
 */
public enum APIService {
    INSTANCE;

    private final String apiurl = "https://still-coast-6987.herokuapp.com/";
    //http://stackoverflow.com/questions/9723106/get-activity-instance
    private static WeakReference<MainActivity> activity;

    /**
     * Réalise l'appel pour récuperer la liste de points d'interêt.
     */
    public void fetchPointsOfInterest(MainActivity activity) {
        String myurl = this.apiurl + "api/interest";
        this.activity = new WeakReference<MainActivity>(activity);
        RestTask task = new RestTask();
        task.execute(myurl);
    }

    /**
     * Callback avec les points d'interets
     * Parse le résultat et envoie les points d'interets à l'activité
     */
    public void setPointsOfInterest(String result) {
        ArrayList<PointOfInterest> pointsOfInterest = new ArrayList<PointOfInterest>();
        try {
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = new JSONArray(result);
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                List<Tags> tags = new ArrayList<Tags>();
                // On fait le lien Personne - Objet JSON
                JSONObject zone = obj.getJSONObject("zone");
                PointOfInterest interest = new PointOfInterest(
                        obj.getLong("latitude"),
                        obj.getLong("longitude"),
                        obj.getString("photo_url"),
                        tags,
                        obj.getString("username"),
                        obj.getLong("created_at"),
                        zone.getLong("latitude1"),
                        zone.getLong("longitude1"),
                        zone.getLong("latitude2"),
                        zone.getLong("longitude2")
                );
                Log.e("APISERVICE", "PointOfInterest created");
                // Add interest into the result
                pointsOfInterest.add(interest);

            }
            activity.get().setPointsOfInterest(pointsOfInterest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class RestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                // Get the response
                InputStream inputStream = connection.getInputStream();
                String result = InputStreamOperations.InputStreamToString(inputStream);
                return result;
            } catch (Exception e) {
                // TODO handle this properly
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e("APISERVICE", "JSON FETCHED :[" + result + "]");
            APIService.INSTANCE.setPointsOfInterest(result);
        }

    }

}
