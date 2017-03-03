package com.project.naveen.bookmymovie.network;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.naveen.bookmymovie.DetailsActivity;
import com.project.naveen.bookmymovie.utils.AppConfig;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class GetMovieDetail extends AsyncTask<String,Void,String>
{
    private ProgressDialog dialog;
    private TextView movie_name,release_date,run_time,language,genre,overview,rating;
    private DetailsActivity activity;
    private LinearLayout layout_back;
    android.support.v7.app.ActionBar actionBar;
    TextView title;
    public GetMovieDetail(TextView rating, TextView movie_name, TextView release_date, TextView run_time, TextView language, TextView genre, TextView overview, DetailsActivity activity, LinearLayout layout_back, ActionBar actionBar, TextView title) {
        this.rating = rating;
        this.movie_name = movie_name;
        this.release_date = release_date;
        this.run_time = run_time;
        this.language = language;
        this.genre = genre;
        this.overview = overview;
        this.activity = activity;
        this.layout_back = layout_back;
        this.actionBar = actionBar;
        this.title = title;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading, please wait");
        dialog.setTitle("Connecting server");
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... params) {
        String response="",jsonResponse="";
        JSONObject json;
        BufferedReader bufferedReader;
        int movie_id=Integer.parseInt(params[0]);
        String api_key=params[1];
        URL url;
        try {

            url = new URL(AppConfig.BASE_URL+"/"+movie_id+"?api_key="+api_key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode= conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //Log.d("Output",br.toString());
                while ((line = br.readLine()) != null) {
                    response += line;
                    Log.d("output lines", line);
                }
                jsonResponse=response;

            }
            else {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getErrorStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                    Log.d("output lines", line);
                }
                Log.i("response",response);
                json = new JSONObject(response);
                jsonResponse=json.getString("error");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            movie_name.setText(jsonObject.getString("original_title"));
            title.setText(jsonObject.getString("original_title"));
            actionBar.setTitle(title.getText().toString());
            release_date.setText(jsonObject.getString("release_date"));
            run_time.setText(jsonObject.getString("runtime"));
            overview.setText(jsonObject.getString("overview"));
            language.setText(jsonObject.getString("original_language"));
            String text_rating=jsonObject.getString("vote_average");
            rating.setText(text_rating+"/10");
            String movie_poster_url="http://image.tmdb.org/t/p/w342/"+jsonObject.getString("poster_path");

            Picasso.with(activity).load(movie_poster_url).into(new Target() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    layout_back.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

            JSONArray jsonArray=jsonObject.getJSONArray("genres");
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                genre.setText(jsonObject1.getString("name")+"\n"+genre.getText().toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
