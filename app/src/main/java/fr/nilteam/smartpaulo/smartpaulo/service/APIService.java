package fr.nilteam.smartpaulo.smartpaulo.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

import fr.nilteam.smartpaulo.smartpaulo.activities.FormulairePhoto;
import fr.nilteam.smartpaulo.smartpaulo.activities.MainActivity;
import fr.nilteam.smartpaulo.smartpaulo.model.PointOfInterest;
import fr.nilteam.smartpaulo.smartpaulo.model.Tags;
import fr.nilteam.smartpaulo.smartpaulo.utils.Base64;
import fr.nilteam.smartpaulo.smartpaulo.utils.InputStreamOperations;

/**
 * Created by Leo on 21/01/16.
 * APIService is a thread-safe singleton
 */
public enum APIService {
    INSTANCE;

    private final String apiurl = "https://still-coast-6987.herokuapp.com/";
    //http://stackoverflow.com/questions/9723106/get-activity-instance
    private static WeakReference<MainActivity> activityMain;
    private static WeakReference<FormulairePhoto> activityFormulairePhoto;

    /**
     * Réalise l'appel pour récuperer la liste de points d'interêt.
     */
    public void fetchPointsOfInterest(MainActivity activity) {
        String myurl = this.apiurl + "api/interest";
        this.activityMain = new WeakReference<MainActivity>(activity);
        RestTask task = new RestTask(REQUESTTYPE.FETCH_POINTS_OF_INTEREST);
        task.execute(myurl);
    }


    /**
     * Réalise l'appel pour inserer un nouveau point d'interêt.
     */
    public void pushPointOfInterest(FormulairePhoto activity, Map<String, Object> params) {
        String myurl = this.apiurl + "api/interest";
        RestTask task = new RestTask(REQUESTTYPE.INSERT_POINTS_OF_INTEREST);
        this.activityFormulairePhoto = new WeakReference<FormulairePhoto>(activity);
        Log.d("DEBUG", "Avant appel a task.execute");
        task.execute(myurl, params);
    }

    public enum REQUESTTYPE {FETCH_POINTS_OF_INTEREST, INSERT_POINTS_OF_INTEREST}

    ;

    /**
     * Callback avec les points d'interets
     * Parse le résultat et envoie les points d'interets à l'activité
     */
    public void setPointsOfInterest(String result) {
        try {
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = new JSONArray(result);
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                Tags tags = Tags.INSALUBRITE;
                if (obj.has("tags")) {
                    //TODO
                    JSONArray jTags = obj.getJSONArray("tags");
                    for (int j = 0; j < jTags.length(); j++) {
                        switch (jTags.getString(j)) {
                            case "Insalubrité":
                                tags = Tags.INSALUBRITE;
                                break;
                            case "Accident":
                                tags = Tags.ACCIDENT;
                                break;
                            case "Recyclage":
                                tags = Tags.RECYCLAGE;
                                break;
                            case "Vandalisme":
                                tags = Tags.VANDALISME;
                                break;
                        }
                    }
                }
                // On fait le lien Personne - Objet JSON
                PointOfInterest interest = new PointOfInterest(
                        obj.getDouble("lat"),
                        obj.getDouble("lng"),
                        obj.getString("url"),
                        tags.toString(),
                        obj.getString("pseudo")
                );
                interest.save();
                Log.e("APISERVICE", "PointOfInterest created");
                // Add interest into the result

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.activityMain.get().redrawGoogleMap();
    }

    public void checkAPIResult(String result) {
        //TODO
        int resultCode;
        try {
            JSONObject r = new JSONObject(result);
            String status = r.getString("status");
            if (status.equals("ok")) {
                resultCode = 0;
            } else {
                resultCode = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = -1;
        }
        this.activityFormulairePhoto.get().receiveServerResult(resultCode);
    }

    public class RestTask extends AsyncTask<Object, Void, String> {
        private final REQUESTTYPE requestType;

        public RestTask(REQUESTTYPE requestType) {
            this.requestType = requestType;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
                URL url = new URL((String)params[0]);
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
                        Map<String, Object> postParams = ((Map) params[1]);

                        String urlParameters = "";
                        try {
                            // Put an approximative amount of byte in it to avoid outofmemory when StringBuilder expand again and
                            // again his memory size (sort of pic when getting more memory)
                            StringBuilder postData = new StringBuilder(((Bitmap)postParams.get("photo")).getByteCount()/8);
                            for (String key : postParams.keySet()) {
                                if (postData.length() != 0) postData.append('&');
                                if (key.equals("photo")) {

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ((Bitmap)postParams.get(key)).compress(Bitmap.CompressFormat.JPEG, 60, baos); //bm is the bitmap object
                                    byte[] byteArrayImage = baos.toByteArray();
                                    postData.append(URLEncoder.encode(key, "UTF-8"));
                                    postData.append('=');
                                    postData.append(URLEncoder.encode(Base64.encodeBytes(byteArrayImage), "UTF-8"));
                                } else {
                                    postData.append(URLEncoder.encode(key, "UTF-8"));
                                    postData.append('=');
                                    postData.append(URLEncoder.encode(String.valueOf(postParams.get(key)), "UTF-8"));
                                }
                            }
                            urlParameters = postData.toString();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

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
