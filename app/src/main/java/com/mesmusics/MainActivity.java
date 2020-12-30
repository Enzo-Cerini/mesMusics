package com.mesmusics;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {
    MediaPlayer music;
    AudioFileManager audioFileManager = new AudioFileManager();
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) == true)
            {
                System.out.println("deja demandé ");
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            }
        }
        else
        {
            System.out.println("TOUTT MARRCHE BIEN");
        }


        //if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        //audioFileManager.putAllAudioFromDevice(this);

       // music = new MediaPlayer();
        /*audioFileManager.putAllAudioFromDevice(this);
        music = new MediaPlayer();
        try {
            music.setDataSource( audioFileManager.getAudioFiles().get(0).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

      /*  try {
            music.setDataSource(this,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            music.start();
        } catch (IOException e) {
            e.printStackTrace();
        } */
        //System.out.println(Uri.parse("/sdcard/Music/son.wav"));
        //music = MediaPlayer.create(this, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
           // music = MediaPlayer.create(this, R.raw.son);
       // }

        //System.out.println("C'est laaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +R.raw.son2);

           // audioFileManager.putAllAudioFromDevice(this);
    }

    public static MediaPlayer create (Context context, int resid){
        MediaPlayer media = MediaPlayer.create(getContext(), R.raw.file);
        return media;
    }

    public static MediaPlayer create (Context context, Uri uri){
        MediaPlayer media = MediaPlayer.create(getContext(),);
        media = MediaPlayer.create(getContext(), Uri.parse("android.ressource://com.mesmusics/raw/son.wav");
        media = MediaPlayer.create(getContext(), Uri.parse("android.ressource://com.mesmusics/raw/son2.wav");
        return media;
    }


    public void playSoundHandler(View view) {
        if(isRunning) {
            pauseSound();

        }
        else {
            playSound();
        }
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

