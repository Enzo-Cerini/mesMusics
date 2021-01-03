package com.mesmusics;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

    public class AudioBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
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

    @Override
    public void onPrepared(MediaPlayer mp){
        mp.start();
        NotificationChannel channel = new NotificationChannel("001","cc", NotificationManager.IMPORTANCE_MIN);
        channel.setLightColor( Color.BLUE );
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this,"001");
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.selector_play)
                .addAction(R.drawable.ic_launcher_foreground, "Play/Pause", pendingIntent)
                .setTicker(audioTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(audioTitle);

        NotificationChannel notificationChannel = new NotificationChannel("001", "cc", NotificationManager.IMPORTANCE_NONE);
        builder.setChannelId("001");
        Notification notification = builder.build();
        startForeground(NOTIFY_ID, notification);


    /*    val channelId = "001";
        val channelName = "myChannel";
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.lightColor = Color.BLUE;
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE;
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
        manager.createNotificationChannel(channel);
        val notification: Notification

                notification = Notification.Builder(applicationContext, channelId)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(101, notification)
*/

        //   notification.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
        //  NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationManager.notify(11, notification);

        //   notificationManager.notify(NOTIFY_ID , builder.build());


        //startForeground(NOTIFY_ID, notification);

       /* NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.selector_play)
                .setTicker(audioTitle)
                .setContentTitle("Playing")
                .setContentText(audioTitle);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(AudioService.this);
.*
        notificationManager.notify(NOTIFY_ID, builder.build());*/
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener){
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    public void initAudioPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
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

    public int getAudioPos(){
        return this.audioPos;
    }

    public void setAudio(int audioPos){
        this.audioPos = audioPos;
    }

    public void setAudioFiles(ArrayList<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
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

    public void stop(){
        mediaPlayer.stop();
    }
}