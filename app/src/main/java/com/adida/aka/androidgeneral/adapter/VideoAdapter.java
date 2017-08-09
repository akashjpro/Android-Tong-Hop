package com.adida.aka.androidgeneral.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adida.aka.androidgeneral.activity.PlayVideoActivity;
import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.model.Video;
import com.adida.aka.androidgeneral.widget.Constans;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tmha on 8/9/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    Activity mContext;
    List<Video> mListVideo;

    public VideoAdapter(Activity mContext, List<Video> mListVideo) {
        this.mContext = mContext;
        this.mListVideo = mListVideo;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = mListVideo.get(position);
        Picasso.with(mContext)
                .load(video.getmImage())
                .placeholder(R.drawable.ic_videocam_black_24dp)
                .fit()
                .into(holder.img);
        holder.title.setText(video.getmTitle());
        holder.channel.setText(video.getmChannelTitle());

        holder.setOnClickLitener(new ListenerItem() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(mContext, PlayVideoActivity.class);
                intent.putExtra(Constans.EXTRA_ID_VIDEO, mListVideo.get(position).getmIdVideo());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListVideo.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView img;
        private TextView  title, channel;
        private ListenerItem mListenerItem;
        public VideoViewHolder(View itemView) {
            super(itemView);
            img     = (ImageView) itemView.findViewById(R.id.imageViewHinh);
            title   = (TextView) itemView.findViewById(R.id.textViewTitle);
            channel = (TextView) itemView.findViewById(R.id.textViewChannelTitle);
            itemView.setOnClickListener(this);
        }

        public void setOnClickLitener(ListenerItem mListenerItem){
            this.mListenerItem = mListenerItem;
        }

        @Override
        public void onClick(View view) {
            mListenerItem.onClick(getAdapterPosition());
        }
    }

    public interface ListenerItem{
        void onClick(int position);
    }
}
