package fr.nilteam.smartpaulo.smartpaulo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import fr.nilteam.smartpaulo.smartpaulo.R;
import fr.nilteam.smartpaulo.smartpaulo.model.PointOfInterest;
import fr.nilteam.smartpaulo.smartpaulo.service.APIService;

public class InterestPoint extends AppCompatActivity {

    private PointOfInterest poi;

    private Double x1;
    private Double x2;
    private Double y1;
    private Double y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_point);

        Intent intent = getIntent();
        poi = PointOfInterest.mapMarkerPoi.get(intent.getStringExtra("markerId"));

        TextView textLocation = (TextView) findViewById(R.id.textLocation);
        textLocation.setText("Localisation : "+poi.getLatitude()+", "+poi.getLongitude());

        TextView textUser = (TextView) findViewById(R.id.textUser);
        String username;
        if (poi.getUsername().equals("")) {
            username = "Anonyme";
        } else {
            username = poi.getUsername();
        }
        textUser.setText("Proposé par "+username);

        TextView textTags = (TextView) findViewById(R.id.textTag);
        textTags.setText("Tag : "+poi.getTags());

        x1 = poi.getX1();
        x2 = poi.getX2();

        y1 = poi.getY1();
        y2 = poi.getY2();

        APIService.INSTANCE.fetchPhoto(this, poi);
    }


    public void outputPhoto(Bitmap bitmapPhoto) {
        ImageView photo = (ImageView) findViewById(R.id.photo);
        photo.setImageBitmap(bitmapPhoto);
    }
}
