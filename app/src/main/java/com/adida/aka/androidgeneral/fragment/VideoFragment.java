package com.adida.aka.androidgeneral.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.adida.aka.androidgeneral.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends android.app.Fragment {

    Button mBtnPlay;
    VideoView videoViewMP4;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mBtnPlay = view.findViewById(R.id.button_play);
        videoViewMP4 =  view.findViewById(R.id.videoViewMp4);
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewMP4.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.huvantruonglao));
                videoViewMP4.start();
                MediaController mediaController = new MediaController(getActivity());
                mediaController.setMediaPlayer(videoViewMP4);
                videoViewMP4.setMediaController(mediaController);
            }
        });
        return view;
    }

}
