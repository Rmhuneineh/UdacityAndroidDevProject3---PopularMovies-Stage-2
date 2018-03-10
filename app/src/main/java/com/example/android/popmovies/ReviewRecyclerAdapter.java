package com.example.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rmhuneineh on 10/03/2018.
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
    Movie mChosenMovie;
    Context mContext;

    public ReviewRecyclerAdapter(Context context, Movie chosenMovie) {
        mChosenMovie = chosenMovie;
        mContext = context;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView contentTV;
        protected TextView authorTV;

        public ViewHolder(View itemView) {
            super(itemView);
            contentTV = itemView.findViewById(R.id.content_text_view);
            authorTV = itemView.findViewById(R.id.author_text_view);
        }
    }

    @Override
    public ReviewRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listITem = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(listITem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewRecyclerAdapter.ViewHolder holder, int position) {
        String review = mChosenMovie.getContents().get(position);
        String author = mChosenMovie.getAuthors().get(position);

        holder.contentTV.setText(review);
        holder.authorTV.setText(author);
    }

    @Override
    public int getItemCount() {
        return  mChosenMovie.getAuthors().size();
    }
}
