package com.example.firas.internfiretest.Tools.Events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Entities.User;
import com.example.firas.internfiretest.R;

import java.util.List;


class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.CustomViewHolder> {
    private List<User> userItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    private User user;

    UsersRecyclerAdapter(List<User> userItemList, Context context) {
        this.userItemList = userItemList;
        this.mContext = context;
    }

    @Override
    public UsersRecyclerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersRecyclerAdapter.CustomViewHolder customViewHolder, int i) {
        user = userItemList.get(i);


        customViewHolder.name.setText(user.getUser_flname());
        customViewHolder.classe.setText(user.getClasse());


        Glide.with(mContext)
                .load(host+"/images/prof/"+user.getProfile_pic()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.image);

    }


    @Override
    public int getItemCount() {
        return (null != userItemList ? userItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView name;
        protected TextView classe;

        protected LinearLayout layout;

        CustomViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.con_image);
            this.name = (TextView) view.findViewById(R.id.stu_name);
            this.classe = (TextView) view.findViewById(R.id.stu_classe);
            this.layout = (LinearLayout) view.findViewById(R.id.layoutt);




            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(mContext," "+userItemList.get(getAdapterPosition()).getFrd_id(),Toast.LENGTH_LONG).show();
/*
                    User i = userItemList.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, MainMessagesActivity.class)
                            .putExtra("cont_id", i.getCont_id())
                            .putExtra("my_id", i.getMy_id())
                            .putExtra("cont_name", i.getCont_image())
                            .putExtra("cont_img", i.getCont_name());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);*/
                }
            });

        }
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}