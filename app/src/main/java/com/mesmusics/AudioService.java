package com.mesmusics;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private ArrayList<AudioFile> audioFiles;
    private int audioPos;
    private final IBinder audioBind = new AudioBinder();

    private boolean shuffle = false;
    private Random ramdom;
    private static final int NOTIFY_ID=1;
    private String audioTitle = "";

    public void onCreate(){
        super.onCreate();
        audioPos = 0;
        ramdom = new Random();
        mediaPlayer = new MediaPlayer();
        initAudioPlayer();
    }

    public void initAudioPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setAudioFiles(ArrayList<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
    }

    public class AudioBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }

    public void setShuffle(){
        if(shuffle)
            shuffle = false;
        else
            shuffle = true;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return audioBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onDestroy(){
        stopForeground(true);
    }

    public void playAudio(){
        mediaPlayer.reset();
        AudioFile audioPlay = audioFiles.get(audioPos);
        audioTitle = audioPlay.getTitle();
        long currentAudio = audioPlay.getId();
        Uri audioUri = ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, currentAudio);
        try {
            mediaPlayer.setDataSource(getApplicationContext(),audioUri);
        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "Error setting data source",e);
        }
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp){
        mp.start();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.selector_play)
                .setTicker(audioTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(audioTitle);
        Notification notification = builder.build();
       // startForeground(NOTIFY_ID, notification);
    }

    public void setAudio(int audioPos){
        this.audioPos = audioPos;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mediaPlayer.getCurrentPosition() >= 0){
            mediaPlayer.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public int getPos(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer(){
        mediaPlayer.pause();
    }

    public void seek(int pos){
        mediaPlayer.seekTo(pos);
    }

    public void startAudio(){
        mediaPlayer.start();
    }

    public void playPrev(){
        audioPos--;
        if(audioPos < 0)
            audioPos=audioFiles.size()-1;
        playAudio();
    }

    public void playNext(){
        if (shuffle){
            int newAudio = audioPos;
            while (newAudio == audioPos){
                newAudio = ramdom.nextInt(audioFiles.size());
            }
            audioPos = newAudio;
        }
        else {
            audioPos++;
            if (audioPos >= audioFiles.size())
                audioPos = 0;
        }
        playAudio();
    }

}
