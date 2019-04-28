package com.example.firas.internfiretest.Tools.Comments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.firas.internfiretest.Entities.Comments;
import com.example.firas.internfiretest.OtherUser.UserViewPager;
import com.example.firas.internfiretest.R;
import com.example.firas.internfiretest.Tools.Services;

import java.text.ParseException;
import java.util.List;


/**
 * Created by Nilanchala Panigrahy on 10/25/16.
 */

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.CustomViewHolder> {
    private List<Comments> commentList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;
    EditText ed;
    private String host ;
    Services s;

    public CommentsRecyclerViewAdapter(Context context, List<Comments> commentList) {
        this.commentList = commentList;
        this.mContext = context;
    }

    public Comments getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        host = mContext.getString(R.string.aphost);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Comments commentItem = commentList.get(i);

      /*  Picasso.with(mContext).load(host+"/images/prof/"+commentItem.getPhotoprof())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.catImg); */
        s=new Services();

            Glide.with(mContext)
                    .load(host+"/"+commentItem.getPhotoprof()).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.user_avatar)
                    .into(customViewHolder.profimg);

        //Setting text view title
        customViewHolder.name.setText(commentItem.getName());

        customViewHolder.comment.setText(String.valueOf(commentItem.getComment()));


        try {
            customViewHolder.date.setText(new Services().convertDate(commentItem.getCreatedAt()));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(mContext,"error date",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return (null != commentList ? commentList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView profimg;
        protected TextView name;
        protected TextView comment;
        protected TextView date;
        CardView cardcm;


        public CustomViewHolder(View view) {
            super(view);
            this.profimg = (ImageView) view.findViewById(R.id.profimg);
            this.name = (TextView) view.findViewById(R.id.name);
            this.comment = (TextView) view.findViewById(R.id.comment);
            this.date = (TextView) view.findViewById(R.id.date);
            this.cardcm = (CardView) view.findViewById(R.id.card_cm);


            profimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = commentList.get(getAdapterPosition()).getUser_id();

                    Intent intent = new Intent(mContext, UserViewPager.class)
                            .putExtra("userid", user_id);

                    mContext.startActivity(intent);
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = commentList.get(getAdapterPosition()).getUser_id();

                    Intent intent = new Intent(mContext, UserViewPager.class)
                            .putExtra("userid", user_id);

                    mContext.startActivity(intent);
                }
            });

            cardcm.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final Comments commentItem =  commentList.get(getAdapterPosition());

                    if (commentItem.getMyid() == commentItem.getUser_id())
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
                            if (which ==0 && commentItem.getMyid() == commentItem.getUser_id())
                            {
                                //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                s.DeleteComment(comment.getId(),mContext);


                                commentList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), commentList.size());

                            }
                            else if(which == 1 && commentItem.getMyid() == commentItem.getUser_id())
                            {
                          //      Toast.makeText(mContext,"it'ssss " +which,Toast.LENGTH_LONG).show();

                                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                                builder2.setTitle("Modifier Commentaire");

                                final EditText input = new EditText(mContext);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                input.setLayoutParams(lp);
                                builder2.setView(input);

                                input.setText(commentItem.getComment());

                                builder2.setPositiveButton("Modifier",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                  //              Toast.makeText(mContext,"it's " +String.valueOf(input.getText()),Toast.LENGTH_LONG).show();
                                                String s = String.valueOf(input.getText());
                                                new Services().UpdateComment(comment.getId(),s,mContext);
                                                commentItem.setComment(s);
                                                notifyDataSetChanged();
                                            }
                                        });
                                //   Dialog firstDialog = builder.create();
                                builder2.show();
                            }



                        }
                    });
                    builder.show();
                    return true;
                }
            });



            comment.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence mine[] = new CharSequence[] {"Supprimer", "Modifier"};
                    CharSequence notMine[] = new CharSequence[] {"Signaler"};
                    CharSequence options[];
                    final Comments commentItem =  commentList.get(getAdapterPosition());

                    if (commentItem.getMyid() == commentItem.getUser_id())
                    {
                        options = mine;
                    }
                    else{
                        options=notMine;
                    }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]


                            if (which ==0 && commentItem.getMyid() == commentItem.getUser_id())
                            {
                            //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                s.DeleteComment(commentItem.getId(),mContext);


                                commentList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), commentList.size());

                            }
                            else if(which != 0 && commentItem.getMyid() == commentItem.getUser_id())
                            {
                             //   Toast.makeText(mContext,"it'saaaaa " +which,Toast.LENGTH_LONG).show();

                                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                                builder2.setTitle("Modifier Commentaire");

                                final EditText input = new EditText(mContext);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                input.setLayoutParams(lp);
                                builder2.setView(input);

                                input.setText(commentItem.getComment());

                                builder2.setPositiveButton("Modifier",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                         //       Toast.makeText(mContext,"it's " +String.valueOf(input.getText()),Toast.LENGTH_LONG).show();
                                                String s = String.valueOf(input.getText());
                                                new Services().UpdateComment(commentItem.getId(),s,mContext);
                                                commentItem.setComment(s);
                                                notifyDataSetChanged();
                                            }
                                        });

                             //   Dialog firstDialog = builder.create();
                                builder2.show();
                            }

                     /*       if (which == 1)
                            {
                                View rootView = ((Activity)mContext.getApplicationContext()).getWindow().getDecorView().findViewById(android.R.id.content);
                                ed = (EditText) rootView.findViewById(R.id.type_cmt);
                                ImageView im = (ImageView) rootView.findViewById(R.id.send);
                                ed.setText(post.getComment());

                                im.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String s =  String.valueOf(ed.getText());
                                    }
                                });

                            }*/
                            //           Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();
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