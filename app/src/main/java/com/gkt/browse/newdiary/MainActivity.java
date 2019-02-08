package com.gkt.browse.newdiary;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.ParseAnalytics;


public class MainActivity extends AppCompatActivity {

   private ImageView fronImage ;
   private int traced = 2;
   private int track = 0;
   private boolean videocheck = false;
   private boolean audiocheck = false;
   private ImageView imageView2;
   private ImageView imageView3;
   private GridLayout gridLayout;
   private VideoView videoView;
   private MediaPlayer mediaPlayer;
   private SeekBar seekBar;
   private AudioManager audioManager;
   private TextView textView;
   private boolean trackImg = false;
   private ImageView iv;



  //this methos handles first page click animation
  public void firstAnimation(View view)
  {

      if(traced==2) {

          fronImage.animate().rotation(360).setDuration(1000);

          traced--;
          //Toast toast = Toast.makeText(getApplication(),"hello",Toast.LENGTH_LONG).show();

      }

      else if(traced==1)
      {

          fronImage.animate().rotation(-360).translationY(200f).setDuration(1500);

          fronImage.animate().setStartDelay(1000).alpha(0).setDuration(1000);

          gridLayout.animate().alpha(1).setDuration(1000);

          traced--;

      }

      else if(traced==0)
      {

          traced--;
      }

  }
//Rotate method

    public void RotateImage(ImageView iv)
    {
        iv.animate()
                .alpha(1)
                .rotation(-360f)
                .setDuration(1000);

    }

    //rotate anticlockwise method

    public void RotateImage1(ImageView iv)
    {
        iv.animate()
                .rotation(360)
                .alpha(0.1f)
                .setDuration(1000);

    }

    //method for location manipulation

//this method handles start screen imaged menu
    public void imageMenuclick(View view)
    {

      iv = (ImageView)view;
      int id = iv.getId();
      LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearPopup);


      switch (id)
      {
          case R.id.imageView8:

              if(track%2 ==0)
              {
                  RotateImage1(iv);

                  linearLayout.animate().alpha(1);

                  track++;
              }
              else if(track%2 ==1)
              {

                  linearLayout.animate().alpha(0);

                  RotateImage(iv);

                  track++;

              }

              if(videocheck)
              {
                  videoView = (VideoView)findViewById(R.id.videoView);

                  videoView.pause();

                  videoView.animate().alpha(0);

                  videocheck = false;

                  LinearLayout linearLayout1 =(LinearLayout)findViewById(R.id.linearPopup);

                  linearLayout1.setVisibility(View.VISIBLE);
              }
              break;

          case R.id.imageView:

              trackImg = true;
              RotateImage1(iv);

              final String arr[]={"Opening soon.","complete idea \nbefore timer\nstops","Thanks..Enjoy"};

               final  Class<?> cls = ScrollingNoteActivity.class;
              textView.animate().alpha(1).setDuration(1000);

              CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {

                  int i=0;
                  @Override
                  public void onTick(long l) {

                      textView.setText(arr[i++]);

                  }

                  @Override
                  public void onFinish() {

                      textView.setText("Go..");


                      callIdeaGame(cls);

                  }
              };

              countDownTimer.start();

              break;

          case R.id.imageView2:

              trackImg = true;

              RotateImage1(iv);

              final Class<?> ccls = PrivateDownloadActivity.class;

              callIdeaGame(ccls);

              break;

          case R.id.imageView3:

              trackImg = true;

              RotateImage1(iv);

              final Class<?> clsss = InfoManual.class;

              callIdeaGame(clsss);

              break;

          case R.id.imageView9:
              trackImg = true;

              RotateImage1(iv);

              final  Class<?> clssss = AccountInfo.class;

              callIdeaGame(clssss);
              break;

          case R.id.imageView7:

              trackImg = true;

              RotateImage(iv);

              final Class<?> reader = NewsReader.class;

              callIdeaGame(reader);
              break;

          case R.id.imageView6:
              //final Class<?> parser = StarterApplication.class;
             // callIdeaGame(parser);
              break;

      }



    }

    //this method handles video and audio

    public void startVideo(View view)
    {
        Button button = (Button)view;

        int id = view.getId();

        videoView = (VideoView)findViewById(R.id.videoView);

        if(id == R.id.video && !videocheck)
        {
            videocheck=true;

            videoView.animate().alpha(1).setDuration(1000);

           LinearLayout linearLayout = (LinearLayout)button.getParent();

            linearLayout.setVisibility(View.INVISIBLE);

           videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.songaudio);

            MediaController mediaController = new MediaController(this);

            mediaController.setAnchorView(videoView);

            videoView.setMediaController(mediaController);

            videoView.start();

        }



    }

    //handles audio

    public void startAudio(View view)
    {
        Button button = (Button)view;

        int id = view.getId();

        if(id == R.id.audio && !audiocheck)
        {
            audiocheck = true;

            mediaPlayer.start();

            seekBar.animate().alpha(1).setDuration(1000);
        }

        else  if(id == R.id.audio && audiocheck)
        {
            audiocheck = false;

            mediaPlayer.pause();

            seekBar.animate().alpha(0).setDuration(1000);
        }
    }

    protected void callIdeaGame(Class<?> cls)
    {
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

       // ParseAnalytics.trackAppOpenedInBackground(getIntent());

        fronImage = (ImageView) findViewById(R.id.image_upper);
        gridLayout = (GridLayout)findViewById(R.id.grid_menu);

        textView = (TextView)findViewById(R.id.InfotextView);


      mediaPlayer = MediaPlayer.create(this, R.raw.songaudio);
      audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

      int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      int minVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

      seekBar = (SeekBar)findViewById(R.id.seekBar);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(minVolume);


      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

              audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);

          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {

          }
      });

      //initilizeLocation();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        if(trackImg)
        {
            textView.animate().alpha(0).setDuration(100);
            RotateImage(iv);
            trackImg = false;
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    }

    //Extracting adresses from Location









