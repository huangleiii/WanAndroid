package com.huanglei.wanandroid.vp.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.SettingActivityContract;
import com.huanglei.wanandroid.vp.setting.SettingActivityPresenter;
import com.huanglei.wanandroid.widget.MyProgressDialog;

import butterknife.BindView;

public class SettingActivity extends MVPBaseActivity<SettingActivityContract.Presenter> implements SettingActivityContract.View, View.OnClickListener {

    @BindView(R.id.tv_title_activity_setting)
    TextView tvTitleActivitySetting;
    @BindView(R.id.toolbar_activity_setting)
    Toolbar toolbarActivitySetting;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.linear_cache)
    LinearLayout linearCache;
    @BindView(R.id.linear_suggestion)
    LinearLayout linearSuggestion;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.linear_version)
    LinearLayout linearVersion;
    @BindView(R.id.linear_log)
    LinearLayout linearLog;
    @BindView(R.id.linear_copyright)
    LinearLayout linearCopyright;
    private MyProgressDialog myProgressDialog;

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showClearCacheSucceed(String cacheSize) {
        myProgressDialog.dismiss();
        tvCacheSize.setText(cacheSize);
    }

    @Override
    public void showClearCacheFailed(String cacheSize) {

    }

    @Override
    public void showCacheSizeSucceed(String cacheSize) {
        tvCacheSize.setText(cacheSize);
    }

    @Override
    public void showCacheSizeFailed(String cacheSize) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected SettingActivityContract.Presenter createPresenter() {
        return new SettingActivityPresenter();
    }


    @Override
    protected void initView() {
        setSupportActionBar(toolbarActivitySetting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        try {
            tvVersion.setText("当前版本 " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        linearCache.setOnClickListener(this);
        linearCopyright.setOnClickListener(this);
        linearLog.setOnClickListener(this);
        linearSuggestion.setOnClickListener(this);
        linearVersion.setOnClickListener(this);
        myProgressDialog=new MyProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_cache:
                myProgressDialog.setText("正在清除缓存，请稍候……").show();
                getPresenter().clearCache();
                break;
            case R.id.linear_suggestion:
                AlertDialog.Builder builder1=new AlertDialog.Builder(this)
                        .setMessage("测试使用")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alertDialog1=builder1.create();
                alertDialog1.setCanceledOnTouchOutside(false);
                alertDialog1.show();
                break;
            case R.id.linear_version:
                AlertDialog.Builder builder2=new AlertDialog.Builder(this)
                        .setMessage("测试使用")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alertDialog2=builder2.create();
                alertDialog2.setCanceledOnTouchOutside(false);
                alertDialog2.show();
                break;
            case R.id.linear_log:
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.releases)));
                startActivity(intent);
                break;
            case R.id.linear_copyright:
                AlertDialog.Builder builder3=new AlertDialog.Builder(this)
                        .setTitle("版权声明")
                        .setMessage("本App所使用的所有API均由http://www.wanandroid.com网站提供，仅供学习交流使用，" +
                                "不可用于任何商业用途。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alertDialog3=builder3.create();
                alertDialog3.setCanceledOnTouchOutside(false);
                alertDialog3.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void requestData() {
        getPresenter().getCacheSize();
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
