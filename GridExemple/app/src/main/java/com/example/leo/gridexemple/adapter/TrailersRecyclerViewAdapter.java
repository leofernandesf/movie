package com.example.leo.gridexemple.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leo.gridexemple.R;

import com.example.leo.gridexemple.model.Youtube;

import java.util.List;

/**
 * Created by leo on 25/05/17.
 */

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {

    private TrailersRecyclerViewAdapter.ItemClickListener mClickListener;
    private List<Youtube> youtubes;

    // data is passed into the constructor
    public TrailersRecyclerViewAdapter(TrailersRecyclerViewAdapter.ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setTrailers(List<Youtube> youtubes) {
        this.youtubes = youtubes;
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @Override
    public TrailersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_cell, parent, false);
        return new TrailersRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(TrailersRecyclerViewAdapter.ViewHolder holder, int position) {
        Youtube r = youtubes.get(position);

        holder.trailerText.setText(r.getName());
        /*
        Glide
                .with(holder.myImage.getContext())
                .load("http://image.tmdb.org/t/p/w185/" + r.getPosterPath())
                .into(holder.myImage);*/
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return youtubes != null ? youtubes.size() : 0;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView trailerImage;
        public TextView trailerText;
        public ViewHolder(View itemView) {
            super(itemView);
            trailerImage = (ImageView) itemView.findViewById(R.id.trailer_image);
            trailerText = (TextView) itemView.findViewById(R.id.trailer_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, youtubes.get(getAdapterPosition()));
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Youtube position);
    }
}
