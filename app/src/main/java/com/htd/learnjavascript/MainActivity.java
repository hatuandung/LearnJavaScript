package com.htd.learnjavascript;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.htd.learnjavascript.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.htd.learnjavascript.DetailActivity.RESULT_BACK;
import static com.htd.learnjavascript.DetailActivity.RESULT_NEXT;

public class MainActivity extends AppCompatActivity implements ChapterAdapter.ChapterItemClickListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE_CHAPTER = 0;
    //ActivityMainBinding binding;
    ActivityMainBinding binding;
    private ArrayList<Chapter> chapters = new ArrayList<>();
    private ChapterAdapter adapter;
    int index;
    FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
    }

    private void initView() {

        adapter = new ChapterAdapter(getLayoutInflater());
        binding.rvChapter.setAdapter(adapter);
        adapter.setData(initData());
        adapter.setListener(this);
//        binding.drawerLayout;
        binding.navView.setNavigationItemSelectedListener(this);
        binding.svChapter.setOnQueryTextListener(this);

        setSupportActionBar(binding.toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Learn JavaScript");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Chapter List");
        //bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Bundle params = new Bundle();
        params.putString("app_name", "Learn JavaScript");
        params.putString("activity", "Select chapter");
        mFirebaseAnalytics.logEvent("Select_chapter", params);
    }


    private ArrayList<Chapter> initData() {
        int count = 0;
        int id;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getApplicationContext().getAssets().open("tieude.txt"), "UTF-8"));
            String mLine;
//            reader.readLine();
            while ((mLine = reader.readLine()) != null) {
                String title = mLine.substring(mLine.indexOf("-") + 2, mLine.indexOf("/") - 1);
                Log.e("initData: ", title);
                id = count + 1;
                chapters.add(new Chapter(title, "file:///android_asset/" + count + ".html", id));
                count++;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return chapters;
    }

    public void intent(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("data", chapters.get(position));
        startActivityForResult(intent, REQUEST_CODE_CHAPTER);
        index = position;
    }

    @Override
    public void onItemClickListener(Chapter item) {
        int index = chapters.indexOf(item);
        intent(index);
        Log.e("onItemClickListener: ", chapters.get(index).getUrl());
        Log.e("onItemClickListener: ", chapters.get(index).getTitle());
        Log.e("onItemClickListener: ", String.valueOf(chapters.get(index).getId()));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        binding.drawerLayout.closeDrawers();


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_CHAPTER && resultCode == RESULT_BACK && data != null) {

            if (index > 1) {
                index--;
                intent(index);
            } else {
                Toast.makeText(this, "Reached at first", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CODE_CHAPTER && resultCode == RESULT_NEXT && data != null) {
            if (index < chapters.size() - 1) {
//                String next = data.getStringExtra("next");
                index++;
                intent(index);
            } else {
                Toast.makeText(this, "Reached at end", Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}