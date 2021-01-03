package com.mesmusics;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mesmusics.adaptater.AudioAdaptaterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private static final int MY_PERMISSION_REQUEST = 1;
    private AudioService audioService;
    private Intent playIntent;
    private boolean audioBound = false;
    private AudioAdaptaterView audioAdaptaterView;
    private AudioFileManager audioFileManager;
    private AudioController audioController;
    private boolean paused = false;
    private boolean playbackpaused = true;
    private SeekBar seekBar;
    private TextView tvTime;
    private ArrayList<AudioFile> allAudioFiles;
    private ArrayList<AudioFile> playlistFiles;
    private ArrayList<AudioFile> currentAudioFiles;
    private SensorManager sensorManager = null;
    private long previousTime;
    private float previousPosition = 0;

    private boolean isFirst = true;
    private TextView tvTitle;

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
            sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
            previousTime = -1;
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
            audioService.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    nextClick(null);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioBound = false;
        }
    };

    public void swithToConfirmPlaylist(View view){
        Intent myIntent = new Intent(getBaseContext(), addPlaylistActivity.class);
        int position = (Integer)((RelativeLayout)view.getParent()).getTag();
        //View v = ( (ListView)findViewById(R.id.lv) ).getChildAt(audioService.getAudioPos());
        if(position < currentAudioFiles.size()) {   //it means that the children is visible by the user
            myIntent.putExtra("path", audioFileManager.getAudioFiles().get(position).getPath());
            myIntent.putExtra("title", audioFileManager.getAudioFiles().get(position).getTitle());
            startActivityForResult(myIntent, 0);
        }
        else{
            Toast.makeText(this, "Impossible d'ajouter le titre a la playlist", Toast.LENGTH_SHORT).show();
        }

        //Utility.addToPlaylist(this,view,audioFileManager.getAudioFiles());
    }
    public void swithToAudioInfos(View view){
        Intent myIntent = new Intent(getBaseContext(), AudioInfosActivity.class);
        startActivityForResult(myIntent, 0);
    }
    public void addToPlaylist(View view){
        Utility.addToPlaylist(this,view,audioFileManager.getAudioFiles());
    }


    public void switchToAccueil(View view){
        ListView listView = findViewById(R.id.lv);
        audioService.setAudioFiles(allAudioFiles);
        audioAdaptaterView = new AudioAdaptaterView(this, allAudioFiles, audioService);
        listView.setAdapter(audioAdaptaterView);
        currentAudioFiles = allAudioFiles;
        reset();
        //manageToolbar();
        Button button = ( (Button)findViewById(R.id.button2) );
        button.setText("Ma playlist");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToPlaylist(v);
            }
        });
        handleSeekbar();
    }

    public void reset(){
        audioService.setAudio(0);
        if(audioService.isPlaying())
            audioService.stop();
        ((ImageView)findViewById(R.id.iv_play)).setImageResource(R.drawable.ic_play_circle_outline_white);
        playbackpaused = true;
    }

    public void switchToPlaylist(View view){
        playlistFiles = Utility.readPlayList(this);
        if(playlistFiles.size() > 0) {
            ListView listView = findViewById(R.id.lv);

            System.out.println(playlistFiles);
            audioService.setAudioFiles(playlistFiles);
            audioAdaptaterView = new AudioAdaptaterView(this, playlistFiles, audioService);
            listView.setAdapter(audioAdaptaterView);
            currentAudioFiles = playlistFiles;
            reset();
            Button button = ((Button) findViewById(R.id.button2));
            button.setText("Accueil");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToAccueil(v);
                }
            });
            handleSeekbar();
        }
        else{
            Toast.makeText(this, "La playslist est vide !", Toast.LENGTH_SHORT).show();
        }
    }

    public void initAudios(){
        ListView listView = findViewById(R.id.lv);
        audioFileManager = new AudioFileManager();
        audioFileManager.getAllAudioFileFromDevice(this);
        allAudioFiles = audioFileManager.getAudioFiles();
        audioService.setAudioFiles(allAudioFiles);
        audioAdaptaterView = new AudioAdaptaterView(this, allAudioFiles, audioService);
        listView.setAdapter(audioAdaptaterView);
        currentAudioFiles = allAudioFiles;
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        tvTime = (TextView)findViewById(R.id.tv_time);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        handleSeekbar();
    }

    public void manageToolbar() {
        Handler mHandler = new Handler();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setMax((int)currentAudioFiles.get(audioService.getAudioPos()).getDuration() / 1000);
                int currentPos = audioService.getPos() / 1000;
                seekBar.setProgress(currentPos);
                tvTime.setText(Utility.convertDuration((long)audioService.getPos()));
                mHandler.postDelayed(this,1000);
                tvTitle.setText(currentAudioFiles.get(audioService.getAudioPos()).getTitle());
            }
        });
    }

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

    private void handleSeekbar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(audioService != null && fromUser){
                    audioService.seek(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void audioPicked(View view){

        if(playbackpaused) {
            View v = ( (ListView)findViewById(R.id.lv) ).getChildAt(audioService.getAudioPos());
            if(v != null)   //it means that the children is visible by the user
                audioAdaptaterView.changeSelectedRow(v);
            if(isFirst) {
                audioService.playAudio();
                isFirst = false;
            }
            else
                audioService.startAudio();
            playbackpaused = false;
            ((ImageView)findViewById(R.id.iv_play)).setImageResource(R.drawable.ic_pause_circle_outline_white);
        }
        else{
            audioService.pausePlayer();
            playbackpaused = true;
            ((ImageView)findViewById(R.id.iv_play)).setImageResource(R.drawable.ic_play_circle_outline_white);
        }
        manageToolbar();
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
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                long currentTime = System.currentTimeMillis();

                if(previousTime == -1)
                    previousTime = 0;
                else {
                    if (currentTime - previousTime > 400) {
                        previousTime = currentTime;
                        float x = event.values[0];
                        float z = event.values[2];
                      //  System.out.println("x = " + x);
                      //  System.out.println("z = " + z);
                        if (x < 0 )
                            audioPicked(null);
                        if (z > 0 )
                            nextClick(null);
                        if (z < 0 )
                            previousClick(null);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        },sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_UI );
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


}
