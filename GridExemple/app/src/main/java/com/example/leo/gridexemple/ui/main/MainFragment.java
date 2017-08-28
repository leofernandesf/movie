package com.example.leo.gridexemple.ui.main;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.leo.gridexemple.R;
import com.example.leo.gridexemple.adapter.MyRecyclerViewAdapter;
import com.example.leo.gridexemple.model.Movie;
import com.example.leo.gridexemple.model.Result;
import com.example.leo.gridexemple.utils.Utils;
import com.example.leo.gridexemple.webservice.MovieService;
import com.example.leo.gridexemple.webservice.RestService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{


    MyRecyclerViewAdapter adapter;
    private SharedPreferences pref;
    private List<Result> resultList;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        pref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String option = pref.getString("filterOption", null);
        if (option == null) {
            resquetMovies("popular");
        } else if (option.equals("favorite")) {
            final Realm realm = Realm.getDefaultInstance();
            resultList = realm.where(Result.class).findAll();
            adapter.setResults(resultList);
        } else {
            Log.i("VAL OLHA AQUI", option);
            resquetMovies(option);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String option = pref.getString("filterOption", null);
        switch (item.getItemId()) {
            case R.id.miPopular:
                if (option != null && option.equals("popular")) {
                    return false;
                }
                adapter.setResults(null);
                resquetMovies("popular");
                setUserFilterOption("popular");
                break;
            case R.id.miTop:
                if (option != null && option.equals("top_rated")) {
                    return false;
                }
                adapter.setResults(null);
                resquetMovies("top_rated");
                setUserFilterOption("top_rated");
                break;
            case R.id.miFavorite:
                if (option != null && option.equals("favorite")) {
                    return false;
                }
                adapter.setResults(null);
                final Realm realm = Realm.getDefaultInstance();
                resultList = realm.where(Result.class).findAll();
                adapter.setResults(resultList);
                setUserFilterOption("favorite");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUserFilterOption(String option) {

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("filterOption", option);
        editor.apply();
    }


    private void resquetMovies(String options) {
        MovieService service = RestService.getClient("https://api.themoviedb.org/3/movie/").create(MovieService.class);
        Call<ResponseBody> call;
        if (options.trim().equals("popular")) {
            call = service.getPopularMovie(Utils.getParameters());
        } else {
            call = service.getHighestMovie(Utils.getParameters());
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {



                    Gson gson = new Gson();
                    Movie movie = gson.fromJson(response.body().string(), Movie.class);
                    Log.i("volotu", movie.toString());
                    adapter.setResults(movie.getResults());
                    resultList = movie.getResults();
                    swipeRefreshLayout.setRefreshing(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.i("deu erro", "treta");
            }
        });
    }

    private void init(View v) {

        //set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.myRecycleView);
        int numberOfColums = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), numberOfColums));
        adapter = new MyRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        if (getActivity() instanceof MainActivity) {
            setHasOptionsMenu(true);
            ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        }
        //v.setSupportActionBar(toolbar);

        this.swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setResults(null);
                String option = pref.getString("filterOption", null);
                if (option == null) {
                    resquetMovies("popular");
                } else if (option.equals("favorite")) {
                    final Realm realm = Realm.getDefaultInstance();
                    resultList = realm.where(Result.class).findAll();
                    adapter.setResults(resultList);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Log.i("VAL OLHA AQUI", option);
                    resquetMovies(option);
                }
            }
        });


        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }



    @Override
    public void onItemClick(View view, Result position) {


        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).gotToDetail(pref.getString("filterOption", ""), position);
        }

    }
}
