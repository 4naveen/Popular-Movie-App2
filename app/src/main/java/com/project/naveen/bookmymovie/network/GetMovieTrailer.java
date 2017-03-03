package com.project.naveen.bookmymovie.network;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.project.naveen.bookmymovie.utils.AppConfig;
import com.project.naveen.bookmymovie.utils.AppSession;
import com.project.naveen.bookmymovie.DetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetMovieTrailer extends AsyncTask<String,Void,String>
{
    private DetailsActivity activity;

    public GetMovieTrailer(DetailsActivity activity) {
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
        String movie_id=params[0];
        String api_key=params[1];
        URL url;
        try {

           // url = new URL("https://api.themoviedb.org/3/movie/297761/videos?api_key=7689120d4883761f7abe4a3e08df9aa8");
            url = new URL(AppConfig.BASE_URL+"/"+movie_id+"/videos?api_key="+api_key);


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
        try {
            jsonObject = new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            JSONObject jsonObject1=jsonArray.getJSONObject(jsonArray.length()-1);
            String trailer_id = jsonObject1.getString("id");
            AppSession.trailer_id= trailer_id;
            Log.i("check_share", String.valueOf(AppSession.check_share));
            Log.i("trailerid",AppSession.trailer_id);

            if (!AppSession.check_share)
            {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer_id));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailer_id));
                try {
                    appIntent.putExtra("force_fullscreen",true);
                    activity.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    activity.startActivity(webIntent);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
