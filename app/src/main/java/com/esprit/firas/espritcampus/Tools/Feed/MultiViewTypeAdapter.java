package com.esprit.firas.espritcampus.Tools.Feed;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import com.esprit.firas.espritcampus.Entities.FeedItem;
import com.esprit.firas.espritcampus.OtherUser.UserViewPager;
import com.esprit.firas.espritcampus.Profile.ProfileViewPager;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.AddData.UpdatePhotoActual;
import com.esprit.firas.espritcampus.Tools.Comments.CommentsDialog;
import com.esprit.firas.espritcampus.Tools.Services;

import java.text.ParseException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FeedItem> dataSet;
    private Context mContext;

    private String host;

    private static final int TEXT_TYPE=0;
    private static final int IMAGE_TYPE=1;


    public static final String TAG = "Comment";


    private class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType, username, comments, likes,cat_name,date;
        ImageButton addLike,addComment;
        CardView post;
        CircleImageView profimg;


        TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = itemView.findViewById(R.id.postname);
            this.username = itemView.findViewById(R.id.username);
            this.addLike = itemView.findViewById(R.id.add_like);
            this.likes = itemView.findViewById(R.id.likes);
            this.comments = itemView.findViewById(R.id.postcomments);
            this.addComment = itemView.findViewById(R.id.add_comment);
            this.post = itemView.findViewById(R.id.post_card);
            this.cat_name = itemView.findViewById(R.id.cat_name);

            this.profimg = itemView.findViewById(R.id.profimg);


            this.date = itemView.findViewById(R.id.date);


            post.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final FeedItem post =  dataSet.get(getAdapterPosition());

                    if (post.getIdme() == post.getUser_id())
                    {
                        options = mine;
                    }
                    else{
                        options=notMine;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]

                            if (which ==0 && post.getIdme() == post.getUser_id())
                            {
                                //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                new Services().DeletePost(post.getId(),mContext);


                                dataSet.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), dataSet.size());

                            }
                            else if(which == 1 && post.getIdme() == post.getUser_id())
                            {
                                // Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(mContext, UpdatePhotoActual.class);
                                intent.putExtra("is_picture",post.is_picture());
                                intent.putExtra("post_name",post.getPost_name());
                                intent.putExtra("post_id",post.getId());
                                intent.putExtra("cat_id",post.getCategory().getId());
                                intent.putExtra("cat_name",post.getCategory().getName());
                                intent.putExtra("cat_pic",post.getCategory().getPic_url());
                                mContext.startActivity(intent);
                            }


                        }
                    });
                    builder.show();
                    return true;
                }
            });


            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post = dataSet.get(getAdapterPosition());

                 //   Toast.makeText(mContext,post.getUser_id()+" ",Toast.LENGTH_LONG).show();

                    if (post.getUser_id() == post.getIdme()) {
                        Intent intent = new Intent(mContext, ProfileViewPager.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(mContext, UserViewPager.class)
                                .putExtra("userid", post.getUser_id());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }

                }
            });

            addLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem feedItem = dataSet.get(getAdapterPosition());

                    if (!feedItem.isLiked()) {
                        int i = feedItem.getId();
                        new Services().AddLike(String.valueOf(i), mContext);
                        feedItem.setLiked(true);

                        addLike.setImageResource(R.drawable.liked);

                        feedItem.setNum_likes(feedItem.getNum_likes()+1);

                        notifyItemChanged(getAdapterPosition());

                        //  notifyDataSetChanged();
                    } else {
                        int i = feedItem.getId();
                        new Services().Unlike(String.valueOf(i), mContext);
                        feedItem.setLiked(false);
                        addLike.setImageResource(R.drawable.not_liked);

                        feedItem.setNum_likes(feedItem.getNum_likes()-1);

                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();

                    }
                }
            });



            comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post =  dataSet.get(getAdapterPosition());

                    android.support.v4.app.FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
                    CommentsDialog commentsDialogFragment = new CommentsDialog();

                    //     int width = mContext.getResources().getDimensionPixelSize();
                    //    int height = mContext.getResources().getDimensionPixelSize(R.dimen.popup_height);

                    Bundle args = new Bundle();


                    args.putString("url", "/api/post/"+post.getId()+"/comments");
                    args.putInt("postid", post.getId());
                    args.putInt("type", 0);
                    //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    commentsDialogFragment.setArguments(args);

                    commentsDialogFragment.show(fm,null);

                }
            });

            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post =  dataSet.get(getAdapterPosition());

                    android.support.v4.app.FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
                    CommentsDialog commentsDialogFragment = new CommentsDialog();

                    Bundle args = new Bundle();

                    args.putString("url", "/api/post/"+post.getId()+"/comments");
                    args.putInt("postid", post.getId());
                    args.putInt("type", 0);

                    commentsDialogFragment.setArguments(args);

                    commentsDialogFragment.show(fm,null);
                }
            });



        }
    }

    private class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType, username,cat_name,likes,comments,date;
        ImageView image;
        ImageButton addLike,addComment;
        CardView post;

        CircleImageView profimg;

        ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = itemView.findViewById(R.id.postname);
            this.username = itemView.findViewById(R.id.username);
            this.image = itemView.findViewById(R.id.thumbnail);
            this.addLike = itemView.findViewById(R.id.add_like);
            this.likes = itemView.findViewById(R.id.likes);
            this.comments = itemView.findViewById(R.id.postcomments);
            this.addComment = itemView.findViewById(R.id.add_comment);

            this.profimg = itemView.findViewById(R.id.profimg);


            this.cat_name = itemView.findViewById(R.id.cat_name);
            this.date = itemView.findViewById(R.id.date);


            this.post = itemView.findViewById(R.id.post_card);



            //      l=dataSet.get(getAdapterPosition()).getNum_likes();



            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post = dataSet.get(getAdapterPosition());

                 //   Toast.makeText(mContext, post.getUser_id() + " ", Toast.LENGTH_LONG).show();

                    if (post.getUser_id() == post.getIdme()) {
                        Intent intent = new Intent(mContext, ProfileViewPager.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(mContext, UserViewPager.class)
                                .putExtra("userid", post.getUser_id());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }

                }
            });

            addLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem feedItem = dataSet.get(getAdapterPosition());

                    if (!feedItem.isLiked()) {
                        int i = feedItem.getId();
                        new Services().AddLike(String.valueOf(i), mContext);
                        feedItem.setLiked(true);

                        addLike.setImageResource(R.drawable.liked);

                        feedItem.setNum_likes(feedItem.getNum_likes()+1);

                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();
                    } else {
                        int i = feedItem.getId();
                        new Services().Unlike(String.valueOf(i), mContext);
                        feedItem.setLiked(false);
                        addLike.setImageResource(R.drawable.not_liked);

                        feedItem.setNum_likes(feedItem.getNum_likes()-1);

                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();

                    }
                }
            });


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transition(v , dataSet.get(getAdapterPosition()).getPic_url() );
                }
            });



            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final FeedItem post =  dataSet.get(getAdapterPosition());

                    if (post.getIdme() == post.getUser_id())
                    {
                        options = mine;
                    }
                    else{
                        options=notMine;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]

                            if (which ==0 && post.getIdme() == post.getUser_id())
                            {
                                //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                new Services().DeletePost(post.getId(),mContext);


                                dataSet.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), dataSet.size());

                            }
                            else if(which == 1 && post.getIdme() == post.getUser_id())
                            {
                                Intent intent = new Intent(mContext, UpdatePhotoActual.class);
                                intent.putExtra("is_picture",post.is_picture());
                                intent.putExtra("thumbnail",post.getPic_url());
                                intent.putExtra("post_name",post.getPost_name());
                                intent.putExtra("post_id",post.getId());
                                intent.putExtra("cat_id",post.getCategory().getId());
                                intent.putExtra("cat_name",post.getCategory().getName());
                                intent.putExtra("cat_pic",post.getCategory().getPic_url());
                                mContext.startActivity(intent);
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });



            post.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final FeedItem post =  dataSet.get(getAdapterPosition());

                    if (post.getIdme() == post.getUser_id())
                    {
                        options = mine;
                    }
                    else{
                        options=notMine;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]

                            if (which ==0 && post.getIdme() == post.getUser_id())
                            {
                                //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                new Services().DeletePost(post.getId(),mContext);


                                dataSet.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), dataSet.size());

                            }

                            else if(which == 1 && post.getIdme() == post.getUser_id())
                            {
                                Intent intent = new Intent(mContext, UpdatePhotoActual.class);
                                intent.putExtra("is_picture",post.is_picture());
                                intent.putExtra("thumbnail",post.getPic_url());
                                intent.putExtra("post_name",post.getPost_name());
                                intent.putExtra("post_id",post.getId());
                                intent.putExtra("cat_id",post.getCategory().getId());
                                intent.putExtra("cat_name",post.getCategory().getName());
                                intent.putExtra("cat_pic",post.getCategory().getPic_url());
                                mContext.startActivity(intent);
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });




            comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post =  dataSet.get(getAdapterPosition());

                    android.support.v4.app.FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
                    CommentsDialog commentsDialogFragment = new CommentsDialog();

                    //     int width = mContext.getResources().getDimensionPixelSize();
                    //    int height = mContext.getResources().getDimensionPixelSize(R.dimen.popup_height);

                    Bundle args = new Bundle();

                    args.putString("url", "/api/post/"+post.getId()+"/comments");
                    args.putInt("postid", post.getId());
                    args.putInt("type", 0);
                    //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    commentsDialogFragment.setArguments(args);

                    commentsDialogFragment.show(fm,null);

                }
            });

            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post =  dataSet.get(getAdapterPosition());

                    android.support.v4.app.FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
                    CommentsDialog commentsDialogFragment = new CommentsDialog();

                    Bundle args = new Bundle();

                    args.putString("url", "/api/post/"+post.getId()+"/comments");
                    args.putInt("postid", post.getId());
                    args.putInt("type", 0);
                    commentsDialogFragment.setArguments(args);

                    commentsDialogFragment.show(fm,null);
                }
            });
        }
    }


    MultiViewTypeAdapter(ArrayList<FeedItem> data, Context context) {
        this.dataSet = data;
        this.mContext = context;


        host = mContext.getString(R.string.aphost);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_no_pic2, parent, false);
                return new TextTypeViewHolder(view);
            case IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_pic2, parent, false);
                return new ImageTypeViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_no_pic2, parent, false);
                return new TextTypeViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {

        boolean i = dataSet.get(position).is_picture();
        if (!i) {
            return TEXT_TYPE;
        } else {
            return IMAGE_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {

        FeedItem object = dataSet.get(listPosition);
        if (object != null) {
            if (!object.is_picture()) {
                ((TextTypeViewHolder) holder).txtType.setText(object.getPost_name());
                ((TextTypeViewHolder) holder).username.setText(object.getUser_flname());
                ((TextTypeViewHolder) holder).likes.setText(String.valueOf(object.getNum_likes()));
                ((TextTypeViewHolder) holder).comments.setText(String.valueOf(object.getNum_comments()));
                ((TextTypeViewHolder) holder).cat_name.setText(object.getCat_name());

                Services s = new Services();
                try {
                    ((TextTypeViewHolder) holder).date.setText(s.convertDate(object.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                FeedItem item = dataSet.get(listPosition);
                if (item.isLiked())
                    ((TextTypeViewHolder) holder).addLike.setImageResource(R.drawable.liked);
                else
                    ((TextTypeViewHolder) holder).addLike.setImageResource(R.drawable.not_liked);


                Glide.with(mContext)
                        .load(host + "/" + object.getPhotoprof()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .error(R.drawable.user_avatar)
                        .placeholder(R.drawable.user_avatar)
                        .into(((TextTypeViewHolder) holder).profimg);

            }




            else if (object.is_picture()) {

                if (!object.getPost_name().equals("")) {
                    ((ImageTypeViewHolder) holder).txtType.setText(object.getPost_name());
                    ((ImageTypeViewHolder) holder).txtType.setVisibility(View.VISIBLE);
                }
                else
                {
                    ((ImageTypeViewHolder) holder).txtType.setVisibility(View.GONE);
                }
                ((ImageTypeViewHolder) holder).username.setText(object.getUser_flname());
                ((ImageTypeViewHolder) holder).comments.setText(String.valueOf(object.getNum_comments()));
                ((ImageTypeViewHolder) holder).likes.setText(String.valueOf(object.getNum_likes()));
                ((ImageTypeViewHolder) holder).cat_name.setText(object.getCat_name());

                Services s = new Services();
                try {
                    ((ImageTypeViewHolder) holder).date.setText(s.convertDate(object.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                FeedItem item = dataSet.get(listPosition);
                if (item.isLiked())
                    ((ImageTypeViewHolder) holder).addLike.setImageResource(R.drawable.liked);
                else
                    ((ImageTypeViewHolder) holder).addLike.setImageResource(R.drawable.not_liked);




                Glide.with(mContext)
                        .load(host +"/"+ object.getPic_url()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(((ImageTypeViewHolder) holder).image);


                Glide.with(mContext)
                        .load(host + "/" + object.getPhotoprof()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .error(R.drawable.user_avatar)
                        .placeholder(R.drawable.user_avatar)
                        .into(((ImageTypeViewHolder) holder).profimg);
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }



    private void transition(View view,String img) {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(mContext, "21+ only, keep out", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mContext, SingleImage.class);
            intent.putExtra("image", img);
          /*  ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) mContext, view ,"");*/

            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, view, "");
            mContext.startActivity(intent);
        }
    }



}