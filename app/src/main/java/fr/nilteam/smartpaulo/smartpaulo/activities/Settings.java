package fr.nilteam.smartpaulo.smartpaulo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.nilteam.smartpaulo.smartpaulo.R;

public class Settings extends AppCompatActivity {

    private EditText editText;
    private Button buttonSauvegarder;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editText = (EditText) findViewById(R.id.editText);
        buttonSauvegarder = (Button) findViewById(R.id.settingsSauvegarderButton);
        buttonSauvegarder.setOnClickListener(sauvegarder);

        settings = getSharedPreferences("SmartPaulo", 0);
        String pseudo = settings.getString("pseudo", "");
        editText.setText(pseudo);

    }

    private View.OnClickListener sauvegarder = new View.OnClickListener() {
        public void onClick(View arg0) {

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("pseudo", editText.getText().toString());
            editor.commit();

            Context context = getApplicationContext();
            CharSequence text = "Pseudo enregistré";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    };

}
