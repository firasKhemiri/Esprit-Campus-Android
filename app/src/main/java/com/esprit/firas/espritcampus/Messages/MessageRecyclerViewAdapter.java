package com.esprit.firas.espritcampus.Messages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.Message;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.CustomViewHolder> {
    private List<Message> msgItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    MessageRecyclerViewAdapter(Context context, List<Message> msgItemList) {
        this.msgItemList = msgItemList;
        this.mContext = context;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
         View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, null);
        return new CustomViewHolder(view);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        Message msg = msgItemList.get(i);

        customViewHolder.setIsRecyclable(false);

        customViewHolder.message.setText(msg.getMessage());

        Services s = new Services();



        if (!Objects.equals(msg.getMy_id(), msg.getReciever())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) customViewHolder.layout.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            customViewHolder.layout.setLayoutParams(params);
            customViewHolder.layout.removeAllViews();
            customViewHolder.layout.addView(customViewHolder.message);

            try {
                if (i < msgItemList.size() && i > 0) {
                    if (s.convertDateLong(msgItemList.get(i - 1).getCreatedAt()) - s.convertDateLong(msg.getCreatedAt()) > 300) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat df = new SimpleDateFormat("dd MMM");

                        Calendar messCal = Calendar.getInstance();
                        Calendar cal = Calendar.getInstance();
                        Date messageTime = dateFormat.parse(msg.getCreatedAt());
                        messCal.setTime(messageTime);

                        if (cal.get(Calendar.DAY_OF_MONTH) == messCal.get(Calendar.DAY_OF_MONTH)) {
                            customViewHolder.messTime.setVisibility(View.VISIBLE);
                            customViewHolder.messTime.setText(timeFormat.format(messCal.getTime()));
                        } else if (cal.get(Calendar.DAY_OF_MONTH) - messCal.get(Calendar.DAY_OF_MONTH) < 3
                                && cal.get(Calendar.MONTH) == messCal.get(Calendar.MONTH))
                        {
                            customViewHolder.messTime.setVisibility(View.VISIBLE);
                            customViewHolder.messTime.setText(s.getDay(messCal.get(Calendar.DAY_OF_WEEK)) + " à " + timeFormat.format(messCal.getTime()));
                        } else {
                            customViewHolder.messTime.setVisibility(View.VISIBLE);
                            customViewHolder.messTime.setText(df.format(messCal.getTime()) + " à " + timeFormat.format(messCal.getTime()));
                        }
                    }
                    else if (s.convertDateLong(msgItemList.get(i - 1).getCreatedAt()) - s.convertDateLong(msg.getCreatedAt()) > 30)
                    {
                        customViewHolder.messSpace.setVisibility(View.VISIBLE);
                    }


                } else if (i == 0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM");

                    Calendar messCal = Calendar.getInstance();
                    Calendar cal = Calendar.getInstance();
                    Date messageTime = dateFormat.parse(msg.getCreatedAt());
                    messCal.setTime(messageTime);

                    if (cal.get(Calendar.DAY_OF_MONTH) == messCal.get(Calendar.DAY_OF_MONTH)) {
                        customViewHolder.messTime.setVisibility(View.VISIBLE);
                        customViewHolder.messTime.setText(timeFormat.format(messCal.getTime()));
                    } else if (cal.get(Calendar.DAY_OF_MONTH) - messCal.get(Calendar.DAY_OF_MONTH) < 3
                            && cal.get(Calendar.MONTH) == messCal.get(Calendar.MONTH))
                    {
                        customViewHolder.messTime.setVisibility(View.VISIBLE);
                        customViewHolder.messTime.setText(s.getDay(messCal.get(Calendar.DAY_OF_WEEK)) + " à " + timeFormat.format(messCal.getTime()));
                    } else {
                        customViewHolder.messTime.setVisibility(View.VISIBLE);
                        customViewHolder.messTime.setText(df.format(messCal.getTime()) + " à " + timeFormat.format(messCal.getTime()));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        else{

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) customViewHolder.layout.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            customViewHolder.layout.setLayoutParams(params);

            customViewHolder.layout.removeAllViews();

            customViewHolder.layout.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.primaryDark));

            customViewHolder.message.setTextColor(mContext.getResources().getColorStateList(R.color.com_facebook_send_button_text_color));

            customViewHolder.layout.addView(customViewHolder.message);


            if (i < msgItemList.size()-1) {
                if (msgItemList.get(i + 1).getSender() != msg.getSender()) {
                    customViewHolder.imgCon.setVisibility(View.VISIBLE);

                    Glide.with(mContext)
                            .load(host + "/" + msg.getConImg()).dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .fitCenter()
                            .error(R.drawable.user_avatar)
                            .placeholder(R.drawable.user_avatar)
                            .into(customViewHolder.imgCon);
                }

                try {
                    if (s.convertDateLong(msg.getCreatedAt()) - s.convertDateLong(msgItemList.get(i + 1).getCreatedAt())  > 300) {
                        customViewHolder.imgCon.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(host + "/" + msg.getConImg()).dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .fitCenter()
                                .error(R.drawable.user_avatar)
                                .placeholder(R.drawable.user_avatar)
                                .into(customViewHolder.imgCon);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            if (msgItemList.size()-1 <= i) {
                customViewHolder.imgCon.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(host + "/" + msg.getConImg()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .error(R.drawable.user_avatar)
                        .placeholder(R.drawable.user_avatar)
                        .into(customViewHolder.imgCon);
            }
            try {
                if (i < msgItemList.size() && i>0) {
                    if (s.convertDateLong(msgItemList.get(i - 1).getCreatedAt()) - s.convertDateLong(msg.getCreatedAt()) > 300) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat df = new SimpleDateFormat("dd MMM");

                        Calendar messCal = Calendar.getInstance();
                        Calendar cal = Calendar.getInstance();
                        Date messageTime = dateFormat.parse(msg.getCreatedAt());
                        messCal.setTime(messageTime);

                        if (cal.get(Calendar.DAY_OF_MONTH) == messCal.get(Calendar.DAY_OF_MONTH)) {
                            customViewHolder.messTime.setVisibility(View.VISIBLE);
                            customViewHolder.messTime.setText(timeFormat.format(messCal.getTime()));
                        } else if (cal.get(Calendar.DAY_OF_MONTH) - messCal.get(Calendar.DAY_OF_MONTH) < 3
                                && cal.get(Calendar.MONTH) == messCal.get(Calendar.MONTH)) {
                            customViewHolder.messTime.setVisibility(View.VISIBLE);
                            customViewHolder.messTime.setText(s.getDay(messCal.get(Calendar.DAY_OF_WEEK)) + " à " + timeFormat.format(messCal.getTime()));
                        } else {
                            customViewHolder.messTime.setVisibility(View.VISIBLE);
                            customViewHolder.messTime.setText(df.format(messCal.getTime()) + " à " + timeFormat.format(messCal.getTime()));
                        }
                    }
                    else if (s.convertDateLong(msgItemList.get(i - 1).getCreatedAt()) - s.convertDateLong(msg.getCreatedAt()) > 30)
                    {
                        customViewHolder.messSpace.setVisibility(View.VISIBLE);
                    }
                }
                    else if (i == 0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM");


                    Calendar messCal = Calendar.getInstance();
                    Calendar cal = Calendar.getInstance();
                    Date messageTime = dateFormat.parse(msg.getCreatedAt());
                    messCal.setTime(messageTime);

                    if (cal.get(Calendar.DAY_OF_MONTH) == messCal.get(Calendar.DAY_OF_MONTH)) {
                        customViewHolder.messTime.setVisibility(View.VISIBLE);
                        customViewHolder.messTime.setText(timeFormat.format(messCal.getTime()));
                    } else if (cal.get(Calendar.DAY_OF_MONTH) - messCal.get(Calendar.DAY_OF_MONTH) < 3
                            && cal.get(Calendar.MONTH) == messCal.get(Calendar.MONTH))
                    {
                        customViewHolder.messTime.setVisibility(View.VISIBLE);
                        customViewHolder.messTime.setText(s.getDay(messCal.get(Calendar.DAY_OF_WEEK)) + " à " + timeFormat.format(messCal.getTime()));
                    } else {
                        customViewHolder.messTime.setVisibility(View.VISIBLE);
                        customViewHolder.messTime.setText(df.format(messCal.getTime()) + " à " + timeFormat.format(messCal.getTime()));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public int getItemCount() {
        return (null != msgItemList ? msgItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView arrow;
        protected TextView message;
        CircleImageView imgCon;
        RelativeLayout messTimeLay;
        TextView messTime;
        TextView messSpace;

        protected LinearLayout layout;

        public CustomViewHolder(View view) {
            super(view);
            this.arrow = view.findViewById(R.id.arrow);
            this.message = view.findViewById(R.id.message);
            this.layout = view.findViewById(R.id.layout);

            this.imgCon = view.findViewById(R.id.con_image);
            this.messTimeLay = view.findViewById(R.id.mess_time);
            this.messTime = view.findViewById(R.id.mess_time_text);
            this.messSpace = view.findViewById(R.id.mess_time_space);
        }

    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}