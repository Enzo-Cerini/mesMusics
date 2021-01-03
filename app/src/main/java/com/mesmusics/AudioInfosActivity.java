package com.mesmusics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AudioInfosActivity extends AppCompatActivity {
    private TextView titleTv;
    private TextView artitsTv;
    private TextView albumTv;
    private TextView durationTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_infos);
        initSongInfos();
    }

    public void initSongInfos(){
        titleTv = findViewById(R.id.Titre);
        artitsTv = findViewById(R.id.Artiste);
        albumTv = findViewById(R.id.Album);
        durationTv = findViewById(R.id.Duree);
        Intent intent = getIntent();
        String extraArtist = intent.getExtras().getString("artist");
        if(extraArtist.equals("<unknown>"))
            extraArtist = "Unknown";
        String title = "Titre : " + intent.getExtras().getString("title");
        String artist = "Artiste : " + extraArtist;
        String album = "Album : " + intent.getExtras().getString("album");
        String duration = "Durée : " + Utility.convertDuration(intent.getExtras().getLong("duration"));
        titleTv.setText(title);
        artitsTv.setText(artist);
        albumTv.setText(album);
        durationTv.setText(duration);
    }

    public void returnToAccueil(View view){
    Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
    startActivityForResult(myIntent, 0);
    }
}
