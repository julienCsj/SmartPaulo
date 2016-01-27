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

    private Float x1;
    private Float x2;
    private Float y1;
    private Float y2;


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

        APIService.INSTANCE.fetchPhoto(this, poi);
    }


    public void outputPhoto(Bitmap bitmapPhoto) {
        ImageView photo = (ImageView) findViewById(R.id.photo);
        //photo.setImageBitmap(bitmapPhoto);

        Canvas canvas = new Canvas(bitmapPhoto);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setAlpha(45);
        canvas.drawRect(x1, y1, x2, y2, p);
        photo.setImageBitmap(bitmapPhoto);
    }
}
