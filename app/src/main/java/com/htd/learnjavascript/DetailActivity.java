package com.htd.learnjavascript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.htd.learnjavascript.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity implements DetailListener {
    ActivityDetailBinding binding;
    public static final int RESULT_BACK = -1;
    public static final int RESULT_NEXT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        initView();
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        Chapter chapter = (Chapter) getIntent().getSerializableExtra("data");
        String url = chapter.getUrl();

        Log.e("TAG", "CHECK URL:" + url);
        binding.wvChapter.loadUrl(url);
        Log.e("initViews: ", url);
        binding.wvChapter.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        binding.wvChapter.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        binding.wvChapter.getSettings().setBuiltInZoomControls(true);
        binding.wvChapter.getSettings().setDisplayZoomControls(false);

//        binding.wvChapter.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
//        binding.wvChapter.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
//        binding.wvChapter.getSettings().setAllowFileAccess(true);
//        binding.wvChapter.getSettings().setAppCacheEnabled(true);
//        binding.wvChapter.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        binding.setListener(this);
        setTitle();


        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Learn JavaScript");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Detail");
        //bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Bundle params = new Bundle();
        params.putString("app_name", "Learn JavaScript");
        params.putString("activity", "detail");
        mFirebaseAnalytics.logEvent("Detail", params);
    }

    private void setTitle() {
        Chapter chapter = (Chapter) getIntent().getSerializableExtra("data");
        String name = chapter.getTitle();
        int id = chapter.getId();

    }

    @Override
    public void onNext() {
        String action = "back";
        Intent intent = new Intent();
        intent.putExtra("back", action);
        setResult(RESULT_NEXT, intent);
        finish();
        Log.e( "onNext: ", "Next" );
    }

    @Override
    public void onBack() {
        String action = "next";
        Intent intent = new Intent();
        intent.putExtra("back", action);
        setResult(RESULT_BACK, intent);
        finish();
    }
}