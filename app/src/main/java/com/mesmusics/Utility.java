package com.mesmusics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import com.mesmusics.adaptater.AudioAdaptaterView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class Utility {

    public static String convertDuration(long duration){
        long minutes = (duration / 1000 ) / 60;
        long seconds = (duration / 1000 ) % 60;
        String converted = String.format("%d:%02d", minutes, seconds);
        return converted;
    }

    public static ArrayList<AudioFile> readPlayList(Context context) {
        ArrayList<AudioFile> audioFiles = new ArrayList<AudioFile>();
        ContentResolver musicResolver = context.getContentResolver();
        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media._ID
        };

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            final Object value = entry.getValue();

            Uri audiosUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            //System.out.println(audiosUri);
            String selection = MediaStore.Audio.Media.DATA + " = ?";
            String[] args = new String[] {(String)value};
            Cursor musicCursor = musicResolver.query(audiosUri, projection, selection, args, null);

            if(musicCursor!=null && musicCursor.moveToNext()){
                //get columns
                String path = musicCursor.getString(0);
                String title = musicCursor.getString(1);
                String album = musicCursor.getString(2);
                String artist = musicCursor.getString(3);
                long duration = musicCursor.getLong(4);
                long id = musicCursor.getLong(5);

                //add songs to list
                audioFiles.add(new AudioFile(path, title, album, artist, duration,id));
            }
        }
       // System.out.println(audioFiles);
        return audioFiles;
    }

    public static void addToPlaylist(Context context, View view,ArrayList<AudioFile> audioFiles){
        AudioFile audioFile = audioFiles.get((Integer)((RelativeLayout)view.getParent()).getTag());


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(audioFile.getTitle(), audioFile.getPath());
        editor.apply();
        System.out.println(audioFile.getPath());
        System.out.println("addeeeeeeeeeeed");
    }

}
