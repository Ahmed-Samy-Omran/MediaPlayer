package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // Widgets
    Button forward_btn, back_btn, play_btn, stop_button;
    TextView time_txt, title_txt;
    WaveformSeekBar waveformSeekBar;

    // Media player
    MediaPlayer mediaPlayer;

    // Handlers
    Handler handler = new Handler();

    // Variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;
    boolean isPlaying = false; // To track play/pause state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_btn = findViewById(R.id.play_btn);
//        stop_button = findViewById(R.id.pause_btn);
        forward_btn = findViewById(R.id.forward_btn);
        back_btn = findViewById(R.id.bcakward_btn);

        title_txt = findViewById(R.id.song_title);
        time_txt = findViewById(R.id.time_left_text);

        waveformSeekBar = findViewById(R.id.waveformSeekBar);

        // Create media player
        mediaPlayer = MediaPlayer.create(this, R.raw.batal_3alam);

        // Display the name of the song at the title song
        title_txt.setText("Batal 3alam");

        // Load waveform data
        List<Float> waveformPoints = WaveformUtils.generateDummyWaveform();
        waveformSeekBar.setWaveformData(waveformPoints);

        // Set up the waveform seek bar listener
        waveformSeekBar.setOnProgressChangedListener(new WaveformSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float progress) {
                int seekToPosition = (int) (progress * finalTime);
                mediaPlayer.seekTo(seekToPosition);
                startTime = seekToPosition;
                updateSongTimeDisplay();
            }
        });

        // Add functionalities for the buttons
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseMusic();
                } else {
                    playMusic();
                }
            }
        });

//        stop_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.pause();
//            }
//        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if (temp + forwardTime <= finalTime) {
                    startTime += forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime -= backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Can't Go Back!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void playMusic() {
        mediaPlayer.start();
        isPlaying = true;
        play_btn.setBackgroundResource(R.drawable.pause_btn); // Change to pause icon

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            oneTimeOnly = 1;
        }

        updateSongTimeDisplay();
        handler.postDelayed(updateSongTime, 100);
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        isPlaying = false;
        play_btn.setBackgroundResource(R.drawable.play_button); // Change to play icon
    }

    private void updateSongTimeDisplay() {
        time_txt.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
        ));
    }

    private Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            updateSongTimeDisplay();

            // Update waveform progress
            waveformSeekBar.setProgress((float) startTime / (float) finalTime);

            handler.postDelayed(this, 1000);
        }
    };
}
