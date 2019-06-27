package com.esprit.firas.espritcampus.Profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.FeedItem;
import com.esprit.firas.espritcampus.R;

import java.util.ArrayList;


class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FeedItem> dataSet;
    private Context mContext;

    private String host;

    private static final int TEXT_TYPE=0;
    private static final int IMAGE_TYPE=1;


    private static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType,cat_name;
        TextView username;

        TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = itemView.findViewById(R.id.postname);
            this.username = itemView.findViewById(R.id.username);

            this.cat_name = itemView.findViewById(R.id.cat_name);
        }
    }

    private static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType,username,cat_name;
        ImageView image;

        ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = itemView.findViewById(R.id.postname);
            this.username = itemView.findViewById(R.id.username);
            this.image = itemView.findViewById(R.id.thumbnail);
            this.cat_name = itemView.findViewById(R.id.cat_name);

        }

    }


    MultiViewTypeAdapter(ArrayList<FeedItem> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        int total_types = dataSet.size();

        host = mContext.getString(R.string.aphost);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {

        FeedItem object = dataSet.get(listPosition);
        if (object != null) {
            if (!object.is_picture()) {
                ((TextTypeViewHolder) holder).txtType.setText(object.getPost_name());
      //          ((TextTypeViewHolder) holder).fac_name.setText(object.getUsername());
                ((TextTypeViewHolder) holder).cat_name.setText(String.valueOf(object.getCat_name()));



            } else if (object.is_picture()) {
                ((ImageTypeViewHolder) holder).txtType.setText(object.getPost_name());
          //      ((ImageTypeViewHolder) holder).fac_name.setText(object.getUsername());
                ((ImageTypeViewHolder) holder).cat_name.setText(String.valueOf(object.getCat_name()));

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