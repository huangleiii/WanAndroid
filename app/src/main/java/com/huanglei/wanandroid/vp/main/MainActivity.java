package com.huanglei.wanandroid.vp.main;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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
import android.view.KeyEvent;
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
import com.huanglei.wanandroid.vp.AboutUsActivity;
import com.huanglei.wanandroid.vp.setting.SettingActivity;
import com.huanglei.wanandroid.vp.login.LoginActivity;
import com.huanglei.wanandroid.vp.main.knowledge.KnowledgeTabListsFragment;
import com.huanglei.wanandroid.vp.main.navigation.NavigationFragment;
import com.huanglei.wanandroid.vp.main.project.ProjectFragment;
import com.huanglei.wanandroid.vp.search.SearchActivity;
import com.huanglei.wanandroid.vp.main.weixin.WeixinFragment;
import com.huanglei.wanandroid.vp.collect.CollectActivity;
import com.huanglei.wanandroid.vp.hotwebsite.HotWebsiteActivity;
import com.huanglei.wanandroid.vp.main.home.HomeFragment;
import com.huanglei.wanandroid.utils.CommonUtils;
import com.huanglei.wanandroid.widget.MyProgressDialog;


import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends MVPBaseActivity<MainActivityContract.Presenter> implements MainActivityContract.View {
    public static final String TAG_PAGE_HOME = "main";
    public static final String TAG_PAGE_KNOWLEDGE = "knowledge";
    public static final String TAG_PAGE_WEIXIN = "weixin";
    public static final String TAG_PAGE_NAVIGATION = "navigation";
    public static final String TAG_PAGE_PROJECT = "project";

    private static final int LOG_OUT = 3;

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
    private WeixinFragment mWeixinFragment;
    private ProjectFragment mProjectFragment;
    private KnowledgeTabListsFragment mKnowledgeTabListsFragment;
    private MyProgressDialog myProgressDialog;
    private boolean canExit = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    canExit = false;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayoutActivityMain.isDrawerOpen(navigationActivityMain)) {
                drawerLayoutActivityMain.closeDrawer(navigationActivityMain);
            } else {
                exit();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (canExit) {
            finish();
        } else {
            canExit = true;
            handler.sendEmptyMessageDelayed(0, 2000);
            CommonUtils.showToastMessage(this, "再按一次退出程序");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setSupportActionBar(toolbarActivityMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myProgressDialog = new MyProgressDialog(MainActivity.this);
        View view = navigationActivityMain.getHeaderView(0);
        mHeaderLinearLayout = view.findViewById(R.id.lin_header_navigation);
        mHeaderCircleImageView = view.findViewById(R.id.circle_header_navigation);
        mHeaderTextView = view.findViewById(R.id.tv_header_navigation);
        mFragmentManager = getSupportFragmentManager();
        bottomActivityMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                hideAllFragments();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.home_bottom_menu:
                        if (mHomeFragment == null) {
                            mHomeFragment = HomeFragment.newInstance();
                            mFragmentTransaction.add(R.id.content_activity_main, mHomeFragment, TAG_PAGE_HOME);
                        }
                        mFragmentTransaction.show(mHomeFragment);
                        mCurrentFragmentTag = TAG_PAGE_HOME;
                        tvTitleActivityMain.setText(R.string.title_page_home);
                        break;
                    case R.id.knowledge_bottom_menu:
                        if (mKnowledgeTabListsFragment == null) {
                            mKnowledgeTabListsFragment = KnowledgeTabListsFragment.newInstance();
                            mFragmentTransaction.add(R.id.content_activity_main, mKnowledgeTabListsFragment, TAG_PAGE_KNOWLEDGE);
                        }
                        mFragmentTransaction.show(mKnowledgeTabListsFragment);
                        mCurrentFragmentTag = TAG_PAGE_KNOWLEDGE;
                        tvTitleActivityMain.setText(R.string.title_page_knowledge);
                        break;
                    case R.id.weixin_bottom_menu:
                        if (mWeixinFragment == null) {
                            mWeixinFragment = WeixinFragment.newInstance();
                            mFragmentTransaction.add(R.id.content_activity_main, mWeixinFragment, TAG_PAGE_WEIXIN);
                        }
                        mFragmentTransaction.show(mWeixinFragment);
                        mCurrentFragmentTag = TAG_PAGE_WEIXIN;
                        tvTitleActivityMain.setText(R.string.title_page_weixin);
                        break;
                    case R.id.navigation_bottom_menu:
                        if (mNavigationFragment == null) {
                            mNavigationFragment = NavigationFragment.newInstance();
                            mFragmentTransaction.add(R.id.content_activity_main, mNavigationFragment, TAG_PAGE_NAVIGATION);
                        }
                        mFragmentTransaction.show(mNavigationFragment);
                        mCurrentFragmentTag = TAG_PAGE_NAVIGATION;
                        tvTitleActivityMain.setText(R.string.title_page_navigation);
                        break;
                    case R.id.project_bottom_menu:
                        if (mProjectFragment == null) {
                            mProjectFragment = ProjectFragment.newInstance();
                            mFragmentTransaction.add(R.id.content_activity_main, mProjectFragment, TAG_PAGE_PROJECT);
                        }
                        mFragmentTransaction.show(mProjectFragment);
                        mCurrentFragmentTag = TAG_PAGE_PROJECT;
                        tvTitleActivityMain.setText(R.string.title_page_project);
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
                    case R.id.about_navigation_menu:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        break;
                    case R.id.set_navigation_menu:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                    case R.id.logout_navigation_menu:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        AlertDialog alertDialog = builder.setMessage("确定退出登录？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //退出登录是GET请求，如果没有网会读取缓存而使得“退出登录成功”，
                                        //但并不会修改cookie，所以连网后仍可以使用cookie保存的登录信息进行收藏等操作。
                                        //因此应让其未连网时反馈“退出登录失败”
                                        //
                                        //登录时POST请求，OKHttp默认不会缓存，所以没有网时不会读取缓存，而是去
                                        //读取网络数据，而使得出现错误，登录失败。因此不用判断登录时是否连网。
                                        //
                                        //但如果POST请求被设置了缓存，在未联网时“登录成功”后，由于没有设置cookie而使得后续
                                        //收藏等操作会发生错误。此时也要先判断是否连网。
                                        if (CommonUtils.isNetworkConnected()) {
                                            myProgressDialog.setText("正在退出登录，请稍候……");
                                            myProgressDialog.show();
                                            getPresenter().logout();
                                        } else {
                                            CommonUtils.showToastMessage(MainActivity.this, "请连接网络后再试");
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
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
                    LoginActivity.startLoginActivity(MainActivity.this, Constants.MAIN_ACTIVITY);
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

    private void hideAllFragments() {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mHomeFragment = (HomeFragment) mFragmentManager.findFragmentByTag(TAG_PAGE_HOME);
        if (mHomeFragment != null) {
            mFragmentTransaction.hide(mHomeFragment);
        }
        mKnowledgeTabListsFragment = (KnowledgeTabListsFragment) mFragmentManager.findFragmentByTag(TAG_PAGE_KNOWLEDGE);
        if (mKnowledgeTabListsFragment != null) {
            mFragmentTransaction.hide(mKnowledgeTabListsFragment);
        }
        mWeixinFragment = (WeixinFragment) mFragmentManager.findFragmentByTag(TAG_PAGE_WEIXIN);
        if (mWeixinFragment != null) {
            mFragmentTransaction.hide(mWeixinFragment);
        }
        mProjectFragment = (ProjectFragment) mFragmentManager.findFragmentByTag(TAG_PAGE_PROJECT);
        if (mProjectFragment != null) {
            mFragmentTransaction.hide(mProjectFragment);
        }
        mNavigationFragment = (NavigationFragment) mFragmentManager.findFragmentByTag(TAG_PAGE_NAVIGATION);
        if (mNavigationFragment != null) {
            mFragmentTransaction.hide(mNavigationFragment);
        }
        mFragmentTransaction.commitAllowingStateLoss();
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
                } else if (fragment instanceof NavigationFragment) {
                    ((NavigationFragment) fragment).jumpToTop();
                } else if (fragment instanceof WeixinFragment) {
                    ((WeixinFragment) fragment).jumpToTop();
                } else if (fragment instanceof ProjectFragment) {
                    ((ProjectFragment) fragment).jumpToTop();
                } else if (fragment instanceof KnowledgeTabListsFragment)
                    ((KnowledgeTabListsFragment) fragment).jumpToTop();
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
        getPresenter().setLoginStatus(false, "");
        myProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, "退出登录成功");
        showUnLogin();
        LoginActivity.startLoginActivity(this, Constants.MAIN_ACTIVITY);
        refreshCurrentFragment();
    }

    @Override
    public void showLogoutFailed(String msg) {
        myProgressDialog.dismiss();
        CommonUtils.showToastMessage(this, msg);
    }

    @Override
    public void subscribeLoginEvent(String activityName) {
        showLogin(getPresenter().getUsername());
        if (activityName.equals(Constants.MAIN_ACTIVITY))
            refreshCurrentFragment();
    }

    @Override
    public void subscribeCancelCollectEvent(String activityName) {
        if (activityName.equals(Constants.MAIN_ACTIVITY))
            if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
                Fragment fragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
                if (fragment != null) {
                    if (fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).updateCollectState(false);
                    } else if (fragment instanceof WeixinFragment) {
                        ((WeixinFragment) fragment).updateCollectState(false);
                    } else if (fragment instanceof ProjectFragment) {
                        ((ProjectFragment) fragment).updateCollectState(false);
                    }
                }
            }
    }

    @Override
    public void subscribeCollectEvent(String activityName) {
        if (activityName.equals(Constants.MAIN_ACTIVITY))
            if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
                Fragment fragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
                if (fragment != null) {
                    if (fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).updateCollectState(true);
                    } else if (fragment instanceof WeixinFragment) {
                        ((WeixinFragment) fragment).updateCollectState(true);
                    } else if (fragment instanceof ProjectFragment) {
                        ((ProjectFragment) fragment).updateCollectState(true);
                    }
                }
            }
    }

    @Override
    public void subscribeLoginExpiredEvent() {
        showUnLogin();
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
                } else if (fragment instanceof WeixinFragment) {
                    ((WeixinFragment) fragment).refreshView();
                } else if (fragment instanceof ProjectFragment) {
                    ((ProjectFragment) fragment).refreshView();
                }
            }
        }
    }

}
