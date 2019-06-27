package com.esprit.firas.espritcampus.Notifications.Pendings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.Pending;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Services;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


class PendingsRecyclerViewAdapter extends RecyclerView.Adapter<PendingsRecyclerViewAdapter.CustomViewHolder> {
    private List<Pending> demandItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    private Pending demand;

    PendingsRecyclerViewAdapter(Context context, List<Pending> demandItemList) {
        this.demandItemList = demandItemList;
        this.mContext = context;
    }

    @Override
    public PendingsRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pending_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingsRecyclerViewAdapter.CustomViewHolder customViewHolder, int i) {
        demand = demandItemList.get(i);




        Glide.with(mContext)
                .load(host+"/"+ demand.getUser().getProfile_pic()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.image);

        if (demand.is_prof())
        {
            customViewHolder.name.setText(demand.getUser().getUser_flname());
            customViewHolder.classe.setVisibility(View.INVISIBLE);
            customViewHolder.classe_text.setVisibility(View.INVISIBLE);
            customViewHolder.dep.setText(demand.getDep_name());
            customViewHolder.name.setText(demand.getUser().getUser_flname());
            customViewHolder.prof.setVisibility(View.VISIBLE);
        }

        else {

            customViewHolder.name.setText(demand.getUser().getUser_flname());
            customViewHolder.classe.setText(demand.getClasse_name());
            customViewHolder.classe_text.setVisibility(View.VISIBLE);
            customViewHolder.dep.setText(demand.getDep_name());
            customViewHolder.name.setText(demand.getUser().getUser_flname());
            customViewHolder.prof.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return (null != demandItemList ? demandItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected CircleImageView image;
        protected TextView message;
        protected TextView name;
        protected TextView classe;
        protected TextView classe_text;
        protected TextView dep;
        protected TextView prof;
        protected Button accept;




        protected LinearLayout layout;

        CustomViewHolder(View view) {
            super(view);
            this.image = (CircleImageView) view.findViewById(R.id.con_image);
         //   this.message = (TextView) view.findViewById(R.id.lat_message);
            this.name = (TextView) view.findViewById(R.id.con_name);
            this.dep = (TextView) view.findViewById(R.id.dep_name);
            this.classe = (TextView) view.findViewById(R.id.classe_name);
            this.classe_text = (TextView) view.findViewById(R.id.classe_text);
            this.accept = (Button) view.findViewById(R.id.accept);
            this.prof = (TextView) view.findViewById(R.id.prof);
       //     this.layout = (LinearLayout) view.findViewById(R.id.layoutt);


            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Pending demand =  demandItemList.get(getAdapterPosition());

                    Services s = new Services();
                    s.AcceptDemand(demand.getId(),mContext);


                    demandItemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), demandItemList.size());

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