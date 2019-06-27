package com.esprit.firas.espritcampus.Notifications.Notifs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.Notif;
import com.esprit.firas.espritcampus.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


class NotifsRecyclerViewAdapter extends RecyclerView.Adapter<NotifsRecyclerViewAdapter.CustomViewHolder> {
    private List<Notif> demandItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    private Notif demand;

    NotifsRecyclerViewAdapter(Context context, List<Notif> demandItemList) {
        this.demandItemList = demandItemList;
        this.mContext = context;
    }

    @Override
    public NotifsRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notif_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotifsRecyclerViewAdapter.CustomViewHolder customViewHolder, int i) {
        demand = demandItemList.get(i);


        Glide.with(mContext)
                .load(host+"/"+ demand.getUser().getProfile_pic()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.image);

        customViewHolder.name.setText(demand.getUser().getUser_flname());
        customViewHolder.description.setText(demand.getDescription());


    }


    @Override
    public int getItemCount() {
        return (null != demandItemList ? demandItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView image;
        protected TextView user_name;
        protected TextView name;
        protected TextView description;


        protected LinearLayout layout;



        CustomViewHolder(View view) {
            super(view);
            this.image = (CircleImageView) view.findViewById(R.id.con_image);
            this.name = (TextView) view.findViewById(R.id.con_name);
            this.description = (TextView) view.findViewById(R.id.description);
       //     this.layout = (LinearLayout) view.findViewById(R.id.layoutt);

       }
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}