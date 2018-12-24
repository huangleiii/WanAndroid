package com.huanglei.wanandroid.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanglei.wanandroid.R;
import com.huanglei.wanandroid.utils.CommonUtils;

/**
 * Created by HuangLei on 2018/11/26.
 */

public class MyProgressDialog {
    private Dialog mDialog;
    private TextView mTextView;
    public MyProgressDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.dp2px(300), CommonUtils.dp2px(100));
        view.setPadding(CommonUtils.dp2px(10), CommonUtils.dp2px(10), CommonUtils.dp2px(10), CommonUtils.dp2px(10));
        view.setLayoutParams(params);
        mTextView=(TextView)view.findViewById(R.id.tv_dialog_progress);
        mDialog = new Dialog(context);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }
    public MyProgressDialog setText(String msg){
        mTextView.setText(msg);
        return this;
    }
    public void show(){
        mDialog.show();
    }
    public void dismiss(){
        if(mDialog.isShowing())
            mDialog.dismiss();
    }
}
