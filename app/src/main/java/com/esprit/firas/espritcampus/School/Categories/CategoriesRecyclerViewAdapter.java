package com.esprit.firas.espritcampus.School.Categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.Category;
import com.esprit.firas.espritcampus.R;

import java.util.List;

import static com.esprit.firas.espritcampus.School.SchoolMainPager.SCHOOl_CATS;


public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CustomViewHolder> {
    private List<Category> catList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;
    private String host ;

    CategoriesRecyclerViewAdapter(Context context, List<Category> catList) {
        this.catList = catList;
        this.mContext = context;
    }

    public Category getItem(int position) {
        return catList.get(position);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item2, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        host = mContext.getString(R.string.aphost);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Category catItem = catList.get(i);


            Glide.with(mContext)
                    .load(host+"/"+catItem.getPic_url()).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.user_avatar)
                    .into(customViewHolder.catImg);

        //Setting text view title
        customViewHolder.name.setText(catItem.getName());


    }

    @Override
    public int getItemCount() {
        return (null != catList ? catList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        TextView name;
        CardView cardct;
        CheckBox catBox;


        public CustomViewHolder(View view) {
            super(view);
            this.catImg = view.findViewById(R.id.profimg);
            this.name = view.findViewById(R.id.name);
            this.cardct = view.findViewById(R.id.card_cm);
            this.catBox =  view.findViewById(R.id.cat_box);


            catImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catBox.toggle();

                    if (catBox.isChecked()) {
                        SCHOOl_CATS.add(catList.get(getAdapterPosition()));
                        Toast.makeText(mContext,"elem"+catList.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
                    }
                    else {
                        SCHOOl_CATS.remove(catList.get(getAdapterPosition()));
                        Toast.makeText(mContext,"elem rem"+catList.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
                    }
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catBox.toggle();
                    if (catBox.isChecked()) {
                        SCHOOl_CATS.add(catList.get(getAdapterPosition()));
                        Toast.makeText(mContext,"elem"+catList.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
                    }
                    else {
                        SCHOOl_CATS.remove(catList.get(getAdapterPosition()));
                        Toast.makeText(mContext,"elem rem"+catList.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
                    }
                }
            });

            cardct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catBox.toggle();
                    if (catBox.isChecked()) {
                        SCHOOl_CATS.add(catList.get(getAdapterPosition()));
                        Toast.makeText(mContext,"elem"+catList.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
                    }
                    else {
                        SCHOOl_CATS.remove(catList.get(getAdapterPosition()));
                        Toast.makeText(mContext,"elem rem"+catList.get(getAdapterPosition()).getId(),Toast.LENGTH_LONG).show();
                    }
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