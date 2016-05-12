package com.alchemistdigital.ziko.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

import com.alchemistdigital.ziko.R;
import com.alchemistdigital.ziko.asynctask.TrimmVideo;
import com.alchemistdigital.ziko.utilities.VideoSliceSeekBar;

import java.io.File;

public class VideoCutterActivity extends AppCompatActivity {

    TextView textViewLeft, textViewRight;
    VideoSliceSeekBar videoSliceSeekBar;
    VideoView videoView;
    View videoControlBtn;
    Uri selectedUri;
    String selectedVideoFilePath;
    private AsyncTask<Void, Void, Void> trimmVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide notification bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_cutter);

        // toolbar set.
        Toolbar toolbar = (Toolbar) findViewById(R.id.videoCutterToolbar);
        setSupportActionBar(toolbar);

        // hide title of activity from toolbar.
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        selectedVideoFilePath = getIntent().getExtras().getString("selectedVideoPath");
        selectedUri = Uri.parse(new File(selectedVideoFilePath).toString());

        textViewLeft = (TextView) findViewById(R.id.left_pointer);
        textViewRight = (TextView) findViewById(R.id.right_pointer);

        videoSliceSeekBar = (VideoSliceSeekBar) findViewById(R.id.seek_bar);
        videoView = (VideoView) findViewById(R.id.video);
        videoControlBtn = findViewById(R.id.video_control_btn);

        initVideoView();
    }

    private void initVideoView() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar.SeekBarChangeListener() {
                    @Override
                    public void SeekBarValueChanged(int leftThumb, int rightThumb) {
                        textViewLeft.setText(getTimeForTrackFormat(leftThumb, true));
                        textViewRight.setText(getTimeForTrackFormat(rightThumb, true));
                    }
                });

                videoSliceSeekBar.setMaxValue(mp.getDuration());
                videoSliceSeekBar.setLeftProgress(0);
                videoSliceSeekBar.setRightProgress(10000); //10 segundos como máximo de entrada
                videoSliceSeekBar.setProgressMinDiff((5000 * 100)/mp.getDuration()); //Diferencia mínima de 5 segundos
                videoSliceSeekBar.setProgressMaxDiff((10000 * 100)/mp.getDuration());//Diferencia máxima de 10 segundos

                videoControlBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         performVideoViewClick();
                    }
                });
            }
        });
        videoView.setVideoURI(selectedUri);
    }


    private void performVideoViewClick() {
        if (videoView.isPlaying()) {
            videoView.pause();
            videoSliceSeekBar.setSliceBlocked(false);
            videoSliceSeekBar.removeVideoStatusThumb();
        } else {
            videoView.seekTo(videoSliceSeekBar.getLeftProgress());
            videoView.start();
            videoSliceSeekBar.setSliceBlocked(true);
            videoSliceSeekBar.videoPlayingProgress(videoSliceSeekBar.getLeftProgress());
            videoStateObserver.startVideoProgressObserving();
        }
    }

    public static String getTimeForTrackFormat(int timeInMills, boolean display2DigitsInMinsSection) {
        int minutes = (timeInMills / (60 * 1000));
        int seconds = (timeInMills - minutes * 60 * 1000) / 1000;
        String result = display2DigitsInMinsSection && minutes < 10 ? "0" : "";
        result += minutes + ":";
        if (seconds < 10) {
            result += "0" + seconds;
        } else {
            result += seconds;
        }
        return result;
    }


    private StateObserver videoStateObserver = new StateObserver();

    private class StateObserver extends Handler {

        private boolean alreadyStarted = false;

        private void startVideoProgressObserving() {
            if (!alreadyStarted) {
                alreadyStarted = true;
                sendEmptyMessage(0);
            }
        }

        private Runnable observerWork = new Runnable() {
            @Override
            public void run() {
                startVideoProgressObserving();
            }
        };

        @Override
        public void handleMessage(Message msg) {
            alreadyStarted = false;
            videoSliceSeekBar.videoPlayingProgress(videoView.getCurrentPosition());
            if (videoView.isPlaying() && videoView.getCurrentPosition() < videoSliceSeekBar.getRightProgress()) {
                postDelayed(observerWork, 50);
            } else {

                if (videoView.isPlaying()) videoView.pause();

                videoSliceSeekBar.setSliceBlocked(false);
                videoSliceSeekBar.removeVideoStatusThumb();
            }
        }
    }

    public void saveCuttedVideo(View view) {
        trimmVideo = new TrimmVideo(VideoCutterActivity.this,
                selectedVideoFilePath,
                videoSliceSeekBar.getLeftProgress(),
                videoSliceSeekBar.getRightProgress()).execute();
    }

    @Override
    protected void onStop() {
        if(trimmVideo != null)
            trimmVideo.cancel(true);
        super.onStop();
    }
}
