package com.project.naveen.bookmymovie;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.naveen.bookmymovie.network.GetMovieDetail;
import com.project.naveen.bookmymovie.network.GetMovieImages;
import com.project.naveen.bookmymovie.network.GetMovieReview;
import com.project.naveen.bookmymovie.network.GetMovieTrailer;
import com.project.naveen.bookmymovie.utils.AppConfig;
import com.project.naveen.bookmymovie.utils.AppSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DetailsActivity extends AppCompatActivity {
    LinearLayout linearLayout,layout_back;
    int movie_id;
    String trailer_id;
    TextView movie_name,release_date,run_time,language,genre,overview,rating;
    ImageView play;
    View view;
    Menu action_menu;
    boolean favourite;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        AppSession.check_share=false;
//        Log.i("trailerId",AppSession.trailer_id);

        linearLayout=(LinearLayout)findViewById(R.id.layout);
        layout_back=(LinearLayout)findViewById(R.id.layout_back);
        view=(View)findViewById(R.id.line);
        movie_id=getIntent().getIntExtra("movie_id",0);
        movie_name=(TextView)findViewById(R.id.movie_name);
        release_date=(TextView)findViewById(R.id.release_date);
        run_time=(TextView)findViewById(R.id.runtime);
        language=(TextView)findViewById(R.id.language);
        genre=(TextView)findViewById(R.id.genre);
        overview=(TextView)findViewById(R.id.overview);
        play=(ImageView)findViewById(R.id.play);
        rating=(TextView)findViewById(R.id.rating);
        setSupportActionBar(toolbar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
           // LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=layoutInflater.inflate(R.layout.movie_title,null);
            title=(TextView)v.findViewById(R.id.title);
            title.setSelected(true);
        }

        new GetMovieDetail(rating,movie_name,release_date,run_time,language,genre,overview,DetailsActivity.this,layout_back,actionBar,title)
                .execute(String.valueOf(movie_id), AppConfig.API_KEY);
        new GetMovieReview(DetailsActivity.this,linearLayout).execute(String.valueOf(movie_id), AppConfig.API_KEY);
        new GetMovieImages(DetailsActivity.this).execute();
        AppSession.check_share=true;

        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSession.check_share=false;

                new GetMovieTrailer(DetailsActivity.this).execute(String.valueOf(movie_id), AppConfig.API_KEY);

            }
        });


   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
        menu.findItem(R.id.action_favourite).setIcon(R.mipmap.check_off);
        action_menu=menu;
        favourite=false;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                break;
            }
            case R.id.action_share:
            {
                new GetMovieTrailer(DetailsActivity.this).execute(String.valueOf(movie_id), AppConfig.API_KEY);
                Log.i("trailerId","ghgjgjgf");
                Intent intentShare=new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT,"http://www.youtube.com/watch?v=" + AppSession.trailer_id);
                startActivity(Intent.createChooser(intentShare, "Select an action"));
                break;
            }
            case R.id.action_favourite:
            {
                if(favourite){
                    item.setIcon(R.mipmap.check_off);
                    favourite = false;
                } else {
                    item.setIcon(R.mipmap.check_on);
                    favourite = true;
                    StoreFavourite();

                }

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void StoreFavourite() {
        SharedPreferences preferences=getSharedPreferences("favourite",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("movie_id",movie_id);
        editor.putString("movie_name",movie_name.getText().toString());
        editor.apply();
    }
}
