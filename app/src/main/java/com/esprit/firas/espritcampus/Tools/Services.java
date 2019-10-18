package com.esprit.firas.espritcampus.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.firas.espritcampus.Accueil.AccueilActivity;
import com.esprit.firas.espritcampus.R;
import com.esprit.firas.espritcampus.School.SchoolMainPager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Services {

    private String host;

    public void AddLike(final String postid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/like/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();
                        //    Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, postid + " error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("post_id", postid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void AddLikeCours(final String postid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/like_cours/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        //   Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, postid + " error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", postid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void Addpending(final int school_id, final int dep_id, final int class_id, final String is_prof, final String message,
                           final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/pending/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(context, SchoolMainPager.class);
                        i.putExtra("school_id", school_id);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                        Toast.makeText(context, school_id + " " + dep_id + " " + class_id + " " + message + " " + is_prof + " r", Toast.LENGTH_LONG).show();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("school_id", String.valueOf(school_id));
                params.put("dep_id", String.valueOf(dep_id));
                params.put("classe_id", String.valueOf(class_id));
                params.put("message", message);
                params.put("is_prof", is_prof);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void Deletepending(final int school_id, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/school/" + school_id + "/update_pending/";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, " error " + school_id, Toast.LENGTH_LONG).show();

                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void AcceptDemand(final int pending_id, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/accept_demand/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pending_id", String.valueOf(pending_id));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void CreateNotif(final int dep_id, final int class_id, final String message,
                            final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/create_notif/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                        Toast.makeText(context, " " + dep_id + " " + class_id + " " + message + " " + " r", Toast.LENGTH_LONG).show();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("dep_id", String.valueOf(dep_id));
                params.put("classe_id", String.valueOf(class_id));
                params.put("description", message);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void Unlike(final String postid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/unlike/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        //  Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, postid + " error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("post_id", postid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void UnlikeCours(final String postid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/unlike_cours/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        //      Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, postid + " error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", postid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void AddComment(final String postid, final String comment, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/create_comment/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        // Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("post_id", postid);
                params.put("comment", comment);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void AddCommentCours(final String postid, final String comment, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/create_comment_cours/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        // Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", postid);
                params.put("comment", comment);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void FollowUser(final int userid, final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/follow/";
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //     Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userid));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void UnfollowUser(final int userid, final Context context) {

        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/unfollow/";
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //   Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userid));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void Participate(final String eventid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/participate/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        //  Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, eventid + " error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", eventid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void Unparticipate(final String eventid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/unparticipate/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        //   Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, eventid + " error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("event_id", eventid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void AddEvent(final String imgname, final String name, final String desc, final String datebeg, final String datend,
                         final String location, final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url;

        url = host + "/api/addevent";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        //    Toast.makeText(context,response,Toast.LENGTH_LONG).show();

                        Intent i = new Intent(context, AccueilActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                params.put("classe", name);
                params.put("imgname", imgname);
                params.put("desc", desc);
                params.put("datebeg", datebeg);
                params.put("datend", datend);
                params.put("location", location);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    public void UpdateEvent(final String postid, final String name, final String desc, final String datebeg, final String datend,
                            final String location, final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url;

        url = host + "/api/updatevent";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        //   Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, AccueilActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                params.put("post_id", postid);
                params.put("classe", name);
                params.put("desc", desc);
                params.put("datebeg", datebeg);
                params.put("datend", datend);
                params.put("location", location);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

public void UpdatePost(final int postid, final String name, final int cat_id, final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url;

        url = host + "/api/post/"+postid+"/update/";
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

//                           Toast.makeText(context, name +" "+cat_id,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, AccueilActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, name +" "+cat_id,Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cat_id", String.valueOf(cat_id));
                params.put("name", name);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);


                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    public String convertDate(String strDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);
        Calendar c = Calendar.getInstance();

        Date d = dateFormat.parse(strDate);

        Date s = c.getTime();

        long diffInMs = ((s.getTime() - d.getTime()) / 1000) - 3600;


        Long days = diffInMs / (60 * 60 * 24);
        //diffInMs -= days * (60 * 60 * 24);
        // Long hours = diffInMs / (60 * 60);

        Long hours = ((60 - (-diffInMs / 60)) / 60);
        // diffInMs -= hours * (60 * 60);
        Long minutes = (60 - (-diffInMs / 60))-1;
        // diffInMs -= minutes *60;
        //Long seconds = -diffInMs / 60 /60;


        //  String temp = " j: "+days+ " / H: "+hours+ " / M: "+minutes+" / S: "+diffInMs;


        if (days >= 1)
            // diff = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
            return days + "j";
        else if (hours >= 1)
            //  diff = TimeUnit.MILLISECONDS.toHours(diffInMs);
            return hours + "h";
        else if (minutes >= 1)
            //  diff = TimeUnit.MILLISECONDS.toDays(diffInMs);
            return minutes + "m";
        else
            // diff = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
            return "quelques instants";

//        return " j: "+days+ " / H: "+hours+ " / M: "+minutes+" / S: "+diffInMs;
    }


    public Long getDateDiffInMins(String strDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        Calendar c = Calendar.getInstance();

        Date d = dateFormat.parse(strDate);

        Date s = c.getTime();


        long diffInMs = ((d.getTime()- s.getTime()) / 1000);

        return (diffInMs);
    }


    public Long compareDates(String strDateEnd,String strDateBeg) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        Calendar c = Calendar.getInstance();

        Date b = dateFormat.parse(strDateBeg);
        Date e = dateFormat.parse(strDateEnd);

        long diffInMs = ((e.getTime()- b.getTime()) / 1000);

        return (diffInMs);
    }


    public Long convertDateLong(String strDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);
        Calendar c = Calendar.getInstance();

        Date d = dateFormat.parse(strDate);

        Date s = c.getTime();

        return ((s.getTime() - d.getTime()) / 1000) - 7200;
    }


    public String convertFutureDate(String strDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        Calendar c = Calendar.getInstance();

        Date s = dateFormat.parse(strDate);

        Date d = c.getTime();

        long diffInMs = (s.getTime() - d.getTime()) / 1000;


        Long days = diffInMs / (60 * 60 * 24);
        //diffInMs -= days * (60 * 60 * 24);
        // Long hours = diffInMs / (60 * 60);
        Long hours = (60 - (-diffInMs / 60)) / 60;
        // diffInMs -= hours * (60 * 60);
        Long minutes = 60 - (-diffInMs / 60);
        // diffInMs -= minutes *60;
        Long seconds = -diffInMs / 60 / 60;
        //  String temp = " j: "+days+ " / H: "+hours+ " / M: "+minutes+" / S: "+diffInMs;


        if (days >= 1)
            // diff = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
            return "il reste " + days + "jours";
        else if (hours >= 1 || minutes > 60)
            //  diff = TimeUnit.MILLISECONDS.toHours(diffInMs);
            return "il reste " + hours + "heures";
        else if (minutes >= 1 && minutes <= 60)
            //  diff = TimeUnit.MILLISECONDS.toDays(diffInMs);
            return minutes + "minutes";
        else if (seconds < 0)
            // diff = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
            return "il reste " + "quelques instants";
        else
            return "expiré";

//        return " j: "+days+ " / H: "+hours+ " / M: "+minutes+" / S: "+diffInMs;
    }


    public void DeleteComment(final int commentid, final Context context) {


        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/comment/" + commentid + "/update/";
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //    Toast.makeText(context," "+ response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    public void DeletePost(final int postid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/post/" + postid + "/update/";
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        // Toast.makeText(context," "+ response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    public void DeleteEvent(final String postid, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/event/" + postid + "/update/";
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //       Toast.makeText(context," "+ response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                params.put("post_id", postid);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    public void UpdateComment(final int commentid, final String comment, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/comment/" + commentid + "/update/";
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //  Toast.makeText(context," "+ response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("comment", comment);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    public void UpdateUser(final Map<String, String> params, final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/profile_update/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        Toast.makeText(context, "Succès", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(context,AccueilActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "Erreur", Toast.LENGTH_LONG).show();

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void DeletUser(final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/profile_update/";
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(context, " " + response, Toast.LENGTH_LONG).show();

                   /*     Intent i = new Intent(context,LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);*/
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    public void UpdatePhoto(final String postid, final String name, final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);


        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/post/" + postid + "/update/";
        StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();

                        Toast.makeText(context, postid + " error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public void ChangePass(final String oldpass, final String newpass, final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url;

        url = host + "/api/changepass";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, AccueilActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                params.put("oldpass", oldpass);
                params.put("newpass", newpass);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    public void DeleteUser(final String oldpass, final Context context) {

        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url;

        url = host + "/api/deleteuser";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

              /*          Intent i = new Intent(context, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                params.put("oldpass", oldpass);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }






    public void AddEventActual(final Bitmap bmp, final Handler handler, final String name, final String description, final String location
            , final String date_beg, final String date_end, final int cat_id, Context context, final boolean isLimited, final int maxLimit,
                               final Double lat, final Double lng) {

        host = context.getString(R.string.aphost);

        final String TAG = "Upload Image Apache";

        final String url = host + "/api/create_event_pic/";

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);


                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    reqEntity.addPart("name", new StringBody(name, ContentType.TEXT_PLAIN));
                    reqEntity.addPart("cat_id", new StringBody(String.valueOf(cat_id), ContentType.TEXT_PLAIN));
                    reqEntity.addPart("desc", new StringBody(description, ContentType.TEXT_PLAIN));
                    reqEntity.addPart("date_beg", new StringBody(date_beg, ContentType.TEXT_PLAIN));
                    reqEntity.addPart("date_end", new StringBody(date_end, ContentType.TEXT_PLAIN));
                    reqEntity.addPart("location", new StringBody(location, ContentType.TEXT_PLAIN));
                    reqEntity.addPart("lat", new StringBody(String.valueOf(lat), ContentType.TEXT_PLAIN));
                    reqEntity.addPart("lng", new StringBody(String.valueOf(lng), ContentType.TEXT_PLAIN));

                    if (isLimited)
                    {
                        reqEntity.addPart("is_limited", new StringBody("True", ContentType.TEXT_PLAIN));
                        reqEntity.addPart("max_limit", new StringBody(String.valueOf(maxLimit), ContentType.TEXT_PLAIN));
                    }
                    else
                    {
                        reqEntity.addPart("is_limited", new StringBody("False", ContentType.TEXT_PLAIN));
                    }


                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] data = bos.toByteArray();
                        ByteArrayBody bab = new ByteArrayBody(data, name + ".jpg");
                        reqEntity.addPart("file", bab);
                    } catch (Exception e) {
                        //Log.v("Exception in Image", ""+e);
                        reqEntity.addPart("file", new StringBody("", ContentType.TEXT_PLAIN));
                    }

                    httppost.setEntity(reqEntity);
                    httppost.setHeader("Authorization", "Bearer " + token);

                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doFileUpload Response : " + responseStr);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    handler.sendEmptyMessage(0);
                }
            }
        });
        t.start();

    }


    public void AddPostNoPic(final Handler handler, final String name, final int cat_id, Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/create_post/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        handler.sendEmptyMessage(1);
                        //Toast.makeText(MainActivity.this," "+ response, Toast.LENGTH_LONG).show();

                        // Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        handler.sendEmptyMessage(0);
                    }
                }
        ) {



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("cat_id", String.valueOf(cat_id));
                params.put("is_picture", "False");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }





    public void AddPostActual(final Bitmap bmp, final Handler handler, final String name, final int cat_id, Context context) {

        host = context.getString(R.string.aphost);

        final String TAG = "Upload Image Apache";
        //final String url = "http://192.168.8.101:8000/adddpost";
        final String url = host + "/api/create_post_pic/";
        //final String url = "http://192.168.8.101/Upload_image_ANDROID/upload_image.php"

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");
            /*    final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("file", convertBitmapToString(bmp)));
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("cat_id", "2"));
*/
                //       nameValuePairs.add(new BasicNameValuePair("Authorization", "Bearer "+token));

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);


                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    reqEntity.addPart("name", new StringBody(name, ContentType.TEXT_PLAIN));
                    reqEntity.addPart("cat_id", new StringBody(String.valueOf(cat_id), ContentType.TEXT_PLAIN));
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] data = bos.toByteArray();
                        ByteArrayBody bab = new ByteArrayBody(data, name + ".jpg");
                        reqEntity.addPart("file", bab);
                    } catch (Exception e) {
                        //Log.v("Exception in Image", ""+e);
                        reqEntity.addPart("file", new StringBody("", ContentType.TEXT_PLAIN));
                    }

                    httppost.setEntity(reqEntity);
                    httppost.setHeader("Authorization", "Bearer " + token);

                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doFileUpload Response : " + responseStr);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    handler.sendEmptyMessage(0);
                }
            }
        });
        t.start();

    }


    public void UpdatePostActual(int post_id,final Bitmap bmp, final Handler handler, final String name, final int cat_id, Context context) {

        host = context.getString(R.string.aphost);

        final String TAG = "Upload Image Apache";
        //final String url = "http://192.168.8.101:8000/adddpost";
        final String url = host + "/api/post/"+post_id+"/update_pic/";
        //final String url = "http://192.168.8.101/Upload_image_ANDROID/upload_image.php"

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPut httppost = new HttpPut(url);


                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    reqEntity.addPart("name", new StringBody(name, ContentType.TEXT_PLAIN));
                    reqEntity.addPart("cat_id", new StringBody(String.valueOf(cat_id), ContentType.TEXT_PLAIN));
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] data = bos.toByteArray();
                        ByteArrayBody bab = new ByteArrayBody(data, name + ".jpg");
                        reqEntity.addPart("file", bab);
                    } catch (Exception e) {
                        //Log.v("Exception in Image", ""+e);
                        reqEntity.addPart("file", new StringBody("", ContentType.TEXT_PLAIN));
                    }

                    httppost.setEntity(reqEntity);
                    httppost.setHeader("Authorization", "Bearer " + token);

                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doFileUpload Response : " + responseStr);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    handler.sendEmptyMessage(0);
                }
            }
        });
        t.start();
    }


    public void AddPhotoprof(final Bitmap bmp, final Handler handler, Context context) {

        host = context.getString(R.string.aphost);

        final String TAG = "Upload Image Apache";
        //final String url = "http://192.168.8.101:8000/adddpost";
        final String url = host + "/api/update_profile_pic/";
        //final String url = "http://192.168.8.101/Upload_image_ANDROID/upload_image.php"

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");

                //       nameValuePairs.add(new BasicNameValuePair("Authorization", "Bearer "+token));

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);


                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] data = bos.toByteArray();
                        ByteArrayBody bab = new ByteArrayBody(data, "file" + ".jpg");
                        reqEntity.addPart("file", bab);
                    } catch (Exception e) {
                        //Log.v("Exception in Image", ""+e);
                        reqEntity.addPart("file", new StringBody("", ContentType.TEXT_PLAIN));
                    }

                    httppost.setEntity(reqEntity);
                    httppost.setHeader("Authorization", "Bearer " + token);


                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doFileUpload Response : " + responseStr);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    handler.sendEmptyMessage(0);
                }
            }
        });
        t.start();

    }


    public void AddPhotoCouv(final Bitmap bmp, final Handler handler, Context context) {

        host = context.getString(R.string.aphost);

        final String TAG = "Upload Image Apache";
        //final String url = "http://192.168.8.101:8000/adddpost";
        final String url = host + "/api/update_cover_pic/";
        //final String url = "http://192.168.8.101/Upload_image_ANDROID/upload_image.php"

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");

                //       nameValuePairs.add(new BasicNameValuePair("Authorization", "Bearer "+token));

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);


                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] data = bos.toByteArray();
                        ByteArrayBody bab = new ByteArrayBody(data, "file" + ".jpg");
                        reqEntity.addPart("file", bab);
                    } catch (Exception e) {
                        //Log.v("Exception in Image", ""+e);
                        reqEntity.addPart("file", new StringBody("", ContentType.TEXT_PLAIN));
                    }

                    httppost.setEntity(reqEntity);
                    httppost.setHeader("Authorization", "Bearer " + token);


                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doFileUpload Response : " + responseStr);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    handler.sendEmptyMessage(0);
                }
            }
        });
        t.start();

    }


    public void AddCours(final String path, final Handler handler, final String name, Context context) {

        host = context.getString(R.string.aphost);

        final String TAG = "Upload Image Apache";
        //final String url = "http://192.168.8.101:8000/adddpost";
        final String url = host + "/api/upload/cours/";
        //final String url = "http://192.168.8.101/Upload_image_ANDROID/upload_image.php"

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);
        // Toast.makeText(getApplicationContext(),"S  "+ path,Toast.LENGTH_LONG).show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(TAG, "Starting Upload...");

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);


                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    reqEntity.addPart("name", new StringBody(name, ContentType.TEXT_PLAIN));

                    File file = new File(path);


                    FileBody fb = new FileBody(file);
                    reqEntity.addPart("file", fb);

                    httppost.setEntity(reqEntity);
                    httppost.setHeader("Authorization", "Bearer " + token);


                    HttpResponse response = httpclient.execute(httppost);
                    String responseStr = EntityUtils.toString(response.getEntity());
                    Log.i(TAG, "doFileUpload Response : " + responseStr);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println("Error in http connection " + e.toString());
                    handler.sendEmptyMessage(0);
                }
            }
        });
        t.start();

    }


    public String convertBitmapToString(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        return Base64.encodeToString(byte_arr, Base64.DEFAULT);
    }


    public String GenerateString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }


    public void AddMessage(final String message, final int cont_id, final Context context) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        final String token = pref.getString("token", null);


        host = context.getString(R.string.aphost);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/message/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        //   Toast.makeText(context,cont_id+" "+message, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reciever", String.valueOf(cont_id));
                params.put("content", message);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    public void AddFCM(final Context context) {

        //String host = context.getString(R.string.aphost);
        host = context.getString(R.string.aphost);

        SharedPreferences pref = context.getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);

        SharedPreferences pref2 = context.getSharedPreferences("MyPREFERENCES2", MODE_PRIVATE);
        final String token = pref.getString("token", null);
        final String reg_id = pref2.getString("regId", null);


        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host + "/api/create_fcm/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);


                        Intent i = new Intent(context, AccueilActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
                        Toast.makeText(context, " " + reg_id + " dsgfdsf", Toast.LENGTH_LONG).show();


                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("registration_id", reg_id);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


    }


    public String getDay(int day) {
        switch (day) {
            case 1:
                return "Dimanche";
            case 2:
                return "lundi";
            case 3:
                return "Mardi";
            case 4:
                return "Mercredi";
            case 5:
                return "Jeudi";
            case 6:
                return "Vendredi";
            case 7:
                return "Samedi";

            default:
                return "";
        }
    }


    public String getDayMinis(int day) {
        switch (day) {
            case 1:
                return "dimanche";
            case 2:
                return "lundi";
            case 3:
                return "mardi";
            case 4:
                return "mercredi";
            case 5:
                return "jeudi";
            case 6:
                return "vendredi";
            case 7:
                return "samedi";

            default:
                return "";
        }
    }


    public String dateEvn(String dateb,String datee) {
        try
        {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.FRANCE);


            Calendar begCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date begTime;
            Date endTime;
            begTime = dateFormat.parse(dateb);
            endTime = dateFormat.parse(datee);

            begCal.setTime(begTime);
            endCal.setTime(endTime);


            if (begCal.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) > -1) {
                if (cal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {

                    if (endCal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {
                        return ("Aujourd'hui à " + timeFormat.format(begCal.getTime()) + " - " + timeFormat.format(endCal.getTime()));
                    } else if (endCal.get(Calendar.DAY_OF_YEAR) - begCal.get(Calendar.DAY_OF_YEAR) < 4) {
                        return ("Aujourd'hui à " + timeFormat.format(begCal.getTime()) + " - " + this.getDay(endCal.get(Calendar.DAY_OF_WEEK)) + " " + timeFormat.format(endCal.getTime()));
                    } else {
                        return ("Aujourd'hui à " + timeFormat.format(begCal.getTime()) + " - " + df.format(endCal.getTime()) + " " + timeFormat.format(endCal.getTime()));
                    }
                } else if (cal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR) - 1) {

                    if (endCal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {
                        return ("Demain à " + timeFormat.format(begCal.getTime()) + " - " + timeFormat.format(endCal.getTime()));
                    } else if (endCal.get(Calendar.DAY_OF_YEAR) - begCal.get(Calendar.DAY_OF_YEAR) < 4) {
                        return ("Demain à " + timeFormat.format(begCal.getTime()) + " - " + this.getDay(endCal.get(Calendar.DAY_OF_WEEK)) + " " + timeFormat.format(endCal.getTime()));
                    } else {
                        return ("Demain à " + timeFormat.format(begCal.getTime()) + " - " + df.format(endCal.getTime()) + " " + timeFormat.format(endCal.getTime()));
                    }
                } else if (begCal.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) < 4
                        && cal.get(Calendar.MONTH) == begCal.get(Calendar.MONTH)) {
                    if (endCal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {
                        return (this.getDay(begCal.get(Calendar.DAY_OF_WEEK)) + " " + timeFormat.format(begCal.getTime()) + " - " + timeFormat.format(endCal.getTime()));
                    } else if (endCal.get(Calendar.DAY_OF_YEAR) - begCal.get(Calendar.DAY_OF_YEAR) < 4) {
                        return (this.getDay(begCal.get(Calendar.DAY_OF_WEEK)) + " " + timeFormat.format(begCal.getTime()) + " - " + this.getDay(endCal.get(Calendar.DAY_OF_WEEK)) + " " + timeFormat.format(endCal.getTime()));
                    } else {
                        return (this.getDay(begCal.get(Calendar.DAY_OF_WEEK)) + " " + timeFormat.format(begCal.getTime()) + " - " + df.format(endCal.getTime()) + " " + timeFormat.format(endCal.getTime()));
                    }
                } else {
                    if (endCal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {
                        return (df.format(begCal.getTime()) + " " + timeFormat.format(begCal.getTime()) + " - " + timeFormat.format(endCal.getTime()));
                    } else {
                        return (df.format(begCal.getTime()) + " " + timeFormat.format(begCal.getTime()) + " - " + df.format(endCal.getTime()) + " " + timeFormat.format(endCal.getTime()));
                    }
                }
            }
            else {
                if (endCal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {
                    return ("(Expiré) "+df.format(begCal.getTime()) + " " + timeFormat.format(begCal.getTime()) + " - " + timeFormat.format(endCal.getTime()));
                } else {
                    return ("(Expiré) "+df.format(begCal.getTime()) + " " + timeFormat.format(begCal.getTime()) + " - " + df.format(endCal.getTime()) + " " + timeFormat.format(endCal.getTime()));
                }
            }

        } catch (
                ParseException e)

        {
            e.printStackTrace();
        }
        return "";
    }




public String singleDateEvn(String dateb) {
        try
        {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.FRANCE);


            Calendar begCal = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date begTime;
            begTime = dateFormat.parse(dateb);

            begCal.setTime(begTime);


            if (begCal.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) > -1) {
                if (cal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {

                    return ("Aujourd'hui à " + timeFormat.format(begCal.getTime()) );

                } else if (cal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR) - 1) {
                    return ("Demain à " + timeFormat.format(begCal.getTime()));

                } else if (begCal.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) < 4
                        && cal.get(Calendar.MONTH) == begCal.get(Calendar.MONTH)) {
                        return (this.getDay(begCal.get(Calendar.DAY_OF_WEEK)) + " à " + timeFormat.format(begCal.getTime()));

                } else {
                        return (df.format(begCal.getTime()) + " à " + timeFormat.format(begCal.getTime()));
                }
            }

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return "";
    }





public String singleDateEvnMinis(String dateb) {
        try
        {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.FRANCE);


            Calendar begCal = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date begTime;
            begTime = dateFormat.parse(dateb);

            begCal.setTime(begTime);


            if (begCal.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) > -1) {
                if (cal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR)) {

                    return ("aujourd'hui à " + timeFormat.format(begCal.getTime()) );

                } else if (cal.get(Calendar.DAY_OF_YEAR) == begCal.get(Calendar.DAY_OF_YEAR) - 1) {
                    return ("demain à " + timeFormat.format(begCal.getTime()));

                } else if (begCal.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) < 4
                        && cal.get(Calendar.MONTH) == begCal.get(Calendar.MONTH)) {
                        return (this.getDayMinis(begCal.get(Calendar.DAY_OF_WEEK)) + " à " + timeFormat.format(begCal.getTime()));

                } else {
                        return (df.format(begCal.getTime()) + " à " + timeFormat.format(begCal.getTime()));
                }
            }

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return "";
    }









}
