package com.mesmusics;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


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
        final ArrayList<AudioFile> tempAudioFiles = new ArrayList<>();


        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);

        if (c != null) {
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
        }
        this.audioFiles = tempAudioFiles;
    }
}


