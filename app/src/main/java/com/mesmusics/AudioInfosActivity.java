package com.mesmusics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AudioInfosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_infos);
    }

    public void returnToAccueil(View view){
    Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
    startActivityForResult(myIntent, 0);
    }
}
