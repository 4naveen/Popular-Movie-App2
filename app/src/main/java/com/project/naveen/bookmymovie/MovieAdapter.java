package com.project.naveen.bookmymovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    private ArrayList<Movie> movies;
    int images[] = {R.drawable.movie_poster, R.drawable.movie_poster, R.drawable.movie_poster, R.drawable.movie_poster,
            R.drawable.movie_poster, R.drawable.movie_poster, R.drawable.movie_poster, R.drawable.movie_poster, R.drawable.movie_poster};
    String[] titles = {"Kabil", "Kabil", "Kabil", "Kabil"};


    MovieAdapter(Context context, ArrayList<Movie> movies) {
       /* this.context = context;
        this.movies = movies;*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.indi_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        for (int i = 0; i < images.length; i++) {
            holder.movie_poster.setImageResource(images[i]);

        }
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView movie_poster;

        ViewHolder(View itemView) {
            super(itemView);
            movie_poster = (ImageView) itemView.findViewById(R.id.movie_poster);
        }
    }
}
