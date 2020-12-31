package com.mesmusics;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mesmusics.adaptater.AudioAdaptaterView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AudioService audioService;
    private Intent playIntent;
    private boolean audioBound = false;

    private static final int MY_PERMISSION_REQUEST = 1;
    ArrayAdapter<String> adapter;
    MediaPlayer mediaPlayer;
    ArrayList<String> testList;

    private AudioAdaptaterView audioAdaptaterView;
    //private ArrayList<AudioFile> audioFiles;
    private AudioFileManager audioFileManager;
    private ListView audioFileView;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            else
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        } else {
            doStuff();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(this, AudioService.class);
            bindService(playIntent, audioConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
    private ServiceConnection audioConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.MusicBinder binder = (AudioService.MusicBinder)service;
            //get service
            audioService = binder.getService();
            //pass list
            audioService.setAudioFiles(audioFileManager.getAudioFiles());
            audioBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioBound = false;
        }
    };

    public void doStuff(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(false) {
                    mp.stop();
                    mp.reset();
                    try {
                        mp.setDataSource("");
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();
                }
                else {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            }
        });
        //mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.son);
        ListView listView = (ListView) findViewById(R.id.lv);
        audioFileManager = new AudioFileManager();
        audioFileManager.getAllAudioFileFromDevice(this);
        audioAdaptaterView = new AudioAdaptaterView(this, audioFileManager.getAudioFiles(), mediaPlayer);
        listView.setAdapter(audioAdaptaterView);







        //listView.setAdapter(adapter);

    }

    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songLocation);
                testList.add(currentTitle + "\n" + currentArtist+ "\n" + currentLocation);
            } while (songCursor.moveToNext());
        }
        else
            Toast.makeText(this, "Aucun fichier audio trouvé",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                        doStuff();
                    }
                }
                else {
                    Toast.makeText(this, "No permission granted!",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

        //putAllAudioFromDevice();
        //EXISTANT
        //   audioFileManager.getAudioFiles().add(new AudioFile("url","titre","album","artiste"));
        //   audioFileManager.getAudioFiles().add(new AudioFile("url1","titre1","album1","artiste1"));
        /////
    public void azert(){
        try {

            ArrayList<String> arrayList = new ArrayList<String>();
            ArrayAdapter<String> adapterView = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, arrayList);
            // ((ListView)findViewById(R.id.lv)).setAdapter(adapterView);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this,Uri.parse(Environment.getExternalStorageDirectory()+"/Music/abc.mp3"));
            mediaPlayer.prepare();

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
        mediaPlayer.start();
    }
    public void pauseSound(){
        isRunning = false ;
        mediaPlayer.pause();
    }
}
