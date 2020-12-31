package com.mesmusics.adaptater;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mesmusics.AudioFile;
import com.mesmusics.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioAdaptaterView extends BaseAdapter {

    private final ArrayList<AudioFile> audioFiles;
    private final LayoutInflater audioInf;
    private final MediaPlayer mediaPlayer;

    public AudioAdaptaterView(Context c, ArrayList<AudioFile> audioFiles, MediaPlayer mediaPlayer){
        this.audioFiles = audioFiles;
        this.audioInf =LayoutInflater.from(c);
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public int getCount() {
        return audioFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return audioFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        RelativeLayout audioLay = (RelativeLayout)audioInf.inflate
                (R.layout.music_poster, parent, false);


        //get title and artist views
        TextView audioView = (TextView)audioLay.findViewById(R.id.audio_title);
        TextView artistView = (TextView)audioLay.findViewById(R.id.audio_artist);

        //get song using position
        AudioFile currSong = audioFiles.get(position);

        //get title and artist strings
        audioView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());

        //set position as tag
        audioLay.setTag(position);
        audioLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                    mediaPlayer.setDataSource(  audioFiles.get((Integer)v.getTag()).getPath() );
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println( audioFiles.get((Integer)v.getTag()).getDuration() );
            }
        });
        return audioLay;
    }

}