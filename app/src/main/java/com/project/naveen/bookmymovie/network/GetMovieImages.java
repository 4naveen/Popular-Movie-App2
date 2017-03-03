package com.project.naveen.bookmymovie.network;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.project.naveen.bookmymovie.DetailsActivity;
import com.project.naveen.bookmymovie.utils.AppSession;
//import com.project.naveen.bookmymovie.utils.AppSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class GetMovieImages extends AsyncTask<String,Void,String>
{
    private DetailsActivity activity;

    public GetMovieImages(DetailsActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        String response="",jsonResponse="";
        JSONObject json;
        BufferedReader bufferedReader;

        URL url;
        try {

            url = new URL("https://api.themoviedb.org/3/movie/popular?api_key=7689120d4883761f7abe4a3e08df9aa8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode= conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
        JSONObject jsonObject;
        AppSession.images=new ArrayList<>();
        try {
            jsonObject = new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String poster_url = "http://image.tmdb.org/t/p/w500/"+jsonObject1.getString("poster_path");

              AppSession.images.add(poster_url);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
