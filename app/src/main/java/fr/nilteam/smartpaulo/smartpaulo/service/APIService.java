package fr.nilteam.smartpaulo.smartpaulo.service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.nilteam.smartpaulo.smartpaulo.activities.MainActivity;
import fr.nilteam.smartpaulo.smartpaulo.model.PointOfInterest;
import fr.nilteam.smartpaulo.smartpaulo.model.Tags;
import fr.nilteam.smartpaulo.smartpaulo.utils.InputStreamOperations;

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
        RestTask task = new RestTask(REQUESTTYPE.FETCH_POINTS_OF_INTEREST);
        task.execute(myurl);
    }


    /**
     * Réalise l'appel pour inserer un nouveau point d'interêt.
     */
    public void pushPointOfInterest(MainActivity activity, Map<String, Object> params) {
        String myurl = this.apiurl + "api/interest";
        RestTask task = new RestTask(REQUESTTYPE.INSERT_POINTS_OF_INTEREST);
        String urlParameters = "";
        try {
            this.activity = new WeakReference<MainActivity>(activity);
            StringBuilder postData = new StringBuilder();
            for (String key : params.keySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(key, "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8"));
            }
            urlParameters = postData.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("DEBUG", "Avant appel a task.execute");
        task.execute(myurl, urlParameters);
    }

    public enum REQUESTTYPE {FETCH_POINTS_OF_INTEREST, INSERT_POINTS_OF_INTEREST}

    ;

    /**
     * Callback avec les points d'interets
     * Parse le résultat et envoie les points d'interets à l'activité
     */
    public void setPointsOfInterest(String result) {
//        ArrayList<PointOfInterest> pointsOfInterest = new ArrayList<PointOfInterest>();
//        try {
//            // On récupère le tableau d'objets qui nous concernent
//            JSONArray array = new JSONArray(result);
//            // Pour tous les objets on récupère les infos
//            for (int i = 0; i < array.length(); i++) {
//                // On récupère un objet JSON du tableau
//                JSONObject obj = new JSONObject(array.getString(i));
//                List<Tags> tags = new ArrayList<Tags>();
//                // On fait le lien Personne - Objet JSON
//                JSONObject zone = obj.getJSONObject("zone");
//                PointOfInterest interest = new PointOfInterest(
//                        obj.getLong("latitude"),
//                        obj.getLong("longitude"),
//                        obj.getString("photo_url"),
//                        tags,
//                        obj.getString("username"),
//                        obj.getLong("created_at"),
//                        zone.getLong("latitude1"),
//                        zone.getLong("longitude1"),
//                        zone.getLong("latitude2"),
//                        zone.getLong("longitude2")
//                );
//                Log.e("APISERVICE", "PointOfInterest created");
//                // Add interest into the result
//                pointsOfInterest.add(interest);
//
//            }
//            activity.get().setPointsOfInterest(pointsOfInterest);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void checkAPIResult(String result) {
        //TODO
    }

    public class RestTask extends AsyncTask<String, Void, String> {
        private final REQUESTTYPE requestType;

        public RestTask(REQUESTTYPE requestType) {
            this.requestType = requestType;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
                URL url = new URL(params[0]);
                String result = "";
                InputStream inputStream;

                Log.d("DEBUG", "TYPE REQUEST = "+this.requestType.toString());


                switch (this.requestType) {
                    case FETCH_POINTS_OF_INTEREST:
                        URLConnection con = url.openConnection();
                        // Get the response
                        inputStream = con.getInputStream();
                        result = InputStreamOperations.InputStreamToString(inputStream);
                        Log.d("DEBUG", "FETCH POINT");

                        break;
                    case INSERT_POINTS_OF_INTEREST:
                        String urlParameters = params[1];
                        byte[] postData = urlParameters.getBytes();
                        int postDataLength = postData.length;

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestProperty("charset", "utf-8");
                        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                        connection.setUseCaches(false);
                        connection.setDoOutput(true);

                        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                        wr.write(postData);
                        // Get the response
                        inputStream = connection.getInputStream();
                        result = InputStreamOperations.InputStreamToString(inputStream);
                        Log.d("DEBUG", "PUSH POINT");
                        break;
                }
                return result;
            } catch (Exception e) {
                // TODO handle this properly
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("APISERVICE", "JSON FETCHED :[" + result + "]");
            switch (this.requestType) {
                case FETCH_POINTS_OF_INTEREST:
                    APIService.INSTANCE.setPointsOfInterest(result);
                    break;
                case INSERT_POINTS_OF_INTEREST:
                    APIService.INSTANCE.checkAPIResult(result);
                    break;
            }
        }

    }

}
