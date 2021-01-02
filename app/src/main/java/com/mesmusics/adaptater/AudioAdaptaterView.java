package com.mesmusics.adaptater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mesmusics.AudioFile;
import com.mesmusics.AudioService;
import com.mesmusics.MainActivity;
import com.mesmusics.R;
import com.mesmusics.Utility;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioAdaptaterView extends BaseAdapter {

    private final ArrayList<AudioFile> audioFiles;
    private final LayoutInflater audioInf;
    private final AudioService audioService;
    private final Context context;
    private View previousView;
    private ImageView imageView;

    public AudioAdaptaterView(Context c, ArrayList<AudioFile> audioFiles, AudioService audioService){
        this.audioFiles = audioFiles;
        this.audioInf =LayoutInflater.from(c);
        this.audioService = audioService;
        this.context = c;
        this.previousView = null;
    }

    private class ViewHolder {
        View view;

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


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        holder = new ViewHolder();
        //map to song layout
        RelativeLayout audioLay = (RelativeLayout)audioInf.inflate
                (R.layout.music_poster, parent, false);
        audioLay.setBackgroundResource(R.drawable.bg_key);
        //get title and artist views
        ImageView imageView = (ImageView)audioLay.findViewById(R.id.iv_play_active);
        TextView audioView = (TextView)audioLay.findViewById(R.id.audio_title);
        TextView artistView = (TextView)audioLay.findViewById(R.id.audio_artist);
        TextView durationView = (TextView)audioLay.findViewById(R.id.tv_duration);

        //get song using position
        AudioFile currSong = audioFiles.get(position);

        //get title and artist strings
        audioView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        durationView.setText(Utility.convertDuration(currSong.getDuration()));

        //set position as tag
        audioLay.setTag(position);

        holder.view = audioLay;

        //  if (this.audioFiles.contains(ticket.getTicketID())
        //        && this.isOnUnload()) {
        holder.view.setSelected(true);
        //    }

        audioLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioService.setAudio( (Integer)view.getTag() );
                audioService.playAudio();
                MainActivity mainActivity = (MainActivity)context;
                if( mainActivity.getPlaybackPaused()){
                    ((ImageView)mainActivity.findViewById(R.id.iv_play)).setImageResource(R.drawable.ic_pause_circle_outline_white);
                    mainActivity.setAudioController();
                    mainActivity.setPlaybackPaused(false);
                }
                changeSelectedRow(view);
                mainActivity.manageToolbar();
                mainActivity.setFirst(false);
                mainActivity.getAudioController().show(0);
                ListView lv = mainActivity.findViewById(R.id.lv);
                //lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                //  lv.setItemSelected((int)view.getTag());
                System.out.println(lv.getCheckedItemCount());
            }
        });
        return audioLay;
    }

    @SuppressLint("ResourceAsColor")
    public void changeSelectedRow(View view){
        if(previousView != null) {
            previousView.setSelected(false);
            ((ImageView)previousView.findViewById(R.id.iv_play_active)).setVisibility(View.INVISIBLE);
        }
        view.setSelected(true);
        ((ImageView)view.findViewById(R.id.iv_play_active)).setVisibility(View.VISIBLE);
        previousView = view;
    }

}