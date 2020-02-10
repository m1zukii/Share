package com.iu.share;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoxiaoxing.phoenix.core.model.MediaEntity;

import java.util.List;

public class MediaAdapter1 extends RecyclerView.Adapter<MediaAdapter1.ViewHolder> {
    List<MediaEntity> mediaList;

    public MediaAdapter1(List<MediaEntity> mediaList) {
        this.mediaList = mediaList;
    }

    public List<MediaEntity> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<MediaEntity> TmediaList) {
        mediaList.clear();
        mediaList.addAll(TmediaList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_media, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaAdapter1.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        TextView tvDuration;

        ViewHolder(View view) {
            super(view);
            ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
            tvDuration = (TextView) view.findViewById(R.id.tvDuration);
        }
    }
}
