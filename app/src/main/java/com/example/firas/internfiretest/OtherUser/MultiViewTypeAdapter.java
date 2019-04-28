package com.example.firas.internfiretest.OtherUser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Entities.FeedItem;
import com.example.firas.internfiretest.R;

import java.util.ArrayList;


class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FeedItem> dataSet;
    private Context mContext;

    private String host;

    private static final int TEXT_TYPE=0;
    private static final int IMAGE_TYPE=1;


    private static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        TextView username;

        TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.postname);

        }
    }

    private static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType,username;
        ImageView image;

        ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.postname);
            this.image = (ImageView) itemView.findViewById(R.id.thumbnail);

        }
    }


    MultiViewTypeAdapter(ArrayList<FeedItem> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        int total_types = dataSet.size();

        host = mContext.getString(R.string.aphost);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_no_pic_prof, parent, false);
                return new TextTypeViewHolder(view);
            case IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_pic_prof, parent, false);
                return new ImageTypeViewHolder(view);
        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        boolean i = dataSet.get(position).is_picture();
        if (!i) {
            return TEXT_TYPE;
        } else {
            return IMAGE_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        FeedItem object = dataSet.get(listPosition);
        if (object != null) {
            if (!object.is_picture()) {
                ((TextTypeViewHolder) holder).txtType.setText(object.getPost_name());


            } else if (object.is_picture()) {
                ((ImageTypeViewHolder) holder).txtType.setText(object.getPost_name());

                Glide.with(mContext)
                        .load(host +"/"+ object.getPic_url()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(((ImageTypeViewHolder) holder).image);

            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}