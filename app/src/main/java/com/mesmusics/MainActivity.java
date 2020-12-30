package com.mesmusics;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mesmusics.adaptater.AudioAdaptater;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MediaPlayer music;
    boolean isRunning = false;

    ArrayList<AudioFile> audioFiles = new ArrayList<AudioFile>();

    private AudioFileManager audioFileManager;
    private ListView audioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioView = (ListView)findViewById(R.id.lv);
        audioFileManager = new AudioFileManager();
        audioFileManager.putAllAudioFromDevice(this);

        AudioAdaptater audioAdpt = new AudioAdaptater(this, audioFileManager.getAudioFiles());
        audioView.setAdapter(audioAdpt);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                System.out.println("deja demandé ");
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        else {
            System.out.println("Permission OK");
        }

        //putAllAudioFromDevice();
        //EXISTANT
        //   audioFileManager.getAudioFiles().add(new AudioFile("url","titre","album","artiste"));
        //   audioFileManager.getAudioFiles().add(new AudioFile("url1","titre1","album1","artiste1"));
        /////

        try {

            ArrayList<String> arrayList = new ArrayList<String>();
            ArrayAdapter<String> adapterView = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, arrayList);
            // ((ListView)findViewById(R.id.lv)).setAdapter(adapterView);

            music = new MediaPlayer();
            music.setDataSource(this,Uri.parse("android.resource://com.mesmusics/raw/son"));
            music.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSoundHandler(View view) {
        if(isRunning)
            pauseSound();
        else
            playSound();
    }
    public void playSound() {
        isRunning = true;
        music.start();
    }
    public void pauseSound(){
        isRunning = false ;
        music.pause();
    }
}
