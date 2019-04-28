package com.example.firas.internfiretest.Categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Entities.Category;
import com.example.firas.internfiretest.R;

import java.util.ArrayList;


public class CategoryDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Category> dataSet;
    private Context mContext;

    private String host;

    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    CategoryDialogAdapter(ArrayList<Category> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

        host = mContext.getString(R.string.aphost);
    }


    private class viewHolder extends RecyclerView.ViewHolder {

        TextView catName;
        ImageView image;
        LinearLayout lay;
        TextView catPostsNum;

        viewHolder(View itemView) {
            super(itemView);


            this.catName = itemView.findViewById(R.id.cat_name);
            this.image = itemView.findViewById(R.id.cat_img);
            this.image = itemView.findViewById(R.id.cat_img);
            this.lay = itemView.findViewById(R.id.layoutt);
            this.catPostsNum = itemView.findViewById(R.id.cat_posts_num);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item3, parent, false);
        return new viewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });

        Category object = dataSet.get(listPosition);
        if (object != null) {
            ((viewHolder) holder).catName.setText(object.getName());

            ((viewHolder) holder).catPostsNum.setText(String.valueOf(object.getNum_posts())+" publication(s)");


            ((viewHolder) holder).image.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(host +"/"+ object.getPic_url()).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .error(R.drawable.london_flat)
                    .placeholder(R.drawable.london_flat)
                    .into(((viewHolder) holder).image);

        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

