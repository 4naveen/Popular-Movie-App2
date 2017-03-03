package com.project.naveen.bookmymovie;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.project.naveen.bookmymovie.network.GetAllMovies;
import com.project.naveen.bookmymovie.utils.AppConfig;
import com.project.naveen.bookmymovie.utils.RecyclerTouchListener;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    ArrayList<Movie> movieArrayList;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    FrameLayout frameLayout;
    public MainFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_main, container, false);
        frameLayout=(FrameLayout)v.findViewById(R.id.frame);
        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerview);
        movieArrayList=new ArrayList<>();
        int orientation= getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            layoutManager=new GridLayoutManager(getActivity(),2);
        }
        else{
            layoutManager=new GridLayoutManager(getActivity(),3);
        }

        new GetAllMovies(recyclerView,layoutManager,getActivity(),movieArrayList).execute(AppConfig.API_KEY);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override

            public void onClick(View view, int position) {

                Movie movie= movieArrayList.get(position);
                 int movieId=movie.getMovie_id();


                if (!isConnected()){
                    Intent i = new Intent(getActivity(),NetworkErrorActivity.class);
                    i.putExtra("movie_id",movieId);
                    startActivity(i);}
                if (isConnected()){

                    Intent i=new Intent(getActivity(),DetailsActivity.class);
                    i.putExtra("movie_id",movieId);
                    Log.i("movie_id", String.valueOf(movieId));
                    getActivity().startActivity(i);}
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return v;
    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!isConnected()){
            final Snackbar snackbar = Snackbar.make(frameLayout, "Please check your Internet Connection !", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view .setMinimumWidth(1000);
            TextView tv = (TextView) view .findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

}
