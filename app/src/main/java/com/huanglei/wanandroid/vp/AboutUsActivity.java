package com.huanglei.wanandroid.vp;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.huanglei.wanandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_activity_about_us)
    Toolbar toolbarActivityAboutUs;
    @BindView(R.id.tv_vision_activity_about_us)
    TextView tvVisionActivityAboutUs;
    @BindView(R.id.tv_content_activity_about_us)
    TextView tvContentActivityAboutUs;
    @BindView(R.id.collapsing_toolbar_activity_about_us)
    CollapsingToolbarLayout collapsingToolbarActivityAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setSupportActionBar(toolbarActivityAboutUs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置字体
        collapsingToolbarActivityAboutUs.setCollapsedTitleTypeface(Typeface.DEFAULT);
        tvContentActivityAboutUs.setText(Html.fromHtml(getString(R.string.about_us_content)));
        tvContentActivityAboutUs.setMovementMethod(LinkMovementMethod.getInstance());
        try {
            String versionName=getString(R.string.app_name)+" v" +
                    getPackageManager().getPackageInfo(getPackageName(),0).versionName;
            tvVisionActivityAboutUs.setText(versionName);
            tvVisionActivityAboutUs.setVisibility(View.VISIBLE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
