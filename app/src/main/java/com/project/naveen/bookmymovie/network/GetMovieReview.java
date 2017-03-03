package com.project.naveen.bookmymovie.network;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.naveen.bookmymovie.DetailsActivity;
import com.project.naveen.bookmymovie.R;
import com.project.naveen.bookmymovie.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class GetMovieReview extends AsyncTask<String,Void,String>
{
    private DetailsActivity activity;
    private LinearLayout linearLayout;

    public GetMovieReview(DetailsActivity activity, LinearLayout linearLayout) {
        this.activity = activity;
        this.linearLayout = linearLayout;
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
        int movie_id=Integer.parseInt(params[0]);
        String api_key=params[1];

        URL url;
        try {

           // url = new URL("https://api.themoviedb.org/3/movie/328111/reviews?api_key=7689120d4883761f7abe4a3e08df9aa8");
            url = new URL(AppConfig.BASE_URL+"/"+movie_id+"/reviews?api_key="+api_key);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode= conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //Log.d("Output",br.toString());
                while ((line = br.readLine()) != null) {
                    response += line;
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
                json = new JSONObject(response);
                jsonResponse=json.getString("error");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(String result) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
if (jsonArray.length()==0)
{
    TextView sorry=new TextView(activity);
    sorry.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
    sorry.setTextSize(16);
    sorry.setPadding(5,2,5,2);
    sorry.setText("Sorry there is no reviews available");

    linearLayout.addView(sorry);

}
            for (int i=0;i<jsonArray.length();i++)
            {   TextView author=new TextView(activity);
                author.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                author.setTextSize(16);
                author.setPadding(5,2,5,2);
                TextView review=new TextView(activity);
                review.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                //review.setEllipsize(TextUtils.TruncateAt.END);
                review.setPadding(5,2,5,2);
                review.setHorizontallyScrolling(true);
                review.setMaxLines(3);
                review.setEms(150);
                TextView read_more=new TextView(activity);
                read_more.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                read_more.setText(R.string.read_more);
                read_more.setTextColor(Color.RED);
                read_more.setPadding(5,2,5,2);
                View v=new View(activity);
                v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                v.setAlpha((float) 0.5);
                v.setPadding(5,3,5,3);
                v.setBackground(activity.getResources().getDrawable(R.drawable.line_divider));
                linearLayout.addView(author);
                linearLayout.addView(review);
                linearLayout.addView(read_more);
                linearLayout.addView(v);
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                author.setText(jsonObject1.getString("author"));
                review.setText(jsonObject1.getString("content"));
                final String finalContent_url = jsonObject1.getString("url");
                read_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(finalContent_url));
                        activity.startActivity(i);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
