package com.esprit.firas.espritcampus.Tools.Lists;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.esprit.firas.espritcampus.Entities.ListItem;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.School.Department.DepartmentViewPager;

import java.util.List;


class DepartmentsRecyclerAdapter extends RecyclerView.Adapter<DepartmentsRecyclerAdapter.CustomViewHolder> {
    private List<ListItem> depItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    private ListItem dep;

    DepartmentsRecyclerAdapter(List<ListItem> depItemList, Context context) {
        this.depItemList = depItemList;
        this.mContext = context;
    }

    @Override
    public DepartmentsRecyclerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartmentsRecyclerAdapter.CustomViewHolder customViewHolder, int i) {
        dep = depItemList.get(i);
        customViewHolder.name.setText(dep.getName());



    }


    @Override
    public int getItemCount() {
        return (null != depItemList ? depItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;

        protected RelativeLayout layout;

        CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.cat_name);
            this.layout = (RelativeLayout) view.findViewById(R.id.lay_cat);




            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(mContext," "+depItemList.get(getAdapterPosition()).getFrd_id(),Toast.LENGTH_LONG).show();

                    ListItem i = depItemList.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, DepartmentViewPager.class)
                            .putExtra("dep_id", i.getId());
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