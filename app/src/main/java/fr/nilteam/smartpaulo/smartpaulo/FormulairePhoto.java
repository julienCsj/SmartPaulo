package fr.nilteam.smartpaulo.smartpaulo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class FormulairePhoto extends AppCompatActivity {

    private Uri imageUri;
    private ImageView iv;
    private Button buttonAnnuler;
    private Button buttonAjouter;
    private Button buttonReprendrePhoto;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;



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
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
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
