package com.esprit.firas.espritcampus.Tools.Events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.User;
import com.esprit.firas.espritcampus.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipatedfollowersRecyclerViewAdapter extends RecyclerView.Adapter<ParticipatedfollowersRecyclerViewAdapter.CustomViewHolder> {
    private List<User> feedItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    ParticipatedfollowersRecyclerViewAdapter(Context context, List<User> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    public User getItem(int position) {
        return feedItemList.get(position);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.like_list_item_layout, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        host = mContext.getString(R.string.aphost);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        final User feedItem = feedItemList.get(i);

        Glide.with(mContext)
                .load(host +"/"+ feedItem.getProfile_pic()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.imageView);



    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.user_pic);
        }
    }


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}