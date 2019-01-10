package com.huanglei.wanandroid.main;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.base.view.activity.MVPBaseActivity;
import com.huanglei.wanandroid.contract.MainActivityContract;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.huanglei.wanandroid.widget.MyProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends MVPBaseActivity<MainActivityContract.Presenter> implements MainActivityContract.View {
    public static final String TAG_PAGE_HOME = "main";
    public static final String TAG_PAGE_KNOWLEDGE = "knowledge";
    public static final String TAG_PAGE_WEIXIN = "weixin";
    public static final String TAG_PAGE_NAVIGATION = "navigation";
    public static final String TAG_PAGE_PROJECT = "project";

    private static final int LOG_OUT = 4;

    @BindView(R.id.toolbar_activity_main)
    Toolbar toolbarActivityMain;
    @BindView(R.id.content_activity_main)
    FrameLayout contentActivityMain;
    @BindView(R.id.bottom_activity_main)
    BottomNavigationView bottomActivityMain;
    @BindView(R.id.drawer_layout_activity_main)
    DrawerLayout drawerLayoutActivityMain;
    @BindView(R.id.tv_title_activity_main)
    TextView tvTitleActivityMain;
    @BindView(R.id.navigation_activity_main)
    NavigationView navigationActivityMain;
    @BindView(R.id.float_button_activity_main)
    FloatingActionButton floatButtonActivityMain;
    private LinearLayout mHeaderLinearLayout;
    private CircleImageView mHeaderCircleImageView;
    private TextView mHeaderTextView;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private String mCurrentFragmentTag;
    private HomeFragment mHomeFragment;
    private NavigationFragment mNavigationFragment;
    private MyProgressDialog myProgressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(toolbarActivityMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initView() {
        myProgressDialog = new MyProgressDialog(this);
        View view = navigationActivityMain.getHeaderView(0);
        mHeaderLinearLayout = view.findViewById(R.id.lin_header_navigation);
        mHeaderCircleImageView = view.findViewById(R.id.circle_header_navigation);
        mHeaderTextView = view.findViewById(R.id.tv_header_navigation);
        bottomActivityMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.home_bottom_menu:
                        if (!TextUtils.isEmpty(mCurrentFragmentTag) && mFragmentManager.findFragmentByTag(mCurrentFragmentTag) != null) {
                            Fragment currentFragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
                            mFragmentTransaction.hide(currentFragment);
                        }
                        if (mFragmentManager.findFragmentByTag(TAG_PAGE_HOME) == null) {
                            mHomeFragment = HomeFragment.newInstance();
                            mFragmentTransaction.add(R.id.content_activity_main, mHomeFragment, TAG_PAGE_HOME);
                        } else {
                            mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(TAG_PAGE_HOME);
                        }
                        mFragmentTransaction.show(mHomeFragment);
                        mCurrentFragmentTag = TAG_PAGE_HOME;
                        tvTitleActivityMain.setText(R.string.title_page_home);
                        break;
                    case R.id.knowledge_bottom_menu:
                        break;
                    case R.id.weixin_bottom_menu:
                        break;
                    case R.id.navigation_bottom_menu:
                        if (!TextUtils.isEmpty(mCurrentFragmentTag) && mFragmentManager.findFragmentByTag(mCurrentFragmentTag) != null) {
                            Fragment currentFragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
                            mFragmentTransaction.hide(currentFragment);
                        }
                        if (mFragmentManager.findFragmentByTag(TAG_PAGE_NAVIGATION) == null) {
                            mNavigationFragment = NavigationFragment.newInstance();
                            mFragmentTransaction.add(R.id.content_activity_main, mNavigationFragment, TAG_PAGE_NAVIGATION);
                        } else {
                            mNavigationFragment = (NavigationFragment) mFragmentManager.findFragmentByTag(TAG_PAGE_NAVIGATION);
                        }
                        mFragmentTransaction.show(mNavigationFragment);
                        mCurrentFragmentTag = TAG_PAGE_NAVIGATION;
                        tvTitleActivityMain.setText(R.string.title_page_navigation);
                        break;
                    case R.id.project_bottom_menu:
                        break;
                    default:
                        break;
                }
                mFragmentTransaction.commitAllowingStateLoss();
                return true;
            }
        });
        bottomActivityMain.setSelectedItemId(R.id.home_bottom_menu);
        navigationActivityMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.favorite_navigation_menu:
                        if (getPresenter().getLoginStatus()) {
                            startActivity(new Intent(MainActivity.this, CollectActivity.class));
                        } else {
                            CommonUtils.showToastMessage(MainActivity.this, "请先登录");
                        }
                        break;
                    case R.id.todo_navigation_menu:
                        break;
                    case R.id.about_navigation_menu:
                        break;
                    case R.id.set_navigation_menu:
                        break;
                    case R.id.logout_navigation_menu:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("确定退出登录？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        myProgressDialog.setText("正在退出登录，请稍候……");
                                        getPresenter().logout();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mHeaderLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().getLoginStatus())
                    CommonUtils.showToastMessage(MainActivity.this, getPresenter().getUsername() + "已登录");
                else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        if (getPresenter().getLoginStatus()) {
            showLogin(getPresenter().getUsername());
        }
        floatButtonActivityMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToTop();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayoutActivityMain.openDrawer(navigationActivityMain);
                break;
            case R.id.hot_website_main_menu:
                startActivity(new Intent(this, HotWebsiteActivity.class));
                break;
            case R.id.search_main_menu:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void requestData() {
    }


    private void jumpToTop() {
        if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
            Fragment fragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
            if (fragment != null) {
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).jumpToTop();
                }else if(fragment instanceof NavigationFragment){
                    ((NavigationFragment) fragment).jumpToTop();
                }
            }
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }


    @Override
    protected MainActivityContract.Presenter createPresenter() {
        return new MainActivityPresenter();
    }

    @Override
    public void showLogoutSucceed(Object o) {
        myProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, "退出登录成功");
        showUnLogin();
        refreshCurrentFragment();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void showLogoutFailed(String msg) {
        myProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, msg);
    }

    @Override
    public void subscribeLoginEvent() {
        showLogin(getPresenter().getUsername());
        refreshCurrentFragment();
    }

    @Override
    public void subscribeCancelCollectEvent(String activityName) {
        if (activityName.equals(Constants.MAIN_ACTIVITY)) {
            if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
                Fragment fragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
                if (fragment != null) {
                    if (fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).updateCollectState(false);
                    }
                }
            }
        }
    }

    @Override
    public void subscribeCollectEvent(String activityName) {
        if (activityName.equals(Constants.MAIN_ACTIVITY)) {
            if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
                Fragment fragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
                if (fragment != null) {
                    if (fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).updateCollectState(true);
                    }
                }
            }
        }
    }

    @Override
    public void subscribeLoginExpiredEvent() {
        showUnLogin();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void showLogin(String username) {
        mHeaderTextView.setText(username);
        navigationActivityMain.getMenu().getItem(LOG_OUT).setVisible(true);
    }

    private void showUnLogin() {
        mHeaderTextView.setText("登录");
        navigationActivityMain.getMenu().getItem(LOG_OUT).setVisible(false);
    }

    private void refreshCurrentFragment() {
        if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
            Fragment fragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
            if (fragment != null) {
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).refreshView();
                }
            }
        }
    }

}
