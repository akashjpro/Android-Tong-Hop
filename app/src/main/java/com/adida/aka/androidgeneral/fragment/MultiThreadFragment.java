package com.adida.aka.androidgeneral.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.activity.NewDetailActivity;
import com.adida.aka.androidgeneral.widget.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultiThreadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<String> arrayTitle;
    private ArrayList<String> arrayLink;
    private ArrayAdapter<String> arrayAdapter;
    private ListView lvTitle;
    private View mView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MultiThreadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_multi_thread, container, false);
        initView();
        getData();
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayTitle);
        lvTitle.setAdapter(arrayAdapter);

        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                intent.putExtra("link",arrayLink.get(position));
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }
        );

        return mView;
    }

    private void getData() {

        if (!isNetworkConnected()){
            Toast.makeText(getActivity(), "Network unavailable, please check and try again", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetXML().execute("http://tuoitre.vn/rss/tt-tin-moi-nhat.rss");

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private void initView() {
        arrayTitle = new ArrayList<String>();
        arrayLink  = new ArrayList<String>();
        lvTitle = (ListView) mView.findViewById(R.id.lvTitile);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.prb_load);
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void onRefresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        arrayTitle.clear();
        arrayAdapter.notifyDataSetChanged();
        getData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private class  GetXML extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            XMLDOMParser parser = new XMLDOMParser();

            Document docs = parser.getDocument(s);

            NodeList nodeList = docs.getElementsByTagName("item");

            String title = "";

            for (int i =0; i < nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element, "title");
                arrayTitle.add(title);
                arrayLink.add(parser.getValue(element, "link"));
                Log.d("title", parser.getValue(element, "title") );
            }
            arrayAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);

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
