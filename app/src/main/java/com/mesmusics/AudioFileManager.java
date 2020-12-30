package com.mesmusics;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AudioFileManager {
    ArrayList<AudioFile> audioFiles;
    MediaMetadataRetriever metadataRetriever;   // a voir

    public AudioFileManager(ArrayList<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
        this.metadataRetriever = new MediaMetadataRetriever();  // avoir
    }

    public AudioFileManager() {
        this.audioFiles = new ArrayList<AudioFile>();
    }

    public ArrayList<AudioFile> getAudioFiles() {
        return audioFiles;
    }

    public void putAllAudioFromDevice(final Context context) {
        final ArrayList<AudioFile> tempAudioFiles = new ArrayList<>();


        Uri uri = Uri.parse("android.resource://com.mesmusics/raw/");
        File f = new File("android.resource://com.mesmusics/raw/son.wav");
        Field[] fields = R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            Log.i("Raw Asset: ", fields[count].getName());
            AudioFile audioFile = new AudioFile();
            metadataRetriever.setDataSource(f.getAbsolutePath());
            String path = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String name = MediaStore.Audio.AudioColumns.TITLE;
            String album = MediaStore.Audio.AudioColumns.TITLE;
            String artist = MediaStore.Audio.AudioColumns.TITLE;

            audioFile.setTitle(name);
            audioFile.setAlbum(album);
            audioFile.setArtist(artist);
            audioFile.setPath(path);

            Log.e("Name :" + name, " Album :" + album);
            Log.e("Path :" + path, " Artist :" + artist);

            tempAudioFiles.add(audioFile);
        }


        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST};
        Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);

     /*   if (c != null) {
            while (c.moveToNext()) {
                AudioFile audioFile = new AudioFile();
                String path = c.getString(0);
                String name = c.getString(1);
                String album = c.getString(2);
                String artist = c.getString(3);

                audioFile.setTitle(name);
                audioFile.setAlbum(album);
                audioFile.setArtist(artist);
                audioFile.setPath(path);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                tempAudioFiles.add(audioFile);
            }
            c.close();
        }*/
        this.audioFiles = tempAudioFiles;
    }
}


