package com.example.leo.gridexemple.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.leo.gridexemple.R;
import com.example.leo.gridexemple.model.Result;
import com.example.leo.gridexemple.ui.detail.DetailFragment;
import com.example.leo.gridexemple.ui.detail.SecondView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_grid);

        if (savedInstanceState == null) {
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.main_fragment, new MainFragment());
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
            ft.commitAllowingStateLoss();
        }



    }

    public void gotToDetail(String option, Result position) {

        if (option.equals("favorite")) {
            Result result = new Result();
            result.setPosterPath(position.getPosterPath());
            result.setOverview(position.getOverview());
            result.setReleaseDate(position.getReleaseDate());
            result.setVoteAverage(position.getVoteAverage());
            result.setOriginalTitle(position.getOriginalTitle());
            result.setId(position.getId());
            result.setTitle(position.getTitle());
            Intent intent = new Intent(MainActivity.this, SecondView.class);
            intent.putExtra("index", result);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, SecondView.class);
            intent.putExtra("index", position);
            startActivity(intent);
        }

    }

}
