package com.adida.aka.androidgeneral.fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.adapter.VideoAdapter;
import com.adida.aka.androidgeneral.model.Video;
import com.adida.aka.androidgeneral.widget.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.adida.aka.androidgeneral.widget.Constans.API_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadImageFragment extends android.app.Fragment {

    private static final String PLAYLIST_ID = "PLC7VoaA2DTCLgAH396ER9Oc2POXTAjdAn";
    private static final String LINK = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
            + PLAYLIST_ID +"&key="+ API_KEY +"&maxResults=50";

    private RecyclerView mRecyclerView;
    List<Video>  mLisrVideo;
    VideoAdapter mVideoAdapter;
    ProgressBar  progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_load_image, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_video);
        mLisrVideo = new ArrayList<>();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mVideoAdapter = new VideoAdapter(getActivity(), mLisrVideo);
        progressBar   = view.findViewById(R.id.prb_load);
        mRecyclerView.setAdapter(mVideoAdapter);
        mSwipeRefreshLayout =  view.findViewById(R.id.swipeRefreshLayout);

        loadData(getActivity());
        refresh();

        return view;
    }

    /**
     * Load data from internet
     * @param context
     */
    private void loadData(Activity context) {
        if (!Utilities.checkNetwok(context)){
            Toast.makeText(context, "Network unavailable, please check and try again",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GET_JSON_YOUTUBE().execute(LINK);
            }
        });
    }

    /**
     * Refresh data
     */
    private void refresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLisrVideo.clear();
                mVideoAdapter.notifyDataSetChanged();
                loadData(getActivity());
                //Stop progress indicator when update finish
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    /**
     * Get data from server youtube with ascynctask
     */
    private  class GET_JSON_YOUTUBE extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonItems = object.getJSONArray("items");
                String url          = "";
                String title        = "";
                String videoId      = "";
                String channelTitle = "";
                for (int i=0; i<jsonItems.length(); i++){
                    JSONObject objectItem = jsonItems.getJSONObject(i);
                    JSONObject jsonSnippet = objectItem.getJSONObject("snippet");
                    //Get title
                    title = jsonSnippet.getString("title");
                    //Get url
                    JSONObject objectThumbnails = jsonSnippet.getJSONObject("thumbnails");
                    JSONObject objectDefault = null;
                    int size = objectThumbnails.length();
                    switch (size){
                        case 1:
                            objectDefault = objectThumbnails.getJSONObject("default");
                            break;
                        case 2:
                            objectDefault = objectThumbnails.getJSONObject("medium");
                            break;

                        case 3:
                            objectDefault = objectThumbnails.getJSONObject("high");
                            break;
                        case 4:
                            objectDefault = objectThumbnails.getJSONObject("standard");
                            break;
                        default:
                            objectDefault = objectThumbnails.getJSONObject("maxres");
                            break;

                    }
                    url = objectDefault.getString("url");
                    //Get videoID
                    JSONObject objectResourceId = jsonSnippet.getJSONObject("resourceId");
                    videoId = objectResourceId.getString("videoId");

                    //Get channelTitle
                    channelTitle = jsonSnippet.getString("channelTitle");
                    mLisrVideo.add(new Video(videoId, title, channelTitle, url));
                    mVideoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }


}
