package com.project.naveen.bookmymovie;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    ArrayList<Movie> movieArrayList;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    public MainFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerview);
        movieArrayList=new ArrayList<>();
        int orientation= getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            layoutManager=new GridLayoutManager(getActivity(),2);
        }
        else{
            layoutManager=new GridLayoutManager(getActivity(),3);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MovieAdapter(getActivity(), movieArrayList));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override

            public void onClick(View view, int position) {
                Intent i=new Intent(getActivity(),DetailsActivity.class);

                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return v;
    }

}
