package com.example.leo.gridexemple.ui.detail;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leo.gridexemple.R;
import com.example.leo.gridexemple.adapter.TrailersRecyclerViewAdapter;
import com.example.leo.gridexemple.model.Example;
import com.example.leo.gridexemple.model.Result;
import com.example.leo.gridexemple.model.ResultReview;
import com.example.leo.gridexemple.model.Trailers;
import com.example.leo.gridexemple.model.Youtube;
import com.example.leo.gridexemple.utils.Utils;
import com.example.leo.gridexemple.webservice.MovieService;
import com.example.leo.gridexemple.webservice.RestService;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements TrailersRecyclerViewAdapter.ItemClickListener{


    TrailersRecyclerViewAdapter adapter;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        init(view);
        return view;
    }


    private void init(View v) {
        final Realm realm = Realm.getDefaultInstance();



        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.second_view_trailer_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new TrailersRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(toolbar.getContext(), R.drawable.ic_arrow_back));
        toolbar.setTitle("Detail");



        if (getActivity() instanceof SecondView) {
            setHasOptionsMenu(true);
            ((SecondView) getActivity()).setSupportActionBar(toolbar);
            ((SecondView) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((SecondView) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }


        final Result index =(Result)getActivity().getIntent().getSerializableExtra("index");
        TextView tvData = (TextView) v.findViewById(R.id.second_view_data_text);
        TextView tvAvaliation = (TextView) v.findViewById(R.id.second_view_avaliation_text);
        TextView tvHeader = (TextView) v.findViewById(R.id.second_view_header_text);
        TextView tvOverview = (TextView) v.findViewById(R.id.second_view_overview_text);
        ImageView thumb = (ImageView) v.findViewById(R.id.image_thumb);
        final Button btFavorite = (Button) v.findViewById(R.id.second_view_favorite_button);

        final RealmResults<Result> puppies = realm.where(Result.class).equalTo("id",index.getId()).findAll();

        Log.i("VAL",""+puppies.size());
        if (puppies.size() > 0 ) {
            btFavorite.setSelected(true);
        }


        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btFavorite.isSelected()) {
                    //btFavorite.setBackgroundResource(R.drawable.ic_star_border);
                    btFavorite.setSelected(false);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            puppies.deleteFirstFromRealm();
                        }
                    });


                } else {
                    // btFavorite.setBackgroundResource(R.drawable.ic_star);
                    btFavorite.setSelected(true);
                    realm.beginTransaction();
                    realm.copyToRealm(index);
                    realm.commitTransaction();
                }

            }
        });

        tvOverview.setText(index.getOverview());
        tvData.setText(index.getReleaseDate());
        tvHeader.setText(index.getOriginalTitle());
        tvAvaliation.setText(String.valueOf(index.getVoteAverage()));
        Glide
                .with(thumb.getContext())
                .load("http://image.tmdb.org/t/p/w185/" + index.getPosterPath())
                .into(thumb);




        Log.i("volotu", index.getId().toString());

        MovieService service = RestService.getClient("https://api.themoviedb.org/3/movie/").create(MovieService.class);
        Call<Trailers> call = service.getTrailersMovie(index.getId(), Utils.getParameters());
        call.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Trailers trailers = response.body();//gson.fromJson(response.body(), Trailers.class);
                    Log.i("volotu", trailers.getId().toString());

                    adapter.setTrailers(trailers.getYoutube());

                }



            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {
                t.printStackTrace();
                Log.i("deu erro", "treta");
            }
        });

        getReviews(index.getId(), v);
    }

    private void getReviews(Integer id, final View v) {

        MovieService service = RestService.getClient("https://api.themoviedb.org/3/movie/").create(MovieService.class);
        Call<Example> call = service.getReviewMovie(id, Utils.getParameters());
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Example review = response.body();//gson.fromJson(response.body(), Trailers.class);
                    Log.i("volotu", review.getId().toString());

                    String reviewStr = "";

                    for (ResultReview r : review.getResults()) {
                        reviewStr += r.getContent() + "\n\n";
                    }

                    TextView tvReview = (TextView) v.findViewById(R.id.second_view_reviews_text);
                    tvReview.setText(reviewStr);


                }



            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                t.printStackTrace();
                Log.i("deu erro", "treta");
            }
        });
    }
    @Override
    public void onItemClick(View view, Youtube position) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + position.getSource())));
        Log.e("OLHA ESSE BAGULHO ", position.getName());
    }
}
