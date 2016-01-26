package fr.nilteam.smartpaulo.smartpaulo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import fr.nilteam.smartpaulo.smartpaulo.R;
import fr.nilteam.smartpaulo.smartpaulo.model.PointOfInterest;
import fr.nilteam.smartpaulo.smartpaulo.utils.Base64;

public class InterestPoint extends AppCompatActivity {

    private PointOfInterest poi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_point);

        Intent intent = getIntent();
        poi = PointOfInterest.mapMarkerPoi.get(intent.getStringExtra("markerId"));

        TextView textLocation = (TextView) findViewById(R.id.textLocation);
        textLocation.setText("Localisation : "+poi.getLatitude()+", "+poi.getLongitude());

        TextView textUser = (TextView) findViewById(R.id.textUser);
        textUser.setText("Proposé par "+poi.getUsername());

        TextView textTags = (TextView) findViewById(R.id.textTag);
        textTags.setText("Tags : "+poi.getTags());

        ImageView photo = (ImageView) findViewById(R.id.photo);
        URL newurl = null;
        try {
            newurl = new URL(poi.getPhoto_url());
            Bitmap image = BitmapFactory.decodeStream((InputStream)newurl.getContent());
            photo.setImageBitmap(image);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Impossible d'afficher la photo. Veuillez vérifier votre connexion internet.", Toast.LENGTH_LONG).show();
        }

    }


}
