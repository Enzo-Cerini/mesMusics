package com.mesmusics;

import android.content.Intent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mesmusics.adaptater.AudioAdaptaterView;

public class InfoActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST = 1;
    private AudioService audioService;
    private Intent playIntent;
    private boolean audioBound = false;
    private MediaPlayer mediaPlayer;
    private AudioAdaptaterView audioAdaptaterView;
    private AudioFileManager audioFileManager;
    private AudioController audioController;
    private boolean paused = false;
    private boolean playbackpaused = true;
    private SeekBar seekBar;
    private TextView tvTime;
    private SensorManager sensorManager = null;
    private long previousTime;


    //play next
    private void playNext(){
        audioService.playNext();
        if(playbackpaused){
            ((ImageView)findViewById(R.id.iv_play)).setImageResource(R.drawable.ic_pause_circle_outline_white);
            setAudioController();
            playbackpaused = false;
        }
        audioAdaptaterView.setSelectedPosition(audioService.getAudioPos());
        audioAdaptaterView.notifyDataSetChanged();
        audioController.show(0);
    }

    public void previousClick(View view){
        playPrev();
    }

    public void nextClick(View view){
        playNext();

    }

    //play previous
    private void playPrev(){
        audioService.playPrev();
        if(playbackpaused){
            ((ImageView)findViewById(R.id.iv_play)).setImageResource(R.drawable.ic_pause_circle_outline_white);
            setAudioController();
            playbackpaused = false;
        }
        audioAdaptaterView.setSelectedPosition(audioService.getAudioPos());
        audioAdaptaterView.notifyDataSetChanged();
        audioController.show(0);
    }


}
