package com.adida.aka.androidgeneral.fragment;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.aka.androidgeneral.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import static com.adida.aka.androidgeneral.R.id.seekBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFragment extends android.app.Fragment {

    private ImageButton mIbtnPlay, mIbtnPrev, mIbtNext, mIbtShuffle, mIbnRepeat;
    private ImageView mImgDiaTron;
    private SeekBar mSkSong;
    private ArrayList<Integer> mListSong;
    private TextView mTxtTimeStart, mTxtTimeTotal;
    private MediaPlayer mMediaPlayer;
    private int mIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_audio, container, false);
        initView();
        initListData();
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        mMediaPlayer = MediaPlayer.create(getActivity(), mListSong.get(mIndex));
        mIbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    mIbtnPlay.setImageResource(R.drawable.ic_play_white);
                    mImgDiaTron.clearAnimation();
                }
                else{
                    mImgDiaTron.startAnimation(animation);
                    mMediaPlayer.start();
                    mIbtnPlay.setImageResource(R.drawable.ic_pause_white);
                }
                timeSong();
                upDateCurrentTime();
            }
        });
        mIbtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIbtnPlay.setImageResource(R.drawable.ic_pause_white);
                xuLyNext();
            }
        });

        mIbtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIbtnPlay.setImageResource(R.drawable.ic_pause_white);
                mIndex--;
                if (mIndex < 0){
                    mIndex = mListSong.size() - 1 ;
                }
                mSkSong.setProgress(0);
                mMediaPlayer.stop();
                mMediaPlayer = MediaPlayer.create(getActivity(), mListSong.get(mIndex));
                mMediaPlayer.start();
                timeSong();
            }
        });

        mIbtShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShuffle){
                    mIbtShuffle.setImageResource(R.drawable.ic_shuffle_black);
                    isShuffle = false;
                    mIbnRepeat.setEnabled(true);
                }
                else {
                    Toast.makeText(getActivity(), "Phát ngẫu nhiên", Toast.LENGTH_SHORT).show();
                    mIbtShuffle.setImageResource(R.drawable.ic_shuffle_white);
                    isShuffle = true;
                    mIbnRepeat.setEnabled(false);
                }

            }
        });

        mIbnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRepeat){
                    mIbnRepeat.setImageResource(R.drawable.ic_repeat_black);
                    isRepeat = false;
                    mIbtShuffle.setEnabled(true);
                }
                else {
                    Toast.makeText(getActivity(), "Phát lặp lại", Toast.LENGTH_SHORT).show();
                    mIbtShuffle.setEnabled(false);
                    mIbnRepeat.setImageResource(R.drawable.ic_repeat_white);
                    isRepeat = true;
                }

            }
        });


        mSkSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser){
                    return;
                }

                mMediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() >= mMediaPlayer.getDuration()){
                    if(isRepeat){
                        playMusic();
                    }
                    else if (isShuffle){
                        Random randomBaiHat = new Random();
                        mIndex = randomBaiHat.nextInt(mListSong.size());
                        playMusic();
                    }
                    else {
                        xuLyNext();
                    }
                }
                mMediaPlayer.seekTo(seekBar.getProgress());// seek to time progress of song
            }
        });
        return mView;
    }

    /**
     * initialize controls view
     */
    private void initView() {
        mIbtnPlay =  mView.findViewById(R.id.imageButtonPlay);
        mIbtNext =  mView.findViewById(R.id.imageButtonNext);
        mIbtnPrev =  mView.findViewById(R.id.imageButtonpPevious);
        mIbtShuffle =  mView.findViewById(R.id.imageButtonShuffle);
        mIbnRepeat =  mView.findViewById(R.id.imageButtonRepeat);

        mImgDiaTron =  mView.findViewById(R.id.imageViewDiaTron);
        mTxtTimeTotal = mView.findViewById(R.id.txtThoiGianKetThuc);
        mTxtTimeStart = mView.findViewById(R.id.txtThoiGianBatDau);

        mSkSong = mView.findViewById(seekBar);
    }

    /**
     * next song
     */
    private void xuLyNext() {
        mIndex++;
        if (mIndex >= mListSong.size()){
            mIndex = 0;
        }
        playMusic();

    }

    /**
     * play audio music
     */
    private void playMusic() {
        mSkSong.setProgress(0);
        mMediaPlayer.stop();
        mMediaPlayer = MediaPlayer.create(getActivity(), mListSong.get(mIndex));
        mMediaPlayer.start();
        timeSong();
    }

    /**
     * update  current time for song
     */
    private void upDateCurrentTime(){
        final Handler handler = new Handler();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTxtTimeStart.setText(simpleDateFormat.format(mMediaPlayer.getCurrentPosition()));
                mSkSong.setProgress(mMediaPlayer.getCurrentPosition());
                if ( mMediaPlayer.getDuration() - mMediaPlayer.getCurrentPosition() <= 500){
                    if(isRepeat){
                        playMusic();
                    }else if(isShuffle){
                        Random randomBaiHat = new Random();
                        mIndex = randomBaiHat.nextInt(mListSong.size());
                        playMusic();
                    }
                    else {
                        xuLyNext();
                    }
                }
                handler.postDelayed(this, 500);
            }
        }, 0);
    }


    /**
     * set time for song
     */
    private void  timeSong(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        mTxtTimeTotal.setText(simpleDateFormat.format(mMediaPlayer.getDuration() )+ "");
        //set max is total duration for seekbar
        mSkSong.setMax(mMediaPlayer.getDuration());

    }

    /**
     * initialize data for list song
     */
    private void initListData() {
        mListSong = new ArrayList<Integer>();
        mListSong.add(R.raw.a);
        mListSong.add(R.raw.b);
        mListSong.add(R.raw.c);
        mListSong.add(R.raw.d);
        mListSong.add(R.raw.e);
        mListSong.add(R.raw.f);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaPlayer !=null)
             mMediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer !=null)
            mMediaPlayer.stop();
    }
}
