package com.example.firas.internfiretest.Search.School;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Entities.School;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.School.SchoolMainPager;

import java.util.List;


class SchoolsViewAdapter extends RecyclerView.Adapter<SchoolsViewAdapter.CustomViewHolder> {


    private List<School> schoolItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    SchoolsViewAdapter(Context context, List<School> userItemList) {
        this.schoolItemList = userItemList;
        this.mContext = context;
    }

    @Override
    public SchoolsViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_school_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SchoolsViewAdapter.CustomViewHolder customViewHolder, int i) {
        School school = schoolItemList.get(i);


        customViewHolder.students_count.setText(String.valueOf(school.getStudents()));
        customViewHolder.name.setText(school.getName());


        Glide.with(mContext)
                .load(host+"/"+ school.getPhotoprof()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.image);


        if (school.getIbelong())
        {
            customViewHolder.ibelong.setVisibility(View.VISIBLE);
        }
        else
            customViewHolder.ibelong.setVisibility(View.INVISIBLE);



    }

    @Override
    public int getItemCount() {
        return (null != schoolItemList ? schoolItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView students_count;
        protected TextView name;

        protected LinearLayout layout;

        ImageButton ibelong;

        CustomViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.con_image);
            this.students_count = (TextView) view.findViewById(R.id.students_count);
            this.name = (TextView) view.findViewById(R.id.name);
            this.layout = (LinearLayout) view.findViewById(R.id.layoutt);

            this.ibelong = (ImageButton) view.findViewById(R.id.ibelong);



            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(mContext," "+schoolItemList.get(getAdapterPosition()).getFrd_id(),Toast.LENGTH_LONG).show();

                    School i = schoolItemList.get(getAdapterPosition());

                        Intent intent = new Intent(mContext, SchoolMainPager.class)
                                .putExtra("school_id", i.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
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