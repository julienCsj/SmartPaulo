package fr.nilteam.smartpaulo.smartpaulo.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
    private Double x1 = -1.0;
    private Double x2 = -1.0;
    private Double y1 = -1.0;
    private Double y2 = -1.0;

    private String tag;

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tag = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        userLoc = new UserLocation(this);
        settings = getSharedPreferences("SmartPaulo", 0);


        iv.setOnTouchListener(new View.OnTouchListener() {

            private Float xInit;
            private Float yInit;

            private Float xFinal;
            private Float yFinal;

            private Float xCourant;
            private Float yCourant;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    xInit = motionEvent.getX();
                    yInit = motionEvent.getY();
                    Toast.makeText(getApplicationContext(), "INIT", Toast.LENGTH_SHORT).show();

                } else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    xFinal = motionEvent.getX();
                    yFinal = motionEvent.getY();
                    Toast.makeText(getApplicationContext(), "LAST", Toast.LENGTH_SHORT).show();

                } else {
                    xCourant = motionEvent.getX();
                    yCourant = motionEvent.getY();
                    Toast.makeText(getApplicationContext(), "EN COURS", Toast.LENGTH_SHORT).show();

                }

                ImageView imageView = (ImageView) view;

                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();

                Canvas canvas = new Canvas(bmap);

                Paint p = new Paint();
                p.setColor(Color.RED);
                p.setAlpha(45);

                Rect rectangle = new Rect(0,0,imageView.getBottom(),imageView.getRight());
                canvas.drawBitmap(bitmap, null, rectangle, null);

                if(xInit != null && yInit != null) {
                    canvas.drawPoint(xInit, yInit, p);
                }

                if(xCourant != null && yCourant != null) {
                    canvas.drawRect(xInit, yInit, xCourant, yCourant, p);
                }

                imageView.setImageBitmap(bmap);
                return true;
            }
        });

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

    public void receiveServerResult(int resultCode) {
        Log.i("FormulairePhoto", "Result transmetted :" + Integer.toString(resultCode));
        switch (resultCode) {
            case -1:
                //error
                Toast.makeText(this, "Error while reading server answer", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                FormulairePhoto.this.finish();
                //ok
                Toast.makeText(this, "Merci ! Votre point d'interêt a bien été reçu", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Server error", Toast.LENGTH_SHORT).show();
                //not ok
                break;
        }
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

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                        //Log.e("Camera", e.toString());
                    }
                }else if (resultCode == Activity.RESULT_CANCELED) {
                    // User cancelled the image capture
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    FormulairePhoto.this.finish();
                } else {
                    // Image capture failed, advise user
                    Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private View.OnClickListener ajouterPointInteret = new View.OnClickListener() {
        public void onClick(View arg0) {
            if(userLoc.getLocation() != null) {
                arg0.setEnabled(false);
                arg0.setClickable(false);

                double latitude = userLoc.getLocation().getLatitude();
                double longitude = userLoc.getLocation().getLongitude();
                /*
                Context context = getApplicationContext();
                CharSequence text = "Location ["+userLoc.getLocation().getLatitude()+", "+userLoc.getLocation().getLongitude()+"] Alt : "+userLoc.getLocation().getAltitude()+ "m - Précision : "+userLoc.getLocation().getAccuracy();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                */
                Map<String, Object> params = new HashMap();
                params.put("tag", tag);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("photo", bitmap);
                params.put("username", settings.getString("pseudo", ""));
                params.put("x1", x1);
                params.put("x2", x2);
                params.put("y1", y1);
                params.put("y2", y2);
                Toast.makeText(FormulairePhoto.this, "Envoi en cours...", Toast.LENGTH_LONG).show();
                APIService.INSTANCE.pushPointOfInterest(FormulairePhoto.this, params);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Impossible d'ajouter le point d'intêret. Veuillez vérifier que le GPS est activé.", Toast.LENGTH_LONG).show();
            }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }
}
