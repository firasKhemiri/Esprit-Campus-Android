package com.esprit.firas.espritcampus.DbHelpers.LocalFeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.esprit.firas.espritcampus.Entities.Conversation;
import com.esprit.firas.espritcampus.Entities.Event;
import com.esprit.firas.espritcampus.Entities.FeedItem;
import com.esprit.firas.espritcampus.Entities.Message;
import com.esprit.firas.espritcampus.Entities.User;

import java.util.ArrayList;

import static com.esprit.firas.espritcampus.Accueil.AccueilActivity.my_id;

public class FeedHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "SocialSpirit";

    private static final String POSTS = "POSTS";
    private static final String POST_ID = "POST_ID";
    private static final String POST_NAME = "POST_NAME";
    private static final String POST_DESC = "POST_DESC";
    private static final String PIC_URL = "PIC_URL";
  //  private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String NUM_LIKES = "NUM_LIKES";
    private static final String NUM_COMMENTS = "NUM_COMMENTS";
    private static final String IS_LIKED = "IS_LIKED";
    private static final String OWNER_ID = "OWNER_ID";
    private static final String OWNER_NAME = "OWNER_NAME";
    private static final String OWNER_PIC = "OWNER_PIC";
    private static final String POST_DATE = "POST_DATE";


    private static final String EVENTS = "EVENTS";
    private static final String DATE_BEG = "DATE_BEG";
    private static final String DATE_END = "DATE_END";
    private static final String LOCATION = "LOCATION";
    private static final String PARTICIPATED = "PARTICIPATED";
    private static final String NUM_PARTICIPATED = "NUM_PARTICIPATED";



    private static final String USER = "USER";
    private static final String USER_ID = "USER_ID";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String CLASSE = "CLASSE";
    private static final String PROFILE_PIC = "PROFILE_PIC";
    private static final String COVER_PIC = "COVER_PIC";



    private static final String CONVERSATIONS = "CONVERSATIONS";
    private static final String CONVERSATION_ID = "CONVERSATION_ID";
    private static final String OTHER_USER_ID = "OTHER_USER_ID";
    private static final String OTHER_USER_NAME = "OTHER_USER_NAME";
    private static final String OTHER_USER_PIC = "OTHER_USER_PIC";
    private static final String LAST_MESSAGE = "LAST_MESSAGE";
    private static final String CONVERSATION_SEEN = "SEEN";
    private static final String RECEIVER = "RECEIVER";
    private static final String CONVO_DATE = "CONVO_DATE";
    private static final String MY_ID = "MY_ID";



    private static final String MESSAGES = "MESSAGES";
    private static final String MESSAGE_ID = "MESSAGE_ID";
    private static final String SENDER_ID = "SENDER_ID";
    private static final String RECEIVER_ID = "RECEIVER_ID";
    private static final String CONTENT = "CONTENT";
    private static final String DATE = "DATE";
    private static final String SEEN = "SEEN";

   // private Context context;


    public FeedHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
     //   this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_POSTS_TABLE = "CREATE TABLE " + POSTS + "("
                + POST_ID + " INTEGER PRIMARY KEY, "+ POST_NAME +" TEXT, "+ POST_DESC +" TEXT, "+ PIC_URL +" TEXT, "
                + CATEGORY_NAME + " TEXT, "+OWNER_ID+" INTEGER, "+OWNER_NAME+" TEXT, "+OWNER_PIC+" TEXT, "+POST_DATE+" TEXT, "
                + IS_LIKED +" INTEGER CHECK ("+IS_LIKED+" IN (0,1)), "+NUM_LIKES+" INTEGER, "+NUM_COMMENTS+" INTEGER  );";

        String CREATE_EVENTS_TABLE = "CREATE TABLE " + EVENTS + "("
                + POST_ID + " INTEGER PRIMARY KEY, "+ POST_NAME +" TEXT, "+ POST_DESC +" TEXT, "+ PIC_URL +" TEXT, "
                + CATEGORY_NAME + " TEXT, "+OWNER_ID+" INTEGER, "+OWNER_NAME+" TEXT, "+OWNER_PIC+" TEXT, "+POST_DATE+" TEXT, "
                + IS_LIKED +" INTEGER CHECK ("+IS_LIKED+" IN (0,1)), "+NUM_LIKES+" INTEGER, " + NUM_COMMENTS+" INTEGER, "

                + NUM_PARTICIPATED+" INTEGER , " + DATE_BEG + " TEXT , "+ DATE_END + " TEXT , " + LOCATION + " TEXT , "
                + PARTICIPATED +" INTEGER CHECK ("+PARTICIPATED+" IN (0,1)) );";

        String CREATE_USER_TABLE = "CREATE TABLE " + USER + "("
                + USER_ID + " INTEGER PRIMARY KEY, "+ FULL_NAME +" TEXT, "+ PROFILE_PIC +" TEXT, "
                + COVER_PIC+" TEXT, "+CLASSE+" TEXT );";


        String CREATE_CONVERSATIONS_TABLE = "CREATE TABLE " + CONVERSATIONS + "("
                + CONVERSATION_ID + " INTEGER PRIMARY KEY, "+ OTHER_USER_ID +" INTEGER, "+ OTHER_USER_NAME +" TEXT, "+ OTHER_USER_PIC +" TEXT, "
                + RECEIVER +" INTEGER, " + MY_ID +" INTEGER, " + LAST_MESSAGE +" TEXT, "
                + CONVO_DATE +" TEXT, "
                + CONVERSATION_SEEN +" INTEGER CHECK ("+CONVERSATION_SEEN+" IN (0,1)));";


        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + MESSAGES + "("
                + MESSAGE_ID + " INTEGER PRIMARY KEY, "+ SENDER_ID +" INTEGER, "+ RECEIVER_ID +" INTEGER, "+ DATE +" TEXT, "
                + CONTENT +" TEXT, " + SEEN +" INTEGER CHECK ("+SEEN+" IN (0,1)), "
                + CONVERSATION_ID + " integer NOT NULL, FOREIGN KEY ("+CONVERSATION_ID+") REFERENCES "+CONVERSATIONS+" ("+CONVERSATION_ID+"));";



        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CONVERSATIONS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + USER);
        db.execSQL("DROP TABLE IF EXISTS " + CONVERSATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES);

        onCreate(db);

    }



    public void AddPOST(FeedItem post){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POST_ID, post.getId());
        values.put(POST_NAME,post.getPost_name());
        values.put(POST_DESC,post.getPost_name());
        values.put(POST_DATE,post.getDate());
        values.put(PIC_URL,post.getPic_url());

        if (post.isLiked())
            values.put(IS_LIKED,1);
        else
            values.put(IS_LIKED,0);

        values.put(NUM_LIKES,post.getNum_likes());
        values.put(NUM_COMMENTS,post.getNum_comments());
        values.put(CATEGORY_NAME,post.getCat_name());
        values.put(OWNER_ID,post.getUser_id());
        values.put(OWNER_NAME,post.getUser_flname());
        values.put(OWNER_PIC,post.getPhotoprof());
        values.put(NUM_COMMENTS,post.getNum_comments());

        db.insert(POSTS, null, values);

    }


    public void AddEvent(Event event){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POST_ID, event.getId());
        values.put(POST_NAME,event.getPost_name());
        values.put(POST_DESC,event.getPost_name());
        values.put(POST_DATE,event.getDate());
        values.put(PIC_URL,event.getPic_url());

        if (event.isLiked())
            values.put(IS_LIKED,1);
        else
            values.put(IS_LIKED,0);

        values.put(NUM_LIKES,event.getNum_likes());
        values.put(NUM_COMMENTS,event.getNum_comments());

        values.put(CATEGORY_NAME,event.getCat_name());
        values.put(OWNER_ID,event.getUser_id());
        values.put(OWNER_NAME,event.getUser_flname());
        values.put(OWNER_PIC,event.getPhotoprof());

        values.put(DATE_BEG,event.getDateBeg());
        values.put(DATE_END,event.getDateEnd());
        values.put(LOCATION,event.getLocation());

        if (event.isParticipated())
            values.put(PARTICIPATED,1);
        else
            values.put(PARTICIPATED,0);

        values.put(NUM_PARTICIPATED,event.getNum_participants());

        db.insert(EVENTS, null, values);

    }


    public void AddUser(User user){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ID, user.getId());
        values.put(FULL_NAME,user.getUser_flname());
        values.put(PROFILE_PIC,user.getProfile_pic());
        values.put(COVER_PIC,user.getCover_pic());
        values.put(CLASSE,user.getClasse());

        db.insert(USER, null, values);

    }


    public void AddConversation(Conversation convo){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CONVERSATION_ID, convo.getConversation_id());
        values.put(OTHER_USER_ID,convo.getCont_id());
        values.put(OTHER_USER_NAME,convo.getCont_name());
        values.put(OTHER_USER_PIC,convo.getCont_image());
        values.put(LAST_MESSAGE,convo.getMessage());
        values.put(CONVO_DATE,convo.getDateStr());
        if (convo.isSeen())
            values.put(CONVERSATION_SEEN,1);
        else
            values.put(CONVERSATION_SEEN,0);
        values.put(RECEIVER,convo.getReciever());
        values.put(MY_ID,convo.getMy_id());

        db.insert(CONVERSATIONS, null, values);

    }

    public void AddMessage(Message msg){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MESSAGE_ID, msg.getId());
        values.put(RECEIVER_ID,msg.getReciever());
        values.put(SENDER_ID,msg.getSender());
        values.put(CONTENT,msg.getMessage());
        values.put(DATE, msg.getCreatedAt());
        values.put(CONVERSATION_ID,msg.getConvo_id());
        if (msg.isSeen())
            values.put(SEEN,1);
        else
            values.put(SEEN,0);

      //  Toast.makeText(context,msg.getConvo_id()+" cnv",Toast.LENGTH_LONG).show();
        db.insert(MESSAGES, null, values);

    }



    public ArrayList<FeedItem> SelectPosts()
    {

        ArrayList<FeedItem> postList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + POSTS +" ORDER BY "+POST_ID+ " DESC ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FeedItem post = new FeedItem() ;
                post.setId(cursor.getInt(0));
                post.setPost_name(cursor.getString(1));
                post.setPost_desc(cursor.getString(2));
                post.setPic_url(cursor.getString(3));
                post.setCat_name(cursor.getString(4));
                post.setUser_id(cursor.getInt(5));
                post.setUser_flname(cursor.getString(6));
                post.setPhotoprof(cursor.getString(7));
                post.setDate(cursor.getString(8));
                if (cursor.getInt(9) == 1)
                    post.setLiked(true);
                else
                    post.setLiked(false);
                post.setNum_likes(cursor.getInt(10));
                post.setNum_comments(cursor.getInt(11));

                if ( (cursor.getString(3) == null )|| (cursor.getString(3).equals("")) )
                    post.setIs_picture(false);
                else
                    post.setIs_picture(true);


                postList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return postList;
    }


    public ArrayList<Event> SelectEvents()
    {

        ArrayList<Event> eventList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EVENTS +" ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event() ;
                event.setId(cursor.getInt(0));
                event.setPost_name(cursor.getString(1));
                event.setPost_desc(cursor.getString(2));
                event.setPic_url(cursor.getString(3));
                event.setCat_name(cursor.getString(4));
                event.setUser_id(cursor.getInt(5));
                event.setUser_flname(cursor.getString(6));
                event.setPhotoprof(cursor.getString(7));
                event.setDate(cursor.getString(8));
                if (cursor.getInt(9) == 1)
                    event.setLiked(true);
                else
                    event.setLiked(false);
                event.setNum_likes(cursor.getInt(10));
                event.setNum_comments(cursor.getInt(11));

                event.setNum_participants(cursor.getInt(12));
                event.setDateBeg(cursor.getString(13));
                event.setDateEnd(cursor.getString(14));
                event.setLocation(cursor.getString(15));

                if (cursor.getInt(16) == 1)
                    event.setParticipated(true);
                else
                    event.setParticipated(false);

                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventList;
    }



    public User SelectUser()
    {

        String selectQuery = "SELECT * FROM " + USER +" ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        User user = new User();

        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            user.setId(cursor.getInt(0));
            user.setUser_flname(cursor.getString(1));
            user.setProfile_pic(cursor.getString(2));
            user.setCover_pic(cursor.getString(3));
            user.setClasse(cursor.getString(4));

            cursor.close();
            return user;
        }
        return user;
    }



    public ArrayList<Conversation> SelectConversations()
    {

        ArrayList<Conversation> convoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CONVERSATIONS +" ORDER BY "+CONVO_DATE+ " DESC ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Conversation conversation = new Conversation() ;
                conversation.setConversation_id(cursor.getInt(0));
                conversation.setCont_id(cursor.getInt(1));
                conversation.setCont_name(cursor.getString(2));
                conversation.setCont_image(cursor.getString(3));
                conversation.setReciever(cursor.getInt(4));
                conversation.setMy_id(cursor.getInt(5));
                conversation.setMessage(cursor.getString(6));
                conversation.setDateStr(cursor.getString(7));

                if (cursor.getInt(7) == 1)
                    conversation.setSeen(true);
                else
                    conversation.setSeen(false);

                convoList.add(conversation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return convoList;
    }


    public ArrayList<Message> SelectMessages(int convo_id)
    {

        ArrayList<Message> messagesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MESSAGES + " WHERE " + CONVERSATION_ID + "= '"+ convo_id+"' ORDER BY "+MESSAGE_ID+ " ASC ;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Message message = new Message() ;
                message.setId(cursor.getInt(0));
                message.setSender(cursor.getInt(1));
                message.setReciever(cursor.getInt(2));
                message.setCreatedAt(cursor.getString(3));
                message.setMessage(cursor.getString(4));

                if (cursor.getInt(5) == 1)
                    message.setSeen(true);
                else
                    message.setSeen(false);

                message.setMy_id(my_id);

          //      Toast.makeText(context,"my id: "+my_id+" sender: "+message.getSender()+" receiver: "+message.getReciever(),Toast.LENGTH_LONG).show();

                messagesList.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return messagesList;
    }


    public void deletePosts(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POSTS,null, null);

    }


    public void deleteEvents(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENTS,null, null);

    }


    public void deleteUser(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER,null, null);

    }


    public void deleteConversations(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONVERSATIONS,null, null);

    }


    public void deleteMessages(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MESSAGES,null, null);

    }

}

