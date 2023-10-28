package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // Widgets

    Button forward_btn, back_btn, play_btn, stop_button;
    TextView time_txt, title_txt;
    SeekBar seekbar;

    //media player
     MediaPlayer  mediaPlayer; //MediaPlayer class is used to play audio and video files.

    // Handlers
    Handler handler=new Handler(); // Handler is important for managing tasks on the UI thread, delaying task execution

    //variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;   //displaying the time of this song

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_btn = findViewById(R.id.play_btn);
        stop_button = findViewById(R.id.pause_btn);
        forward_btn = findViewById(R.id.forward_btn);
        back_btn = findViewById(R.id.back_btn);

        title_txt = findViewById(R.id.song_title);
        time_txt = findViewById(R.id.time_left_text);

        seekbar = findViewById(R.id.seekBar);

        //create media player
        mediaPlayer=MediaPlayer.create(this,
                R.raw.batal_3alam // batal_3alam name of the song in file called raw
                );

        // this to display the name of song at title song

        title_txt.setText(getResources().getIdentifier(
                "batal_3alam" ,//name of song
                "raw",//kind of dataType
                getPackageName() //It returns the package name of the Android application in which it is called.
        ));
      seekbar.setClickable(false);

       //  Adding functionalities for the buttons

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int) startTime; //casting startTime from double to int
                if (temp +forwardTime<=finalTime) {
                    startTime=startTime+forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else {
                    Toast.makeText(MainActivity.this,
                            "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0){
                    startTime = startTime - backwardTime; //assign new position to startTime
                    mediaPlayer.seekTo((int) startTime); // go to new startTime
                }else{
                    Toast.makeText(MainActivity.this,
                            "Can't Go Back!", Toast.LENGTH_SHORT).show();
                }
            }
        });
     }

    @SuppressLint("DefaultLocale")
    private void playMusic() {
        mediaPlayer.start(); // when button is clicked start mediaPlayer

        finalTime=mediaPlayer.getDuration();
        startTime=mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {               //  finalTime represents a duration or time value.
            seekbar.setMax((int) finalTime); //assign max to seekbar  and cast it to int
            oneTimeOnly=1;                   // This ensures that the setup code is executed only once.
        }

        time_txt.setText(String.format( //format of string %d %d: Represents an integer.and it took 2 integer one for minutes and one for seconds
                "%%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),//This part converts the finalTime value from milliseconds to minutes using the
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-  // This calculates the remaining seconds after converting the entire time in milliseconds to minutes.
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes //. It does this by subtracting the number of seconds in the whole minutes from the total number of seconds.
                                ((long) finalTime))
        ));

        seekbar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime,100);// it's set to 100 milliseconds, so the task will be executed after 100 milliseconds.
    }       //UpdateSongTime is likely a Runnable or a method reference that defines the task you want to execute. It represents the code that will be run when the delayed execution occurs.

    // Creating the Runnable
    //In Java, a Runnable is an interface that defines a single method called run()
    //used for creating and managing threads in a multi-threaded program.
    private Runnable UpdateSongTime=new Runnable() { //create a new ooobj runable
        @Override
        public void run() {
        startTime=mediaPlayer.getCurrentPosition();
        time_txt.setText(
                String.format("%d min,%d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime ),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime )-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))

        );
        seekbar.setProgress((int) startTime);
        handler.postDelayed(this,1000);
        }
    };
}