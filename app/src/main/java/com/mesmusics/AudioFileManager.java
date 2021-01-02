package com.mesmusics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;

public class AudioFileManager {
    ArrayList<AudioFile> audioFiles;

    public AudioFileManager() {
        this.audioFiles = new ArrayList<AudioFile>();
    }

    public ArrayList<AudioFile> getAudioFiles() {
        return audioFiles;
    }

    public void getAllAudioFileFromDevice(final Context context) {

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri ;
        musicUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media._ID
        };
        Cursor musicCursor = musicResolver.query(musicUri, projection, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){

            while (musicCursor.moveToNext()) {
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
        else
            Toast.makeText(context, "Aucun fichier audio trouvé",Toast.LENGTH_LONG).show();
    }
}

//  https://www.youtube.com/watch?v=D9--BF-W0AY

//  https://www.youtube.com/watch?v=TQg98mQL2hs

//  https://www.youtube.com/watch?v=U5yDjBUSAic