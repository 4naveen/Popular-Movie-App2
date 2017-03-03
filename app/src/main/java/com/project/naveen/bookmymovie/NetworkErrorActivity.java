package com.project.naveen.bookmymovie;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NetworkErrorActivity extends AppCompatActivity {
    Button cancel,retry;
    int movie_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);
        movie_id=getIntent().getIntExtra("movie_id",0);
        cancel=(Button)findViewById(R.id.cancel);
            retry=(Button)findViewById(R.id.retry);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isConnected()){
                        //do nothing
                    }
                    if (isConnected()){
                        Intent i = new Intent(NetworkErrorActivity.this, DetailsActivity.class);
                        i.putExtra("movie_id",movie_id);
                        startActivity(i);}
                }
            });
    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;

        }
    }
}
