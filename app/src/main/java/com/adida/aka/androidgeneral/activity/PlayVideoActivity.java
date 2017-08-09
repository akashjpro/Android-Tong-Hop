package com.adida.aka.androidgeneral.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.widget.Constans;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Formatter;
import java.util.Locale;

import static com.adida.aka.androidgeneral.widget.Constans.API_KEY;

public class PlayVideoActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,
        View.OnClickListener {
    public static final int REQUEST_VIDEO = 111;

    private boolean isDragging; // check user drag
    private boolean isEndTouch;
    private boolean isExit = false;

    private String mId = "";
    private YouTubePlayerView mYouTubePlayerView;

    private YouTubePlayer mYouTubePlayer;
    private ImageButton mBtnPlayPause, mImgbFullScreen;
    private SeekBar mSeekBar;
    private TextView mTxtStartTime, mTxtEndTime;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        initView();
        addListeners();

    }

    private void initView() {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mYouTubePlayerView =  findViewById(R.id.youtubePlayerView);
        mBtnPlayPause      =  findViewById(R.id.imgbtnPlayPause);
        mImgbFullScreen    =  findViewById(R.id.fullSreen);
        mSeekBar           =  findViewById(R.id.video_seekbar);
        mTxtStartTime      =  findViewById(R.id.thoiGianBT);
        mTxtEndTime        =  findViewById(R.id.thoiGianKT);

        Intent intent = getIntent();
        mId = intent.getStringExtra(Constans.EXTRA_ID_VIDEO);
        mYouTubePlayerView.initialize(API_KEY, PlayVideoActivity.this);
    }

    private void addListeners() {

        mBtnPlayPause.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        mImgbFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int orientation = PlayVideoActivity.this.getResources().getConfiguration().orientation;
                switch(orientation) {
                    case Configuration.ORIENTATION_PORTRAIT:
                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case Configuration.ORIENTATION_LANDSCAPE:
                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }
            }
        });
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mYouTubePlayer = youTubePlayer;
        mYouTubePlayer.cueVideo(mId);
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mYouTubePlayer.setPlayerStateChangeListener(stateChangeListener);
        mYouTubePlayer.setPlaybackEventListener(playbackEventListener);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, REQUEST_VIDEO);
        }else {
            Toast.makeText(this, "Video error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_VIDEO){
            mYouTubePlayerView.initialize(API_KEY, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


    YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            isEndTouch = false;
            mShowProgress.run();
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onStopped() {
        }

        @Override
        public void onBuffering(boolean isBuffering) {
            ViewGroup ytView = (ViewGroup) mYouTubePlayerView.getRootView();
            ProgressBar progressBar;
            progressBar = findProgressBar(ytView);
            try {
                //The ProgressBar is at position 0 -> 3 -> 2 in the view tree of the Youtube Player Fragment
                ViewGroup child1 = (ViewGroup)ytView.getChildAt(0);
                ViewGroup child2 = (ViewGroup)child1.getChildAt(3);
                progressBar = (ProgressBar)child2.getChildAt(2);
            } catch (Throwable t) {
                // As its position may change, we fallback to looking for it
                progressBar = findProgressBar(ytView);
                // TODO I recommend reporting this problem so that you can update the code in the try branch: direct access is more efficient than searching for it
            }
            //progressBar.setBackgroundColor(Color.MAGENTA);

            int visibility = isBuffering ? View.VISIBLE : View.INVISIBLE;
            if (progressBar != null) {
                progressBar.setVisibility(visibility);
                // Note that you could store the ProgressBar instance somewhere from here, and use that later instead of accessing it again.
            }
        }

        @Override
        public void onSeekTo(int i) {

        }
        private ProgressBar findProgressBar(View view) {
            if (view instanceof ProgressBar) {
                return (ProgressBar) view;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
                    if (res != null) return res;
                }
            }
            return null;
        }
    };


    YouTubePlayer.PlayerStateChangeListener stateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
        }

        @Override
        public void onLoaded(String s) {
            mYouTubePlayer.play();
        }

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onVideoStarted() {
        }

        @Override
        public void onVideoEnded() {
            mBtnPlayPause.setImageResource(R.drawable.ic_play);
            mYouTubePlayer.pause();
            isEndTouch = true;
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Toast.makeText(PlayVideoActivity.this, "onError", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        if(isEndTouch){
            mYouTubePlayer.play();
            mBtnPlayPause.setImageResource(R.drawable.ic_pause);
        }else {
            if(mYouTubePlayer != null & !mYouTubePlayer.isPlaying()){
                mYouTubePlayer.play();
                mBtnPlayPause.setImageResource(R.drawable.ic_pause);
            }else {
                mYouTubePlayer.pause();
                mBtnPlayPause.setImageResource(R.drawable.ic_play);
            }
        }

    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            long duration = mYouTubePlayer.getDurationMillis();
            long newposition = (duration * progress) / 1000L;
            mYouTubePlayer.seekToMillis( (int) newposition);
            if (mTxtStartTime != null)
                mTxtStartTime.setText(stringForTime( (int) newposition));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isDragging = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isDragging = false;
            setProgress();
            mShowProgress.run();
        }
    };

    @Override
    public void onBackPressed() {
        isExit = true;
        mYouTubePlayer.pause();
        finish();
        super.onBackPressed();
    }

    /**
     * Format time for song
     * @param timeMs
     * @return
     */
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    /**
     * Set time progress
     * @return
     */
    private int setProgress() {
        if (mYouTubePlayer == null || isDragging) {
            return 0;
        }

        int position = mYouTubePlayer.getCurrentTimeMillis();
        int duration = mYouTubePlayer.getDurationMillis();
        if (mSeekBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mSeekBar.setProgress((int) pos);
            }
        }

        if (mTxtEndTime != null)
            mTxtEndTime.setText(stringForTime(duration));
        if (mTxtStartTime != null)
            mTxtStartTime.setText(stringForTime(position));

        return position;
    }


    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            if(!isExit){
                int pos = setProgress();
                final Handler handler = new Handler();
                if (!isDragging && mYouTubePlayer.isPlaying()) {
                    handler.postDelayed(mShowProgress, 1000 - (pos % 1000));
                }
            }
        }
    };


}
