package com.adida.aka.androidgeneral.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import com.adida.aka.androidgeneral.widget.Utilities;
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
public class MultiThreadFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<String> mListTitle;
    private ArrayList<String> mLisrLink;
    private ArrayAdapter<String> mArrayAdapter;
    private ListView mLvTitle;
    private View mView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MultiThreadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_multi_thread, container, false);
        initView();
        loadData(getActivity());
        mArrayAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, mListTitle);
        mLvTitle.setAdapter(mArrayAdapter);

        mLvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                intent.putExtra("link", mLisrLink.get(position));
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        mArrayAdapter.notifyDataSetChanged();
                                    }
                                }
        );

        return mView;
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

       context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetXML().execute("http://tuoitre.vn/rss/tt-tin-moi-nhat.rss");

            }
        });
    }
    private void initView() {
        mListTitle          = new ArrayList<>();
        mLisrLink           = new ArrayList<>();
        mLvTitle            = mView.findViewById(R.id.lvTitile);
        mProgressBar        = mView.findViewById(R.id.prb_load);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void onRefresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        mListTitle.clear();
        mArrayAdapter.notifyDataSetChanged();
        loadData(getActivity());
        mSwipeRefreshLayout.setRefreshing(false);
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
                mListTitle.add(title);
                mLisrLink.add(parser.getValue(element, "link"));
                Log.d("title", parser.getValue(element, "title") );
            }
            mArrayAdapter.notifyDataSetChanged();
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
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));

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
