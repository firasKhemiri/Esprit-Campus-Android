package com.esprit.firas.espritcampus.Messages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esprit.firas.espritcampus.Entities.Conversation;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.Tools.Services;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.ParseException;
import java.util.List;


class ConversationsRecyclerViewAdapter extends RecyclerView.Adapter<ConversationsRecyclerViewAdapter.CustomViewHolder> {
    private List<Conversation> convoItemList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    private String host;

    ConversationsRecyclerViewAdapter(Context context, List<Conversation> convoItemList) {
        this.convoItemList = convoItemList;
        this.mContext = context;
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public ConversationsRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        host = mContext.getString(R.string.aphost);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.conversation_item, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsRecyclerViewAdapter.CustomViewHolder customViewHolder, int i) {
        Conversation convo = convoItemList.get(i);


        customViewHolder.message.setText(StringEscapeUtils.unescapeJava(convo.getMessage()));
        customViewHolder.name.setText(convo.getCont_name());


        Services s = new Services();
        try {
            customViewHolder.date_msg.setText(s.convertDate(convo.getDateStr()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!convo.isSeen() && (convo.getMy_id() == convo.getReciever()))
        {
            customViewHolder.message.setTypeface(null, Typeface.BOLD);
            customViewHolder.name.setTypeface(null, Typeface.BOLD);
            customViewHolder.date_msg.setTypeface(null, Typeface.BOLD);

            customViewHolder.message.setTextColor(Color.parseColor("#3a3a3a"));
            customViewHolder.name.setTextColor(Color.parseColor("#3a3a3a"));
            customViewHolder.date_msg.setTextColor(Color.parseColor("#3a3a3a"));

        }


        Glide.with(mContext)
                .load(host+"/"+ convo.getCont_image()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.drawable.user_avatar)
                .placeholder(R.drawable.user_avatar)
                .into(customViewHolder.image);

    }


    @Override
    public int getItemCount() {
        return (null != convoItemList ? convoItemList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView message;
        TextView name;
        TextView date_msg;

        LinearLayout layout;

        CustomViewHolder(View view) {
            super(view);
            this.image = view.findViewById(R.id.con_image);
            this.message = view.findViewById(R.id.lat_message);
            this.name = view.findViewById(R.id.con_name);
            this.date_msg = view.findViewById(R.id.date_msg);
            this.layout = view.findViewById(R.id.layoutt);




            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(mContext," "+convoItemList.get(getAdapterPosition()).getFrd_id(),Toast.LENGTH_LONG).show();

                    Conversation i = convoItemList.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, MainMessagesActivity.class)
                            .putExtra("convo_id",i.getConversation_id())
                            .putExtra("cont_id", i.getCont_id())
                            .putExtra("my_id", i.getMy_id())
                            .putExtra("cont_img", i.getCont_image())
                            .putExtra("cont_name", i.getCont_name())
                            .putExtra("msg_id", i.getMsg_id())
                            .putExtra("seen", i.isSeen())
                            .putExtra("receiver", i.getReciever());
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