package com.example.leo.gridexemple.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leo.gridexemple.R;
import com.example.leo.gridexemple.adapter.TrailersRecyclerViewAdapter;
import com.example.leo.gridexemple.model.Example;
import com.example.leo.gridexemple.model.Result;
import com.example.leo.gridexemple.model.ResultReview;
import com.example.leo.gridexemple.model.Trailers;
import com.example.leo.gridexemple.model.Youtube;
import com.example.leo.gridexemple.ui.main.MainFragment;
import com.example.leo.gridexemple.utils.Utils;
import com.example.leo.gridexemple.webservice.MovieService;
import com.example.leo.gridexemple.webservice.RestService;
import com.google.gson.Gson;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by leo on 22/05/17.
 */

public class SecondView extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_view);

        if (savedInstanceState == null) {
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.detail_fragment, new DetailFragment());
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commitAllowingStateLoss();
        }

    }

}
