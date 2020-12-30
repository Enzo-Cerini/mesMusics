package com.mesmusics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.util.ArrayList;

public class AudioFileManager {
    ArrayList<AudioFile> audioFiles;
    MediaMetadataRetriever metadataRetriever;

    public AudioFileManager(ArrayList<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
        this.metadataRetriever = new MediaMetadataRetriever();
    }

    public AudioFileManager() {
        this.audioFiles = new ArrayList<AudioFile>();

    }

    public ArrayList<AudioFile> getAudioFiles() {
        return audioFiles;
    }

    public void putAllAudioFromDevice(final Context context) {

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri ;//= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            musicUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            musicUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        }

        System.out.println(musicUri.toString());
        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        };
        Cursor musicCursor = musicResolver.query(musicUri, projection, MediaStore.Audio.Media.IS_MUSIC, null, null);


        if(musicCursor!=null && musicCursor.moveToFirst()){
            System.out.println("music cursor pas null !!!!!!!!!!!!!!");
            //get columns
            String path = musicCursor.getString(0);
            String title = musicCursor.getString(1);
            String album = musicCursor.getString(2);
            String artist = musicCursor.getString(3);
            String duration = musicCursor.getString(4);
            //add songs to list
            do {
                audioFiles.add(new AudioFile(path, title, album, artist, duration));
            }
            while (musicCursor.moveToNext());
            System.out.println("album : "+ audioFiles.get(0).getAlbum());
            System.out.println("duree : "+audioFiles.get(0).getDuration());
            System.out.println("artiste : "+audioFiles.get(0).getArtist());
            System.out.println("titre : "+audioFiles.get(0).getTitle());
            System.out.println("path : "+audioFiles.get(0).getPath());
        }
        else
            System.out.println("il est vide !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}