package com.esprit.firas.espritcampus.Tools.Events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Categories.CategoryPostsViewPager;
import com.esprit.firas.espritcampus.Entities.Event;
import com.esprit.firas.espritcampus.Entities.User;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.AddData.UpdateEventActual;
import com.esprit.firas.espritcampus.Tools.Services;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyEventRecyclerInRecyclerViewAdapter extends RecyclerView.Adapter<MyEventRecyclerInRecyclerViewAdapter.CustomViewHolder> {
    private List<Event> feedItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_SINGLE = 1;

    private boolean particicpated;

    MyEventRecyclerInRecyclerViewAdapter(Context context, List<Event> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }
    public Event getItem(int position) {
        return feedItemList.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        host = mContext.getString(R.string.aphost);


        View view;
        if (viewType==TYPE_DEFAULT) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_two_item_row, viewGroup, false);
            return new CustomViewHolder(view);

        } else if (viewType==TYPE_SINGLE){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item_row, viewGroup, false);
            return new CustomViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public int getItemViewType(int position) {
        return (feedItemList.get(position).getType()==TYPE_DEFAULT) ? TYPE_DEFAULT : TYPE_SINGLE;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        final Event feedItem = feedItemList.get(i);
        Services s = new Services();


        Glide.with(mContext)
                .load(host + "/" + feedItem.getPic_url()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.imageView);


        customViewHolder.title.setText(feedItem.getPost_name());
        customViewHolder.location.setText(feedItem.getLocation());

        customViewHolder.cat_name.setText(feedItem.getCategory().getName());

        Glide.with(mContext)
                .load(host + "/" + feedItem.getCategory().getPic_url()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.cat_img);


        customViewHolder.cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CategoryPostsViewPager.class)
                        .putExtra("cat_id", feedItem.getCategory().getId())
                        .putExtra("cat_name", feedItem.getCategory().getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        customViewHolder.cat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CategoryPostsViewPager.class)
                        .putExtra("cat_id", feedItem.getCategory().getId())
                        .putExtra("cat_name", feedItem.getCategory().getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


        customViewHolder.date.setText(s.dateEvn(feedItem.getDateBeg(),feedItem.getDateEnd()));


        if (feedItem.getParticpants().size()>0) {

            ArrayList<User> users = new ArrayList<>();
            int numberPar = feedItem.getParticpants().size();

            if (feedItem.getParticpants().size()>3)
            {
                for (int u = 0; u < 3; u++) {
                    users.add(feedItem.getParticpants().get(u));
                    numberPar--;
                }
                customViewHolder.num_fol.setVisibility(View.VISIBLE);
                customViewHolder.par_lay.setVisibility(View.VISIBLE);
                customViewHolder.num_fol.setText("+"+numberPar);
            }
            else {
                users = feedItem.getParticpants();
                numberPar = 0;
                customViewHolder.num_fol.setVisibility(View.GONE);
            }

            customViewHolder.adapter = new ParticipatedfollowersRecyclerViewAdapter(mContext, users);
            customViewHolder.particpantsRecycler.setAdapter(customViewHolder.adapter);
            customViewHolder.particpantsRecycler.setVisibility(View.VISIBLE);
        }
        else {
            customViewHolder.particpantsRecycler.setVisibility(View.GONE);
            customViewHolder.num_fol.setVisibility(View.GONE);
            customViewHolder.par_lay.setVisibility(View.GONE);
            customViewHolder.particpantsRecycler.setVisibility(View.GONE);
        }


        if (feedItem.isParticipated())
        {
            customViewHolder.participate.setChecked(true);
            particicpated = true;
        }
        else
        {
            if (feedItem.isLimited())
            {
                if (feedItem.isReachedLimit())
                {
                    customViewHolder.participate.setEnabled(false);
                }
            }
            customViewHolder.participate.setChecked(false);
            particicpated = false;
        }


        customViewHolder.participate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Services().Participate(String.valueOf(feedItem.getId()), mContext);
                    feedItem.setParticipated(true);
                }
                else
                {
                    new Services().Unparticipate(String.valueOf(feedItem.getId()), mContext);
                    feedItem.setParticipated(false);
                }
            }
        });

        if (feedItem.isLimited()) {
            if (feedItem.isReachedLimit()) {
                customViewHolder.participate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,"Limite d'utilisateurs atteint",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;
        private TextView date;

        private TextView location;
        private TextView cat_name;
        private TextView num_fol;
        private CircleImageView cat_img;
        private RelativeLayout layout;
        private RelativeLayout par_lay;
        private RecyclerView particpantsRecycler;
        private LinearLayoutManager mLayoutManager;

        private Switch participate;

        private ParticipatedfollowersRecyclerViewAdapter adapter;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.thumbnail);
            this.title = view.findViewById(R.id.event_name);
            this.location = view.findViewById(R.id.location);
            this.date = view.findViewById(R.id.date);

            this.cat_name = view.findViewById(R.id.cat_name);
            this.cat_img = view.findViewById(R.id.cat_pic);
            this.layout = view.findViewById(R.id.event_lay);
            this.particpantsRecycler = view.findViewById(R.id.f_particip);
            this.num_fol = view.findViewById(R.id.num_fol);
            this.par_lay = view.findViewById(R.id.particip_lay);
            this.participate = view.findViewById(R.id.participate);

            imageView.setClipToOutline(true);

            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            particpantsRecycler.setLayoutManager(mLayoutManager);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event post =  feedItemList.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, MainSingleEventActivity.class)
                            .putExtra("id", post.getId())
                            .putExtra("title", post.getPost_name())
                            .putExtra("desc", post.getPost_desc())
                            .putExtra("pic", post.getPic_url())
                            .putExtra("thumbnail", post.getPic_url())
                            .putExtra("user_name",post.getUser_flname())
                            .putExtra("photoprof", post.getPhotoprof())

                            .putExtra("location", post.getLocation())
                            .putExtra("particip", post.getNum_participants())
                            .putExtra("datebeg", post.getDateBeg())
                            .putExtra("datend", post.getDateEnd())
                            .putExtra("participated", post.isParticipated())
                            .putExtra("creator_id",post.getUser_id())
                            .putExtra("my_id",post.getIdme());

                    mContext.startActivity(intent);
                }
            });


            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final Event event =  feedItemList.get(getAdapterPosition());

                    if (event.getIdme() == event.getUser_id())
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

                            if (which ==0 && event.getIdme() == event.getUser_id())
                            {
                                new Services().DeleteEvent(String.valueOf(event.getId()),mContext);

                                feedItemList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), feedItemList.size());

                            }
                            else if(which == 1 && event.getIdme() == event.getUser_id()) {

                                Intent intent = new Intent(mContext, UpdateEventActual.class);
                                intent.putExtra("id", event.getId())
                                        .putExtra("title", event.getPost_name())
                                        .putExtra("desc", event.getPost_desc())
                                        .putExtra("thumbnail", event.getPic_url())
                                        .putExtra("location", event.getLocation())
                                        .putExtra("datebeg", event.getDateBeg())
                                        .putExtra("datend", event.getDateEnd())
                                        .putExtra("cat_id",event.getCategory().getId())
                                        .putExtra("cat_name",event.getCategory().getName())
                                        .putExtra("cat_pic",event.getCategory().getPic_url())
                                        .putExtra("is_limited",event.isLimited());

                                if (event.isLimited())
                                {
                                    intent.putExtra("max_limit",event.getMaxLimit());
                                }

                                mContext.startActivity(intent);
                            }
                        }
                    });
                    builder.show();
                    return true;
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