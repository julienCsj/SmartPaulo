package fr.nilteam.smartpaulo.smartpaulo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FormulairePhoto extends AppCompatActivity {

    private Uri imageUri;
    private ImageView iv;
    private Button buttonAnnuler;
    private Button buttonAjouter;
    private Button buttonReprendrePhoto;
    private Spinner spinner;
    private Location currentLocation;
    LocationManager mLocationManager;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_photo);

        iv = (ImageView) findViewById(R.id.imageView2);
        iv.setMaxHeight(200);
        buttonAjouter = (Button) findViewById(R.id.buttonAjouter);
        buttonAnnuler = (Button) findViewById(R.id.buttonAnnuler);
        buttonReprendrePhoto = (Button) findViewById(R.id.buttonReprendrePhoto);

        buttonAjouter.setOnClickListener(ajouterPointInteret);
        buttonAnnuler.setOnClickListener(annuler);
        buttonReprendrePhoto.setOnClickListener(reprendrePhoto);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<Tags>(this, android.R.layout.simple_spinner_item, Tags.values()));

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0F, mLocationListener);

        settings = getSharedPreferences("SmartPaulo", 0);

        // prendre une photo
        takePhoto();
    }

    public void takePhoto() {
        //Création d'un intent
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        //Création du fichier image
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);

        //On lance l'intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //Si l'activité était une prise de photo
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();

                    try {
                        this.bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        iv.setImageBitmap(bitmap);
                        iv.setMaxHeight(200);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }

    private View.OnClickListener ajouterPointInteret = new View.OnClickListener() {
        public void onClick(View arg0) {
            if(currentLocation != null) {
                Context context = getApplicationContext();
                CharSequence text = "Location ["+currentLocation.getLatitude()+", "+currentLocation.getLongitude()+"] Alt : "+currentLocation.getAltitude()+ "m - Précision : "+currentLocation.getAccuracy();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            Bitmap bm = bitmap;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            Map<String, Object> params = new HashMap();
            params.put("tags", "sometags");
            params.put("latitude", 4.23145);
            params.put("longitude", 41.4532);
            params.put("photo", encodedImage);
            String pseudo = settings.getString("pseudo", "");
            params.put("username", pseudo);
            APIService.INSTANCE.pushPointOfInterest(null, params);
        }
    };

    private View.OnClickListener annuler = new View.OnClickListener() {
        public void onClick(View arg0) {
            FormulairePhoto.this.finish();
        }
    };

    private View.OnClickListener reprendrePhoto = new View.OnClickListener() {
        public void onClick(View arg0) {
            takePhoto();
        }
    };

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
