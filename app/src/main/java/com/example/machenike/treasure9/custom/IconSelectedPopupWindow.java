package com.example.machenike.treasure9.custom;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.machenike.treasure9.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MACHENIKE on 2017/8/14.
 */

public class IconSelectedPopupWindow extends PopupWindow {
    private Activity mActivity;
    private OnClickListener mOnClickListener;

    public IconSelectedPopupWindow(Activity mActivity, OnClickListener onClickListener) {
        super(mActivity.getLayoutInflater().inflate(R.layout.window_select_icon, null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        this.mActivity = mActivity;
        mOnClickListener = onClickListener;
        ButterKnife.bind(this,getContentView());
        //注意Popupwindow一定要给它设置背景
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void show() {
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.btn_gallery, R.id.btn_camera, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery://去相册
                mOnClickListener.toGalarry();
                break;
            case R.id.btn_camera://去相机
                mOnClickListener.toCamera();
                break;
            case R.id.btn_cancel://取消
                dismiss();
                break;
        }
    }

    public interface OnClickListener {
        void toGalarry();

        void toCamera();

        void cancel();
    }
}
