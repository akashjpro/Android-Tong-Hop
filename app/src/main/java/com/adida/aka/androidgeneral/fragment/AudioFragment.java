package com.adida.aka.androidgeneral.fragment;


import android.graphics.drawable.StateListDrawable;
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

    private ImageButton ibtnPlay, ibtnPrev, ibtNext, ibtShuffle, ibnRepeat;
    private ImageView imgDiaTron;
    private SeekBar skSong;
    private ArrayList<Integer> arraySong;
    private TextView txtTimeStart, txtTimeTotal;
    private MediaPlayer mediaPlayer;
    private int vitri = 0;
    private boolean shuffle = false;
    private boolean repeat  = false;
    private View mView;


    public AudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_audio, container, false);
        initView();
        khoiTaoMang();
        final StateListDrawable stateListDrawablec = new StateListDrawable();
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        mediaPlayer = MediaPlayer.create(getActivity(), arraySong.get(vitri));
        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    ibtnPlay.setImageResource(R.drawable.play1_1);
                    imgDiaTron.clearAnimation();
                    // imgDiaTron.clearAnimation();
                    //stateListDrawablec.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.play1_1) );
                }
                else{
                    imgDiaTron.startAnimation(animation);
                    mediaPlayer.start();
                    ibtnPlay.setImageResource(R.drawable.pause1);
                    // stateListDrawablec.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.pause1) );

                }
                timeSong();
                upDateCurrentTime();
            }
        });
        ibtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnPlay.setImageResource(R.drawable.pause1);
                xuLyNext();
            }
        });

        ibtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnPlay.setImageResource(R.drawable.pause1);
                vitri--;
                if (vitri < 0){
                    vitri = arraySong.size() - 1 ;
                }
                skSong.setProgress(0);
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(getActivity(), arraySong.get(vitri));
                mediaPlayer.start();
                timeSong();
            }
        });

        ibtShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffle){
                    ibtShuffle.setImageResource(R.drawable.shuffle);
                    shuffle = false;
                    ibnRepeat.setEnabled(true);
                }
                else {
                    Toast.makeText(getActivity(), "Phát ngẫu nhiên", Toast.LENGTH_SHORT).show();
                    ibtShuffle.setImageResource(R.drawable.shuffle1);
                    shuffle = true;
                    ibnRepeat.setEnabled(false);
                }

            }
        });

        ibnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(repeat){
                    ibnRepeat.setImageResource(R.drawable.ic_repeat);
                    repeat = false;
                    ibtShuffle.setEnabled(true);
                }
                else {
                    Toast.makeText(getActivity(), "Phát lặp lại", Toast.LENGTH_SHORT).show();
                    ibtShuffle.setEnabled(false);
                    ibnRepeat.setImageResource(R.drawable.repeat1);
                    repeat = true;
                }

            }
        });

//        ibnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.stop();
//                ibtnPlay.setImageResource(R.drawable.img_btn_play);
//                mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(vitri));
//            }
//        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser){
                    return;
                }

                mediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() >= mediaPlayer.getDuration()){
                    if(repeat){
                        playMusic();
                    }
                    else if (shuffle){
                        Random randomBaiHat = new Random();
                        vitri = randomBaiHat.nextInt(arraySong.size());
                        playMusic();
                    }
                    else {
                        xuLyNext();
                    }
                }
                mediaPlayer.seekTo(seekBar.getProgress());// seekTo la nhay toi dau
            }
        });
        return mView;
    }

    private void initView() {
        ibtnPlay    =  mView.findViewById(R.id.imageButtonPlay);
        ibtNext     =  mView.findViewById(R.id.imageButtonNext);
        ibtnPrev    =  mView.findViewById(R.id.imageButtonpPevious);
        ibtShuffle  =  mView.findViewById(R.id.imageButtonShuffle);
        ibnRepeat   =  mView.findViewById(R.id.imageButtonRepeat);

        imgDiaTron   =  mView.findViewById(R.id.imageViewDiaTron);
        txtTimeTotal = mView.findViewById(R.id.txtThoiGianKetThuc);
        txtTimeStart = mView.findViewById(R.id.txtThoiGianBatDau);

        skSong = mView.findViewById(seekBar);
    }

    private void xuLyNext() {
        vitri++;
        if (vitri >= arraySong.size()){
            vitri = 0;
        }
        playMusic();

    }

    private void playMusic() {
        skSong.setProgress(0);
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(getActivity(), arraySong.get(vitri));
        mediaPlayer.start();
        timeSong();
    }

    private void upDateCurrentTime(){
        final Handler handler = new Handler();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtTimeStart.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                if ( mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 500){
                    if(repeat){
//                        ibnRepeat.setImageResource(R.drawable.ic_repeat);
//                        repeat = false;
//                        ibtShuffle.setEnabled(true);
                        playMusic();
                    }else if(shuffle){
                        Random randomBaiHat = new Random();
                        vitri = randomBaiHat.nextInt(arraySong.size());
//                        ibtShuffle.setImageResource(R.drawable.shuffle);
//                        shuffle = false;
//                        ibnRepeat.setEnabled(true);
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



    private void  timeSong(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration() )+ "");
        //gan max cho seekbar bang tong thoi gian cua bai hat
        skSong.setMax(mediaPlayer.getDuration());

    }
    private void khoiTaoMang() {
        arraySong = new ArrayList<Integer>();
        arraySong.add(R.raw.a);
        arraySong.add(R.raw.b);
        arraySong.add(R.raw.c);
        arraySong.add(R.raw.d);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer!=null)
             mediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null)
            mediaPlayer.stop();
    }
}
