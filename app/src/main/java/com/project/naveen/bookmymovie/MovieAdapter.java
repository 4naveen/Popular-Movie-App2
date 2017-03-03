package com.project.naveen.bookmymovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.naveen.bookmymovie.utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public Context context;
    public ArrayList<Movie> movies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.indi_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Movie movie=movies.get(position);
       //Log.i("image_url in adap",AppConfig.MOVIES_IMAGE_URL+movie.getMovie_poster_url());
        Picasso.with(context)
                .load(AppConfig.MOVIES_IMAGE_URL+movie.getMaovie_poster_url())
                .placeholder(R.drawable.movie_poster)
                .error(R.drawable.movie_poster)
                .into(holder.movie_poster);
       // holder.title.setText(movie.getMovie_name());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movie_poster;
        TextView title;
        ViewHolder(View itemView) {
            super(itemView);
            movie_poster = (ImageView) itemView.findViewById(R.id.thumbnail);
            //title=(TextView)itemView.findViewById(R.id.title);
        }
    }
}
