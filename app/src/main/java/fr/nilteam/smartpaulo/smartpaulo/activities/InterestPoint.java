package fr.nilteam.smartpaulo.smartpaulo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.nilteam.smartpaulo.smartpaulo.R;
import fr.nilteam.smartpaulo.smartpaulo.model.PointOfInterest;

public class InterestPoint extends AppCompatActivity {

    private PointOfInterest poi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_point);

        Intent intent = getIntent();
        poi = PointOfInterest.mapMarkerPoi.get(intent.getStringExtra("markerId"));
    }


}
