package com.mesmusics;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import com.mesmusics.adaptater.AudioAdaptaterView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

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

    boolean isRunning = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "No permission granted!",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        } else {
            setAudioController();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(this, AudioService.class);
            bindService(playIntent, audioConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private ServiceConnection audioConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder)service;
            //get service
            audioService = binder.getService();
            //init list and view and pass it
            initAudios();
            audioService.setAudioFiles(audioFileManager.getAudioFiles());
            audioBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioBound = false;
        }
    };

    public void initAudios(){
        ListView listView = findViewById(R.id.lv);
        audioFileManager = new AudioFileManager();
        audioFileManager.getAllAudioFileFromDevice(this);
        audioService.setAudioFiles(audioFileManager.getAudioFiles());
        audioAdaptaterView = new AudioAdaptaterView(this, audioFileManager.getAudioFiles(), audioService);
        listView.setAdapter(audioAdaptaterView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch ( item.getItemId() ){
            case R.id.shuffle:
                audioService.setShuffle();
                break;

            case R.id.iv_play:
                playSound();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //play next
    private void playNext(){
        audioService.playNext();
        if(playbackpaused){
            setAudioController();
            playbackpaused = false;
        }
        audioController.show(0);
    }

    //play previous
    private void playPrev(){
        audioService.playPrev();
        if(playbackpaused){
            setAudioController();
            playbackpaused = false;
        }
        audioController.show(0);
    }


    public void audioPicked(View view){
        //audioService.setAudio( (Integer)view.getTag() );
        if(playbackpaused) {
            // ((ImageView)findViewById(R.id.iv_play)).setImageRessource();
            audioService.playAudio();
            playbackpaused = false;
        }
        else{
            audioService.pausePlayer();
            playbackpaused = true;
        }
    }

    public void setAudioController(){
        audioController = new AudioController(this);
        audioController.setPrevNextListeners(view -> playNext(), view -> playPrev());
    }

    public AudioController getAudioController(){
        return this.audioController;
    }

    public boolean getPlaybackPaused(){
        return playbackpaused;
    }

    public void setPlaybackPaused(boolean playbackpaused){
        this.playbackpaused = playbackpaused;
    }




    @Override
    public void onDestroy() {
        stopService(playIntent);
        super.onDestroy();
    }


    @Override
    public void start() {
        audioService.startAudio();
    }

    @Override
    public void pause() {
        playbackpaused = true;
        audioService.pausePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    protected void onStop() {
        audioController.hide();
        super.onStop();
    }

    @Override
    public int getDuration() {
        if(audioService != null && audioBound)
            return audioService.getDuration();
        else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(audioService != null && audioBound && audioService.isPlaying())
            return audioService.getPos();
        else
            return 0;
    }

    @Override
    public void seekTo(int pos) {
        audioService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(audioService != null && audioBound )
            return audioService.isPlaying();
        else
            return false;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }




    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }




    public void playSoundHandler(View view) {
        if(isRunning)
            pauseSound();
        else
            playSound();
    }
    public void playSound() {
        isRunning = true;
        mediaPlayer.start();
    }
    public void pauseSound(){
        isRunning = false ;
        mediaPlayer.pause();
    }

}
