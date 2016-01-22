package fr.nilteam.smartpaulo.smartpaulo.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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

import fr.nilteam.smartpaulo.smartpaulo.R;
import fr.nilteam.smartpaulo.smartpaulo.model.Tags;
import fr.nilteam.smartpaulo.smartpaulo.service.APIService;
import fr.nilteam.smartpaulo.smartpaulo.utils.UserLocation;

public class FormulairePhoto extends AppCompatActivity {

    private Uri imageUri;
    private ImageView iv;
    private Button buttonAnnuler;
    private Button buttonAjouter;
    private Button buttonReprendrePhoto;
    private Spinner spinner;
    private UserLocation userLoc;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_photo);

        iv = (ImageView) findViewById(R.id.imageView2);
        iv.setMaxHeight(200);

        getSupportActionBar().setTitle("SmartPaulo - Ajout de photo");

        buttonAjouter = (Button) findViewById(R.id.buttonAjouter);
        buttonAnnuler = (Button) findViewById(R.id.buttonAnnuler);
        buttonReprendrePhoto = (Button) findViewById(R.id.buttonReprendrePhoto);

        buttonAjouter.setOnClickListener(ajouterPointInteret);
        buttonAnnuler.setOnClickListener(annuler);
        buttonReprendrePhoto.setOnClickListener(reprendrePhoto);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<Tags>(this, android.R.layout.simple_spinner_item, Tags.values()));

        userLoc = new UserLocation(this);
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
            if(userLoc.getLocation() != null) {
                Context context = getApplicationContext();
                CharSequence text = "Location ["+userLoc.getLocation().getLatitude()+", "+userLoc.getLocation().getLongitude()+"] Alt : "+userLoc.getLocation().getAltitude()+ "m - Précision : "+userLoc.getLocation().getAccuracy();
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
}
