package com.esprit.firas.espritcampus.Tools.Events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.Event;
import com.esprit.firas.espritcampus.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyEventRecyclerViewAdapter extends RecyclerView.Adapter<MyEventRecyclerViewAdapter.CustomViewHolder> {
    private List<Event> feedItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    MyEventRecyclerViewAdapter(Context context, List<Event> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    public Event getItem(int position) {
        return feedItemList.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_two_item_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        host = mContext.getString(R.string.aphost);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        final Event feedItem = feedItemList.get(i);

        Glide.with(mContext)
                .load(host +"/"+ feedItem.getPic_url()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.imageView);


        customViewHolder.imageView.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);


        // customViewHolder.imageView.setColorFilter(R.color.light_green, PorterDuff.Mode.OVERLAY);

        customViewHolder.imageView.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
/*
        Glide.with(mContext)
                .load(host+"/images/prof/"+feedItem.getPhotoprof()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.photoprof);

      /*  customViewHolder.title.setText(feedItem.getPost_name());
        customViewHolder.username.setText(feedItem.getUser_flname());
        customViewHolder.location.setText(feedItem.getLocation());
*/

     /*   try {
            customViewHolder.date.setText(new Services().convertFutureDate(feedItem.getDateBeg()));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(mContext,"error date",Toast.LENGTH_LONG).show();
        }*/


        /*customViewHolder.likes.setText(String.valueOf(feedItem.getLikes())+" likes");
        if (feedItem.getLikes()==0)
        {
            customViewHolder.likes.setVisibility(View.INVISIBLE);
        }
        customViewHolder.comments.setText(String.valueOf(feedItem.getComments())+" comments");
        if (feedItem.getComments()==0)
        {
            customViewHolder.comments.setVisibility(View.INVISIBLE);
        }

        customViewHolder.descri.setText(feedItem.getDescription());
        if (feedItem.getDescription()=="" || feedItem.getDescription()==null)
        {
            customViewHolder.minidesc.setVisibility(View.INVISIBLE);
        }*/


     //   customViewHolder.particip.setText(String.valueOf(feedItem.getNum_participants()));


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected CircleImageView photoprof;
        protected TextView title;
        protected TextView date;
        protected TextView username;


        protected TextView likes;
        protected TextView comments;
        protected TextView location;
        protected TextView particip;
        protected TextView date_left;

        /*protected ImageButton addLike;
        protected ImageButton addComment;*/



        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.thumbnail);
            this.photoprof = view.findViewById(R.id.profimg);
            this.title = view.findViewById(R.id.event_name);
        //    this.date_left = view.findViewById(R.id.date_left);
            this.location = view.findViewById(R.id.location);
            this.date = view.findViewById(R.id.date);
            this.username = view.findViewById(R.id.username);


            imageView.setClipToOutline(true);

       /*     this.addLike = (ImageButton) view.findViewById(R.id.addlike);
            this.addComment = (ImageButton) view.findViewById(R.id.addcomment);

            addLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!feedItemList.get(getAdapterPosition()).isLiked()) {
                        int i = feedItemList.get(getAdapterPosition()).getId();
                        new Services().addLike(String.valueOf(i),"event", mContext);
                        feedItemList.get(getAdapterPosition()).setLiked(true);
                    }
                    else
                    {
                        int i = feedItemList.get(getAdapterPosition()).getId();
                        new Services().Unike(String.valueOf(i),"event", mContext);
                        feedItemList.get(getAdapterPosition()).setLiked(false);
                    }
                }
            });


            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = feedItemList.get(getAdapterPosition()).getId();
                    String comment = "android test: "+ i;
                    new Services().addComment(String.valueOf(i), comment ,mContext);
                }
            });
*/

/*
            title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final Event post =  feedItemList.get(getAdapterPosition());

                    if (Integer.valueOf(post.getIdme()) == post.getUser_id())
                    {
                        options = mine;
                        Toast.makeText(mContext,"mineee",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(mContext,"not mineee"+Integer.valueOf(post.getIdme()) +" "+
                                post.getUser_id() ,Toast.LENGTH_LONG).show();
                        options=notMine;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (which ==0 && Integer.valueOf(post.getIdme()) == post.getUser_id())
                            {

                                new Services().DeleteEvent(String.valueOf(post.getId()),mContext);

                                Toast.makeText(mContext,"it's " +post.getId(),Toast.LENGTH_LONG).show();


                                feedItemList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), feedItemList.size());

                            }
                            else if(which == 1 && Integer.valueOf(post.getIdme()) == post.getUser_id())
                            {
                                Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    builder.show();
                    return true;
                }
            });


/*
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final Event post =  feedItemList.get(getAdapterPosition());

                    if (Integer.valueOf(post.getIdme()) == post.getUser_id())
                    {
                        options = mine;
                        //     Toast.makeText(mContext,"mineee",Toast.LENGTH_LONG).show();
                    }
                    else{
                        // Toast.makeText(mContext,"not mineee"+Integer.valueOf(post.getIdme()) +" "+ post.getUser_id() ,Toast.LENGTH_LONG).show();
                        options=notMine;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (which ==0 && Integer.valueOf(post.getIdme()) == post.getUser_id())
                            {
                                //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                new Services().DeleteEvent(String.valueOf(post.getId()),mContext);


                                feedItemList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), feedItemList.size());

                            }
                            else if(which == 1 && Integer.valueOf(post.getIdme()) == post.getUser_id())
                            {
                                //   Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                          /*      Intent intent = new Intent(mContext, UpdateEvent.class);
                                intent.putExtra("image",post.getThumbnail());
                                intent.putExtra("name",post.getTitle());
                                intent.putExtra("id",post.getId());
                                intent.putExtra("descri",post.getDescription());
                                intent.putExtra("location",post.getLocation());
                                mContext.startActivity(intent);*/
                            }


/*
                        }
                    });
                    builder.show();
                    return true;
                }
            });



            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event post =  feedItemList.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, MainSingleEventActivity.class)
                            .putExtra("postid", post.getId())
                            .putExtra("title", post.getEvent_name())
                            .putExtra("desc", post.getEvent_desc())
                            .putExtra("pic", post.getPic_url())
                            .putExtra("thumbnail", post.getPic_url())
                            .putExtra("photoprof", post.getPhotoprof())

                            .putExtra("location", post.getLocation())
                            .putExtra("particip", post.getNum_participants())
                            .putExtra("datebeg", post.getDateBeg())
                            .putExtra("datend", post.getDateEnd())
                            .putExtra("participated", post.isParticipated());

                    mContext.startActivity(intent);
                }
            });
        }*/
    }


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}