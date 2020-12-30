package com.mesmusics.adaptater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mesmusics.AudioFile;
import com.mesmusics.R;

import java.util.ArrayList;

public class AudioAdaptater  extends BaseAdapter {

    private ArrayList<AudioFile> audioFiles;
    private LayoutInflater audioInf;

    public AudioAdaptater(Context c, ArrayList<AudioFile> audioFiles){
        this.audioFiles = audioFiles;
        this.audioInf =LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return audioFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
        return audioLay;
    }
}