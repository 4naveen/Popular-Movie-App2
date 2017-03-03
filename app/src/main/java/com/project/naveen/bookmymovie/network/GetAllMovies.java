package com.project.naveen.bookmymovie.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.project.naveen.bookmymovie.DetailsActivity;


import com.project.naveen.bookmymovie.Movie;
import com.project.naveen.bookmymovie.MovieAdapter;
import com.project.naveen.bookmymovie.utils.AppConfig;
import com.project.naveen.bookmymovie.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by 2naveen on 2/15/2017.
 */
public class GetAllMovies extends AsyncTask<String,Void,String>
{   String response;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    Context context;
    ArrayList<Movie> movieArrayList;



    public GetAllMovies(RecyclerView recyclerView, GridLayoutManager layoutManager, Context context, ArrayList<Movie> movieArrayList) {
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        this.context = context;
        this.movieArrayList = movieArrayList;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
           /* dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);*/
    }

    @Override
    protected String doInBackground(String... params) {
        URL url;
        String response="";
        HttpURLConnection connection ;
        BufferedReader bufferedReader;
        StringBuffer  buffer;
        try {
            url = new URL(AppConfig.MOVIES_URL+params[0]);
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                buffer=new StringBuffer();
                //Log.d("Output",br.toString());
                while ((line = bufferedReader.readLine()) != null) {
                    // response += line;
                    buffer.append(line);
                    Log.d("output lines", line);
                }
                response=buffer.toString();
                //Get Values from JSONobject
                //System.out.println("success=" + json.get("success"));
                Log.i("response in customer", response);
            }
            else {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                buffer=new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    //  response += line;
                    buffer.append(line);
                    Log.d("output lines", line);
                }
                response=buffer.toString();
                Log.i("response in customer", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // connection.disconnect();
        return response;
    }
    @Override
    protected void onPostExecute(String response) {
        // dialog.dismiss();
        try {
            Log.i("response--",response);
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject object=jsonArray.getJSONObject(i);
                Movie movie=new Movie();
                movie.setMaovie_poster_url(object.getString("poster_path"));
                movie.setMovie_name(object.getString("original_title"));
                movie.setMovie_id(object.getInt("id"));
                movieArrayList.add(movie);
                // Log.i("movieList--",movie.getMovie_poster_url());
            }
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new MovieAdapter(context, movieArrayList));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
