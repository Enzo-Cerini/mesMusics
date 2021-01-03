package com.mesmusics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class addPlaylistActivity extends AppCompatActivity {
    private ArrayList<AudioFile> audioFiles;
    private View view;
    private Context context;
    private AudioFileManager audioFileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
    }

    public void confirmAddToPlaylist(View view){
        Utility.addToPlaylistSimple(this,view,getIntent().getExtras().getString("title"),getIntent().getExtras().getString("path"));
        Toast.makeText(this, "Le titre a bien été ajouté à la playlist", Toast.LENGTH_SHORT).show();
        goToMenu(view);
    }

    public void goToMenu(View view){
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void start (View view, Context context){

        //Utility.addToPlaylist(this, view, audioFiles);
    }

}

