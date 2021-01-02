package com.mesmusics;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mesmusics.adaptater.AudioAdaptaterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {
    private ArrayList<AudioFile> audioFiles;
    private View view;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioFiles = Utility.readPlayList(this);
        //start(view,context);
        setContentView(R.layout.activity_playlist);

        Button navigation = (Button) findViewById(R.id.button);
      /*  navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });*/
    }

    public void initAudios(){

    }

    public void goToMenu(View view){
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void start (View view, Context context){

        //Utility.addToPlaylist(this, view, audioFiles);
    }


    
}

