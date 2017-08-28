package com.example.leo.gridexemple.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.leo.gridexemple.R;
import com.example.leo.gridexemple.model.Result;

import java.util.List;

/**
 * Created by leo on 22/05/17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ItemClickListener mClickListener;
    private List<Result> results;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setResults(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Result r = results.get(position);
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide
                .with(holder.myImage.getContext())
                .load("http://image.tmdb.org/t/p/w300/" + r.getPosterPath())

                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .crossFade()
                .into(holder.myImage);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return results != null ? results.size() : 0;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView myImage;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            myImage = (ImageView) itemView.findViewById(R.id.ivMovie);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, results.get(getAdapterPosition()));
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Result position);
    }
}
