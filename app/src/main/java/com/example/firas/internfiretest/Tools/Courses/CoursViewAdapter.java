package com.example.firas.internfiretest.Tools.Courses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Entities.FeedItem;
import com.example.firas.internfiretest.OtherUser.UserViewPager;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Comments.CommentsDialog;
import com.example.firas.internfiretest.Tools.Services;

import java.text.ParseException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CoursViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FeedItem> dataSet;
    private Context mContext;

    private String host;


    FeedItem feedItem;


    public CoursViewAdapter(ArrayList<FeedItem> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

        host = mContext.getString(R.string.aphost);
    }



    private class viewHolder extends RecyclerView.ViewHolder {

        TextView catName;

        TextView txtType, username,file_path,date;
        ImageView image;
        ImageButton addLike,addComment;
        TextView likes,comments;
        CircleImageView profimg;

        viewHolder(View itemView) {
            super(itemView);


            this.txtType = (TextView) itemView.findViewById(R.id.postname);
       //     this.file_path = (TextView) itemView.findViewById(R.id.file_path);
            this.username = (TextView) itemView.findViewById(R.id.username);
            this.image = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.addLike = (ImageButton) itemView.findViewById(R.id.add_like);
            this.likes = (TextView) itemView.findViewById(R.id.likes);
            this.comments = (TextView) itemView.findViewById(R.id.postcomments);
            this.addComment = (ImageButton) itemView.findViewById(R.id.add_comment);

            this.profimg = (CircleImageView) itemView.findViewById(R.id.profimg);


            this.date = (TextView) itemView.findViewById(R.id.date);
            //      l=dataSet.get(getAdapterPosition()).getNum_likes();


            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedItem = dataSet.get(getAdapterPosition());

                    Toast.makeText(mContext, feedItem.getUser_id() + " ", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(mContext, UserViewPager.class)
                            .putExtra("userid", feedItem.getUser_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                }
            });

            addLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedItem = dataSet.get(getAdapterPosition());

                    if (!feedItem.isLiked()) {
                        int i = feedItem.getId();
                        new Services().AddLikeCours(String.valueOf(i), mContext);
                        feedItem.setLiked(true);

                        addLike.setImageResource(R.drawable.liked);

                        feedItem.setNum_likes(feedItem.getNum_likes() + 1);

                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();
                    } else {
                        int i = feedItem.getId();
                        new Services().UnlikeCours(String.valueOf(i), mContext);
                        feedItem.setLiked(false);
                        addLike.setImageResource(R.drawable.not_liked);

                        feedItem.setNum_likes(feedItem.getNum_likes() - 1);


                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();

                    }
                }
            });


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedItem = dataSet.get(getAdapterPosition());

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(host+"/"+feedItem.getFilePath()));
                    mContext.startActivity(intent);
                }
            });


            comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem feedItem = dataSet.get(getAdapterPosition());


                    android.support.v4.app.FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
                    CommentsDialog commentsDialogFragment = new CommentsDialog();

                    //     int width = mContext.getResources().getDimensionPixelSize();
                    //    int height = mContext.getResources().getDimensionPixelSize(R.dimen.popup_height);

                    Bundle args = new Bundle();
                     args.putInt("postid", feedItem.getId());
                     args.putInt("type", 1);


                    args.putString("url", "/api/cours/"+feedItem.getId()+"/comments");
                    //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    commentsDialogFragment.setArguments(args);

                    commentsDialogFragment.show(fm,null);

                }
            });

            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedItem = dataSet.get(getAdapterPosition());

                    android.support.v4.app.FragmentManager fm = ((FragmentActivity) mContext).getSupportFragmentManager();
                    CommentsDialog commentsDialogFragment = new CommentsDialog();

                    Bundle args = new Bundle();
                    args.putString("url", "/api/cours/"+feedItem.getId()+"/comments");
                    args.putInt("postid", feedItem.getId());
                    args.putInt("type", 1);

                    commentsDialogFragment.setArguments(args);


                    commentsDialogFragment.show(fm, null);
                }
            });
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_cours, parent, false);
        return new viewHolder(view);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        FeedItem feedItem = dataSet.get(listPosition);
        ((viewHolder) holder).txtType.setText(feedItem.getPost_name());
     //   ((viewHolder) holder).file_path.setText(host+"/"+feedItem.getFilePath());
        ((viewHolder) holder).username.setText(feedItem.getUser_flname());
        ((viewHolder) holder).comments.setText(String.valueOf(feedItem.getNum_comments()));
        ((viewHolder) holder).likes.setText(String.valueOf(feedItem.getNum_likes()));

        Services s = new Services();
        try {
            ((viewHolder) holder).date.setText(s.convertDate(feedItem.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (feedItem.isLiked())
            ((viewHolder) holder).addLike.setImageResource(R.drawable.liked);
        else
            ((viewHolder) holder).addLike.setImageResource(R.drawable.not_liked);


        Glide.with(mContext)
                .load(host + "/" + feedItem.getPhotoprof()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(((viewHolder) holder).profimg);



    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}