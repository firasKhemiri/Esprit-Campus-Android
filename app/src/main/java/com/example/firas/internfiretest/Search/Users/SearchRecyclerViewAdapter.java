package com.example.firas.internfiretest.Search.Users;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Entities.User;
import com.example.firas.internfiretest.OtherUser.UserViewPager;
import com.example.firas.internfiretest.Profile.ProfileViewPager;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Services;


import java.util.List;


public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.CustomViewHolder> {


    private List<User> userItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    SearchRecyclerViewAdapter(Context context, List<User> userItemList) {
        this.userItemList = userItemList;
        this.mContext = context;
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public SearchRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
         View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerViewAdapter.CustomViewHolder customViewHolder, int i) {
        User user = userItemList.get(i);


        customViewHolder.fac_name.setText(user.getClasse());
        customViewHolder.name.setText(user.getUser_flname());


        Glide.with(mContext)
                .load(host+"/"+ user.getProfile_pic()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.image);


        if (user.isIfollowuser())
        {
            customViewHolder.followUser.setChecked(true);
        }
        else
            customViewHolder.followUser.setChecked(false);
        if(user.getMy_id() == user.getId())
            customViewHolder.followUser.setVisibility(View.INVISIBLE);
        else
            customViewHolder.followUser.setVisibility(View.VISIBLE);

      /*  Picasso.with(mContext).load(host+"/images/prof/"+ user.getImageUser())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.image);*/



    }

    @Override
    public int getItemCount() {
        return (null != userItemList ? userItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView fac_name;
        protected TextView name;

        protected LinearLayout layout;

        private boolean first = true;

        Switch followUser;

        public CustomViewHolder(View view) {
            super(view);
            this.image = view.findViewById(R.id.con_image);
            this.fac_name = view.findViewById(R.id.fac_name);
            this.name = view.findViewById(R.id.name);
            this.layout = view.findViewById(R.id.layoutt);


            this.followUser = view.findViewById(R.id.add_friend);

            followUser.setChecked(true);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(mContext," "+userItemList.get(getAdapterPosition()).getFrd_id(),Toast.LENGTH_LONG).show();

                    User i = userItemList.get(getAdapterPosition());

                    if(i.getMy_id() == i.getId())
                    {
                        Intent intent = new Intent(mContext, ProfileViewPager.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, UserViewPager.class)
                                .putExtra("userid", i.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });


            followUser.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    User user = userItemList.get(getAdapterPosition());

                    //is chkIos checked?
                    if (!((Switch) v).isChecked()) {
                        new Services().UnfollowUser(user.getId(), mContext);
                    }
                    else {
                        new Services().FollowUser(user.getId(), mContext);
                    }

                }
            });

        /*    followUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    User user = userItemList.get(getAdapterPosition());

                    if(!isChecked) {
                        new Services().UnfollowUser(user.getId(), mContext);
                    }
                    else {
                        new Services().FollowUser(user.getId(), mContext);
                        Toast.makeText(mContext, " " + user.getId(), Toast.LENGTH_LONG).show();
                    }
                }
            });
*/


        }


    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}