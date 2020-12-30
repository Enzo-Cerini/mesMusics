package com.mesmusics;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MediaPlayer music;
    AudioFileManager audioFileManager = new AudioFileManager();
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) == true)
                System.out.println("deja demandé ");

            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        else {
            System.out.println("TOUTT MARRCHE BIEN");
        }

        music = new MediaPlayer();
        try {
            music.setDataSource(this,Uri.parse("android.resource://com.mesmusics/raw/son"));
            music.prepare();
            audioFileManager.putAllAudioFromDevice(this);
            System.out.println(audioFileManager.getAudioFiles());
          /*  ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("fffff");
            arrayList.add("fffff");
            arrayList.add("fffff");
            arrayList.add("fffff");
            ArrayAdapter<String> adapterView = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, arrayList);


            ((ListView)findViewById(R.id.lv)).setAdapter(adapterView);
*/
        } catch (IOException e) {
            e.printStackTrace();
        }
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

  /*  String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
        //here populate your listview
        Log.d("Files", "FileName:" + file[i].getName());
        }*/

